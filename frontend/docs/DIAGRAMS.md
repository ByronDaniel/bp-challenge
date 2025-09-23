# 📊 Diagramas de Arquitectura - BP Challenge

## 🏗️ Diagrama de Componentes

```mermaid
graph TB
    subgraph "🌐 Angular Application"
        subgraph "📱 Core Components"
            APP[App Component]
            HEADER[Header Component]
            SIDEBAR[Sidebar Component]
            ROUTER[Router Outlet]
        end

        subgraph "👀 Views Layer"
            ACCOUNTS_VIEW[Accounts View]
            CLIENTS_VIEW[Clients View]
            MOVEMENTS_VIEW[Movements View]
            REPORTS_VIEW[Reports View]
        end

        subgraph "🧩 Feature Components"
            subgraph "💳 Account Components"
                ACC_FORM[Account Form]
                ACC_LIST[Account List]
                ACC_SEARCH[Account Search]
            end

            subgraph "👥 Client Components"
                CLI_FORM[Client Form]
                CLI_LIST[Client List]
                CLI_SEARCH[Client Search]
            end

            subgraph "💰 Movement Components"
                MOV_FORM[Movement Form]
                MOV_LIST[Movement List]
                MOV_SEARCH[Movement Search]
            end

            subgraph "📊 Report Components"
                REP_FORM[Report Form]
                REP_LIST[Report List]
                REP_SEARCH[Report Search]
            end
        end

        subgraph "🔧 Services Layer"
            ACC_SERVICE[Account Service]
            CLI_SERVICE[Client Service]
            MOV_SERVICE[Movement Service]
            REP_SERVICE[Report Service]
            ALERT_SERVICE[Alert Service]
            MENU_SERVICE[Menu Service]
        end

        subgraph "📦 Base Classes"
            BASE_COMP[Base Component]
            BASE_FORM[Base Form Component]
        end
    end

    subgraph "🌍 External APIs"
        BACKEND[Backend API]
        MOCK_API[Mock API]
    end

    %% Connections
    APP --> HEADER
    APP --> SIDEBAR
    APP --> ROUTER

    ROUTER --> ACCOUNTS_VIEW
    ROUTER --> CLIENTS_VIEW
    ROUTER --> MOVEMENTS_VIEW
    ROUTER --> REPORTS_VIEW

    ACCOUNTS_VIEW --> ACC_FORM
    ACCOUNTS_VIEW --> ACC_LIST
    ACCOUNTS_VIEW --> ACC_SEARCH

    CLIENTS_VIEW --> CLI_FORM
    CLIENTS_VIEW --> CLI_LIST
    CLIENTS_VIEW --> CLI_SEARCH

    MOVEMENTS_VIEW --> MOV_FORM
    MOVEMENTS_VIEW --> MOV_LIST
    MOVEMENTS_VIEW --> MOV_SEARCH

    REPORTS_VIEW --> REP_FORM
    REPORTS_VIEW --> REP_LIST
    REPORTS_VIEW --> REP_SEARCH

    %% Service connections
    ACC_FORM --> ACC_SERVICE
    ACC_LIST --> ACC_SERVICE
    CLI_FORM --> CLI_SERVICE
    CLI_LIST --> CLI_SERVICE
    MOV_FORM --> MOV_SERVICE
    MOV_LIST --> MOV_SERVICE
    REP_FORM --> REP_SERVICE
    REP_LIST --> REP_SERVICE

    %% Base class inheritance
    BASE_COMP -.-> ACCOUNTS_VIEW
    BASE_COMP -.-> CLIENTS_VIEW
    BASE_COMP -.-> MOVEMENTS_VIEW
    BASE_COMP -.-> REPORTS_VIEW

    BASE_FORM -.-> ACC_FORM
    BASE_FORM -.-> CLI_FORM
    BASE_FORM -.-> MOV_FORM
    BASE_FORM -.-> REP_FORM

    %% External connections
    ACC_SERVICE --> BACKEND
    CLI_SERVICE --> BACKEND
    MOV_SERVICE --> BACKEND
    REP_SERVICE --> MOCK_API

    SIDEBAR --> MENU_SERVICE

    %% Styling
    classDef viewClass fill:#e1f5fe,stroke:#01579b,stroke-width:2px
    classDef componentClass fill:#f3e5f5,stroke:#4a148c,stroke-width:2px
    classDef serviceClass fill:#e8f5e8,stroke:#1b5e20,stroke-width:2px
    classDef baseClass fill:#fff3e0,stroke:#e65100,stroke-width:2px
    classDef externalClass fill:#ffebee,stroke:#b71c1c,stroke-width:2px

    class ACCOUNTS_VIEW,CLIENTS_VIEW,MOVEMENTS_VIEW,REPORTS_VIEW viewClass
    class ACC_FORM,ACC_LIST,ACC_SEARCH,CLI_FORM,CLI_LIST,CLI_SEARCH,MOV_FORM,MOV_LIST,MOV_SEARCH,REP_FORM,REP_LIST,REP_SEARCH componentClass
    class ACC_SERVICE,CLI_SERVICE,MOV_SERVICE,REP_SERVICE,ALERT_SERVICE,MENU_SERVICE serviceClass
    class BASE_COMP,BASE_FORM baseClass
    class BACKEND,MOCK_API externalClass
```

