package com.challenge.account.infrastructure.input.adapter.rest.impl;

import com.challenge.account.application.input.port.AccountInputPort;
import com.challenge.account.infrastructure.input.adapter.rest.mapper.AccountDtoMapper;
import com.challenge.services.server.CuentasApi;
import com.challenge.services.server.models.AccountRequestDto;
import com.challenge.services.server.models.AccountRequestPutDto;
import com.challenge.services.server.models.AccountResponseDto;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class AccountController implements CuentasApi {

  AccountInputPort accountInputPort;
  AccountDtoMapper accountDtoMapper;

  @Override
  public Mono<ResponseEntity<AccountResponseDto>> getById(Integer id,
      ServerWebExchange exchange) {
    return accountInputPort.getById(id)
        .map(accountDtoMapper::toAccountResponseDto)
        .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<AccountResponseDto>> save(AccountRequestDto accountRequestDto,
      ServerWebExchange exchange) {
    return accountInputPort.save(accountDtoMapper.toAccount(accountRequestDto))
        .map(accountDtoMapper::toAccountResponseDto)
        .map(accountResponseDto ->
            ResponseEntity.created(URI.create("/cuentas/" + accountResponseDto.getAccountId()))
                .body(accountResponseDto));
  }

  @Override
  public Mono<ResponseEntity<AccountResponseDto>> updateById(Integer id,
      AccountRequestPutDto accountRequestPutDto, ServerWebExchange exchange) {
    return accountInputPort.updateById(id, accountDtoMapper.toAccount(accountRequestPutDto))
        .map(accountDtoMapper::toAccountResponseDto)
        .map(ResponseEntity::ok);
  }

  @Override
  public Mono<ResponseEntity<Void>> deleteById(Integer id, ServerWebExchange exchange) {
    return accountInputPort.deleteById(id)
        .thenReturn(ResponseEntity.noContent().build());
  }

  @Override
  public Mono<ResponseEntity<Flux<AccountResponseDto>>> getAll(String accountNumber,
      String clientIdentification, ServerWebExchange exchange) {
    return Mono.just(
        ResponseEntity.ok(accountInputPort.getAllByFilter(accountNumber, clientIdentification)
            .map(accountDtoMapper::toAccountResponseDto)));
  }

}
