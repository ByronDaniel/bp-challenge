# 📚 Guía de Estándares y Mejores Prácticas - BP Challenge

## 🎯 Principios de Desarrollo

### 1. SOLID Principles

- **S** - Single Responsibility: Cada clase/componente tiene una responsabilidad
- **O** - Open/Closed: Abierto para extensión, cerrado para modificación
- **L** - Liskov Substitution: Los subtipos deben ser sustituibles por sus tipos base
- **I** - Interface Segregation: Interfaces específicas mejor que una general
- **D** - Dependency Inversion: Depender de abstracciones, no de concreciones

### 2. DRY (Don't Repeat Yourself)

```typescript
// ❌ Mal - Código duplicado
export class ClientFormComponent {
  getError(field: string): string {
    const control = this.form.get(field);
    if (!control?.touched || !control?.errors) return "";
    if (control.errors["required"]) return "Campo requerido";
    // ... más validaciones
  }
}

export class AccountFormComponent {
  getError(field: string): string {
    const control = this.form.get(field);
    if (!control?.touched || !control?.errors) return "";
    if (control.errors["required"]) return "Campo requerido";
    // ... mismo código duplicado
  }
}

// ✅ Bien - Usando clase base
export abstract class BaseFormComponent {
  protected getError(field: string): string {
    const control = this.form.get(field);
    if (!control?.touched || !control?.errors) return "";
    if (control.errors["required"]) return "Campo requerido";
    // ... lógica centralizada
  }
}
```

## 🏗️ Arquitectura de Componentes

### Jerarquía de Herencia

```typescript
// Componente base para funcionalidades comunes
export abstract class BaseComponent implements OnDestroy {
  protected readonly cdr = inject(ChangeDetectorRef);
  protected readonly destroy$ = new Subject<void>();

  // Estados comunes
  isLoading = false;
  hasError = false;
  isEmpty = false;

  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// Componente base para formularios
export abstract class BaseFormComponent extends BaseComponent {
  protected readonly fb = inject(FormBuilder);
  protected readonly router = inject(Router);
  protected readonly alert = inject(AlertService);

  protected abstract form: FormGroup;

  protected markTouched(): void {
    Object.values(this.form.controls).forEach((control) => control.markAsTouched());
  }
}
```

### Nomenclatura de Componentes

```typescript
// ✅ Patrón recomendado
export class ClientFormComponent extends BaseFormComponent {}
export class ClientListComponent extends BaseComponent {}
export class ClientSearchComponent extends BaseComponent {}

// Nomenclatura de archivos
client - form.component.ts;
client - form.component.html;
client - form.component.scss;
client - form.component.spec.ts;
```

## 🎨 Estándares de CSS/SCSS

### Metodología BEM

```scss
// ✅ Correcto - BEM
.client-form {
  padding: $spacing-large;

  &__field {
    margin-bottom: $spacing-medium;

    &--required {
      border-left: 3px solid $color-danger;
    }
  }

  &__input {
    width: 100%;
    padding: $spacing-small;

    &:focus {
      border-color: $color-primary;
    }

    &--error {
      border-color: $color-danger;
    }
  }
}

// ❌ Incorrecto - Anidación profunda
.client-form {
  .field {
    .input {
      .label {
        .text {
          color: red; // Muy anidado
        }
      }
    }
  }
}
```

### Variables y Mixins

```scss
// _variables.scss
// Colores semánticos
$color-primary: #0ea5e9;
$color-secondary: #3b82f6;
$color-success: #10b981;
$color-danger: #ef4444;
$color-warning: #f59e0b;

// Spacing sistema
$spacing-xs: 4px;
$spacing-sm: 8px;
$spacing-md: 16px;
$spacing-lg: 24px;
$spacing-xl: 32px;
$spacing-xxl: 48px;

// Breakpoints
$breakpoint-mobile: 768px;
$breakpoint-tablet: 1024px;
$breakpoint-desktop: 1200px;

// _mixins.scss
@mixin button-variant($bg-color, $text-color: white) {
  background-color: $bg-color;
  color: $text-color;
  border: none;
  padding: $spacing-sm $spacing-md;
  border-radius: 6px;
  cursor: pointer;

  &:hover {
    background-color: darken($bg-color, 10%);
  }

  &:disabled {
    opacity: 0.6;
    cursor: not-allowed;
  }
}

@mixin responsive($breakpoint) {
  @if $breakpoint == mobile {
    @media (max-width: #{$breakpoint-mobile - 1px}) {
      @content;
    }
  }
  @if $breakpoint == tablet {
    @media (min-width: #{$breakpoint-mobile}) and (max-width: #{$breakpoint-tablet - 1px}) {
      @content;
    }
  }
  @if $breakpoint == desktop {
    @media (min-width: #{$breakpoint-desktop}) {
      @content;
    }
  }
}
```