## 🔄 Flujo de Datos

```mermaid
sequenceDiagram
    participant U as 👤 Usuario
    participant V as 📱 Vista
    participant C as 🧩 Componente
    participant S as 🔧 Servicio
    participant H as 🌐 HttpClient
    participant A as 🚨 AlertService
    participant B as 🖥️ Backend

    U->>V: Interacción (click, input)
    V->>C: Evento del template
    C->>C: Validaciones locales

    alt Formulario válido
        C->>S: Llamada al método del servicio
        S->>H: HTTP Request
        H->>B: API Call
        B-->>H: Response Data
        H-->>S: Observable<T>
        S-->>C: Observable<T>
        C->>C: Actualizar estado local
        C->>A: Mostrar éxito
        A-->>U: Notificación
        C->>V: Actualizar UI
    else Formulario inválido
        C->>A: Mostrar errores
        A-->>U: Notificación de error
    end

    Note over C,S: Error Handling
    S->>S: catchError()
    S->>S: retry(2)
    S-->>C: Error Observable
    C->>A: Mostrar error
    A-->>U: Notificación de error
```

## 🏛️ Arquitectura por Capas

```mermaid
graph TD
    subgraph "🎨 Presentation Layer"
        VIEWS[Views]
        COMPONENTS[Components]
        TEMPLATES[Templates]
    end

    subgraph "💼 Business Logic Layer"
        SERVICES[Services]
        VALIDATORS[Validators]
        INTERCEPTORS[Interceptors]
    end

    subgraph "📊 Data Layer"
        HTTP[Http Client]
        TYPES[Types/Interfaces]
        CONSTANTS[Constants]
    end

    subgraph "🌍 External Layer"
        API[REST API]
        STORAGE[Local Storage]
        BROWSER[Browser APIs]
    end

    VIEWS --> COMPONENTS
    COMPONENTS --> SERVICES
    COMPONENTS --> VALIDATORS
    SERVICES --> HTTP
    SERVICES --> TYPES
    HTTP --> API
    SERVICES --> STORAGE
    COMPONENTS --> BROWSER

    classDef presentationClass fill:#e3f2fd,stroke:#1976d2,stroke-width:2px
    classDef businessClass fill:#e8f5e8,stroke:#388e3c,stroke-width:2px
    classDef dataClass fill:#fff3e0,stroke:#f57c00,stroke-width:2px
    classDef externalClass fill:#fce4ec,stroke:#c2185b,stroke-width:2px

    class VIEWS,COMPONENTS,TEMPLATES presentationClass
    class SERVICES,VALIDATORS,INTERCEPTORS businessClass
    class HTTP,TYPES,CONSTANTS dataClass
    class API,STORAGE,BROWSER externalClass
```

## 🎯 Patrón de Navegación

```mermaid
stateDiagram-v2
    [*] --> AppComponent

    AppComponent --> Header
    AppComponent --> Sidebar
    AppComponent --> RouterOutlet

    state RouterOutlet {
        [*] --> ClientsView
        ClientsView --> ClientForm : Crear/Editar
        ClientForm --> ClientsView : Guardar/Cancelar

        ClientsView --> AccountsView : Navegación
        AccountsView --> AccountForm : Crear/Editar
        AccountForm --> AccountsView : Guardar/Cancelar

        AccountsView --> MovementsView : Navegación
        MovementsView --> MovementForm : Crear
        MovementForm --> MovementsView : Guardar/Cancelar

        MovementsView --> ReportsView : Navegación
        ReportsView --> ReportsView : Generar Reporte
    }

    Sidebar --> RouterOutlet : Cambio de ruta
```

## 🔧 Gestión de Estado

```mermaid
graph LR
    subgraph "🧩 Component State"
        LOCAL[Estado Local]
        FORM[Form State]
        UI[UI State]
    end

    subgraph "🔄 Reactive State"
        OBS[Observables]
        SUB[Subscriptions]
        PIPE[Operators]
    end

    subgraph "💾 Persistence"
        MEMORY[Memory]
        SESSION[Session Storage]
        LOCAL_STORAGE[Local Storage]
    end

    LOCAL --> OBS
    FORM --> OBS
    UI --> OBS

    OBS --> PIPE
    PIPE --> SUB

    SUB --> MEMORY
    SUB --> SESSION
    SUB --> LOCAL_STORAGE

    %% RxJS Operators
    PIPE --> DEBOUNCE[debounceTime]
    PIPE --> DISTINCT[distinctUntilChanged]
    PIPE --> TAKEUNTIL[takeUntil]
    PIPE --> FINALIZE[finalize]
    PIPE --> CATCHERROR[catchError]
    PIPE --> RETRY[retry]
```

