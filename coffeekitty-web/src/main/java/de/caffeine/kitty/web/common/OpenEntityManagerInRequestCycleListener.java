package de.caffeine.kitty.web.common;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.jpa.EntityManagerFactoryUtils;
import org.springframework.orm.jpa.EntityManagerHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceException;
import javax.persistence.PersistenceUnit;

@Configurable
public final class OpenEntityManagerInRequestCycleListener extends
        AbstractRequestCycleListener {
	private static final Logger LOG = LoggerFactory.getLogger(OpenEntityManagerInRequestCycleListener.class);
	@SuppressWarnings("serial")
	static final MetaDataKey<Boolean> PARTICIPATE = new MetaDataKey<Boolean>() {};

	@PersistenceUnit
	private EntityManagerFactory emf;
	
	@Override
	public void onBeginRequest(RequestCycle cycle) {
		cycle.setMetaData(PARTICIPATE, TransactionSynchronizationManager.hasResource(emf));
		if(!cycle.getMetaData(PARTICIPATE)) {
			try {
				LOG.debug("OPENING NEW ENTITY MANAGER FOR THIS REQUEST.");
				EntityManager em = emf.createEntityManager();
				TransactionSynchronizationManager.bindResource(emf, new EntityManagerHolder(em));
			}
			catch (PersistenceException ex) {
				throw new DataAccessResourceFailureException("Could not create JPA EntityManager", ex);
			}
		}		
	}


    @Override
    public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler) {
		if (!cycle.getMetaData(PARTICIPATE)) {
            LOG.debug("CLOSING ENTITY MANAGER FOR THIS REQUEST.");
            EntityManagerHolder emHolder = (EntityManagerHolder)
                    TransactionSynchronizationManager.unbindResource(emf);
            EntityManagerFactoryUtils.closeEntityManager(emHolder.getEntityManager());
		}
	}

    @Override
    public IRequestHandler onException(RequestCycle cycle, Exception ex) {
        return super.onException(cycle, ex);    //To change body of overridden methods use File | Settings | File Templates.
    }

    protected Session openSession(SessionFactory sessionFactory) throws DataAccessResourceFailureException {
		try {
			Session session = SessionFactoryUtils.openSession(sessionFactory);
			session.setFlushMode(FlushMode.MANUAL);
			return session;
		}
		catch (HibernateException ex) {
			throw new DataAccessResourceFailureException("Could not open Hibernate Session", ex);
		}
	}

}