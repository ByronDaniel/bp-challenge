package com.challenge.client.application.output.port;

import com.challenge.client.domain.Client;
import com.challenge.client.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientOutputPort {

  Flux<Client> findAll();

  Mono<Client> findByIdentification(String identification);
  
  Mono<Client> saveClient(Client client);

  Mono<Person> savePerson(Person person);

}
