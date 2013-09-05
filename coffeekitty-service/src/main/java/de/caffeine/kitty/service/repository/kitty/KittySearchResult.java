package de.caffeine.kitty.service.repository.kitty;

import java.io.Serializable;
import java.util.List;

import de.caffeine.kitty.entities.Kitty;


@SuppressWarnings("serial")
public class KittySearchResult implements Serializable{
	public Long total;
	public List<Kitty> kitties;
}
