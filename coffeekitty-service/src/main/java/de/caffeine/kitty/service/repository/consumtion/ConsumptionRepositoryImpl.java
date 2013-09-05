package de.caffeine.kitty.service.repository.consumtion;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.mysema.query.jpa.impl.JPAQuery;
import com.mysema.query.jpa.impl.JPASubQuery;
import com.mysema.query.types.ConstructorExpression;
import com.mysema.query.types.query.DateTimeSubQuery;
import de.caffeine.kitty.entities.Consumption;
import de.caffeine.kitty.entities.QConsumption;

public class ConsumptionRepositoryImpl implements
		ConsumptionRepositoryCustom {

	@PersistenceContext
	private EntityManager em;
	
	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Consumption findMostRecentConsumptionByUserId(String userId) {
		JPAQuery query = new JPAQuery(em);
		DateTimeSubQuery subQuery = new JPASubQuery().from(QConsumption.consumption).where(QConsumption.consumption.user.id.eq(userId)).unique(QConsumption.consumption.timeOfConsumption.max());
		List<Consumption> res = query.from(QConsumption.consumption).where(QConsumption.consumption.user.id.eq(userId).and(QConsumption.consumption.timeOfConsumption.eq(subQuery))).list(QConsumption.consumption);
		return res.size() > 0 ? res.get(0) : null;
	}

	@Override
	public List<Consumption> findConsumptionsByUserIdAndTimeRange(String userId, Date from,
			Date to) {
		JPAQuery query = new JPAQuery(em);
		return query.from(QConsumption.consumption).where(QConsumption.consumption.user.id.eq(userId).and(QConsumption.consumption.timeOfConsumption.between(from, to))).list(QConsumption.consumption);
	}

	@Override
	public List<HiscoreDTO> findConsumptionsWithTopCaffeineByTimeRange(Integer max, Date from, Date to) {
		JPAQuery query = new JPAQuery(em);
		return query.from(QConsumption.consumption).where(QConsumption.consumption.timeOfConsumption.between(from, to)).groupBy(QConsumption.consumption.user.id, QConsumption.consumption.user.displayName, QConsumption.consumption.user.shareStatistics)
				.list(ConstructorExpression.create(HiscoreDTO.class, QConsumption.consumption.user.id, QConsumption.consumption.user.displayName, QConsumption.consumption.caffeineLevelAnHourAfterConsumption.max(), QConsumption.consumption.user.shareStatistics));
	}

	@Override
	public List<HiscoreDTO> findTopConsumptionsAllTime(Integer max) {
		JPAQuery query = new JPAQuery(em);
		return query.from(QConsumption.consumption).groupBy(QConsumption.consumption.user.id, QConsumption.consumption.user.displayName, QConsumption.consumption.user.shareStatistics).limit(max)
				.list(ConstructorExpression.create(HiscoreDTO.class, QConsumption.consumption.user.id, QConsumption.consumption.user.displayName, QConsumption.consumption.caffeineLevelAnHourAfterConsumption.max(), QConsumption.consumption.user.shareStatistics));
	}

	@Override
	public List<Consumption> findConsumptionsByUserIAndYoungerThan(String userId, Date from) {
		JPAQuery query = new JPAQuery(em);
		return query.from(QConsumption.consumption).where(QConsumption.consumption.user.id.eq(userId).and(QConsumption.consumption.timeOfConsumption.after(from))).list(QConsumption.consumption);
	}

	
	
}
