package com.challengue.client.infrastructure.output.repository;

import com.challengue.client.infrastructure.output.repository.entity.PersonEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface PersonRepository extends ReactiveCrudRepository<PersonEntity, Integer> {

  Mono<PersonEntity> findByIdentification(String identification);
}