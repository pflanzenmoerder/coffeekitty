package de.caffeine.kitty.web.common;

import org.apache.wicket.MetaDataKey;
import org.apache.wicket.request.IRequestHandler;
import org.apache.wicket.request.cycle.AbstractRequestCycleListener;
import org.apache.wicket.request.cycle.RequestCycle;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.orm.hibernate4.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Configurable
final class OpenSessionInRequestCycleListener extends
        AbstractRequestCycleListener {
	@SuppressWarnings("serial")
	static final MetaDataKey<Boolean> PARTICIPATE = new MetaDataKey<Boolean>() {};
	
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void onBeginRequest(RequestCycle cycle) {
		cycle.setMetaData(PARTICIPATE, TransactionSynchronizationManager.hasResource(sessionFactory));
		if(!cycle.getMetaData(PARTICIPATE)) {
			Session session = openSession(sessionFactory);
			TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(session));
		}		
	}
	
	@Override
    public void onRequestHandlerExecuted(RequestCycle cycle, IRequestHandler handler) {
		if (!cycle.getMetaData(PARTICIPATE)) {
            SessionHolder sessionHolder =
                    (SessionHolder) TransactionSynchronizationManager.unbindResource(sessionFactory);
            SessionFactoryUtils.closeSession(sessionHolder.getSession());
		}
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