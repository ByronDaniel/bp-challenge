package com.challenge.client.infrastructure.output.repository;

import com.challenge.client.infrastructure.output.repository.entity.ClientEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<ClientEntity, Integer> {

}