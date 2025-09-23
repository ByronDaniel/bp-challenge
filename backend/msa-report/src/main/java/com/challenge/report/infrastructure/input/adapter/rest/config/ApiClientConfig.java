package com.challenge.report.infrastructure.input.adapter.rest.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import task___buildSpringClient0__property__packageName_.clients0.client.ClientManagementApi;
import task___buildSpringClient1__property__packageName_.clients1.client.AccountManagementApi;
import task___buildSpringClient2__property__packageName_.clients2.client.MovementsApi;

@Configuration
public class ApiClientConfig {

  @Value("${spring.services.msa-client.url}")
  private String msaClientUrl;

  @Value("${spring.services.msa-account.url}")
  private String msaAccountUrl;

  @Value("${spring.services.msa-movement.url}")
  private String msaMovementUrl;

  @NotNull
  @Bean
  public AccountManagementApi accountManagementApi() {
    final var accountManagementApi = new AccountManagementApi();
    accountManagementApi.getApiClient()
        .setBasePath(msaAccountUrl);
    return accountManagementApi;
  }

  @NotNull
  @Bean
  public MovementsApi movementsApi() {
    final var movementsApi = new MovementsApi();
    movementsApi.getApiClient()
        .setBasePath(msaMovementUrl);
    return movementsApi;
  }

  @NotNull
  @Bean
  public ClientManagementApi clientManagementApi() {
    final var clientManagementApi = new ClientManagementApi();
    clientManagementApi.getApiClient()
        .setBasePath(msaClientUrl);
    return clientManagementApi;
  }
}
