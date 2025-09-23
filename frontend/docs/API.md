# 📡 Documentación de API - BP Challenge

## 🌐 Configuración de Endpoints

### Base URL

```typescript
// Development
const API_BASE_URL = "https://9e7043da-4f4b-431d-afdf-d300eafc5e90.mock.pstmn.io";

// Production
const API_BASE_URL = "https://api.production-domain.com";
```

## 👥 Clientes API

### 📋 GET /clientes

Obtener todos los clientes

**Request:**

```http
GET /clientes
Content-Type: application/json
```

**Response:**

```json
[
  {
    "id": 1,
    "identification": "12345678",
    "name": "Juan Pérez",
    "gender": "Masculino",
    "age": 30,
    "address": "Av. Principal 123",
    "phone": "0999123456",
    "password": "password123",
    "status": true
  }
]
```

### 📄 GET /clientes/{id}

Obtener cliente por ID

**Request:**

```http
GET /clientes/1
Content-Type: application/json
```

**Response:**

```json
{
  "id": 1,
  "identification": "12345678",
  "name": "Juan Pérez",
  "gender": "Masculino",
  "age": 30,
  "address": "Av. Principal 123",
  "phone": "0999123456",
  "password": "password123",
  "status": true
}
```

### ➕ POST /clientes

Crear nuevo cliente

**Request:**

```http
POST /clientes
Content-Type: application/json

{
  "identification": "87654321",
  "name": "María García",
  "gender": "Femenino",
  "age": 25,
  "address": "Calle Secundaria 456",
  "phone": "0987654321",
  "password": "securepass",
  "status": true
}
```

**Response:**

```json
{
  "id": 2,
  "identification": "87654321",
  "name": "María García",
  "gender": "Femenino",
  "age": 25,
  "address": "Calle Secundaria 456",
  "phone": "0987654321",
  "password": "securepass",
  "status": true
}
```

### ✏️ PUT /clientes/{id}

Actualizar cliente

**Request:**

```http
PUT /clientes/1
Content-Type: application/json

{
  "name": "Juan Carlos Pérez",
  "age": 31,
  "phone": "0999111222"
}
```

**Response:**

```json
{
  "id": 1,
  "identification": "12345678",
  "name": "Juan Carlos Pérez",
  "gender": "Masculino",
  "age": 31,
  "address": "Av. Principal 123",
  "phone": "0999111222",
  "password": "password123",
  "status": true
}
```

### 🗑️ DELETE /clientes/{id}

Eliminar cliente

**Request:**

```http
DELETE /clientes/1
```

**Response:**

```http
HTTP/1.1 204 No Content
```

## 💳 Cuentas API

### 📋 GET /cuentas

Obtener todas las cuentas

**Request:**

```http
GET /cuentas
Content-Type: application/json
```

**Response:**

```json
[
  {
    "accountId": 1,
    "number": "001-0001-0001",
    "type": "Corriente",
    "balance": 1500.5,
    "status": true,
    "clientId": 1
  }
]
```

### 📄 GET /cuentas/{id}

Obtener cuenta por ID

**Request:**

```http
GET /cuentas/1
Content-Type: application/json
```

**Response:**

```json
{
  "accountId": 1,
  "number": "001-0001-0001",
  "type": "Corriente",
  "balance": 1500.5,
  "status": true,
  "clientId": 1
}
```

### ➕ POST /cuentas

Crear nueva cuenta

**Request:**

```http
POST /cuentas
Content-Type: application/json

{
  "type": "Ahorros",
  "balance": 1000.00,
  "clientId": 1,
  "status": true
}
```

**Response:**

```json
{
  "accountId": 2,
  "number": "001-0001-0002",
  "type": "Ahorros",
  "balance": 1000.0,
  "status": true,
  "clientId": 1
}
```

### ✏️ PUT /cuentas/{id}

Actualizar cuenta

**Request:**

```http
PUT /cuentas/1
Content-Type: application/json

{
  "status": false
}
```

**Response:**

```json
{
  "accountId": 1,
  "number": "001-0001-0001",
  "type": "Corriente",
  "balance": 1500.5,
  "status": false,
  "clientId": 1
}
```

