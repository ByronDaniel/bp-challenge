package com.challenge.report.infrastructure.output.adapter;

import com.challenge.report.application.output.port.ClientOutputPort;
import com.challenge.report.domain.Client;
import com.challenge.report.infrastructure.output.repository.mapper.ClientMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import task___buildSpringClient0__property__packageName_.clients0.client.ClientManagementApi;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientAdapter implements ClientOutputPort {

  ClientManagementApi clientManagementApi;
  ClientMapper clientMapper;

  @Override
  public Mono<Client> getById(Integer id) {
    return clientManagementApi.getById(id)
        .map(clientMapper::toClient);
  }
}
