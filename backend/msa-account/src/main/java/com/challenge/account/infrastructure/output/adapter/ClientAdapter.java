package com.challenge.account.infrastructure.output.adapter;

import com.challenge.account.application.output.port.ClientOutputPort;
import com.challenge.account.domain.Client;
import com.challenge.account.infrastructure.output.repository.mapper.ClientEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import task___buildSpringClient0__property__packageName_.clients0.client.ClientManagementApi;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class ClientAdapter implements ClientOutputPort {

  ClientEntityMapper clientEntityMapper;
  ClientManagementApi clientManagementApi;

  @Override
  public Flux<Client> getAll(String identification) {
    return clientManagementApi.getAll(identification)
        .map(clientEntityMapper::toClient);
  }
}
