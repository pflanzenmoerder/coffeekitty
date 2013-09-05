package de.caffeine.kitty.service;

import java.io.Serializable;
import java.util.Date;

public class CaffeineDTO implements Serializable {
	public Integer level;
	public Date time;
	//Getters and Setters just here for the stupid CSV-lib
	public Integer getLevel() {
		return level;
	}
	public void setLevel(Integer level) {
		this.level = level;
	}
	public Date getTime() {
		return time;
	}
	public void setTime(Date time) {
		this.time = time;
	}
	
}