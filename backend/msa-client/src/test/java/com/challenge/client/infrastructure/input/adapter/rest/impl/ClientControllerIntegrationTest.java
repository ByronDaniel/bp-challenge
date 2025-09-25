package com.challenge.client.infrastructure.input.adapter.rest.impl;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.challenge.client.application.input.port.ClientInputPort;
import com.challenge.client.domain.Client;
import com.challenge.client.infrastructure.exception.ConflictException;
import com.challenge.client.infrastructure.exception.NotFoundException;
import com.challenge.client.infrastructure.input.adapter.rest.mapper.ClientDtoMapper;
import com.challenge.services.server.models.ClientRequestDto;
import com.challenge.services.server.models.ClientResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@WebFluxTest(ClientController.class)
@DisplayName("ClientController Integration Tests")
class ClientControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ClientInputPort clientInputPort;

    @MockitoBean
    private ClientDtoMapper clientDtoMapper;

    @Autowired
    private ObjectMapper objectMapper;

    private Client testClient;
    private ClientRequestDto testClientRequestDto;
    private ClientResponseDto testClientResponseDto;

    @BeforeEach
    void setUp() {
        testClient = new Client();
        testClient.setClientId(1);
        testClient.setPersonId(1);
        testClient.setIdentification("1234567890");
        testClient.setName("John Doe");
        testClient.setGender("Masculino");
        testClient.setAge(30);
        testClient.setAddress("123 Main St");
        testClient.setPhone("0985350314");
        testClient.setPassword("password123");
        testClient.setStatus(true);

        testClientRequestDto = new ClientRequestDto();
        testClientRequestDto.setIdentification("1234567890");
        testClientRequestDto.setName("John Doe");
        testClientRequestDto.setGender(ClientRequestDto.GenderEnum.MASCULINO);
        testClientRequestDto.setAge(30);
        testClientRequestDto.setAddress("123 Main St");
        testClientRequestDto.setPhone("0985350314");
        testClientRequestDto.setPassword("password123");

        testClientResponseDto = new ClientResponseDto();
        testClientResponseDto.setId(1);
        testClientResponseDto.setIdentification("1234567890");
        testClientResponseDto.setName("John Doe");
        testClientResponseDto.setGender(ClientResponseDto.GenderEnum.MASCULINO);
        testClientResponseDto.setAge(30);
        testClientResponseDto.setAddress("123 Main St");
        testClientResponseDto.setPhone("0985350314");
        testClientResponseDto.setPassword("password123");
    }

    @Nested
    @DisplayName("GET /clientes - getAll endpoint tests")
    class GetAllEndpointTests {

        @Test
        @DisplayName("Should return all clients when no identification parameter is provided")
        void shouldReturnAllClientsWhenNoIdentificationParameter() {
            when(clientInputPort.getAll(null))
                    .thenReturn(Flux.just(testClient));
            when(clientDtoMapper.toClientResponseDto(testClient))
                    .thenReturn(testClientResponseDto);

            webTestClient.get()
                    .uri("/clientes")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(ClientResponseDto.class)
                    .hasSize(1)
                    .contains(testClientResponseDto);

            verify(clientInputPort).getAll(null);
            verify(clientDtoMapper).toClientResponseDto(testClient);
        }

        @Test
        @DisplayName("Should return specific client when identification parameter is provided")
        void shouldReturnSpecificClientWhenIdentificationParameterProvided() {
            String identification = "1234567890";
            when(clientInputPort.getAll(identification))
                    .thenReturn(Flux.just(testClient));
            when(clientDtoMapper.toClientResponseDto(testClient))
                    .thenReturn(testClientResponseDto);

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/clientes")
                            .queryParam("identification", identification)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(ClientResponseDto.class)
                    .hasSize(1)
                    .contains(testClientResponseDto);

            verify(clientInputPort).getAll(identification);
            verify(clientDtoMapper).toClientResponseDto(testClient);
        }

        @Test
        @DisplayName("Should return empty list when no clients found")
        void shouldReturnEmptyListWhenNoClientsFound() {
            when(clientInputPort.getAll(null))
                    .thenReturn(Flux.empty());

            webTestClient.get()
                    .uri("/clientes")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectBodyList(ClientResponseDto.class)
                    .hasSize(0);

            verify(clientInputPort).getAll(null);
            verify(clientDtoMapper, never()).toClientResponseDto(any());
        }

        @Test
        @DisplayName("Should return 404 when client with identification not found")
        void shouldReturn404WhenClientNotFound() {
            String identification = "nonexistent";
            when(clientInputPort.getAll(identification))
                    .thenReturn(Flux.error(new NotFoundException("Client not found")));

            webTestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/clientes")
                            .queryParam("identification", identification)
                            .build())
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isNotFound();

            verify(clientInputPort).getAll(identification);
        }

        @Test
        @DisplayName("Should handle service errors gracefully")
        void shouldHandleServiceErrorsGracefully() {
            when(clientInputPort.getAll(null))
                    .thenReturn(Flux.error(new RuntimeException("Database connection error")));

            webTestClient.get()
                    .uri("/clientes")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().is5xxServerError();

            verify(clientInputPort).getAll(null);
        }
    }

    @Nested
    @DisplayName("POST /clientes - save endpoint tests")
    class SaveEndpointTests {

        @Test
        @DisplayName("Should create new client successfully with valid request")
        void shouldCreateNewClientSuccessfullyWithValidRequest() {
            when(clientDtoMapper.toClient(testClientRequestDto))
                    .thenReturn(testClient);
            when(clientInputPort.save(testClient))
                    .thenReturn(Mono.just(testClient));
            when(clientDtoMapper.toClientResponseDto(testClient))
                    .thenReturn(testClientResponseDto);

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(testClientRequestDto)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectHeader().contentType(MediaType.APPLICATION_JSON)
                    .expectHeader().location("/clientes/1")
                    .expectBody(ClientResponseDto.class)
                    .isEqualTo(testClientResponseDto);

            verify(clientDtoMapper).toClient(testClientRequestDto);
            verify(clientInputPort).save(testClient);
            verify(clientDtoMapper).toClientResponseDto(testClient);
        }

        @Test
        @DisplayName("Should return 409 when client already exists")
        void shouldReturn409WhenClientAlreadyExists() {
            when(clientDtoMapper.toClient(testClientRequestDto))
                    .thenReturn(testClient);
            when(clientInputPort.save(testClient))
                    .thenReturn(Mono.error(new ConflictException("Client already exists")));

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(testClientRequestDto)
                    .exchange()
                    .expectStatus().isEqualTo(409);

            verify(clientDtoMapper).toClient(testClientRequestDto);
            verify(clientInputPort).save(testClient);
        }

        @Test
        @DisplayName("Should return 400 for invalid request data")
        void shouldReturn400ForInvalidRequestData() {
            ClientRequestDto invalidRequest = new ClientRequestDto();
            invalidRequest.setName("");
            invalidRequest.setAge(15);
            invalidRequest.setIdentification("123");

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(invalidRequest)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should handle malformed JSON request")
        void shouldHandleMalformedJsonRequest() {
            String malformedJson = "{ invalid json }";

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(malformedJson)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate required fields")
        void shouldValidateRequiredFields() {
            ClientRequestDto incompleteRequest = new ClientRequestDto();

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(incompleteRequest)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate identification pattern")
        void shouldValidateIdentificationPattern() {
            ClientRequestDto invalidRequest = new ClientRequestDto();
            invalidRequest.setIdentification("123");
            invalidRequest.setName("John Doe");
            invalidRequest.setGender(ClientRequestDto.GenderEnum.MASCULINO);
            invalidRequest.setAge(30);
            invalidRequest.setAddress("123 Main St");
            invalidRequest.setPhone("0985350314");
            invalidRequest.setPassword("password123");

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(invalidRequest)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate phone pattern")
        void shouldValidatePhonePattern() {
            ClientRequestDto invalidRequest = new ClientRequestDto();
            invalidRequest.setIdentification("1234567890");
            invalidRequest.setName("John Doe");
            invalidRequest.setGender(ClientRequestDto.GenderEnum.MASCULINO);
            invalidRequest.setAge(30);
            invalidRequest.setAddress("123 Main St");
            invalidRequest.setPhone("123");
            invalidRequest.setPassword("password123");

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(invalidRequest)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate minimum age")
        void shouldValidateMinimumAge() {
            ClientRequestDto invalidRequest = new ClientRequestDto();
            invalidRequest.setIdentification("1234567890");
            invalidRequest.setName("John Doe");
            invalidRequest.setGender(ClientRequestDto.GenderEnum.MASCULINO);
            invalidRequest.setAge(17);
            invalidRequest.setAddress("123 Main St");
            invalidRequest.setPhone("0985350314");
            invalidRequest.setPassword("password123");

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(invalidRequest)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should validate password length")
        void shouldValidatePasswordLength() {
            ClientRequestDto invalidRequest = new ClientRequestDto();
            invalidRequest.setIdentification("1234567890");
            invalidRequest.setName("John Doe");
            invalidRequest.setGender(ClientRequestDto.GenderEnum.MASCULINO);
            invalidRequest.setAge(30);
            invalidRequest.setAddress("123 Main St");
            invalidRequest.setPhone("0985350314");
            invalidRequest.setPassword("123");

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(invalidRequest)
                    .exchange()
                    .expectStatus().isBadRequest();

            verify(clientInputPort, never()).save(any());
        }

        @Test
        @DisplayName("Should handle internal server errors during save")
        void shouldHandleInternalServerErrorsDuringSave() {
            when(clientDtoMapper.toClient(testClientRequestDto))
                    .thenReturn(testClient);
            when(clientInputPort.save(testClient))
                    .thenReturn(Mono.error(new RuntimeException("Database error")));

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(testClientRequestDto)
                    .exchange()
                    .expectStatus().is5xxServerError();

            verify(clientDtoMapper).toClient(testClientRequestDto);
            verify(clientInputPort).save(testClient);
        }
    }

    @Nested
    @DisplayName("Edge cases and boundary tests")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle concurrent requests properly")
        void shouldHandleConcurrentRequestsProperly() {
            Client client1 = new Client();
            client1.setClientId(1);
            client1.setIdentification("1111111111");
            
            Client client2 = new Client();
            client2.setClientId(2);
            client2.setIdentification("2222222222");

            when(clientInputPort.getAll(null))
                    .thenReturn(Flux.just(client1, client2));
            when(clientDtoMapper.toClientResponseDto(any()))
                    .thenAnswer(invocation -> {
                        Client client = invocation.getArgument(0);
                        ClientResponseDto dto = new ClientResponseDto();
                        dto.setId(client.getClientId());
                        dto.setIdentification(client.getIdentification());
                        return dto;
                    });

            webTestClient.get()
                    .uri("/clientes")
                    .accept(MediaType.APPLICATION_JSON)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBodyList(ClientResponseDto.class)
                    .hasSize(2);
        }

        @Test
        @DisplayName("Should handle special characters in client data")
        void shouldHandleSpecialCharactersInClientData() {
            ClientRequestDto specialCharRequest = new ClientRequestDto();
            specialCharRequest.setIdentification("1234567890");
            specialCharRequest.setName("José María O'Connor-Smith");
            specialCharRequest.setGender(ClientRequestDto.GenderEnum.MASCULINO);
            specialCharRequest.setAge(30);
            specialCharRequest.setAddress("Calle 123 #45-67, Apt. 8B");
            specialCharRequest.setPhone("0985350314");
            specialCharRequest.setPassword("pássword123@");

            Client specialCharClient = new Client();
            specialCharClient.setName("José María O'Connor-Smith");

            when(clientDtoMapper.toClient(specialCharRequest))
                    .thenReturn(specialCharClient);
            when(clientInputPort.save(specialCharClient))
                    .thenReturn(Mono.just(specialCharClient));
            when(clientDtoMapper.toClientResponseDto(specialCharClient))
                    .thenReturn(testClientResponseDto);

            webTestClient.post()
                    .uri("/clientes")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(specialCharRequest)
                    .exchange()
                    .expectStatus().isCreated();
        }
    }
}