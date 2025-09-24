package com.challenge.client.application.service;

import static com.challenge.client.application.service.utils.Constants.CLIENT_ALREADY_EXISTS;
import static com.challenge.client.application.service.utils.Constants.CLIENT_NOT_ACTIVE;
import static com.challenge.client.application.service.utils.Constants.CLIENT_NOT_FOUND;

import com.challenge.client.application.input.port.ClientInputPort;
import com.challenge.client.application.output.port.ClientOutputPort;
import com.challenge.client.domain.Client;
import com.challenge.client.infrastructure.exception.ConflictException;
import com.challenge.client.infrastructure.exception.NotFoundException;
import java.util.Objects;
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
  public Flux<Client> getAll(String identification) {
    if (Objects.nonNull(identification)) {
      return findClientByIdentification(identification);
    }
    return clientOutputPort.findAll();
  }

  private Flux<Client> findClientByIdentification(String identification) {
    return clientOutputPort.findByIdentification(identification)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flux();
  }

  @Override
  public Mono<Client> save(Client client) {
    return clientOutputPort.findByIdentification(client.getIdentification())
        .flatMap(existingPerson -> Mono.<Client>error(new ConflictException(CLIENT_ALREADY_EXISTS)))
        .switchIfEmpty(Mono.defer(() ->
            clientOutputPort.savePerson(client)
                .flatMap(personSaved -> {
                  client.setPersonId(personSaved.getPersonId());
                  client.setStatus(true);
                  return clientOutputPort.saveClient(client);
                }))
        ).as(transactionalOperator::transactional);
  }

  @Override
  public Mono<Client> update(Client client) {
    return clientOutputPort.findByIdentification(client.getIdentification())
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flatMap(existingClient -> validateClientIsActive(existingClient)
            .then(Mono.defer(() -> {
              updateClientData(existingClient, client);
              return saveUpdatedClient(existingClient);
            }))
        ).as(transactionalOperator::transactional);
  }

  private Mono<Void> validateClientIsActive(Client client) {
    if (Boolean.FALSE.equals(client.getStatus())) {
      return Mono.error(new ConflictException(CLIENT_NOT_ACTIVE));
    }
    return Mono.empty();
  }

  private void updateClientData(Client existingClient, Client newClientData) {
    existingClient.setPassword(newClientData.getPassword());
    existingClient.setName(newClientData.getName());
    existingClient.setGender(newClientData.getGender());
    existingClient.setAge(newClientData.getAge());
    existingClient.setAddress(newClientData.getAddress());
    existingClient.setPhone(newClientData.getPhone());
  }

  private Mono<Client> saveUpdatedClient(Client client) {
    return clientOutputPort.savePerson(client)
        .flatMap(personSaved -> clientOutputPort.saveClient(client));
  }

  @Override
  public Mono<Void> deleteByIdentification(String identification) {
    return clientOutputPort.findByIdentification(identification)
        .switchIfEmpty(Mono.error(new NotFoundException(CLIENT_NOT_FOUND)))
        .flatMap(client -> validateClientIsActive(client)
            .then(Mono.defer(() -> {
              client.setStatus(false);
              return clientOutputPort.saveClient(client).then();
            }))
        ).as(transactionalOperator::transactional);
  }
}