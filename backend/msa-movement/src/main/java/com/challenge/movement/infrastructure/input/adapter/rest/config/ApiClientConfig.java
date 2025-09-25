package com.challenge.movement.infrastructure.input.adapter.rest.config;

import io.netty.channel.ChannelOption;
import jakarta.validation.constraints.NotNull;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import task___buildSpringClient0__property__packageName_.clients0.client.AccountManagementApi;

@Configuration
public class ApiClientConfig {

  @Value("${spring.services.msa-account.url}")
  private String msaAccountUrl;

  @Value("${webclient.timeout.connect:5000}")
  private int connectTimeout;

  @Value("${webclient.timeout.read:10000}")
  private int readTimeout;

  @Value("${webclient.timeout.write:10000}")
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
        .setBasePath(msaAccountUrl);
    return accountManagementApi;
  }
}
