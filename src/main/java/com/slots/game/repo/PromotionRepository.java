package com.slots.game.repo;

import com.slots.game.model.Promotion;
import org.springframework.data.repository.CrudRepository;

public interface PromotionRepository extends CrudRepository<Promotion, String> {
    public Promotion findById(int id);
}
