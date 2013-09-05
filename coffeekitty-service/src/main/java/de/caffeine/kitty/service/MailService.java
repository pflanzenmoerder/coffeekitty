package de.caffeine.kitty.service;

import java.util.HashMap;
import java.util.Map;
import java.util.TimerTask;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Service;

import de.caffeine.kitty.entities.Consumption;
import de.caffeine.kitty.entities.User;

@Service
public class MailService extends TimerTask{
	//yes no clean up, I am lazy
	private Map<String, String> userIdToConsumptionIdMap = new HashMap<String, String>();
	private static final Logger LOG = LoggerFactory.getLogger(MailService.class);
	@Autowired
	private UserService userService;
	@Autowired
	private CaffeinieService caffeinieService;
	@Autowired
    private MailSender mailSender;
	@Autowired
    private SimpleMailMessage templateMessage;

	@Override
	public void run() {
		for (User user : userService.findUsersWithWarnLevelSet()) {
			Consumption consumption  = caffeinieService.findLatestConsumptionByUser(user);
			//Too many queries here, see first comment
			if (consumption != null && (new Period(new DateTime(consumption.getTimeOfConsumption()), DateTime.now()).getHours()>24 || consumption.getCaffeineLevelAnHourAfterConsumption()<user.getWarnLevel())) {
				String userId = consumption.getUser().getId();
				if(!(userIdToConsumptionIdMap.containsKey(userId) && userIdToConsumptionIdMap.get(userId).equals(consumption.getId()))) {					
					SimpleMailMessage msg = new SimpleMailMessage(this.templateMessage);
					msg.setTo(user.getEmail());
					msg.setText(
							"You need some frickin' caffeine");
					userIdToConsumptionIdMap.put(userId, consumption.getId());
					try{
						this.mailSender.send(msg);
					}
					catch(MailException ex) {
						LOG.warn(ex.toString());            
					}	
				}
			}
		}
	}
}