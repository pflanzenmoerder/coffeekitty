package de.caffeine.kitty.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.joda.time.DateMidnight;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.caffeine.kitty.entities.Consumption;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.repository.consumtion.ConsumptionRepository;
import de.caffeine.kitty.service.repository.consumtion.HiscoreDTO;

@Service
@Transactional
public class CaffeinieService {
	private static final int HOUR_DIVISOR = 1000*60*60;

	@Autowired
	private ConsumptionRepository consumptionRepository;
	
	public Consumption findLatestConsumptionByUser(User user) {
		return consumptionRepository.findMostRecentConsumptionByUserId(user.getId());
	}
	
	public List<CaffeineDTO> getCaffeineDTOsForLast24HoursByUser(User user) {
		DateTime startDate = DateTime.now().minusHours(48);
		List<Consumption> consumptions=consumptionRepository.findConsumptionsByUserIAndYoungerThan(user.getId(), startDate.toDate());
		return calculateDTOs(startDate, consumptions);
	}
	
	public List<CaffeineDTO> getCaffeineDTOsForLast7DaysByUser(User user) {
		DateTime startDate = new DateMidnight().minusDays(6).toDateTime();
		List<Consumption> consumptions=consumptionRepository.findConsumptionsByUserIAndYoungerThan(user.getId(), startDate.toDate());
		return calculateDTOs(startDate, consumptions);
	}
	
	public List<HiscoreDTO> getTopConsumptionsByTimeRange(Integer max, Date from, Date to) {
		return consumptionRepository.findConsumptionsWithTopCaffeineByTimeRange(max, from, to);
	}
	
	public List<HiscoreDTO> getTopConsumptionsAllTime(Integer max) {
		return consumptionRepository.findTopConsumptionsAllTime(max);
	}
	
	private static List<CaffeineDTO> calculateDTOs(DateTime startDate, List<Consumption> consumptions) {
		List<CaffeineDTO> ret = new ArrayList<CaffeineDTO>();
		DateTime currentIndex = startDate;
		while (currentIndex.isBeforeNow()) {
			currentIndex = currentIndex.plusMinutes(30);
			float caffeineLevel = 0;
			for (Consumption consumption:consumptions) {
				DateTime timeOfConsumption = new DateTime(consumption.getTimeOfConsumption());
				if (timeOfConsumption.isBefore(currentIndex) && new Period(timeOfConsumption, currentIndex).getHours()<=24) {
					caffeineLevel += calculateCurrentLevel(consumption.getCaffeineLevel(), (float)currentIndex.minus(timeOfConsumption.getMillis()).getMillis()/HOUR_DIVISOR);
				}
			}	
			CaffeineDTO dto = new CaffeineDTO();
			dto.level = (int)caffeineLevel;
			dto.time = currentIndex.toDate();
			ret.add(dto);
		}
		return ret;
	}
	
	private static double calculateCurrentLevel(float startValue, float timeSpanInH) {
		if (timeSpanInH > 1) {
			return startValue * Math.pow(0.5, timeSpanInH/5);	
		}
		return startValue * timeSpanInH * Math.pow(0.5, timeSpanInH/5);
	}
}
