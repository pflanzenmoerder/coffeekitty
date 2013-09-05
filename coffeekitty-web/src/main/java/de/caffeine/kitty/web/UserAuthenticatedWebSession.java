package de.caffeine.kitty.web;

import org.apache.wicket.Session;
import org.apache.wicket.authroles.authentication.AuthenticatedWebSession;
import org.apache.wicket.authroles.authorization.strategies.role.Roles;
import org.apache.wicket.model.IModel;
import org.apache.wicket.protocol.http.WebSession;
import org.apache.wicket.request.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.UserService;
import de.caffeine.kitty.web.common.CurrentUserModel;


@SuppressWarnings("serial")
@Configurable
public class UserAuthenticatedWebSession extends AuthenticatedWebSession {

	private String userId = null;

	private Boolean admin =Boolean.FALSE;
	
	@Autowired
	private transient UserService platformsService;

	/**
	 * Constructor.
	 * @param request The current request.
	 */
	public UserAuthenticatedWebSession(Request request) {
		super(request);
	}

	/**
	 * Get the session.
	 * @return The current web session.
	 */
	public static UserAuthenticatedWebSession get() {
		return (UserAuthenticatedWebSession) Session.get();
	}

	/**
	 * Authenticate a user by username and password credentials.
	 * @param username Possibly an email address.
	 * @param password A password.
	 * @return The result, <code>true</code> if successfull.
	 */
	@Override
	public boolean authenticate(String username, String password) {
		if ("admin".equals(username) && "123".equals(password)) {
			admin = Boolean.TRUE;
			return true;
		}
		else {
			User user = platformsService.authenticateAndGet(username, password);

			if (user != null) {
				WebSession.get().replaceSession();
				userId = user.getId();
				return true;
			}	
		}
		
		return false;
	}

	public void setUser(User user) {
		WebSession.get().replaceSession();
		userId = user.getId();
		signIn(true);
	}
	
	/**
	 * Get the roles of the current authenticated user.
	 * Returns an empty object if no user is authenticated.
	 * @return The roles.
	 */
	@Override
	public Roles getRoles() {
		Roles roles = new Roles();
		if (isAdmin()) {
			roles.add(Roles.ADMIN);
		}
		else if (isSignedIn()) {
			roles.add(Roles.USER);
		}
		return roles;
	}

	public IModel<User> getUserModel() {
		return new CurrentUserModel(userId);
	}

	public Boolean isAdmin() {
		return admin;
	}

}
