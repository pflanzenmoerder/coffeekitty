package de.caffeine.kitty.service.repository.kitty;


public interface KittyRepositoryCustom {
	KittySearchResult findByNameAndPageIdWithPageSize(String name, Integer page, Integer pageSize);
}
