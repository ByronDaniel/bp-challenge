package com.challenge.client.infrastructure.output.adapter;

import com.challenge.client.application.output.port.ClientOutputPort;
import com.challenge.client.domain.Client;
import com.challenge.client.domain.Person;
import com.challenge.client.infrastructure.output.repository.ClientPersonRepository;
import com.challenge.client.infrastructure.output.repository.ClientRepository;
import com.challenge.client.infrastructure.output.repository.PersonRepository;
import com.challenge.client.infrastructure.output.repository.mapper.ClientEntityMapper;
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
  public Flux<Client> findAll() {
    return clientPersonRepository.findAll()
        .map(tuple2 -> clientEntityMapper.toClient(tuple2.getT1(), tuple2.getT2()));
  }

  @Override
  public Mono<Client> findByIdentification(String identification) {
    return clientPersonRepository.findByIdentification(identification)
        .map(tuple2 -> clientEntityMapper.toClient(tuple2.getT1(), tuple2.getT2()));
  }

  @Override
  public Mono<Client> saveClient(Client client) {
    return clientRepository.save(clientEntityMapper.toClientEntity(client))
        .map(client1 -> client);
  }

  @Override
  public Mono<Person> savePerson(Person person) {
    return personRepository.save(clientEntityMapper.toPersonEntity(person))
        .map(clientEntityMapper::toPerson);
  }
}