### Organización de Archivos SCSS

```
src/app/styles/
├── _variables.scss      # Variables globales
├── _mixins.scss        # Mixins reutilizables
├── _utilities.scss     # Clases utilitarias
├── _base.scss         # Reset y estilos base
└── _components.scss   # Componentes globales
```

## 🔧 TypeScript Standards

### Tipado Estricto

```typescript
// ✅ Interfaces bien definidas
export interface Client {
  readonly id: number;
  identification: string;
  name: string;
  gender: "Masculino" | "Femenino";
  age: number;
  address: string;
  phone: string;
  password: string;
  status: boolean;
}

// ✅ Utility types
export type CreateClient = Omit<Client, "id">;
export type UpdateClient = Partial<Omit<Client, "id" | "identification">>;

// ✅ Union types para estados
export type LoadingState = "idle" | "loading" | "success" | "error";

// ✅ Generic types
export interface ApiResponse<T> {
  data: T;
  message: string;
  status: number;
}
```

### Dependency Injection Moderna

```typescript
// ✅ Patrón moderno con inject()
export class ClientService {
  private readonly http = inject(HttpClient);
  private readonly alert = inject(AlertService);

  constructor() {} // Constructor vacío
}

// ❌ Patrón antiguo
export class ClientService {
  constructor(
    private http: HttpClient,
    private alert: AlertService,
  ) {}
}
```

### Error Handling

```typescript
// ✅ Manejo robusto de errores
export class ClientService {
  getAll(): Observable<Client[]> {
    return this.http.get<Client[]>(this.apiUrl).pipe(
      retry(2),
      timeout(10000),
      catchError(this.handleError.bind(this)),
      shareReplay(1), // Cache para múltiples subscripciones
    );
  }

  private handleError(error: HttpErrorResponse): Observable<never> {
    let errorMessage = "Error desconocido";

    if (error.error instanceof ErrorEvent) {
      // Error del cliente
      errorMessage = `Error: ${error.error.message}`;
    } else {
      // Error del servidor
      switch (error.status) {
        case 400:
          errorMessage = "Datos inválidos";
          break;
        case 404:
          errorMessage = "Recurso no encontrado";
          break;
        case 500:
          errorMessage = "Error interno del servidor";
          break;
        default:
          errorMessage = `Error ${error.status}: ${error.message}`;
      }
    }

    console.error("Error en ClientService:", errorMessage);
    return throwError(() => new Error(errorMessage));
  }
}
```

## 🔄 RxJS Best Practices

### Manejo de Subscripciones

```typescript
// ✅ Patrón takeUntil para evitar memory leaks
export class ClientListComponent extends BaseComponent implements OnInit {
  clients$ = new BehaviorSubject<Client[]>([]);
  loading$ = new BehaviorSubject<boolean>(false);

  ngOnInit(): void {
    this.loadClients();
  }

  private loadClients(): void {
    this.loading$.next(true);

    this.clientService
      .getAll()
      .pipe(
        takeUntil(this.destroy$), // ✅ Previene memory leaks
        finalize(() => this.loading$.next(false)),
      )
      .subscribe({
        next: (clients) => this.clients$.next(clients),
        error: (error) => this.handleError(error),
      });
  }
}

// ❌ Sin unsubscribe - memory leak
export class BadComponent implements OnInit {
  ngOnInit(): void {
    this.service.getData().subscribe((data) => {
      // Esta subscripción nunca se limpia
    });
  }
}
```

### Operators Útiles

