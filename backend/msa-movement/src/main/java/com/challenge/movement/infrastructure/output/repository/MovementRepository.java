package com.challenge.movement.infrastructure.output.repository;

import com.challenge.movement.infrastructure.output.repository.entity.MovementEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface MovementRepository extends ReactiveCrudRepository<MovementEntity, Integer> {

  Flux<MovementEntity> findByAccountId(Integer accountId);
}