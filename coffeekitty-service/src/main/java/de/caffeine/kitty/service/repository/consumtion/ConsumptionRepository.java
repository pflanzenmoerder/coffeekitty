package de.caffeine.kitty.service.repository.consumtion;

import org.springframework.data.repository.CrudRepository;

import de.caffeine.kitty.entities.Consumption;

public interface ConsumptionRepository extends CrudRepository<Consumption, String>, ConsumptionRepositoryCustom{
}
