package de.caffeine.kitty.web.user.profile;

import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.EqualPasswordInputValidator;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.UserService;
import de.caffeine.kitty.web.common.InputFieldFeedbackContainer;
import de.caffeine.kitty.web.validation.JSR303FormValidator;


/**
 * Panel to manage the profile (account details) of a user.
 */
@Configurable
@SuppressWarnings("serial")
public class ManageUserProfilePanel extends Panel {

	private static final String CONTAINER_EXTENDSION = "_container";
	private static final String UPDATE_FEEDBACK = "update-feedback";
	private static final String WICKETID_SUBMIT = "update-submit";
	private static final String WICKETID_PUBLISH = "update-publish";
	private static final String WICKETID_FULLNAME = "update-fullname";
	private static final String WICKETID_DISPLAYNAME = "update-displayname";
	private static final String WICKETID_REPEAT_PASSWORD = "update-repeat-password";
	private static final String WICKETID_OLD_PASSWORD = "update-old-password";
	private static final String WICKETID_PASSWORD = "update-password";
	private static final String WICKETID_EMAIL = "update-email";

	@Autowired
	private transient UserService platformsService;

	/**
	 * Constructor.
	 * <p/>
	 * This panel does not receive input model objects from outside
	 * as it gathers all relevant data in a form and creates a user.
	 * @param id Component-ID.
	 */
	public ManageUserProfilePanel(String id, IModel<User> user) {
		super(id, user);
		add(new FeedbackPanel(UPDATE_FEEDBACK, new ContainerFeedbackMessageFilter(this))
				.setOutputMarkupId(true)
				.setVisibilityAllowed(false));

		CompoundPropertyModel<User> formModel = new CompoundPropertyModel<User>(user);
		
		final Form<User> form = new Form<User>("update-form", formModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				platformsService.saveUser(getModelObject());
			}
			
			@Override
			protected void onError() {
				super.onError();
				ManageUserProfilePanel.this.get(UPDATE_FEEDBACK).setVisibilityAllowed(true);
			}
		};
		add(form);

		form.add(new InputFieldFeedbackContainer(WICKETID_EMAIL+ CONTAINER_EXTENDSION)
				.add(new EmailTextField(WICKETID_EMAIL,formModel.<String> bind("email"))
				.setMarkupId(WICKETID_EMAIL)));

		PasswordTextField password = new PasswordTextField(WICKETID_PASSWORD, formModel.<String> bind("password"));
		form.add(new InputFieldFeedbackContainer(WICKETID_PASSWORD+CONTAINER_EXTENDSION)
				.add(password.setMarkupId(WICKETID_PASSWORD)));

		PasswordTextField passwordRepeat = new PasswordTextField(WICKETID_REPEAT_PASSWORD, new Model<String>(""));
		form.add(new InputFieldFeedbackContainer(WICKETID_REPEAT_PASSWORD + CONTAINER_EXTENDSION)
				.add(passwordRepeat.setMarkupId(WICKETID_REPEAT_PASSWORD)));

		form.add(new InputFieldFeedbackContainer(WICKETID_DISPLAYNAME+ CONTAINER_EXTENDSION)
				.add(new TextField<String>(WICKETID_DISPLAYNAME, formModel.<String> bind("displayName"))
				.setMarkupId(WICKETID_DISPLAYNAME)));

		form.add(new InputFieldFeedbackContainer(WICKETID_FULLNAME+ CONTAINER_EXTENDSION)
				.add(new TextField<String>(WICKETID_FULLNAME, formModel.<String> bind("fullName"))
				.setMarkupId(WICKETID_FULLNAME)));

		form.add(new CheckBox(WICKETID_PUBLISH, formModel
				.<Boolean> bind("shareStatistics"))
				.setMarkupId(WICKETID_PUBLISH));

		form.add(new InputFieldFeedbackContainer(WICKETID_OLD_PASSWORD + CONTAINER_EXTENDSION)
				.add(new PasswordTextField(WICKETID_OLD_PASSWORD, Model.of(""))
				.setMarkupId(WICKETID_OLD_PASSWORD)));

		form.add(new Button(WICKETID_SUBMIT)
				.setMarkupId(WICKETID_SUBMIT));

		// add validator to ensure that password and repeated password are equal
		form.add(new EqualPasswordInputValidator(password, passwordRepeat));
		form.add(new JSR303FormValidator());
	}
}
