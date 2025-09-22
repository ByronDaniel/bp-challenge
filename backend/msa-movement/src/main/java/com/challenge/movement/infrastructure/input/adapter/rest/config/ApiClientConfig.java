package com.challenge.movement.infrastructure.input.adapter.rest.config;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import task___buildSpringClient0__property__packageName_.clients0.client.AccountManagementApi;

@Configuration
public class ApiClientConfig {

  @Value("${spring.services.msa-account.url}")
  private String msaAccountUrl;

  @NotNull
  @Bean
  public AccountManagementApi accountManagementApi() {
    final var accountManagementApi = new AccountManagementApi();
    accountManagementApi.getApiClient()
        .setBasePath(msaAccountUrl);
    return accountManagementApi;
  }
}
