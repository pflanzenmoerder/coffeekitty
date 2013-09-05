package de.caffeine.kitty.service.repository.kitty;

import java.util.Collections;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;

import de.caffeine.kitty.entities.QKitty;


public class KittyRepositoryImpl implements KittyRepositoryCustom {
    @PersistenceContext
    private EntityManager em;

	@Override
	public KittySearchResult findByNameAndPageIdWithPageSize(String name,
			Integer page, Integer pageSize) {
		KittySearchResult ret = new KittySearchResult();
		JPAQuery query = new JPAQuery(em);
		ret.total = query.from(QKitty.kitty).where(QKitty.kitty.name.like("%"+name+"%")).count();
		if(ret.total > 0 || ret.total > page*pageSize) {
			query = new JPAQuery(em);
			ret.kitties = query.from(QKitty.kitty).where(QKitty.kitty.name.like("%"+name+"%")).limit(pageSize).offset(pageSize*page).list(QKitty.kitty);	
		}
		else {
			ret.kitties = Collections.emptyList();
		}
		return ret;
	}

}
