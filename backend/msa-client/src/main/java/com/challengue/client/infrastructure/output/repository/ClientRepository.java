package com.challengue.client.infrastructure.output.repository;

import com.challengue.client.infrastructure.output.repository.entity.ClientEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends ReactiveCrudRepository<ClientEntity, Integer> {

}