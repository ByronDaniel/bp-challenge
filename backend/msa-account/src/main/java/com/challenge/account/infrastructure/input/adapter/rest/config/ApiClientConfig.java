package com.challenge.account.infrastructure.input.adapter.rest.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import task___buildSpringClient0__property__packageName_.clients0.client.ClientManagementApi;

@Configuration
public class ApiClientConfig {

  @Value("${spring.services.msa-client.url}")
  private String msaClientUrl;

  @NotNull
  @Bean
  public ClientManagementApi clientManagementApi() {
    final var clientManagementApi = new ClientManagementApi();
    clientManagementApi.getApiClient()
        .setBasePath(msaClientUrl);
    return clientManagementApi;
  }
}
