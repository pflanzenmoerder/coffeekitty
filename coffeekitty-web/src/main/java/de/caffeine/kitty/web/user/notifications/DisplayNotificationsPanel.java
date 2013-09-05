package de.caffeine.kitty.web.user.notifications;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.authorization.Action;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.authroles.authorization.strategies.role.annotations.AuthorizeAction;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.model.StringResourceModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Account;
import de.caffeine.kitty.service.AccountService;


/**
 * Panel to display current notifications for a given user.
 */
@AuthorizeAction(action=Action.RENDER,roles = {Roles.USER})
@SuppressWarnings("serial")
@Configurable
public class DisplayNotificationsPanel extends Panel {

	@Autowired
	private transient AccountService accountService;
	
	/**
	 * Constructor.
	 * todo change model to IModel<List<Notification>>
	 * @param id Component-ID.
	 * @param model Model to deliver the notifications, for example of a user.
	 */
	public DisplayNotificationsPanel(String id, IModel<List<Account>> model) {
		super(id, model);
		add(new ListView<Account>("notificationsRepeater", model) {

			@Override
			protected void populateItem(final ListItem<Account> item) {
				item.add(new Label("notificationMessage", new StringResourceModel("notificationMessage", this, item.getModel(), new Object[] {
					new PropertyModel<String>(item.getModelObject(), "user.fullName"),
					new PropertyModel<String>(item.getModelObject(), "user.displayName"),
					new PropertyModel<String>(item.getModelObject(), "kitty.name")
				})));
				item.add(new AjaxLink<Void>("acceptButton") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						accountService.acceptMembership(item.getModelObject());
					}
				});
				item.add(new AjaxLink<Void>("declineButton") {
					@Override
					public void onClick(AjaxRequestTarget target) {
						accountService.rejectMembership(item.getModelObject());
					}
				});
			}
			
		});
	}

}