```typescript
// Búsqueda con debounce
setupSearch(): void {
  this.searchControl.valueChanges.pipe(
    debounceTime(300),           // Espera 300ms después del último cambio
    distinctUntilChanged(),      // Solo emite si el valor cambió
    filter(term => term.length > 2), // Solo busca con 3+ caracteres
    switchMap(term =>            // Cancela búsquedas anteriores
      this.searchService.search(term).pipe(
        catchError(() => of([])) // Maneja errores devolviendo array vacío
      )
    ),
    takeUntil(this.destroy$)
  ).subscribe(results => {
    this.searchResults = results;
  });
}
```

## 🧪 Testing Standards

### Unit Tests

```typescript
describe("ClientService", () => {
  let service: ClientService;
  let httpMock: HttpTestingController;
  let alertSpy: jasmine.SpyObj<AlertService>;

  beforeEach(() => {
    const spy = jasmine.createSpyObj("AlertService", ["error", "success"]);

    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [ClientService, { provide: AlertService, useValue: spy }],
    });

    service = TestBed.inject(ClientService);
    httpMock = TestBed.inject(HttpTestingController);
    alertSpy = TestBed.inject(AlertService) as jasmine.SpyObj<AlertService>;
  });

  afterEach(() => {
    httpMock.verify();
  });

  describe("getAll", () => {
    it("should return clients", () => {
      const mockClients: Client[] = [{ id: 1, name: "Test Client" /* ... */ }];

      service.getAll().subscribe((clients) => {
        expect(clients).toEqual(mockClients);
      });

      const req = httpMock.expectOne(`${environment.apiUrl}/clientes`);
      expect(req.request.method).toBe("GET");
      req.flush(mockClients);
    });

    it("should handle errors", () => {
      service.getAll().subscribe({
        next: () => fail("should have failed"),
        error: (error) => {
          expect(error.message).toContain("Error");
        },
      });

      const req = httpMock.expectOne(`${environment.apiUrl}/clientes`);
      req.flush("Error", { status: 500, statusText: "Server Error" });
    });
  });
});
```

### Component Tests

```typescript
describe("ClientFormComponent", () => {
  let component: ClientFormComponent;
  let fixture: ComponentFixture<ClientFormComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [ClientFormComponent, ReactiveFormsModule],
      providers: [{ provide: ClientService, useValue: jasmine.createSpyObj("ClientService", ["create"]) }],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientFormComponent);
    component = fixture.componentInstance;
  });

  it("should create", () => {
    expect(component).toBeTruthy();
  });

  it("should validate required fields", () => {
    component.form.patchValue({
      name: "",
      identification: "",
    });

    expect(component.form.invalid).toBeTruthy();
    expect(component.getFieldError("name")).toBe("Este campo es requerido");
  });

  it("should submit valid form", () => {
    const clientService = TestBed.inject(ClientService) as jasmine.SpyObj<ClientService>;
    clientService.create.and.returnValue(of(mockClient));

    component.form.patchValue(validClientData);
    component.onSubmit();

    expect(clientService.create).toHaveBeenCalledWith(validClientData);
  });
});
```

## 📁 Estructura de Proyecto

### Organización de Archivos

```
src/app/
├── components/
│   ├── base/                    # Componentes base abstractos
│   │   ├── base.component.ts
│   │   └── base-form.component.ts
│   ├── shared/                  # Componentes compartidos
│   │   ├── loading/
│   │   ├── error-message/
│   │   └── confirm-dialog/
│   └── feature/                 # Componentes específicos
│       ├── client/
│       ├── account/
│       └── movement/
├── views/                       # Páginas principales
├── services/                    # Servicios de negocio
├── guards/                      # Route guards
├── interceptors/               # HTTP interceptors
├── pipes/                      # Custom pipes
├── directives/                 # Custom directives
├── types/                      # Interfaces y tipos
├── constants/                  # Constantes
├── utils/                      # Funciones utilitarias
└── styles/                     # Estilos globales
```

### Naming Conventions