## 🎨 Estructura de Estilos

```mermaid
graph TD
    subgraph "🎨 Styles Architecture"
        GLOBAL[styles.scss]

        subgraph "📁 app/styles/"
            VARS[_variables.scss]
            MIXINS[_mixins.scss]
        end

        subgraph "🧩 Component Styles"
            COMP1[component1.scss]
            COMP2[component2.scss]
            COMP3[component3.scss]
        end

        subgraph "🎯 Methodology"
            BEM[BEM Naming]
            ATOMIC[Atomic CSS]
            UTILS[Utility Classes]
        end
    end

    GLOBAL --> VARS
    GLOBAL --> MIXINS
    GLOBAL --> UTILS

    VARS --> COMP1
    VARS --> COMP2
    VARS --> COMP3

    MIXINS --> COMP1
    MIXINS --> COMP2
    MIXINS --> COMP3

    BEM --> COMP1
    BEM --> COMP2
    BEM --> COMP3

    classDef styleClass fill:#f8bbd9,stroke:#ad1457,stroke-width:2px
    classDef methodClass fill:#c8e6c9,stroke:#2e7d32,stroke-width:2px

    class GLOBAL,VARS,MIXINS,COMP1,COMP2,COMP3 styleClass
    class BEM,ATOMIC,UTILS methodClass
```

## 📱 Responsive Breakpoints

```mermaid
graph LR
    MOBILE[📱 Mobile<br/>320px - 767px] --> TABLET[💻 Tablet<br/>768px - 1199px]
    TABLET --> DESKTOP[🖥️ Desktop<br/>1200px+]

    subgraph "🎯 Design Considerations"
        MOBILE_FEATURES[• Hamburger Menu<br/>• Stacked Layout<br/>• Touch Friendly]
        TABLET_FEATURES[• Sidebar Collapsed<br/>• Grid Responsive<br/>• Moderate Spacing]
        DESKTOP_FEATURES[• Full Sidebar<br/>• Multi-column<br/>• Maximum Features]
    end

    MOBILE --> MOBILE_FEATURES
    TABLET --> TABLET_FEATURES
    DESKTOP --> DESKTOP_FEATURES
```

## 🚀 Build & Deploy Pipeline

```mermaid
graph TD
    subgraph "💻 Development"
        DEV_CODE[Source Code]
        DEV_SERVER[ng serve]
        DEV_TEST[ng test]
    end

    subgraph "🔨 Build Process"
        COMPILE[TypeScript Compilation]
        BUNDLE[Webpack Bundling]
        OPTIMIZE[Tree Shaking & Minification]
        ASSETS[Asset Processing]
    end

    subgraph "🚀 Deployment"
        DIST[dist/ folder]
        STATIC[Static Server]
        CDN[CDN Distribution]
    end

    DEV_CODE --> COMPILE
    DEV_SERVER --> DEV_CODE
    DEV_TEST --> DEV_CODE

    COMPILE --> BUNDLE
    BUNDLE --> OPTIMIZE
    OPTIMIZE --> ASSETS
    ASSETS --> DIST

    DIST --> STATIC
    STATIC --> CDN

    classDef devClass fill:#e8f5e8,stroke:#2e7d32,stroke-width:2px
    classDef buildClass fill:#fff3e0,stroke:#f57c00,stroke-width:2px
    classDef deployClass fill:#e3f2fd,stroke:#1976d2,stroke-width:2px

    class DEV_CODE,DEV_SERVER,DEV_TEST devClass
    class COMPILE,BUNDLE,OPTIMIZE,ASSETS buildClass
    class DIST,STATIC,CDN deployClass
```

---

## 📝 Notas sobre los Diagramas

### Herramientas Recomendadas para Visualización:

1. **Mermaid**: Los diagramas están en formato Mermaid y se pueden visualizar en:
   - GitHub (automáticamente)
   - VS Code con extensión Mermaid
   - [Mermaid Live Editor](https://mermaid.live/)

2. **Draw.io**: Para diagramas más complejos
3. **PlantUML**: Para diagramas UML detallados
4. **Lucidchart**: Para diagramas profesionales

### Leyenda de Colores:

- 🔵 **Azul**: Componentes de presentación
- 🟢 **Verde**: Lógica de negocio/servicios
- 🟠 **Naranja**: Capa de datos
- 🔴 **Rojo**: APIs externas
- 🟣 **Púrpura**: Componentes reutilizables

Estos diagramas proporcionan una visión clara de la arquitectura y pueden ser utilizados en presentaciones técnicas o documentación para otros desarrolladores.
