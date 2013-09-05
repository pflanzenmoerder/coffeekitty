package de.caffeine.kitty.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "PF_CONSUMPTION")
public class Consumption extends BaseEntity{
	private static final long serialVersionUID = -1314862581557422091L;
	
	@Column(name = "TIMEOFCONSUMPTION", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date timeOfConsumption;
	@ManyToOne
	@JoinColumn(name = "USER_ID", nullable = false)
	private User user;
	@Column(name = "CAFFEINELEVEL", nullable = false)
	private Integer caffeineLevel;
	@Column(name = "CAFFEINELEVELONEHOUR", nullable = false)
	private Integer caffeineLevelAnHourAfterConsumption;

	public Integer getCaffeineLevelAnHourAfterConsumption() {
		return caffeineLevelAnHourAfterConsumption;
	}

	public void setCaffeineLevelAnHourAfterConsumption(Integer caffeineLevelAnHourAfterConsumption) {
		this.caffeineLevelAnHourAfterConsumption = caffeineLevelAnHourAfterConsumption;
	}

	public Integer getCaffeineLevel() {
		return caffeineLevel;
	}

	public void setCaffeineLevel(Integer caffeineLevel) {
		this.caffeineLevel = caffeineLevel;
	}

	public Date getTimeOfConsumption() {
		return timeOfConsumption;
	}

	public void setTimeOfConsumption(Date timeOfConsumption) {
		this.timeOfConsumption = timeOfConsumption;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
