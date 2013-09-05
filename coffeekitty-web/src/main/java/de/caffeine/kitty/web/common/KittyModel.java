package de.caffeine.kitty.web.common;

import org.apache.wicket.model.LoadableDetachableModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import de.caffeine.kitty.entities.Kitty;
import de.caffeine.kitty.service.KittyService;


@SuppressWarnings("serial")
@Configurable
public class KittyModel extends LoadableDetachableModel<Kitty>{
	@Autowired
	private transient KittyService kittyService;
	private final String kittyId;
	
	public KittyModel(String kittyId) {
		super();
		this.kittyId = kittyId;
	}

	@Override
	protected Kitty load() {
		return kittyService.findKittyById(kittyId);
	}

}
