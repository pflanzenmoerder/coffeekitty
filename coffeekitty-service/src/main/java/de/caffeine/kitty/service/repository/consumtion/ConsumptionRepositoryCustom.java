package de.caffeine.kitty.service.repository.consumtion;

import java.util.Date;
import java.util.List;

import de.caffeine.kitty.entities.Consumption;

public interface ConsumptionRepositoryCustom {
	Consumption findMostRecentConsumptionByUserId(String userId);
	List<Consumption> findConsumptionsByUserIAndYoungerThan(String userId, Date from);
	List<Consumption> findConsumptionsByUserIdAndTimeRange(String userId, Date from, Date to);
	List<HiscoreDTO> findConsumptionsWithTopCaffeineByTimeRange(Integer max, Date from, Date to);
	List<HiscoreDTO> findTopConsumptionsAllTime(Integer max);
}