```typescript
// ✅ Archivos y clases
client.service.ts           → ClientService
client-form.component.ts    → ClientFormComponent
client.type.ts             → Client (interface)
client.guard.ts            → ClientGuard
auth.interceptor.ts        → AuthInterceptor

// ✅ Variables y métodos
const userName = 'john';           // camelCase
const API_BASE_URL = 'https://';   // UPPER_SNAKE_CASE para constantes
private loadClients(): void {}    // camelCase para métodos

// ✅ Selectores de componentes
app-client-form               // kebab-case
app-account-list
app-movement-search
```

## 🚀 Performance Best Practices

### Change Detection

```typescript
// ✅ OnPush para mejor performance
@Component({
  selector: "app-client-list",
  changeDetection: ChangeDetectionStrategy.OnPush,
  template: `...`,
})
export class ClientListComponent {
  // Angular solo verifica cambios cuando:
  // 1. @Input() cambia (por referencia)
  // 2. Evento del template
  // 3. Async pipe emite
  // 4. Manualmente con cdr.markForCheck()
}
```

### TrackBy Functions

```typescript
// ✅ TrackBy para listas grandes
export class ClientListComponent {
  trackByClientId = (index: number, client: Client): number => client.id;
}
```

```html
<!-- ✅ En el template -->
<div *ngFor="let client of clients; trackBy: trackByClientId">{{ client.name }}</div>
```

### Lazy Loading

```typescript
// ✅ Lazy loading de rutas
export const routes: Routes = [
  {
    path: "clientes",
    loadComponent: () => import("./views/clients-view/clients-view.component").then((m) => m.ClientsViewComponent),
  },
  {
    path: "cuentas",
    loadComponent: () => import("./views/accounts-view/accounts-view.component").then((m) => m.AccountsViewComponent),
  },
];
```

## 🔒 Security Best Practices

### Validaciones

```typescript
// ✅ Validaciones robustas
export class ClientFormComponent {
  private createForm(): FormGroup {
    return this.fb.group({
      identification: [
        "",
        [
          Validators.required,
          Validators.pattern(/^[0-9]{8,13}$/), // Solo números, 8-13 dígitos
          this.customIdentificationValidator,
        ],
      ],
      email: ["", [Validators.required, Validators.email, this.customEmailValidator]],
      age: ["", [Validators.required, Validators.min(18), Validators.max(100)]],
    });
  }

  private customIdentificationValidator(control: AbstractControl): ValidationErrors | null {
    const value = control.value;
    if (!value) return null;

    // Lógica específica de validación de identificación
    const isValid = validateIdentification(value);
    return isValid ? null : { invalidIdentification: true };
  }
}
```

### Sanitización

```typescript
// ✅ Angular sanitiza automáticamente, pero para casos especiales:
import { DomSanitizer } from "@angular/platform-browser";

export class SafeHtmlComponent {
  constructor(private sanitizer: DomSanitizer) {}

  getSafeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }
}
```

## 📝 Documentation Standards

### JSDoc para Funciones Públicas

````typescript
/**
 * Obtiene todos los clientes del sistema
 * @returns Observable que emite un array de clientes
 * @throws Error si la petición falla después de 2 reintentos
 * @example
 * ```typescript
 * this.clientService.getAll().subscribe(clients => {
 *   console.log('Clientes:', clients);
 * });
 * ```
 */
getAll(): Observable<Client[]> {
  return this.http.get<Client[]>(this.apiUrl)
    .pipe(retry(2), catchError(this.handleError));
}

/**
 * Crea un nuevo cliente en el sistema
 * @param client - Datos del cliente sin ID
 * @returns Observable que emite el cliente creado con ID
 */
create(client: CreateClient): Observable<Client> {
  return this.http.post<Client>(this.apiUrl, client);
}
````

### README por Feature

```markdown
# Client Management Feature

## Componentes

- `ClientFormComponent`: Formulario para crear/editar clientes
- `ClientListComponent`: Lista de clientes con acciones
- `ClientSearchComponent`: Búsqueda de clientes

## Servicios

- `ClientService`: CRUD operations para clientes

## Rutas

- `/clientes` - Lista de clientes
- `/clientes/crear` - Crear cliente
- `/clientes/editar/:id` - Editar cliente
```

---

**Siguiendo estos estándares garantizas código de alta calidad, mantenible y escalable para el proyecto BP Challenge.**