### 🗑️ DELETE /cuentas/{id}

Eliminar cuenta

**Request:**

```http
DELETE /cuentas/1
```

**Response:**

```http
HTTP/1.1 204 No Content
```

## 💰 Movimientos API

### 📋 GET /movimientos

Obtener todos los movimientos

**Request:**

```http
GET /movimientos
Content-Type: application/json
```

**Response:**

```json
[
  {
    "movementId": 1,
    "date": "2025-01-15T10:30:00Z",
    "type": "Credito",
    "value": 500.0,
    "balance": 2000.5,
    "accountId": 1
  }
]
```

### 🔍 GET /movimientos?accountId={accountId}

Obtener movimientos por cuenta

**Request:**

```http
GET /movimientos?accountId=1
Content-Type: application/json
```

**Response:**

```json
[
  {
    "movementId": 1,
    "date": "2025-01-15T10:30:00Z",
    "type": "Credito",
    "value": 500.0,
    "balance": 2000.5,
    "accountId": 1
  },
  {
    "movementId": 2,
    "date": "2025-01-15T14:20:00Z",
    "type": "Debito",
    "value": 100.0,
    "balance": 1900.5,
    "accountId": 1
  }
]
```

### ➕ POST /movimientos

Crear nuevo movimiento

**Request:**

```http
POST /movimientos
Content-Type: application/json

{
  "type": "Debito",
  "value": 200.00,
  "accountId": 1
}
```

**Response:**

```json
{
  "movementId": 3,
  "date": "2025-01-15T16:45:00Z",
  "type": "Debito",
  "value": 200.0,
  "balance": 1700.5,
  "accountId": 1
}
```

## 📊 Reportes API

### 📋 GET /reporte

Generar reporte de movimientos

**Request:**

```http
GET /reporte?date=2025-01-01,2025-01-31&clientId=1
Content-Type: application/json
```

**Query Parameters:**

- `date`: Rango de fechas (formato: YYYY-MM-DD,YYYY-MM-DD)
- `clientId`: ID del cliente

**Response:**

```json
[
  {
    "reportId": 1,
    "date": "2025-01-15T10:30:00Z",
    "client": "Juan Pérez",
    "accountNumber": "001-0001-0001",
    "accountType": "Corriente",
    "initialBalance": 1500.5,
    "movementType": "Credito",
    "movementValue": 500.0,
    "finalBalance": 2000.5
  }
]
```

## 🔧 Configuración de Servicios Angular

### ClientService

```typescript
@Injectable({ providedIn: "root" })
export class ClientService {
  private readonly endpoint = "clientes";
  private readonly apiUrl = `${environment.apiUrl}/${this.endpoint}`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl).pipe(retry(2), catchError(this.handleError));
  }

  getById(id: number): Observable<Client> {
    return this.http.get<Client>(`${this.apiUrl}/${id}`);
  }

  create(client: Omit<Client, "id">): Observable<Client> {
    return this.http.post<Client>(this.apiUrl, client);
  }

  update(id: number, client: Partial<Client>): Observable<Client> {
    return this.http.put<Client>(`${this.apiUrl}/${id}`, client);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  private handleError(error: HttpErrorResponse) {
    console.error("An error occurred:", error);
    return throwError(() => error.error?.message || "No se pudo conectar con el servidor");
  }
}
```

### AccountService

```typescript
@Injectable({ providedIn: "root" })
export class AccountService {
  private readonly endpoint = "cuentas";
  private readonly apiUrl = `${environment.apiUrl}/${this.endpoint}`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Account[]> {
    return this.http.get<Account[]>(this.apiUrl).pipe(retry(2), catchError(this.handleError));
  }

  getById(id: number): Observable<Account> {
    return this.http.get<Account>(`${this.apiUrl}/${id}`);
  }

  create(account: Omit<Account, "accountId" | "number">): Observable<Account> {
    return this.http.post<Account>(this.apiUrl, account);
  }

  update(id: number, account: Partial<Account>): Observable<Account> {
    return this.http.put<Account>(`${this.apiUrl}/${id}`, account);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  private handleError(error: HttpErrorResponse) {
    console.error("An error occurred:", error);
    return throwError(() => error.error?.message || "No se pudo conectar con el servidor");
  }
}
```

