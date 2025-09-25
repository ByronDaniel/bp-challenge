package com.challenge.client.application.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import com.challenge.client.application.output.port.ClientOutputPort;
import com.challenge.client.domain.Client;
import com.challenge.client.domain.Person;
import com.challenge.client.infrastructure.exception.ConflictException;
import com.challenge.client.infrastructure.exception.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
@DisplayName("ClientService Unit Tests")
class ClientServiceTest {

    @Mock
    private ClientOutputPort clientOutputPort;

    @Mock
    private TransactionalOperator transactionalOperator;

    @InjectMocks
    private ClientService clientService;

    private Client testClient;
    private Person testPerson;

    @BeforeEach
    void setUp() {
        testPerson = new Person();
        testPerson.setPersonId(1);
        testPerson.setIdentification("1234567890");
        testPerson.setName("John Doe");
        testPerson.setGender("M");
        testPerson.setAge(30);
        testPerson.setAddress("123 Main St");
        testPerson.setPhone("555-1234");

        testClient = new Client();
        testClient.setClientId(1);
        testClient.setPersonId(1);
        testClient.setIdentification("1234567890");
        testClient.setName("John Doe");
        testClient.setGender("M");
        testClient.setAge(30);
        testClient.setAddress("123 Main St");
        testClient.setPhone("555-1234");
        testClient.setPassword("password123");
        testClient.setStatus(true);
    }

    @Nested
    @DisplayName("getAll method tests")
    class GetAllTests {

        @Test
        @DisplayName("Should return all clients when identification is null")
        void shouldReturnAllClientsWhenIdentificationIsNull() {
            Flux<Client> expectedClients = Flux.just(testClient);
            when(clientOutputPort.findAll()).thenReturn(expectedClients);

            Flux<Client> result = clientService.getAll(null);

            StepVerifier.create(result)
                    .expectNext(testClient)
                    .verifyComplete();

            verify(clientOutputPort).findAll();
            verify(clientOutputPort, never()).findByIdentification(anyString());
        }

        @Test
        @DisplayName("Should return specific client when identification is provided")
        void shouldReturnSpecificClientWhenIdentificationIsProvided() {
            String identification = "1234567890";
            when(clientOutputPort.findByIdentification(identification))
                    .thenReturn(Mono.just(testClient));

            Flux<Client> result = clientService.getAll(identification);

            StepVerifier.create(result)
                    .expectNext(testClient)
                    .verifyComplete();

            verify(clientOutputPort).findByIdentification(identification);
            verify(clientOutputPort, never()).findAll();
        }

        @Test
        @DisplayName("Should throw NotFoundException when client with identification not found")
        void shouldThrowNotFoundExceptionWhenClientNotFound() {
            String identification = "nonexistent";
            when(clientOutputPort.findByIdentification(identification))
                    .thenReturn(Mono.empty());

            Flux<Client> result = clientService.getAll(identification);

            StepVerifier.create(result)
                    .expectError(NotFoundException.class)
                    .verify();

            verify(clientOutputPort).findByIdentification(identification);
        }

        @Test
        @DisplayName("Should handle empty result when getting all clients")
        void shouldHandleEmptyResultWhenGettingAllClients() {
            when(clientOutputPort.findAll()).thenReturn(Flux.empty());

            Flux<Client> result = clientService.getAll(null);

            StepVerifier.create(result)
                    .verifyComplete();

            verify(clientOutputPort).findAll();
        }
    }

    @Nested
    @DisplayName("save method tests")
    class SaveTests {

