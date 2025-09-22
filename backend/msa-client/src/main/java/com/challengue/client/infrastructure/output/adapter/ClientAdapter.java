package com.challengue.client.infrastructure.output.adapter;

import com.challengue.client.application.output.port.ClientOutputPort;
import com.challengue.client.domain.Client;
import com.challengue.client.domain.Person;
import com.challengue.client.infrastructure.output.repository.ClientPersonRepository;
import com.challengue.client.infrastructure.output.repository.ClientRepository;
import com.challengue.client.infrastructure.output.repository.PersonRepository;
import com.challengue.client.infrastructure.output.repository.mapper.ClientEntityMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientAdapter implements ClientOutputPort {

  ClientEntityMapper clientEntityMapper;
  ClientPersonRepository clientPersonRepository;
  ClientRepository clientRepository;
  PersonRepository personRepository;

  @Override
  public Flux<Client> findAllClientsWithPerson() {
    return clientPersonRepository.findAllWithPerson()
        .map(tuple2 -> clientEntityMapper.toClient(tuple2.getT1(), tuple2.getT2()));
  }

  @Override
  public Mono<Client> findClientByIdWithPerson(Integer id) {
    return clientPersonRepository.findByIdWithPerson(id)
        .map(tuple2 -> clientEntityMapper.toClient(tuple2.getT1(), tuple2.getT2()));
  }

  @Override
  public Mono<Client> findClientById(Integer id) {
    return clientRepository.findById(id)
        .map(clientEntityMapper::toClient);
  }

  @Override
  public Mono<Person> findPersonByIdentification(String identification) {
    return personRepository.findByIdentification(identification)
        .map(clientEntityMapper::toPerson);
  }

  @Override
  public Mono<Client> saveClient(Client client) {
    return clientRepository.save(clientEntityMapper.toClientEntity(client))
        .map(client1 -> {
          client.setClientId(client1.getClientId());
          return client;
        });
  }

  @Override
  public Mono<Person> savePerson(Person person) {
    return personRepository.save(clientEntityMapper.toPersonEntity(person))
        .map(clientEntityMapper::toPerson);
  }

  @Override
  public Mono<Void> deletePersonById(Integer id) {
    return personRepository.deleteById(id);
  }
}
