package de.caffeine.kitty.web.page;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.event.IEvent;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.AccountService;
import de.caffeine.kitty.service.CaffeineDTO;
import de.caffeine.kitty.service.CaffeinieService;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.caffeine.CaffeineLevelDisplayMode;
import de.caffeine.kitty.web.caffeine.DisplayUserCaffeineLevelsPanel;
import de.caffeine.kitty.web.user.caffeinealert.ManageCaffeineLevelAlertPanel;
import de.caffeine.kitty.web.user.notifications.DisplayNotificationsPanel;

@SuppressWarnings("serial")
@Configurable
public class DisplayUserCaffeineStatisticsPage extends BaseCafManPage {
	private static final String DISPLAY_NOTIFICATIONS = "displayNotifications";
	
	@Autowired
	private transient AccountService accountService;
	
	@Autowired
	private transient CaffeinieService caffeinieService;
	
	
	public DisplayUserCaffeineStatisticsPage() {
		this(UserAuthenticatedWebSession.get().getUserModel());
	}
	
	public DisplayUserCaffeineStatisticsPage(final IModel<User> userModel) {
		final IModel<List<Account>> accounts = new LoadableDetachableModel<List<Account>>() {
				@Override
				protected List<Account> load() {
					return accountService.findAccountRequestsByUser(userModel.getObject());
				}
			};			
		add(new Label("header", new StringResourceModel("headerMessage", this, new CompoundPropertyModel<User>(userModel))));
		add(new DisplayNotificationsPanel(DISPLAY_NOTIFICATIONS, accounts) {
				@Override
				protected void onConfigure() {
					super.onConfigure();
					setVisibilityAllowed(!accounts.getObject().isEmpty());
				}
			}
			.setOutputMarkupPlaceholderTag(true)
			.setOutputMarkupId(true));

		DayEntries dayEntries = new DayEntries(userModel);
		WeekEntries weekEntries = new WeekEntries(userModel);
		
		add(new DisplayUserCaffeineLevelsPanel("displayUserCaffeineStatisticsDay", dayEntries, dayEntries, CaffeineLevelDisplayMode.DAY, Model.of("24 hours")));

		add(new DisplayUserCaffeineLevelsPanel("displayUserCaffeineStatisticsWeek", weekEntries, weekEntries, CaffeineLevelDisplayMode.WEEK, Model.of("7 days")));

		add(new ManageCaffeineLevelAlertPanel("manageCaffeineLevelAlert", userModel));
	}
	
	@Override
	public void onEvent(IEvent<?> event) {
		super.onEvent(event);
		if (event.getPayload() instanceof AjaxRequestTarget) {
			AjaxRequestTarget target = ((AjaxRequestTarget)event.getPayload());
			target.add(get(DISPLAY_NOTIFICATIONS));
		}
	}
	
	private class DayEntries extends LoadableDetachableModel<List<CaffeineDTO>> {
		private IModel<User> user;
		
		public DayEntries(IModel<User> user) {
			super();
			this.user = user;
		}

		@Override
		protected List<CaffeineDTO> load() {
			return caffeinieService.getCaffeineDTOsForLast24HoursByUser(user.getObject());
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			user.detach();
		}
		
		
	}
	
	private class WeekEntries extends LoadableDetachableModel<List<CaffeineDTO>> {
		private IModel<User> user;
		
		public WeekEntries(IModel<User> user) {
			super();
			this.user = user;
		}

		@Override
		protected List<CaffeineDTO> load() {
			return caffeinieService.getCaffeineDTOsForLast7DaysByUser(user.getObject());
		}

		@Override
		protected void onDetach() {
			super.onDetach();
			user.detach();
		}
		
		
	}

}
