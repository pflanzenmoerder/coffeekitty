package de.caffeine.kitty.web.common;

import org.apache.wicket.model.LoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.User;
import de.caffeine.kitty.service.UserService;


@SuppressWarnings("serial")
@Configurable
public class CurrentUserModel extends LoadableDetachableModel<User>{
	@Autowired
	private transient UserService platformsService;
	private final String userId;
	
	public CurrentUserModel(String userId) {
		super();
		this.userId = userId;
	}

	@Override
	protected User load() {
		return platformsService.findUserById(userId);
	}

}
