package de.caffeine.kitty.service.repository.consumtion;

import java.io.Serializable;

import com.mysema.query.annotations.QueryProjection;

@SuppressWarnings("serial")
public class HiscoreDTO implements Comparable<HiscoreDTO>, Serializable{
	public String userId;
	public String displayName;
	public Integer caffeineLevel;
	public Boolean publish;
	
	@QueryProjection
	public HiscoreDTO(String userId, String displayName, Integer caffeineLevel, Boolean publish) {
		super();
		this.userId = userId;
		this.displayName = displayName;
		this.caffeineLevel = caffeineLevel;
		this.publish = publish;
	}

	@Override
	public int compareTo(HiscoreDTO arg0) {
		return caffeineLevel.compareTo(arg0.caffeineLevel);
	}
	
}