        @Test
        @DisplayName("Should save new client successfully when client does not exist")
        void shouldSaveNewClientSuccessfullyWhenClientDoesNotExist() {
            Client newClient = new Client();
            newClient.setIdentification("9876543210");
            newClient.setName("Jane Doe");
            newClient.setPassword("newpassword");

            Person savedPerson = new Person();
            savedPerson.setPersonId(2);
            savedPerson.setIdentification("9876543210");
            savedPerson.setName("Jane Doe");

            Client savedClient = new Client();
            savedClient.setClientId(2);
            savedClient.setPersonId(2);
            savedClient.setIdentification("9876543210");
            savedClient.setName("Jane Doe");
            savedClient.setPassword("newpassword");
            savedClient.setStatus(true);

            when(clientOutputPort.findByIdentification("9876543210"))
                    .thenReturn(Mono.empty());
            when(clientOutputPort.savePerson(any(Client.class)))
                    .thenReturn(Mono.just(savedPerson));
            when(clientOutputPort.saveClient(any(Client.class)))
                    .thenReturn(Mono.just(savedClient));
            when(transactionalOperator.transactional(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Mono<Client> result = clientService.save(newClient);

            StepVerifier.create(result)
                    .assertNext(client -> {
                        assertEquals(2, client.getClientId());
                        assertEquals(2, client.getPersonId());
                        assertEquals("9876543210", client.getIdentification());
                        assertEquals("Jane Doe", client.getName());
                        assertEquals("newpassword", client.getPassword());
                        assertTrue(client.getStatus());
                    })
                    .verifyComplete();

            verify(clientOutputPort).findByIdentification("9876543210");
            verify(clientOutputPort).savePerson(any(Client.class));
            verify(clientOutputPort).saveClient(any(Client.class));
            verify(transactionalOperator).transactional(any(Mono.class));
        }

        @Test
        @DisplayName("Should throw ConflictException when client already exists")
        void shouldThrowConflictExceptionWhenClientAlreadyExists() {
            Client existingClient = new Client();
            existingClient.setIdentification("1234567890");
            existingClient.setName("Existing Client");

            when(clientOutputPort.findByIdentification("1234567890"))
                    .thenReturn(Mono.just(testClient));
            when(transactionalOperator.transactional(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Mono<Client> result = clientService.save(existingClient);

            StepVerifier.create(result)
                    .expectError(ConflictException.class)
                    .verify();

            verify(clientOutputPort).findByIdentification("1234567890");
            verify(clientOutputPort, never()).savePerson(any());
            verify(clientOutputPort, never()).saveClient(any());
            verify(transactionalOperator).transactional(any(Mono.class));
        }

        @Test
        @DisplayName("Should set status to true when saving new client")
        void shouldSetStatusToTrueWhenSavingNewClient() {
            Client newClient = new Client();
            newClient.setIdentification("5555555555");
            newClient.setName("Test Client");
            newClient.setPassword("testpass");
            newClient.setStatus(null);

            Person savedPerson = new Person();
            savedPerson.setPersonId(3);

            Client expectedClient = new Client();
            expectedClient.setClientId(3);
            expectedClient.setPersonId(3);
            expectedClient.setStatus(true);

            when(clientOutputPort.findByIdentification("5555555555"))
                    .thenReturn(Mono.empty());
            when(clientOutputPort.savePerson(any(Client.class)))
                    .thenReturn(Mono.just(savedPerson));
            when(clientOutputPort.saveClient(any(Client.class)))
                    .thenReturn(Mono.just(expectedClient));
            when(transactionalOperator.transactional(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Mono<Client> result = clientService.save(newClient);

            StepVerifier.create(result)
                    .assertNext(client -> {
                        assertTrue(client.getStatus());
                        assertEquals(3, client.getPersonId());
                    })
                    .verifyComplete();

            verify(clientOutputPort).saveClient(argThat(client -> 
                client.getStatus() != null && client.getStatus()));
        }

        @Test
        @DisplayName("Should handle transaction rollback on save failure")
        void shouldHandleTransactionRollbackOnSaveFailure() {
            Client newClient = new Client();
            newClient.setIdentification("7777777777");
            newClient.setName("Failing Client");

            Person savedPerson = new Person();
            savedPerson.setPersonId(4);

            when(clientOutputPort.findByIdentification("7777777777"))
                    .thenReturn(Mono.empty());
            when(clientOutputPort.savePerson(any(Client.class)))
                    .thenReturn(Mono.just(savedPerson));
            when(clientOutputPort.saveClient(any(Client.class)))
                    .thenReturn(Mono.error(new RuntimeException("Database error")));
            when(transactionalOperator.transactional(any(Mono.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            Mono<Client> result = clientService.save(newClient);

            StepVerifier.create(result)
                    .expectError(RuntimeException.class)
                    .verify();

            verify(clientOutputPort).findByIdentification("7777777777");
            verify(clientOutputPort).savePerson(any(Client.class));
            verify(clientOutputPort).saveClient(any(Client.class));
            verify(transactionalOperator).transactional(any(Mono.class));
        }
    }

    @Nested
    @DisplayName("Edge cases and error handling")
    class EdgeCasesTests {

        @Test
        @DisplayName("Should handle null identification in getAll gracefully")
        void shouldHandleNullIdentificationInGetAllGracefully() {
            when(clientOutputPort.findAll()).thenReturn(Flux.just(testClient));

            Flux<Client> result = clientService.getAll(null);

            StepVerifier.create(result)
                    .expectNext(testClient)
                    .verifyComplete();
        }

        @Test
        @DisplayName("Should handle empty string identification")
        void shouldHandleEmptyStringIdentification() {
            when(clientOutputPort.findByIdentification(""))
                    .thenReturn(Mono.empty());

            Flux<Client> result = clientService.getAll("");

            StepVerifier.create(result)
                    .expectError(NotFoundException.class)
                    .verify();
        }
    }
}