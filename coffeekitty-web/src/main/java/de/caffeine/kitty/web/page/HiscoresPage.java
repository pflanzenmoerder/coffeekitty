package de.caffeine.kitty.web.page;

import java.util.Date;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.joda.time.DateMidnight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.service.CaffeinieService;
import de.caffeine.kitty.service.repository.consumtion.HiscoreDTO;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.highscore.DisplayCaffeineHighscoresPanel;
import de.caffeine.kitty.web.user.notifications.DisplayNotificationsPanel;


@Configurable
public class HiscoresPage extends BaseCafManPage {
	private static final int SEVENDAYS = 1000*60*60*24*7;

	private static final long serialVersionUID = 1L;

	private static final String DISPLAY_NOTIFICATIONS = "displayNotifications";

	private static final int MAXSIZE = 10;

	@Autowired
	private transient AccountService accountService;
	
	@Autowired
	private transient CaffeinieService caffeinieService;
	
	@SuppressWarnings("serial")
	public HiscoresPage() {
		final IModel<User> userModel = UserAuthenticatedWebSession.get().getUserModel();
		final IModel<List<Account>> accounts = new LoadableDetachableModel<List<Account>>() {
				@Override
				protected List<Account> load() {
					return accountService.findAccountRequestsByUser(userModel.getObject());
				}
			};			
		add(new DisplayNotificationsPanel(DISPLAY_NOTIFICATIONS, accounts) {
				@Override
				protected void onConfigure() {
					super.onConfigure();
					if(UserAuthenticatedWebSession.get().isSignedIn()) {
						setVisibilityAllowed(!accounts.getObject().isEmpty());	
					}
					else {
						setVisibilityAllowed(false);
					}
				}
			}
			.setOutputMarkupPlaceholderTag(true)
			.setOutputMarkupId(true));
		add(new DisplayCaffeineHighscoresPanel("hiscoresAlltime", new AbstractReadOnlyModel<List<HiscoreDTO>>() {
					@Override
					public List<HiscoreDTO> getObject() {
						return fillList(caffeinieService.getTopConsumptionsAllTime(MAXSIZE),MAXSIZE);
					}
				}));
		add(new DisplayCaffeineHighscoresPanel("hiscoresThisweek", new AbstractReadOnlyModel<List<HiscoreDTO>>() {
					@Override
					public List<HiscoreDTO> getObject() {
						Date today = new Date();
						Date sevenDaysBack = new Date(today.getTime()-SEVENDAYS);
						return fillList(caffeinieService.getTopConsumptionsByTimeRange(MAXSIZE, sevenDaysBack, today),MAXSIZE);
					}
				}));
		add(new DisplayCaffeineHighscoresPanel("hiscoresToday", new AbstractReadOnlyModel<List<HiscoreDTO>>() {
					@Override
					public List<HiscoreDTO> getObject() {
						Date today = new Date();
						Date midnight = new DateMidnight().toDate();
						return fillList(caffeinieService.getTopConsumptionsByTimeRange(MAXSIZE, midnight, today), MAXSIZE);
					}
				}));
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AjaxRequestTarget) {
			AjaxRequestTarget target = ((AjaxRequestTarget)event.getPayload());
			target.add(get(DISPLAY_NOTIFICATIONS));
		}
	}

	private static List<HiscoreDTO> fillList(List<HiscoreDTO> listToFill, int max) {
		int delta = max - listToFill.size();
		for (int count = 0; count < delta; count++) {
			listToFill.add(new HiscoreDTO(null, null, null, Boolean.FALSE));
		}
		return listToFill;
	}
}
