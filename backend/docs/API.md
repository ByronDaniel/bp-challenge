# 🔌 API Documentation

## 📋 Tabla de Contenidos

- [🌐 Visión General de APIs](#-visión-general-de-apis)
- [👥 Client Service API](#-client-service-api)
- [💰 Account Service API](#-account-service-api)
- [📈 Movement Service API](#-movement-service-api)
- [📊 Report Service API](#-report-service-api)
- [🔐 Authentication API](#-authentication-api)
- [📨 Error Handling](#-error-handling)
- [🧪 Testing APIs](#-testing-apis)

## 🌐 **Visión General de APIs**

### **Principios de Diseño API**

1. **RESTful Design**: Seguimos los principios REST con recursos bien definidos
2. **API-First**: Las APIs se diseñan antes de la implementación
3. **Consistency**: Formato consistente en todas las APIs
4. **Versioning**: Versionado semántico para backward compatibility
5. **Documentation**: OpenAPI 3.0 para documentación automática

### **Base URLs**

| Service              | Local Development       | Production                |
| -------------------- | ----------------------- | ------------------------- |
| **API Gateway**      | `http://localhost:8000` | `https://api.banking.com` |
| **Client Service**   | `http://localhost:8080` | Internal only             |
| **Account Service**  | `http://localhost:8081` | Internal only             |
| **Movement Service** | `http://localhost:8082` | Internal only             |
| **Report Service**   | `http://localhost:8083` | Internal only             |

### **Common Headers**

```http
Content-Type: application/json
Accept: application/json
Authorization: Bearer {jwt_token}
X-Request-ID: {unique_request_id}
X-Client-Version: 1.0.0
```

### **Standard Response Format**

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success|error",
  "message": "Operation completed successfully",
  "data": { ... },
  "errors": [],
  "metadata": {
    "requestId": "uuid-v4",
    "version": "v1"
  }
}
```

## 👥 **Client Service API**

### **Base Path**: `/api/v1/clientes`

### **Endpoints**

#### **GET /clientes**

Obtiene la lista de todos los clientes.

```http
GET /api/v1/clientes
Authorization: Bearer {token}
```

**Response Success (200)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success",
  "data": [
    {
      "clientId": 1,
      "personId": 1,
      "identification": "1234567890",
      "name": "Juan Pérez",
      "gender": "Masculino",
      "age": 35,
      "address": "Av. Principal 123",
      "phone": "0999123456",
      "password": "********",
      "status": true
    }
  ],
  "metadata": {
    "total": 1,
    "page": 0,
    "size": 20
  }
}
```

#### **GET /clientes/{id}**

Obtiene un cliente específico por ID.

```http
GET /api/v1/clientes/1
Authorization: Bearer {token}
```

**Response Success (200)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success",
  "data": {
    "clientId": 1,
    "personId": 1,
    "identification": "1234567890",
    "name": "Juan Pérez",
    "gender": "Masculino",
    "age": 35,
    "address": "Av. Principal 123",
    "phone": "0999123456",
    "status": true
  }
}
```

**Response Error (404)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "error",
  "message": "Cliente no encontrado",
  "errors": ["CLIENT_NOT_FOUND"],
  "metadata": {
    "requestId": "123e4567-e89b-12d3-a456-426614174000"
  }
}
```

#### **POST /clientes**

Crea un nuevo cliente.

```http
POST /api/v1/clientes
Content-Type: application/json
Authorization: Bearer {token}

{
  "identification": "1234567890",
  "name": "Juan Pérez",
  "gender": "Masculino",
  "age": 35,
  "address": "Av. Principal 123",
  "phone": "0999123456",
  "password": "securePassword123",
  "status": true
}
```

**Validation Rules**:

- `identification`: Required, 10 digits, unique
- `name`: Required, max 70 characters
- `gender`: Required, "Masculino" or "Femenino"
- `age`: Required, 18-100
- `address`: Required, max 100 characters
- `phone`: Required, 10 digits
- `password`: Required, min 8 characters, must contain letters and numbers

**Response Success (201)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success",
  "message": "Cliente creado exitosamente",
  "data": {
    "clientId": 2,
    "personId": 2,
    "identification": "1234567890",
    "name": "Juan Pérez",
    "status": true
  }
}
```

#### **PUT /clientes/{id}**

Actualiza un cliente existente.

```http
PUT /api/v1/clientes/1
Content-Type: application/json
Authorization: Bearer {token}

{
  "name": "Juan Carlos Pérez",
  "address": "Av. Secundaria 456",
  "phone": "0999654321",
  "status": true
}
```

#### **DELETE /clientes/{id}**

Elimina un cliente (soft delete).

```http
DELETE /api/v1/clientes/1
Authorization: Bearer {token}
```

## 💰 **Account Service API**

### **Base Path**: `/api/v1/cuentas`

#### **GET /cuentas**

Lista todas las cuentas o filtra por cliente.

```http
GET /api/v1/cuentas?clienteId=1
Authorization: Bearer {token}
```

**Query Parameters**:

- `clienteId` (optional): Filtrar por ID de cliente
- `tipo` (optional): Filtrar por tipo de cuenta
- `estado` (optional): Filtrar por estado (activo/inactivo)

**Response Success (200)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success",
  "data": [
    {
      "accountId": 1,
      "clientId": 1,
      "number": "0123456789",
      "type": "AHORROS",
      "balance": 1500.0,
      "status": true,
      "createdAt": "2024-01-01T00:00:00Z"
    }
  ]
}
```

#### **POST /cuentas**

Crea una nueva cuenta bancaria.

```http
POST /api/v1/cuentas
Content-Type: application/json
Authorization: Bearer {token}

{
  "clientId": 1,
  "type": "AHORROS",
  "balance": 1000.00,
  "status": true
}
```

**Account Types**:

- `AHORROS`: Cuenta de ahorros
- `CORRIENTE`: Cuenta corriente
- `PLAZO_FIJO`: Cuenta a plazo fijo

**Validation Rules**:

- `clientId`: Required, must exist
- `type`: Required, valid account type
- `balance`: Required, >= 0
- `status`: Required, boolean

## 📈 **Movement Service API**

### **Base Path**: `/api/v1/movimientos`

#### **GET /movimientos**

Lista movimientos con filtros opcionales.

```http
GET /api/v1/movimientos?cuentaId=1&fechaInicio=2024-01-01&fechaFin=2024-01-31
Authorization: Bearer {token}
```

**Query Parameters**:

- `cuentaId` (optional): Filtrar por cuenta
- `fechaInicio` (optional): Fecha inicio (ISO 8601)
- `fechaFin` (optional): Fecha fin (ISO 8601)
- `tipo` (optional): CREDITO o DEBITO
- `limite` (optional): Número máximo de resultados

#### **POST /movimientos**

Crea un nuevo movimiento (transacción).

```http
POST /api/v1/movimientos
Content-Type: application/json
Authorization: Bearer {token}

{
  "accountId": 1,
  "type": "DEBITO",
  "value": 100.00,
  "description": "Retiro en cajero automático"
}
```

**Movement Types**:

- `CREDITO`: Incrementa el saldo
- `DEBITO`: Decrementa el saldo

**Validation Rules**:

- `accountId`: Required, must exist and be active
- `type`: Required, CREDITO or DEBITO
- `value`: Required, > 0, max 2 decimals
- `description`: Optional, max 255 characters

**Business Rules**:

- Para DEBITO: Verificar saldo suficiente
- Límite diario de transacciones: $5,000
- Horario de operaciones: 06:00 - 22:00

## 📊 **Report Service API**

### **Base Path**: `/api/v1/reportes`

#### **GET /reportes**

Genera reporte de estado de cuenta.

```http
GET /api/v1/reportes?fecha=2024-01-01,2024-01-31&cliente=1
Authorization: Bearer {token}
```

**Query Parameters**:

- `fecha`: Required, formato "YYYY-MM-DD,YYYY-MM-DD"
- `cliente`: Required, ID del cliente
- `formato`: Optional, "JSON" (default) | "PDF" | "CSV"

**Response Success (200)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success",
  "data": {
    "client": {
      "clientId": 1,
      "name": "Juan Pérez",
      "identification": "1234567890"
    },
    "period": {
      "startDate": "2024-01-01",
      "endDate": "2024-01-31"
    },
    "accounts": [
      {
        "accountId": 1,
        "number": "0123456789",
        "type": "AHORROS",
        "initialBalance": 1000.0,
        "finalBalance": 1500.0,
        "movements": [
          {
            "movementId": 1,
            "date": "2024-01-15T10:30:00Z",
            "type": "CREDITO",
            "amount": 500.0,
            "balance": 1500.0,
            "description": "Depósito"
          }
        ]
      }
    ],
    "summary": {
      "totalCredits": 500.0,
      "totalDebits": 0.0,
      "netMovement": 500.0,
      "transactionCount": 1
    }
  }
}
```

## 🔐 **Authentication API**

### **Base Path**: `/api/v1/auth`

#### **POST /auth/login**

Autenticación de usuario.

```http
POST /api/v1/auth/login
Content-Type: application/json

{
  "username": "1234567890",
  "password": "securePassword123"
}
```

**Response Success (200)**:

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "success",
  "data": {
    "accessToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "user": {
      "id": 1,
      "name": "Juan Pérez",
      "roles": ["CLIENT"]
    }
  }
}
```

#### **POST /auth/refresh**

Renovación de token.

```http
POST /api/v1/auth/refresh
Content-Type: application/json

{
  "refreshToken": "eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

## 📨 **Error Handling**

### **Standard Error Codes**

| HTTP Status | Error Code                | Description                                   |
| ----------- | ------------------------- | --------------------------------------------- |
| 400         | `VALIDATION_ERROR`        | Datos de entrada inválidos                    |
| 401         | `UNAUTHORIZED`            | Token inválido o expirado                     |
| 403         | `FORBIDDEN`               | Sin permisos para el recurso                  |
| 404         | `RESOURCE_NOT_FOUND`      | Recurso no encontrado                         |
| 409         | `CONFLICT`                | Conflicto de negocio (ej: saldo insuficiente) |
| 422         | `BUSINESS_RULE_VIOLATION` | Violación de reglas de negocio                |
| 429         | `RATE_LIMIT_EXCEEDED`     | Límite de requests excedido                   |
| 500         | `INTERNAL_SERVER_ERROR`   | Error interno del servidor                    |

### **Error Response Format**

```json
{
  "timestamp": "2024-01-15T10:30:00Z",
  "status": "error",
  "message": "Validation failed",
  "errors": [
    {
      "field": "email",
      "code": "INVALID_FORMAT",
      "message": "Email format is invalid"
    }
  ],
  "metadata": {
    "requestId": "123e4567-e89b-12d3-a456-426614174000",
    "path": "/api/v1/clientes",
    "method": "POST"
  }
}
```

## 🧪 **Testing APIs**

### **Postman Collection**

Importar la colección: `BP Challenge.postman_collection.json`

### **cURL Examples**

#### **Crear Cliente**

```bash
curl -X POST http://localhost:8080/api/v1/clientes \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "identification": "1234567890",
    "name": "Juan Pérez",
    "gender": "Masculino",
    "age": 35,
    "address": "Av. Principal 123",
    "phone": "0999123456",
    "password": "securePassword123",
    "status": true
  }'
```

#### **Realizar Movimiento**

```bash
curl -X POST http://localhost:8082/api/v1/movimientos \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{
    "accountId": 1,
    "type": "DEBITO",
    "value": 100.00,
    "description": "Retiro cajero automático"
  }'
```

### **API Testing Tools**

1. **Postman**: Para testing manual y automático
2. **Newman**: Para CI/CD pipeline testing
3. **JMeter**: Para performance testing
4. **Swagger UI**: Para exploración interactiva

### **OpenAPI Specification**

Cada microservicio expone su especificación OpenAPI en:

- Client Service: `http://localhost:8080/v3/api-docs`
- Account Service: `http://localhost:8081/v3/api-docs`
- Movement Service: `http://localhost:8082/v3/api-docs`
- Report Service: `http://localhost:8083/v3/api-docs`

---

> 📋 **Siguiente**: [Deployment Guide](./DEPLOYMENT.md) | [Development Guide](./DEVELOPMENT.md)
