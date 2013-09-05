package de.caffeine.kitty.web.caffeine;

import java.text.SimpleDateFormat;

import org.jfree.chart.axis.DateTickUnit;
import org.jfree.chart.axis.DateTickUnitType;

public enum CaffeineLevelDisplayMode {
	DAY(new DateTickUnit(DateTickUnitType.HOUR, 5, new SimpleDateFormat("HH:mm"))), 
	WEEK(new DateTickUnit(DateTickUnitType.DAY, 1, new SimpleDateFormat("MMM dd"))); 

	private final DateTickUnit dateTickUnit;
	
	private CaffeineLevelDisplayMode(DateTickUnit dateTickUnit) {
		this.dateTickUnit = dateTickUnit;
	}
	
	public DateTickUnit getDateTickUnit() {
		return dateTickUnit;
	}
}