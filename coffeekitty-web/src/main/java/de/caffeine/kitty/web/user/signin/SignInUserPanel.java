package de.caffeine.kitty.web.user.signin;

import org.apache.wicket.Application;
import org.apache.wicket.feedback.ContainerFeedbackMessageFilter;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;
import org.apache.wicket.validation.ValidationError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.caffeine.kitty.web.UserAuthenticatedWebSession;
import de.caffeine.kitty.web.common.InputFieldFeedbackContainer;

public class SignInUserPanel extends Panel {

	private static final String CONTAINER_EXTENSION = "_container";

	private static final Logger LOG = LoggerFactory
			.getLogger(SignInUserPanel.class);

	private static final String SIGNIN_FEEDBACK = "signin-feedback";
	private static final String WICKETID_SIGNIN_EMAIL = "signin-email";
	private static final String WICKETID_SIGNIN_EMAIL_CONTAINER = WICKETID_SIGNIN_EMAIL
			+ CONTAINER_EXTENSION;
	private static final String WICKETID_SIGNIN_PASSWORD = "signin-password";
	private static final String WICKETID_SIGNIN_PASSWORD_CONTAINER = WICKETID_SIGNIN_PASSWORD
			+ CONTAINER_EXTENSION;

	private static final long serialVersionUID = 1L;

	public SignInUserPanel(String id) {
		super(id);
		
		add(new FeedbackPanel(SIGNIN_FEEDBACK, new ContainerFeedbackMessageFilter(this))
				.setOutputMarkupId(true)
				.setVisibilityAllowed(false));

		final Form<Void> form = new Form<Void>("signin-form") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit() {
				boolean signedin = UserAuthenticatedWebSession.get().signIn(
						get(WICKETID_SIGNIN_EMAIL_CONTAINER).get(
								WICKETID_SIGNIN_EMAIL)
								.getDefaultModelObjectAsString(),
						get(WICKETID_SIGNIN_PASSWORD_CONTAINER).get(
								WICKETID_SIGNIN_PASSWORD)
								.getDefaultModelObjectAsString());

				if (!signedin) {
					final ValidationError error = new ValidationError();
					error.addKey("error.user.login.denied");
					error(error);
					onError();
					LOG.warn("Login failed for user "+get(WICKETID_SIGNIN_EMAIL_CONTAINER).get(
								WICKETID_SIGNIN_EMAIL).getDefaultModelObjectAsString());
				} else {
					setResponsePage(Application.get().getHomePage());
				}
			}
			
			@Override
			protected void onError() {
				super.onError();
				SignInUserPanel.this.get(SIGNIN_FEEDBACK).setVisibilityAllowed(true);
			}
		};
		add(form);

		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNIN_EMAIL + CONTAINER_EXTENSION)
				.add(new TextField<String>(WICKETID_SIGNIN_EMAIL,Model.of(""))
					.setRequired(true)
					.setMarkupId(WICKETID_SIGNIN_EMAIL)));

		form.add(new InputFieldFeedbackContainer(WICKETID_SIGNIN_PASSWORD + CONTAINER_EXTENSION)
				.add(new PasswordTextField(WICKETID_SIGNIN_PASSWORD, Model.of(""))
				.setRequired(true)
				.setMarkupId(WICKETID_SIGNIN_PASSWORD)));
		
		form.add(new Button("signin-submit").setMarkupId("signin-submit"));

	}

}
