package de.caffeine.kitty.web.user.signup;

import org.apache.wicket.Application;
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
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.dao.DataIntegrityViolationException;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.UserService;
import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.common.InputFieldFeedbackContainer;
import de.caffeine.kitty.web.validation.JSR303FormValidator;


/**
 * Panel to sign up a new user.
 */
@Configurable
public class SignUpUserPanel extends Panel {
	private static final String CONTAINER_EXTENSION = "_container";

	private static final String SIGNUP_FEEDBACK = "signup-feedback";

	private static final String SIGNUP_FORM = "signup-form";

	private static final long serialVersionUID = 1L;

	private static final Logger LOG = LoggerFactory
			.getLogger(SignUpUserPanel.class);

	private static final String WICKETID_SIGNUP_SUBMIT = "signup-submit";
	private static final String WICKETID_SIGNUP_PUBLISH = "signup-publish";
	private static final String WICKETID_SIGNUP_FULLNAME = "signup-fullname";
	private static final String WICKETID_SIGNUP_DISPLAYNAME = "signup-displayname";
	private static final String WICKETID_SIGNUP_REPEAT_PASSWORD = "signup-repeat-password";
	private static final String WICKETID_SIGNUP_PASSWORD = "signup-password";
	private static final String WICKETID_SIGNUP_EMAIL = "signup-email";

	@Autowired
	private transient UserService platformsService;

	/**
	 * Constructor.
	 * <p/>
	 * This panel does not receive input model objects from outside as it
	 * gathers all relevant data in a form and creates a user.
	 * 
	 * @param id
	 *            Component-ID.
	 */
	public SignUpUserPanel(String id) {
		super(id);
		add(new FeedbackPanel(
				SIGNUP_FEEDBACK, new ContainerFeedbackMessageFilter(this))
				.setOutputMarkupId(true)
				.setVisibilityAllowed(false));

		CompoundPropertyModel<User> formModel = new CompoundPropertyModel<User>(
				new User());

		final Form<User> form = new Form<User>(SIGNUP_FORM, formModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				try {
					User user = getModelObject();
					platformsService.createUser(user);
					UserAuthenticatedWebSession.get().setUser(user);
					setResponsePage(Application.get().getHomePage());

				} catch (DataIntegrityViolationException e) {
					LOG.warn("catched exception on create new user", e);
					final ValidationError error = new ValidationError();
					error.addKey("error.user.exists");
					error.setVariable("username", getModelObject().getEmail());
					get(WICKETID_SIGNUP_EMAIL + CONTAINER_EXTENSION).get(
							WICKETID_SIGNUP_EMAIL).error(error);
					onError();
				}

			}

			@Override
			protected void onError() {
				super.onError();
				SignUpUserPanel.this.get(SIGNUP_FEEDBACK).setVisibilityAllowed(true);
			}
		};
		add(form);

		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNUP_EMAIL + CONTAINER_EXTENSION)
					.add(new EmailTextField(WICKETID_SIGNUP_EMAIL, formModel.<String> bind("email"))
					.setMarkupId(WICKETID_SIGNUP_EMAIL)));

		PasswordTextField password = new PasswordTextField(WICKETID_SIGNUP_PASSWORD, formModel.<String> bind("password"));
		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNUP_PASSWORD + CONTAINER_EXTENSION)
					.add(password.setMarkupId(WICKETID_SIGNUP_PASSWORD)));

		PasswordTextField passwordRepeat = new PasswordTextField(WICKETID_SIGNUP_REPEAT_PASSWORD, new Model<String>(""));
		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNUP_REPEAT_PASSWORD + CONTAINER_EXTENSION)
					.add(passwordRepeat.setMarkupId(WICKETID_SIGNUP_REPEAT_PASSWORD)));

		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNUP_DISPLAYNAME + CONTAINER_EXTENSION)
					.add(new TextField<String>(WICKETID_SIGNUP_DISPLAYNAME, formModel.<String> bind("displayName"))
							.setMarkupId(WICKETID_SIGNUP_DISPLAYNAME)));

		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNUP_FULLNAME + CONTAINER_EXTENSION)
					.add(new TextField<String>(WICKETID_SIGNUP_FULLNAME, formModel.<String> bind("fullName"))
							.setMarkupId(WICKETID_SIGNUP_FULLNAME)));

		form.add(new CheckBox(WICKETID_SIGNUP_PUBLISH, formModel.<Boolean> bind("shareStatistics"))
					.setMarkupId(WICKETID_SIGNUP_PUBLISH));

		form.add(new Button(WICKETID_SIGNUP_SUBMIT)
					.setMarkupId(WICKETID_SIGNUP_SUBMIT));

		// add validator to ensure that password and repeated password are equal
		form.add(new EqualPasswordInputValidator(password, passwordRepeat));
		form.add(new JSR303FormValidator());
	}

}
