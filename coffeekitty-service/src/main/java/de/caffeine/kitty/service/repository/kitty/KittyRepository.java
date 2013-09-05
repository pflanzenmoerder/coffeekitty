package de.caffeine.kitty.service.repository.kitty;

import org.springframework.data.repository.CrudRepository;

import de.caffeine.kitty.entities.Kitty;


public interface KittyRepository extends CrudRepository<Kitty, String>, KittyRepositoryCustom{
}
