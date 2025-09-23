# 👨‍💻 Development Guide

## 📋 Tabla de Contenidos

- [🎯 Visión General](#-visión-general)
- [🛠️ Setup del Entorno](#️-setup-del-entorno)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [🏗️ Estándares de Código](#️-estándares-de-código)
- [🧪 Testing Guidelines](#-testing-guidelines)
- [🔄 Git Workflow](#-git-workflow)
- [📖 Documentación](#-documentación)
- [🚀 Performance Guidelines](#-performance-guidelines)
- [🤝 Contribución](#-contribución)

## 🎯 **Visión General**

Este documento establece las guías y estándares para desarrolladores que trabajen en el proyecto de microservicios bancarios.

### **Principios de Desarrollo**

1. **🏗️ Clean Code**: Código legible, mantenible y autodocumentado
2. **🧪 Test-Driven Development**: Tests primero, código después
3. **🔒 Security First**: Seguridad como prioridad desde el diseño
4. **📈 Performance Aware**: Consideraciones de performance en cada decisión
5. **📝 Documentation**: Documentar decisiones y APIs

## 🛠️ **Setup del Entorno**

### **1. Herramientas Requeridas**

```bash
# Java 21 (recomendado: SDKMAN)
curl -s "https://get.sdkman.io" | bash
sdk install java 21.0.1-tem
sdk use java 21.0.1-tem

# Gradle (viene con wrapper en el proyecto)
./gradlew --version

# Docker & Docker Compose
docker --version
docker-compose --version

# Git
git --version

# IDE recomendado: IntelliJ IDEA Ultimate
# Alternativas: VS Code con extensiones Java
```

### **2. Configuración del IDE**

#### **IntelliJ IDEA**

```bash
# Plugins recomendados
- Lombok Plugin
- MapStruct Support
- SonarLint
- Docker
- Database Navigator
- PlantUML Integration
- Rainbow Brackets
```

**Configuración de Code Style**:

```bash
# Importar configuración desde
./config/intellij-code-style.xml

# Configuración de formato automático
File > Settings > Tools > Actions on Save
✅ Reformat code
✅ Optimize imports
✅ Rearrange code
```

#### **VS Code**

```json
// .vscode/settings.json
{
  "java.configuration.updateBuildConfiguration": "automatic",
  "java.saveActions.organizeImports": true,
  "java.format.settings.url": "./config/eclipse-formatter.xml",
  "java.checkstyle.configuration": "./config/checkstyle.xml",
  "files.trimTrailingWhitespace": true,
  "files.insertFinalNewline": true,
  "editor.formatOnSave": true
}
```

### **3. Setup Local**

```bash
# Clonar repositorio
git clone <repository-url>
cd bp-challenge/backend

# Copiar configuración de desarrollo
cp .env.example .env

# Instalar dependencias y construir
./gradlew build

# Levantar infraestructura (BD)
docker-compose up -d sqlserver redis

# Ejecutar tests
./gradlew test

# Correr aplicación en modo desarrollo
./gradlew bootRun --args='--spring.profiles.active=dev'
```

## 📁 **Estructura del Proyecto**

### **Arquitectura Hexagonal Standard**

```
msa-{service}/
├── 📄 build.gradle                    # Dependencias y configuración build
├── 📄 Dockerfile                      # Imagen Docker
├── 📁 src/
│   ├── 📁 main/
│   │   ├── 📁 java/com/challenge/{service}/
│   │   │   ├── 📄 {Service}Application.java    # Main class
│   │   │   ├── 📁 domain/                      # 🎯 Capa de Dominio
│   │   │   │   ├── 📄 {Entity}.java           # Entidades de negocio
│   │   │   │   ├── 📄 {ValueObject}.java      # Value Objects
│   │   │   │   ├── 📄 {DomainService}.java    # Servicios de dominio
│   │   │   │   └── 📄 {DomainEvent}.java      # Eventos de dominio
│   │   │   ├── 📁 application/                 # 🎮 Capa de Aplicación
│   │   │   │   ├── 📁 input/
│   │   │   │   │   └── 📁 port/
│   │   │   │   │       └── 📄 {UseCase}InputPort.java
│   │   │   │   ├── 📁 output/
│   │   │   │   │   └── 📁 port/
│   │   │   │   │       └── 📄 {Repository}OutputPort.java
│   │   │   │   └── 📁 service/
│   │   │   │       ├── 📄 {UseCase}Service.java
│   │   │   │       └── 📁 utils/
│   │   │   └── 📁 infrastructure/              # 🔌 Capa de Infraestructura
│   │   │       ├── 📁 input/                   # Adaptadores de entrada
│   │   │       │   └── 📁 adapter/
│   │   │       │       ├── 📄 {Controller}.java
│   │   │       │       └── 📄 {MessageHandler}.java
│   │   │       ├── 📁 output/                  # Adaptadores de salida
│   │   │       │   ├── 📁 adapter/
│   │   │       │   │   ├── 📄 {Repository}Impl.java
│   │   │       │   │   └── 📄 {ExternalClient}.java
│   │   │       │   └── 📁 repository/
│   │   │       │       └── 📄 {Entity}Repository.java
│   │   │       └── 📁 exception/
│   │   │           ├── 📄 GlobalExceptionHandler.java
│   │   │           └── 📄 {Custom}Exception.java
│   │   └── 📁 resources/
│   │       ├── 📄 application.yml
│   │       ├── 📄 application-{profile}.yml
│   │       └── 📄 openapi.yaml
│   └── 📁 test/                               # 🧪 Tests
│       ├── 📁 java/com/challenge/{service}/
│       │   ├── 📁 domain/                     # Tests de dominio
│       │   ├── 📁 application/                # Tests de aplicación
│       │   ├── 📁 infrastructure/             # Tests de infraestructura
│       │   └── 📁 integration/                # Tests de integración
│       └── 📁 resources/
└── 📁 config/                                 # Configuraciones
    ├── 📄 checkstyle.xml
    ├── 📄 spotbugs-exclude.xml
    └── 📄 jacoco.gradle
```

## 🏗️ **Estándares de Código**

### **1. Convenciones de Naming**

#### **Classes**

```java
// ✅ Correcto
public class ClientService implements ClientInputPort { }
public class AccountNotFoundException extends RuntimeException { }
public class MovementCreatedEvent { }

// ❌ Incorrecto
public class clientservice { }
public class accountnotfoundexception { }
```

#### **Variables y Métodos**

```java
// ✅ Correcto
private final ClientOutputPort clientOutputPort;
public Mono<Client> findByIdentification(String identification) { }
private boolean isValidBalance(BigDecimal balance) { }

// ❌ Incorrecto
private final ClientOutputPort port;
public Mono<Client> find(String id) { }
private boolean check(BigDecimal b) { }
```

#### **Constants**

```java
// ✅ Correcto
public static final String CLIENT_NOT_FOUND = "CLIENT_NOT_FOUND";
public static final int MAX_RETRY_ATTEMPTS = 3;
public static final Duration TIMEOUT_DURATION = Duration.ofSeconds(30);

// ❌ Incorrecto
public static final String clientNotFound = "CLIENT_NOT_FOUND";
public static final int maxRetry = 3;
```

### **2. Code Style Guidelines**

#### **Lombok Usage**

```java
// ✅ Correcto - Uso conservador de Lombok
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Client extends Person {
    Integer clientId;
    String password;
    Boolean status;

    // Métodos de negocio explícitos
    public boolean isActive() {
        return Boolean.TRUE.equals(status);
    }
}

// ❌ Evitar - Sobre uso de Lombok
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Client { } // Pierde control sobre el comportamiento
```

#### **Reactive Programming Patterns**

```java
// ✅ Correcto - Manejo de errores y flujo claro
@Override
public Mono<Movement> processMovement(CreateMovementCommand command) {
    return validateAccount(command.getAccountId())
        .flatMap(account -> validateBalance(account, command.getAmount()))
        .flatMap(account -> createMovement(command, account))
        .flatMap(this::saveMovement)
        .flatMap(movement -> updateAccountBalance(movement))
        .doOnSuccess(movement -> publishMovementEvent(movement))
        .doOnError(error -> logError("Failed to process movement", error))
        .onErrorMap(ValidationException.class,
                   ex -> new BusinessRuleException("Invalid movement data", ex));
}

// ❌ Incorrecto - Callback hell reactivo
public Mono<Movement> processMovement(CreateMovementCommand command) {
    return accountRepository.findById(command.getAccountId())
        .flatMap(account -> {
            return movementRepository.save(movement)
                .flatMap(saved -> {
                    return accountRepository.save(account)
                        .flatMap(updatedAccount -> {
                            // Nested hell...
                        });
                });
        });
}
```

### **3. Error Handling Standards**

```java
// Custom Exceptions con jerarquía clara
public abstract class BusinessException extends RuntimeException {
    protected BusinessException(String message) {
        super(message);
    }

    protected BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

public class InsufficientBalanceException extends BusinessException {
    public InsufficientBalanceException(BigDecimal required, BigDecimal available) {
        super(String.format("Insufficient balance. Required: %s, Available: %s",
                          required, available));
    }
}

// Global Exception Handler
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public Mono<ResponseEntity<ErrorResponse>> handleBusinessException(
            BusinessException ex, ServerHttpRequest request) {

        ErrorResponse error = ErrorResponse.builder()
            .timestamp(Instant.now())
            .status("error")
            .message(ex.getMessage())
            .path(request.getPath().value())
            .build();

        return Mono.just(ResponseEntity.status(HttpStatus.CONFLICT).body(error));
    }
}
```

## 🧪 **Testing Guidelines**

### **Estructura de Tests**

```
test/
├── 📁 unit/                    # Tests unitarios (>80% coverage)
│   ├── 📁 domain/             # Tests de entidades y lógica de negocio
│   ├── 📁 application/        # Tests de servicios de aplicación
│   └── 📁 infrastructure/     # Tests de adaptadores
├── 📁 integration/            # Tests de integración (BD, HTTP)
└── 📁 contract/               # Tests de contratos (Pact)
```

### **1. Unit Tests**

```java
// Test de dominio - Entidad
@ExtendWith(MockitoExtension.class)
class AccountTest {

    @Test
    @DisplayName("Should successfully debit amount when sufficient balance")
    void shouldDebitWhenSufficientBalance() {
        // Given
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(1000));
        BigDecimal debitAmount = BigDecimal.valueOf(300);

        // When
        BigDecimal newBalance = account.debit(debitAmount);

        // Then
        assertThat(newBalance).isEqualTo(BigDecimal.valueOf(700));
        assertThat(account.getBalance()).isEqualTo(BigDecimal.valueOf(700));
    }

    @Test
    @DisplayName("Should throw exception when insufficient balance")
    void shouldThrowExceptionWhenInsufficientBalance() {
        // Given
        Account account = new Account();
        account.setBalance(BigDecimal.valueOf(100));
        BigDecimal debitAmount = BigDecimal.valueOf(300);

        // When & Then
        assertThatThrownBy(() -> account.debit(debitAmount))
            .isInstanceOf(InsufficientBalanceException.class)
            .hasMessageContaining("Insufficient balance");
    }
}

// Test de servicio de aplicación
@ExtendWith(MockitoExtension.class)
class MovementServiceTest {

    @Mock
    private MovementOutputPort movementOutputPort;

    @Mock
    private AccountOutputPort accountOutputPort;

    @InjectMocks
    private MovementService movementService;

    @Test
    @DisplayName("Should process credit movement successfully")
    void shouldProcessCreditMovementSuccessfully() {
        // Given
        Integer accountId = 1;
        Account account = createTestAccount(accountId, BigDecimal.valueOf(1000));
        Movement movement = createTestMovement(accountId, CREDIT, BigDecimal.valueOf(500));

        when(accountOutputPort.getById(accountId)).thenReturn(Mono.just(account));
        when(movementOutputPort.save(any(Movement.class))).thenReturn(Mono.just(movement));
        when(accountOutputPort.updateById(eq(accountId), any(Account.class)))
            .thenReturn(Mono.just(account));

        // When
        StepVerifier.create(movementService.save(movement))
            // Then
            .expectNextMatches(result ->
                result.getBalance().equals(BigDecimal.valueOf(1500)))
            .verifyComplete();

        verify(accountOutputPort).getById(accountId);
        verify(movementOutputPort).save(movement);
        verify(accountOutputPort).updateById(eq(accountId), any(Account.class));
    }
}
```

### **2. Integration Tests**

```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.r2dbc.url=r2dbc:h2:mem:///testdb",
    "logging.level.org.springframework.data.r2dbc=DEBUG"
})
@Sql(scripts = "/test-data.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
class ClientControllerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    @Autowired
    private ClientRepository clientRepository;

    @Test
    @DisplayName("Should create client successfully")
    void shouldCreateClientSuccessfully() {
        // Given
        ClientRequest request = ClientRequest.builder()
            .identification("1234567890")
            .name("Juan Pérez")
            .gender("Masculino")
            .age(35)
            .address("Av. Principal 123")
            .phone("0999123456")
            .password("securePassword123")
            .status(true)
            .build();

        // When & Then
        webTestClient.post()
            .uri("/api/v1/clientes")
            .contentType(MediaType.APPLICATION_JSON)
            .body(Mono.just(request), ClientRequest.class)
            .exchange()
            .expectStatus().isCreated()
            .expectBody(ClientResponse.class)
            .value(response -> {
                assertThat(response.getClientId()).isNotNull();
                assertThat(response.getName()).isEqualTo("Juan Pérez");
                assertThat(response.getStatus()).isTrue();
            });

        // Verificar en BD
        StepVerifier.create(clientRepository.count())
            .expectNext(1L)
            .verifyComplete();
    }
}
```

### **3. Test Configuration**

```java
// TestConfiguration para mocks comunes
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public Clock testClock() {
        return Clock.fixed(Instant.parse("2024-01-15T10:30:00Z"), ZoneOffset.UTC);
    }

    @Bean
    @Primary
    public TransactionalOperator testTransactionalOperator() {
        return TransactionalOperator.create(new MockReactiveTransactionManager());
    }
}

// Test Utilities
public class TestDataFactory {

    public static Client createTestClient() {
        Client client = new Client();
        client.setClientId(1);
        client.setIdentification("1234567890");
        client.setName("Test Client");
        client.setGender("Masculino");
        client.setAge(30);
        client.setAddress("Test Address");
        client.setPhone("0999123456");
        client.setStatus(true);
        return client;
    }

    public static Account createTestAccount(Integer clientId, BigDecimal balance) {
        Account account = new Account();
        account.setAccountId(1);
        account.setClientId(clientId);
        account.setNumber("1234567890");
        account.setType("AHORROS");
        account.setBalance(balance);
        account.setStatus(true);
        return account;
    }
}
```

### **4. Performance Tests**

```java
@Test
@DisplayName("Should handle 1000 concurrent movements")
void shouldHandleConcurrentMovements() {
    // Given
    int concurrentRequests = 1000;
    List<Mono<Movement>> movements = IntStream.range(0, concurrentRequests)
        .mapToObj(i -> createTestMovement())
        .map(movement -> movementService.save(movement))
        .collect(Collectors.toList());

    // When
    Instant start = Instant.now();
    StepVerifier.create(Flux.merge(movements))
        .expectNextCount(concurrentRequests)
        .verifyComplete();
    Instant end = Instant.now();

    // Then
    Duration duration = Duration.between(start, end);
    assertThat(duration).isLessThan(Duration.ofSeconds(5));
}
```

## 🔄 **Git Workflow**

### **Branch Strategy**

```bash
main                 # Production branch
├── develop         # Integration branch
├── feature/        # Feature branches
│   ├── feature/client-validation
│   └── feature/movement-limits
├── hotfix/         # Emergency fixes
│   └── hotfix/critical-security-patch
└── release/        # Release preparation
    └── release/v1.2.0
```

### **Commit Messages Convention**

```bash
# Formato: <type>(<scope>): <description>
#
# Types:
# feat: Nueva funcionalidad
# fix: Corrección de bug
# docs: Cambios en documentación
# style: Cambios de formato (no afectan lógica)
# refactor: Refactoring de código
# test: Agregar o modificar tests
# chore: Tareas de mantenimiento

# Ejemplos:
feat(client): add email validation for client registration
fix(movement): resolve insufficient balance validation issue
docs(api): update OpenAPI specification for account endpoints
test(movement): add integration tests for concurrent transactions
refactor(account): extract balance validation to domain service
```

### **Pull Request Template**

```markdown
## 📋 Description

Brief description of changes made.

## 🎯 Type of Change

- [ ] 🐛 Bug fix (non-breaking change which fixes an issue)
- [ ] ✨ New feature (non-breaking change which adds functionality)
- [ ] 💥 Breaking change (fix or feature that would cause existing functionality to not work as expected)
- [ ] 📚 Documentation update

## 🧪 Testing

- [ ] Unit tests pass
- [ ] Integration tests pass
- [ ] Manual testing completed
- [ ] Performance impact assessed

## 📝 Checklist

- [ ] Code follows style guidelines
- [ ] Self-review completed
- [ ] Code is documented
- [ ] Tests added/updated
- [ ] No new warnings or errors
- [ ] Breaking changes documented

## 📸 Screenshots (if applicable)

[Add screenshots for UI changes]

## 🔗 Related Issues

Closes #[issue number]
```

## 🚀 **Performance Guidelines**

### **Database Optimization**

```java
// ✅ Correcto - Uso eficiente de R2DBC
@Repository
public class ClientRepositoryImpl implements ClientOutputPort {

    // Usar índices apropiados
    @Query("""
        SELECT c.*, p.*
        FROM clients c
        INNER JOIN persons p ON c.person_id = p.person_id
        WHERE c.status = 1
        ORDER BY p.name
        """)
    Flux<Client> findActiveClientsWithPerson();

    // Paginación para grandes datasets
    @Query("""
        SELECT c.*, p.*
        FROM clients c
        INNER JOIN persons p ON c.person_id = p.person_id
        ORDER BY c.client_id
        OFFSET :offset ROWS FETCH NEXT :limit ROWS ONLY
        """)
    Flux<Client> findClientsWithPagination(@Param("offset") int offset,
                                          @Param("limit") int limit);
}

// ❌ Evitar - N+1 queries
public Flux<Client> findAllClientsWithAccounts() {
    return clientRepository.findAll()
        .flatMap(client ->
            accountRepository.findByClientId(client.getClientId()) // N+1!
                .collectList()
                .map(accounts -> {
                    client.setAccounts(accounts);
                    return client;
                })
        );
}
```

### **Caching Strategy**

```java
@Service
public class ClientService {

    private final ReactiveRedisTemplate<String, Client> redisTemplate;

    public Mono<Client> getById(Integer id) {
        String cacheKey = "client:" + id;

        return redisTemplate.opsForValue()
            .get(cacheKey)
            .switchIfEmpty(
                clientOutputPort.findById(id)
                    .doOnNext(client ->
                        redisTemplate.opsForValue()
                            .set(cacheKey, client, Duration.ofMinutes(30))
                            .subscribe()
                    )
            );
    }

    @EventListener
    public void handleClientUpdated(ClientUpdatedEvent event) {
        // Invalidar cache cuando se actualiza
        String cacheKey = "client:" + event.getClientId();
        redisTemplate.delete(cacheKey).subscribe();
    }
}
```

### **Resource Management**

```java
// Configuración de pools de conexión
@Configuration
public class DatabaseConfig {

    @Bean
    @ConfigurationProperties("spring.r2dbc.pool")
    public ConnectionPoolConfiguration connectionPoolConfiguration() {
        return ConnectionPoolConfiguration.builder()
            .initialSize(10)
            .maxSize(20)
            .acquireRetry(3)
            .maxIdleTime(Duration.ofMinutes(5))
            .maxLifeTime(Duration.ofMinutes(30))
            .validationQuery("SELECT 1")
            .build();
    }
}

// WebClient con timeouts y retry
@Configuration
public class WebClientConfig {

    @Bean
    public WebClient accountServiceClient() {
        return WebClient.builder()
            .baseUrl("http://account-service:8080")
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create()
                    .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 3000)
                    .responseTimeout(Duration.ofSeconds(5))
                    .compress(true)
            ))
            .build();
    }
}
```

## 🤝 **Contribución**

### **Code Review Checklist**

#### **Functionality**

- [ ] El código funciona como se espera
- [ ] El código maneja casos edge apropiadamente
- [ ] Validaciones de entrada están implementadas
- [ ] Manejo de errores es apropiado

#### **Architecture & Design**

- [ ] Sigue principios de arquitectura hexagonal
- [ ] Separación de responsabilidades clara
- [ ] No hay tight coupling entre capas
- [ ] Patrones de diseño apropiados

#### **Performance**

- [ ] No hay problemas de performance evidentes
- [ ] Queries de BD son eficientes
- [ ] Uso apropiado de cache
- [ ] Reactive streams bien implementados

#### **Security**

- [ ] No hay vulnerabilidades de seguridad
- [ ] Validaciones de entrada seguras
- [ ] Datos sensibles protegidos
- [ ] Principio de menor privilegio

#### **Testing**

- [ ] Tests unitarios cubren casos importantes
- [ ] Tests de integración para flujos críticos
- [ ] Coverage de código > 80%
- [ ] Tests son mantenibles

---

> 📋 **¿Preguntas?** Contacta al equipo de arquitectura o crea un issue en GitHub.

> 📚 **Recursos adicionales**:
>
> - [Clean Code Practices](https://clean-code-developer.com/)
> - [Reactive Programming Guide](https://projectreactor.io/docs)
> - [Spring Boot Best Practices](https://spring.io/guides)