### MovementService

```typescript
@Injectable({ providedIn: "root" })
export class MovementService {
  private readonly endpoint = "movimientos";
  private readonly apiUrl = `${environment.apiUrl}/${this.endpoint}`;

  constructor(private http: HttpClient) {}

  getAll(): Observable<Movement[]> {
    return this.http.get<Movement[]>(this.apiUrl).pipe(retry(2), catchError(this.handleError));
  }

  getByAccountId(accountId: number): Observable<Movement[]> {
    return this.http.get<Movement[]>(`${this.apiUrl}?accountId=${accountId}`).pipe(retry(2), catchError(this.handleError));
  }

  create(movement: Omit<Movement, "movementId" | "date" | "balance">): Observable<Movement> {
    return this.http.post<Movement>(this.apiUrl, movement);
  }

  private handleError(error: HttpErrorResponse) {
    console.error("An error occurred:", error);
    return throwError(() => error.error?.message || "No se pudo conectar con el servidor");
  }
}
```

### ReportService

```typescript
@Injectable({ providedIn: "root" })
export class ReportService {
  private readonly baseUrl = environment.apiUrl;

  constructor(private http: HttpClient) {}

  generateReport(filter: ReportFilter): Observable<Report[]> {
    const url = `${this.baseUrl}/reporte?date=${filter.startDate},${filter.endDate}&clientId=${filter.clientId}`;

    return this.http.get<Report[]>(url).pipe(retry(2), catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    console.error("An error occurred:", error);
    return throwError(() => error.error?.message || "No se pudo conectar con el servidor");
  }
}
```

## 🚨 Manejo de Errores

### Códigos de Estado HTTP

| Código | Descripción           | Ejemplo               |
| ------ | --------------------- | --------------------- |
| 200    | OK                    | Operación exitosa     |
| 201    | Created               | Recurso creado        |
| 204    | No Content            | Eliminación exitosa   |
| 400    | Bad Request           | Datos inválidos       |
| 404    | Not Found             | Recurso no encontrado |
| 409    | Conflict              | Recurso ya existe     |
| 500    | Internal Server Error | Error del servidor    |

### Respuestas de Error

```json
{
  "error": {
    "code": "VALIDATION_ERROR",
    "message": "Los datos proporcionados no son válidos",
    "details": [
      {
        "field": "identification",
        "message": "La identificación ya existe"
      }
    ]
  }
}
```

### Estrategia de Retry

```typescript
// Configuración de retry en servicios
.pipe(
  retry(2), // Reintenta 2 veces
  catchError(this.handleError)
)
```

## 🔒 Seguridad

### Headers Requeridos

```http
Content-Type: application/json
Accept: application/json
```

### Headers Recomendados para Producción

```http
Authorization: Bearer <token>
X-Requested-With: XMLHttpRequest
X-CSRF-Token: <token>
```

## 📊 Límites y Cuotas

| Endpoint | Límite por minuto | Límite por hora |
| -------- | ----------------- | --------------- |
| GET      | 100 requests      | 1000 requests   |
| POST     | 30 requests       | 300 requests    |
| PUT      | 30 requests       | 300 requests    |
| DELETE   | 10 requests       | 100 requests    |

## 🧪 Testing

### Ejemplo de Test de Servicio

```typescript
describe("ClientService", () => {
  let service: ClientService;
  let httpMock: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ClientService],
    });
    service = TestBed.inject(ClientService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  it("should get all clients", () => {
    const mockClients: Client[] = [{ id: 1, name: "Test Client" /* ... */ }];

    service.getAll().subscribe((clients) => {
      expect(clients).toEqual(mockClients);
    });

    const req = httpMock.expectOne(`${environment.apiUrl}/clientes`);
    expect(req.request.method).toBe("GET");
    req.flush(mockClients);
  });
});
```

---

**Esta documentación debe actualizarse cuando cambien los endpoints o la estructura de datos del backend.**
