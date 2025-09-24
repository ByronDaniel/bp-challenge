package com.challenge.report.infrastructure.input.adapter.rest.config;

import io.netty.channel.ChannelOption;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
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

  @Value("${webclient.timeout.connect:5000}")
  private int connectTimeout;

  @Value("${webclient.timeout.read:15000}")
  private int readTimeout;

  @Value("${webclient.timeout.write:15000}")
  private int writeTimeout;

  @Bean
  public WebClient webClient() {
    HttpClient httpClient = HttpClient.create()
        .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectTimeout)
        .responseTimeout(Duration.ofMillis(readTimeout))
        .doOnConnected(conn -> conn
            .addHandlerLast(new io.netty.handler.timeout.ReadTimeoutHandler(readTimeout / 1000))
            .addHandlerLast(new io.netty.handler.timeout.WriteTimeoutHandler(writeTimeout / 1000)));

    return WebClient.builder()
        .clientConnector(new ReactorClientHttpConnector(httpClient))
        .build();
  }

  @NotNull
  @Bean
  public AccountManagementApi accountManagementApi() {
    final var accountManagementApi = new AccountManagementApi();
    accountManagementApi.getApiClient()
        .setWebClient(webClient())
        .setBasePath(msaAccountUrl);
    return accountManagementApi;
  }

  @NotNull
  @Bean
  public MovementsApi movementsApi() {
    final var movementsApi = new MovementsApi();
    movementsApi.getApiClient()
        .setWebClient(webClient())
        .setBasePath(msaMovementUrl);
    return movementsApi;
  }

  @NotNull
  @Bean
  public ClientManagementApi clientManagementApi() {
    final var clientManagementApi = new ClientManagementApi();
    clientManagementApi.getApiClient()
        .setWebClient(webClient())
        .setBasePath(msaClientUrl);
    return clientManagementApi;
  }
}
