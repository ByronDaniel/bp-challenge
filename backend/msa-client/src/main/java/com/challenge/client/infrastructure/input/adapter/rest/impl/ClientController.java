package com.challenge.client.infrastructure.input.adapter.rest.impl;

import com.challenge.client.application.input.port.ClientInputPort;
import com.challenge.client.infrastructure.input.adapter.rest.mapper.ClientDtoMapper;
import com.challenge.services.server.ClientesApi;
import com.challenge.services.server.models.ClientRequestDto;
import com.challenge.services.server.models.ClientResponseDto;
import java.net.URI;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ClientController implements ClientesApi {

  ClientInputPort clientInputPort;
  ClientDtoMapper clientDtoMapper;

  @Override
  public Mono<ResponseEntity<Flux<ClientResponseDto>>> getAll(ServerWebExchange exchange) {
    return Mono.just(ResponseEntity.ok(clientInputPort.getAll()
        .map(clientDtoMapper::toClientResponseDto)));
  }

  @Override
  public Mono<ResponseEntity<ClientResponseDto>> getById(Integer id,
      ServerWebExchange exchange) {
    return clientInputPort.getById(id)
        .map(clientDtoMapper::toClientResponseDto)
        .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<ClientResponseDto>> save(ClientRequestDto clientRequestDto,
      ServerWebExchange exchange) {
    return clientInputPort.save(clientDtoMapper.toClient(clientRequestDto))
        .map(clientDtoMapper::toClientResponseDto)
        .map(clientResponseDto ->
            ResponseEntity.created(URI.create("/clientes/" + clientResponseDto.getId()))
                .body(clientResponseDto));
  }

  @Override
  public Mono<ResponseEntity<ClientResponseDto>> updateById(Integer id,
      ClientRequestDto clientRequestDto,
      ServerWebExchange exchange) {
    return clientInputPort.updateById(id, clientDtoMapper.toClient(clientRequestDto))
        .map(clientDtoMapper::toClientResponseDto)
        .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteById(Integer id, ServerWebExchange exchange) {
    return clientInputPort.deleteById(id)
        .thenReturn(ResponseEntity.noContent().build());
  }
}
