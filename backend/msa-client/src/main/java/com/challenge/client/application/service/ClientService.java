package com.challenge.client.application.service;

import static com.challenge.client.application.service.utils.Constants.CLIENT_ALREADY_EXISTS;
import static com.challenge.client.application.service.utils.Constants.CLIENT_NOT_FOUND;

import com.challenge.client.application.input.port.ClientInputPort;
import com.challenge.client.application.output.port.ClientOutputPort;
import com.challenge.client.domain.Client;
import com.challenge.client.infrastructure.exception.ConflictException;
import com.challenge.client.infrastructure.exception.NotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientService implements ClientInputPort {

  ClientOutputPort clientOutputPort;
  TransactionalOperator transactionalOperator;

  @Override
  public Flux<Client> getAll() {
    return clientOutputPort.findAllClientsWithPerson();
  }

  @Override
  public Mono<Client> getById(Integer id) {
    return clientOutputPort.findClientByIdWithPerson(id)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)));
  }

  @Override
  public Mono<Client> save(Client client) {
    return clientOutputPort.findPersonByIdentification(client.getIdentification())
        .flatMap(existingPerson -> Mono.<Client>error(new ConflictException(CLIENT_ALREADY_EXISTS)))
        .switchIfEmpty(Mono.defer(() ->
            clientOutputPort.savePerson(client)
                .flatMap(personSaved -> {
                  client.setPersonId(personSaved.getPersonId());
                  return clientOutputPort.saveClient(client);
                }))
        ).as(transactionalOperator::transactional);
  }

  @Override
  public Mono<Client> updateById(Integer id, Client client) {
    return clientOutputPort.findClientByIdWithPerson(id)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flatMap(existingClient -> Mono.defer(() -> {
          client.setClientId(existingClient.getClientId());
          client.setPersonId(existingClient.getPersonId());
          client.setIdentification(existingClient.getIdentification());
          return clientOutputPort.savePerson(client)
              .flatMap(personSaved -> clientOutputPort.saveClient(client));
        })).as(transactionalOperator::transactional);
  }

  @Override
  public Mono<Void> deleteById(Integer id) {
    return clientOutputPort.findClientById(id)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flatMap(client -> Mono.defer(() ->
            clientOutputPort.deletePersonById(client.getPersonId()))
        ).as(transactionalOperator::transactional);
  }
}