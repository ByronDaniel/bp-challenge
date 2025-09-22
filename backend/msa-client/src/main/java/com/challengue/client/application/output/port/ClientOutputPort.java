package com.challengue.client.application.output.port;

import com.challengue.client.domain.Client;
import com.challengue.client.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClientOutputPort {

  Flux<Client> findAllClientsWithPerson();

  Mono<Client> findClientByIdWithPerson(Integer id);

  Mono<Client> findClientById(Integer id);

  Mono<Person> findPersonByIdentification(String identification);

  Mono<Client> saveClient(Client client);

  Mono<Person> savePerson(Person person);

  Mono<Void> deletePersonById(Integer id);
}
