# 🏦 BP Challenge - Banking Microservices Platform

[![Java](https://img.shields.io/badge/Java-21-orange.svg)](https://openjdk.org/projects/jdk/21/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Docker](https://img.shields.io/badge/Docker-Containerized-blue.svg)](https://www.docker.com/)
[![Architecture](https://img.shields.io/badge/Architecture-Hexagonal-purple.svg)](https://alistair.cockburn.us/hexagonal-architecture/)
[![Reactive](https://img.shields.io/badge/Programming-Reactive-red.svg)](https://projectreactor.io/)

> Sistema bancario moderno construido con arquitectura de microservicios, programación reactiva y patrones de diseño avanzados.

## 📋 **Tabla de Contenidos**

- [🎯 Descripción del Proyecto](#-descripción-del-proyecto)
- [🏗️ Arquitectura](#️-arquitectura)
- [🚀 Tecnologías](#-tecnologías)
- [📦 Microservicios](#-microservicios)
- [⚡ Quick Start](#-quick-start)
- [📊 Endpoints](#-endpoints)
- [🗂️ Estructura del Proyecto](#️-estructura-del-proyecto)
- [📖 Documentación](#-documentación)
- [🤝 Contribución](#-contribución)

## 🎯 **Descripción del Proyecto**

**BP Challenge** es una plataforma bancaria digital que implementa las operaciones fundamentales de un sistema financiero a través de microservicios independientes y escalables.

### **Funcionalidades Principales**

- 👥 **Gestión de Clientes**: CRUD completo de clientes con herencia de personas
- 💰 **Gestión de Cuentas**: Creación y administración de cuentas bancarias
- 📈 **Movimientos**: Procesamiento de débitos y créditos con validación de saldos
- 📊 **Reportes**: Generación de reportes financieros con filtros avanzados

### **Características Técnicas**

- ✅ **Arquitectura Hexagonal** (Ports & Adapters)
- ✅ **Programación Reactiva** (Spring WebFlux, R2DBC)
- ✅ **API-First** con OpenAPI 3.0
- ✅ **Transacciones ACID** con manejo reactivo
- ✅ **Conteneurización** completa con Docker

## 🏗️ **Arquitectura**

```
┌─────────────────────────────────────────────────────────────┐
│                    🌐 API Gateway                            │
├─────────────────┬─────────────────┬─────────────────────────┤
│   👥 Client     │   💰 Account    │   📈 Movement           │
│   Service       │   Service       │   Service               │
│   Port: 8080    │   Port: 8081    │   Port: 8082            │
├─────────────────┴─────────────────┴─────────────────────────┤
│                📊 Report Service                            │
│                   Port: 8083                                │
├─────────────────────────────────────────────────────────────┤
│                🗄️ SQL Server Database                       │
│                   Port: 1433                                │
└─────────────────────────────────────────────────────────────┘
```

> 📋 **Ver**: [Documentación de Arquitectura Detallada](./docs/ARCHITECTURE.md)

## 🚀 **Tecnologías**

### **Backend Stack**

| Tecnología         | Versión | Propósito                |
| ------------------ | ------- | ------------------------ |
| **Java**           | 21      | Lenguaje principal       |
| **Spring Boot**    | 3.5.5   | Framework principal      |
| **Spring WebFlux** | 3.x     | Programación reactiva    |
| **R2DBC**          | 2.x     | Acceso reactivo a BD     |
| **MapStruct**      | 1.5.3   | Mapping de objetos       |
| **Lombok**         | Latest  | Reducción de boilerplate |

### **Database & Infrastructure**

| Tecnología     | Versión | Propósito               |
| -------------- | ------- | ----------------------- |
| **SQL Server** | 2019    | Base de datos principal |
| **Docker**     | Latest  | Conteneurización        |
| **Gradle**     | 8.x     | Build tool              |
| **OpenAPI**    | 3.0     | Documentación de API    |

## 📦 **Microservicios**

### 🏢 **msa-client** (Puerto 8080)

Gestión completa del ciclo de vida de clientes bancarios.

**Responsabilidades:**

- CRUD de clientes y personas
- Validación de datos personales
- Manejo de relaciones persona-cliente

### 💳 **msa-account** (Puerto 8081)

Administración de cuentas bancarias y sus propiedades.

**Responsabilidades:**

- CRUD de cuentas bancarias
- Generación automática de números de cuenta
- Asociación cliente-cuenta

### 💸 **msa-movement** (Puerto 8082)

Procesamiento de transacciones financieras con validaciones de negocio.

**Responsabilidades:**

- Procesamiento de débitos y créditos
- Validación de saldos disponibles
- Actualización automática de balances
- Comunicación con servicio de cuentas

### 📈 **msa-report** (Puerto 8083)

Generación de reportes y análisis financiero.

**Responsabilidades:**

- Consolidación de datos de múltiples servicios
- Filtrado por fechas y clientes
- Agregación de movimientos y balances

## ⚡ **Quick Start**

### **Prerrequisitos**

- Docker & Docker Compose
- Java 21+ (para desarrollo local)
- Gradle 8+ (para desarrollo local)

### **1. Clonar el Repositorio**

```bash
git clone <repository-url>
cd bp-challenge/backend
```

### **2. Levantar la Infraestructura**

```bash
# Construir y levantar todos los servicios
docker-compose up --build

# Solo la base de datos (para desarrollo local)
docker-compose up sqlserver db_init
```

### **3. Verificar el Despliegue**

```bash
# Health checks
curl http://localhost:8080/actuator/health  # Client Service
curl http://localhost:8081/actuator/health  # Account Service
curl http://localhost:8082/actuator/health  # Movement Service
curl http://localhost:8083/actuator/health  # Report Service
```

### **4. Acceder a las APIs**

- 📋 **Postman Collection**: `BP Challenge.postman_collection.json`
- 📖 **Swagger UI**:
  - Client: http://localhost:8080/webjars/swagger-ui/index.html
  - Account: http://localhost:8081/webjars/swagger-ui/index.html
  - Movement: http://localhost:8082/webjars/swagger-ui/index.html
  - Report: http://localhost:8083/webjars/swagger-ui/index.html

## 📊 **Endpoints Principales**

### **👥 Clientes** (Port 8080)

```http
GET    /clientes           # Listar todos los clientes
GET    /clientes/{id}      # Obtener cliente por ID
POST   /clientes           # Crear nuevo cliente
PUT    /clientes/{id}      # Actualizar cliente
DELETE /clientes/{id}      # Eliminar cliente
```

### **💰 Cuentas** (Port 8081)

```http
GET    /cuentas                    # Listar cuentas
GET    /cuentas?clienteId={id}     # Cuentas por cliente
GET    /cuentas/{id}               # Obtener cuenta por ID
POST   /cuentas                    # Crear nueva cuenta
PUT    /cuentas/{id}               # Actualizar cuenta
DELETE /cuentas/{id}               # Eliminar cuenta
```

### **📈 Movimientos** (Port 8082)

```http
GET    /movimientos                     # Listar movimientos
GET    /movimientos?cuentaId={id}       # Movimientos por cuenta
POST   /movimientos                     # Crear movimiento
```

### **📊 Reportes** (Port 8083)

```http
GET    /reportes?fecha={rango}&cliente={id}  # Reporte por filtros
```

## 🗂️ **Estructura del Proyecto**

```
backend/
├── 📄 docker-compose.yml              # Orquestación de contenedores
├── 📄 BP Challenge.postman_collection.json  # Colección de Postman
├── 📁 db-init/
│   └── 📄 basedatos.sql               # Script de inicialización de BD
├── 📁 msa-client/                     # Microservicio de clientes
│   ├── 📄 build.gradle
│   ├── 📄 Dockerfile
│   └── 📁 src/main/java/com/challenge/client/
│       ├── 📁 domain/                 # Entidades de negocio
│       ├── 📁 application/            # Casos de uso
│       └── 📁 infrastructure/         # Adaptadores
├── 📁 msa-account/                    # Microservicio de cuentas
├── 📁 msa-movement/                   # Microservicio de movimientos
├── 📁 msa-report/                     # Microservicio de reportes
└── 📁 docs/                          # Documentación adicional
```

## 📖 **Documentación**

| Documento                                        | Descripción                                    |
| ------------------------------------------------ | ---------------------------------------------- |
| 🏗️ [**ARCHITECTURE.md**](./docs/ARCHITECTURE.md) | Patrones arquitectónicos y decisiones técnicas |
| 🔌 [**API.md**](./docs/API.md)                   | Documentación detallada de APIs                |
| 🚀 [**DEPLOYMENT.md**](./docs/DEPLOYMENT.md)     | Guía de despliegue y configuración             |
| 👨‍💻 [**DEVELOPMENT.md**](./docs/DEVELOPMENT.md)   | Guía para desarrolladores                      |
| 📊 [**DIAGRAMS/**](./docs/diagrams/)             | Diagramas de arquitectura y flujos             |

## 🧪 **Testing**

```bash
# Ejecutar tests unitarios
./gradlew test

# Ejecutar tests de integración
./gradlew integrationTest

# Reporte de cobertura
./gradlew jacocoTestReport
```

## 🤝 **Contribución**

1. Fork el proyecto
2. Crear una rama feature (`git checkout -b feature/nueva-funcionalidad`)
3. Commit los cambios (`git commit -m 'Add: nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abrir un Pull Request

> 📋 **Ver**: [Guía de Contribución](./docs/DEVELOPMENT.md)

## 📞 **Contacto & Soporte**

- 👤 **Autor**: Byron - Banco Pichincha Challenge
- 📧 **Email**: [contacto@proyecto.com]
- 📋 **Issues**: [GitHub Issues](link-to-issues)

---

⭐ **¡Si te gusta el proyecto, dale una estrella!** ⭐
