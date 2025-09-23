# 🏗️ Architecture Documentation

## 📋 Tabla de Contenidos

- [🎯 Visión Arquitectónica](#-visión-arquitectónica)
- [🏛️ Patrones Arquitectónicos](#️-patrones-arquitectónicos)
- [🔧 Decisiones Técnicas](#-decisiones-técnicas)
- [📐 Diagramas de Arquitectura](#-diagramas-de-arquitectura)
- [🌐 Comunicación entre Servicios](#-comunicación-entre-servicios)
- [💾 Modelo de Datos](#-modelo-de-datos)
- [🔒 Seguridad](#-seguridad)
- [📈 Escalabilidad](#-escalabilidad)

## 🎯 **Visión Arquitectónica**

### **Principios de Diseño**

1. **🔄 Separación de Responsabilidades**: Cada microservicio tiene una responsabilidad específica y bien definida
2. **🏗️ Arquitectura Hexagonal**: Isolación del dominio de negocio de los detalles técnicos
3. **⚡ Programación Reactiva**: Manejo asíncrono y no-bloqueante para mayor throughput
4. **🚀 API-First**: Diseño de contratos antes de la implementación
5. **📦 Conteneurización**: Despliegue consistente y escalable

### **Objetivos de Calidad**

| Atributo            | Objetivo               | Estrategia                                |
| ------------------- | ---------------------- | ----------------------------------------- |
| **Performance**     | < 500ms response time  | Reactive programming, caching             |
| **Availability**    | 99.9% uptime           | Health checks, circuit breakers           |
| **Scalability**     | Horizontal scaling     | Stateless services, load balancing        |
| **Maintainability** | Clean code practices   | Hexagonal architecture, DDD               |
| **Security**        | Banking-grade security | Authentication, authorization, encryption |

## 🏛️ **Patrones Arquitectónicos**

### **1. Arquitectura Hexagonal (Ports & Adapters)**

```
┌─────────────────────────────────────────────────────────┐
│                    🎯 DOMAIN LAYER                      │
│  ┌─────────────────┐    ┌─────────────────────────────┐ │
│  │   📋 Entities   │    │     🎯 Business Rules      │ │
│  │   - Client      │    │     - Validation Logic     │ │
│  │   - Account     │    │     - Domain Services      │ │
│  │   - Movement    │    │     - Domain Events        │ │
│  └─────────────────┘    └─────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
           ↑                                    ↑
    📥 INPUT PORTS                        📤 OUTPUT PORTS
           │                                    │
┌─────────────────────┐              ┌──────────────────────┐
│   🌐 Web Adapters   │              │  💾 Database Adapters │
│   - REST Controllers│              │  - R2DBC Repositories│
│   - Request/Response│              │  - Data Mapping      │
│   - Validation      │              │  - Transactions      │
└─────────────────────┘              └──────────────────────┘
```

**Beneficios:**

- ✅ Testabilidad mejorada
- ✅ Independencia de frameworks
- ✅ Flexibilidad para cambios
- ✅ Separación clara de responsabilidades

### **2. Domain-Driven Design (DDD)**

```java
// Ejemplo: Dominio de Account con lógica de negocio
@Entity
public class Account {
    private AccountId id;
    private Balance balance;
    private AccountType type;

    public Movement debit(Amount amount) {
        if (balance.isInsufficientFor(amount)) {
            throw new InsufficientBalanceException();
        }
        balance = balance.subtract(amount);
        return Movement.debit(this.id, amount, balance);
    }
}
```

### **3. CQRS Pattern (Command Query Responsibility Segregation)**

```java
// Commands (Write operations)
public interface AccountCommandHandler {
    Mono<Account> createAccount(CreateAccountCommand command);
    Mono<Account> updateBalance(UpdateBalanceCommand command);
}

// Queries (Read operations)
public interface AccountQueryHandler {
    Mono<Account> findById(AccountId id);
    Flux<Account> findByClientId(ClientId clientId);
}
```

## 🔧 **Decisiones Técnicas**

### **Stack Tecnológico**

| Decisión            | Alternativas Consideradas | Justificación                                  |
| ------------------- | ------------------------- | ---------------------------------------------- |
| **Spring Boot 3.x** | Quarkus, Micronaut        | Ecosistema maduro, comunidad, reactive support |
| **R2DBC**           | JPA/Hibernate             | Non-blocking I/O, mejor performance            |
| **SQL Server**      | PostgreSQL, MySQL         | Requerimiento específico del negocio           |
| **Docker**          | VM, Bare Metal            | Portabilidad, consistency, CI/CD               |
| **Gradle**          | Maven                     | Flexibilidad, performance, Kotlin DSL          |

### **Programación Reactiva**

**¿Por qué Reactive?**

- 🚀 **Performance**: Non-blocking I/O
- 📈 **Scalability**: Mejor utilización de threads
- 🔄 **Backpressure**: Manejo de carga
- 💾 **Resource Efficiency**: Menor uso de memoria

```java
// Ejemplo de flujo reactivo
@Service
public class MovementService {

    public Mono<Movement> processMovement(CreateMovementCommand command) {
        return validateAccount(command.getAccountId())
            .flatMap(account -> validateBalance(account, command.getAmount()))
            .flatMap(account -> createMovement(command))
            .flatMap(movement -> updateAccountBalance(movement))
            .doOnSuccess(movement -> publishEvent(movement))
            .doOnError(error -> logError(error));
    }
}
```

## 📐 **Diagramas de Arquitectura**

### **Diagrama de Contexto (C4 Level 1)**

```plantuml
@startuml context
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Context.puml

Person(customer, "Bank Customer", "Cliente que realiza operaciones bancarias")
Person(admin, "Bank Admin", "Administrador del sistema bancario")

System(banking_system, "Banking Microservices Platform", "Sistema bancario con arquitectura de microservicios")

System_Ext(email_system, "Email System", "Sistema de notificaciones por email")
System_Ext(sms_system, "SMS Gateway", "Gateway de mensajes SMS")

Rel(customer, banking_system, "Realizar operaciones bancarias", "HTTPS/REST")
Rel(admin, banking_system, "Administrar sistema", "HTTPS/REST")
Rel(banking_system, email_system, "Enviar notificaciones", "SMTP")
Rel(banking_system, sms_system, "Enviar alertas", "HTTP/API")

@enduml
```

### **Diagrama de Contenedores (C4 Level 2)**

```plantuml
@startuml containers
!include https://raw.githubusercontent.com/plantuml-stdlib/C4-PlantUML/master/C4_Container.puml

Person(customer, "Bank Customer")

System_Boundary(banking_system, "Banking Platform") {
    Container(web_app, "Web Application", "React/Angular", "Interfaz de usuario web")
    Container(api_gateway, "API Gateway", "Spring Cloud Gateway", "Enrutamiento y autenticación")

    Container(client_service, "Client Service", "Spring Boot + WebFlux", "Gestión de clientes")
    Container(account_service, "Account Service", "Spring Boot + WebFlux", "Gestión de cuentas")
    Container(movement_service, "Movement Service", "Spring Boot + WebFlux", "Procesamiento de movimientos")
    Container(report_service, "Report Service", "Spring Boot + WebFlux", "Generación de reportes")

    ContainerDb(database, "Database", "SQL Server", "Almacenamiento de datos")
    Container(cache, "Cache", "Redis", "Cache distribuido")
    Container(message_queue, "Message Queue", "Apache Kafka", "Mensajería asíncrona")
}

Rel(customer, web_app, "Utiliza", "HTTPS")
Rel(web_app, api_gateway, "Hace llamadas API", "HTTPS/REST")
Rel(api_gateway, client_service, "Enruta requests", "HTTP")
Rel(api_gateway, account_service, "Enruta requests", "HTTP")
Rel(api_gateway, movement_service, "Enruta requests", "HTTP")
Rel(api_gateway, report_service, "Enruta requests", "HTTP")

Rel(client_service, database, "Lee/Escribe", "R2DBC")
Rel(account_service, database, "Lee/Escribe", "R2DBC")
Rel(movement_service, database, "Lee/Escribe", "R2DBC")
Rel(report_service, database, "Lee", "R2DBC")

Rel(movement_service, account_service, "Valida cuenta", "HTTP")
Rel(report_service, client_service, "Obtiene cliente", "HTTP")
Rel(report_service, account_service, "Obtiene cuentas", "HTTP")
Rel(report_service, movement_service, "Obtiene movimientos", "HTTP")

@enduml
```

## 🌐 **Comunicación entre Servicios**

### **Patrones de Comunicación**

1. **Synchronous Communication (HTTP/REST)**

   ```java
   @Component
   public class AccountClient {

       @CircuitBreaker(name = "account-service", fallbackMethod = "fallbackAccount")
       @Retry(name = "account-service")
       @TimeLimiter(name = "account-service")
       public Mono<Account> getAccount(Integer accountId) {
           return webClient
               .get()
               .uri("/accounts/{id}", accountId)
               .retrieve()
               .bodyToMono(Account.class)
               .timeout(Duration.ofSeconds(3));
       }
   }
   ```

2. **Asynchronous Communication (Events)**
   ```java
   @EventListener
   public void handleMovementCreated(MovementCreatedEvent event) {
       notificationService.sendTransactionAlert(event.getClientId(), event.getAmount());
       auditService.logTransaction(event);
   }
   ```

### **Service Mesh Architecture**

```
┌─────────────────────────────────────────────────────────┐
│                    🌐 Service Mesh                      │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐ │
│  │   Envoy     │    │   Envoy     │    │   Envoy     │ │
│  │   Proxy     │    │   Proxy     │    │   Proxy     │ │
│  └─────────────┘    └─────────────┘    └─────────────┘ │
│         │                  │                  │        │
│  ┌─────────────┐    ┌─────────────┐    ┌─────────────┐ │
│  │   Client    │    │   Account   │    │  Movement   │ │
│  │   Service   │◄──►│   Service   │◄──►│   Service   │ │
│  └─────────────┘    └─────────────┘    └─────────────┘ │
└─────────────────────────────────────────────────────────┘
```

## 💾 **Modelo de Datos**

### **Diagrama de Entidad-Relación**

```sql
-- Esquema de Base de Datos
CREATE TABLE persons (
    person_id INT IDENTITY(1,1) PRIMARY KEY,
    name VARCHAR(70) NOT NULL,
    gender VARCHAR(9) NOT NULL,
    age INT NOT NULL,
    identification VARCHAR(10) NOT NULL UNIQUE,
    address VARCHAR(100) NOT NULL,
    phone VARCHAR(10) NOT NULL
);

CREATE TABLE clients (
    client_id INT IDENTITY(1,1) PRIMARY KEY,
    person_id INT NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Encrypted
    status BIT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (person_id) REFERENCES persons(person_id) ON DELETE CASCADE
);

CREATE TABLE accounts (
    account_id INT IDENTITY(1,1) PRIMARY KEY,
    client_id INT NOT NULL,
    number VARCHAR(20) NOT NULL UNIQUE,
    type VARCHAR(50) NOT NULL,
    balance DECIMAL(19, 4) NOT NULL DEFAULT 0,
    status BIT NOT NULL,
    created_at DATETIME DEFAULT GETDATE(),
    FOREIGN KEY (client_id) REFERENCES clients(client_id) ON DELETE CASCADE
);

CREATE TABLE movements (
    movement_id INT IDENTITY(1,1) PRIMARY KEY,
    account_id INT NOT NULL,
    transaction_date DATETIME NOT NULL DEFAULT GETDATE(),
    type VARCHAR(20) NOT NULL, -- 'CREDIT' or 'DEBIT'
    amount DECIMAL(19, 4) NOT NULL,
    balance_after DECIMAL(19, 4) NOT NULL,
    description VARCHAR(255),
    reference_number VARCHAR(50),
    FOREIGN KEY (account_id) REFERENCES accounts(account_id) ON DELETE CASCADE
);
```

## 🔒 **Seguridad**

### **Modelo de Seguridad Propuesto**

```java
// JWT Security Configuration
@EnableWebFluxSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        return http
            .authorizeExchange(exchanges -> exchanges
                .pathMatchers("/actuator/health").permitAll()
                .pathMatchers("/auth/**").permitAll()
                .pathMatchers(HttpMethod.GET, "/api/**").hasRole("USER")
                .pathMatchers(HttpMethod.POST, "/api/**").hasRole("USER")
                .pathMatchers("/admin/**").hasRole("ADMIN")
                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(OAuth2ResourceServerSpec::jwt)
            .csrf().disable()
            .build();
    }
}
```

### **Principios de Seguridad**

1. **Authentication**: JWT tokens with OAuth2
2. **Authorization**: Role-based access control (RBAC)
3. **Data Encryption**: TLS in transit, AES-256 at rest
4. **Input Validation**: Bean validation + custom validators
5. **Audit Logging**: All financial operations logged

## 📈 **Escalabilidad**

### **Estrategias de Escalabilidad**

1. **Horizontal Scaling**: Stateless services + Load balancing
2. **Database Sharding**: Por región geográfica o cliente
3. **Caching Strategy**: Redis para datos frecuentemente accedidos
4. **Async Processing**: Kafka para operaciones no críticas

### **Performance Targets**

| Métrica       | Target   | Monitoring           |
| ------------- | -------- | -------------------- |
| Response Time | < 500ms  | Prometheus + Grafana |
| Throughput    | 1000 TPS | Load testing         |
| Availability  | 99.9%    | Health checks        |
| Error Rate    | < 0.1%   | Error tracking       |

### **Monitoring Stack**

```yaml
# Observability Stack
monitoring:
  metrics:
    - Prometheus
    - Grafana
    - Micrometer
  logging:
    - ELK Stack (Elasticsearch, Logstash, Kibana)
    - Structured logging (JSON)
  tracing:
    - Jaeger
    - Spring Cloud Sleuth
  alerting:
    - PagerDuty
    - Slack integration
```

---

> 📋 **Siguiente**: [API Documentation](./API.md) | [Deployment Guide](./DEPLOYMENT.md)
