# Proceso de verificación de Arquitectura de Carpetas y Nombrado de objetos en Angular.


## Verificación Estructura del Proyecto
+ Verifique que la estructura de carpetas y archivos dentro de la carpeta `src` siga las convenciones de Angular. A continuación se muestra un ejemplo de una estructura de proyecto típica:

```
helm/
│
src/
├── app/
│   ├── components/           # Componentes.
│   ├── config/               # Configuración.
│   ├── constants/            # Constantes.
│   ├── directives/           # Directivas.
│   ├── guard/                # Guardias de ruta   
│   ├── pipes/                # Pipes   
│   ├── services/             # Servicios 
│   ├── types/                # Tipos  
│   ├── utils/                # Utilidades
│   ├── views/                # Vistas  
│   ├── app.component.html
│   ├── app.component.scss
│   ├── app.component.ts
│   ├── app.module.ts         # Módulo raíz de la aplicación
│   └── app-routing.module.ts # Módulo de enrutamiento principal
├── assets/                   # Recursos estáticos (imágenes, fuentes, etc.)
│   ├── icons/
│   ├── images/
│   └── i18n/                 # Archivos de internacionalización
├── environments/             # Configuraciones de entorno (dev, prod)
│   ├── environment.prod.ts
│   └── environment.ts
├── styles/                   # Estilos globales SCSS
├── index.html                # Página HTML principal
├── main.ts                   # Punto de entrada principal de la aplicación
├── polyfills.ts              # Polyfills para compatibilidad de navegadores
└── test.ts                   # Configuración principal para pruebas unitarias
```

## Verificación Nombrado de Objetos.

### Idioma utilizado.
- Todos los artefactos y archivos relacionados con el FRAMEWORK ANGULAR como: componentes, módulos, servicios, etc., deben estar en inglés.

### Nombramiento de Clases.
+ Utilice nombres coherentes para todos los módulos y componentes que representen su funcionalidad.
+ Use la estrategia de nombrado UpperCamelCase para los nombres de las clases. Ejemplo `export class UserComponent`.
+ Añada al nombre de la clase en UpperCamelCase el sufijo convencional (por ejemplo: `Component`, `Service`, `Module`) según corresponda.

#### Ejemplo
```typescript
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent { // componente
  constructor() {}
}
@Directive({
  selector: '[appUser]'
})
export class UserDirective { // directiva
  constructor() {}
}
@Injectable()
export class UserService { // servicio
  constructor() {}
}

@NgModule({
  declarations: [UserComponent],
  imports: [],
  providers: [UserService]
})
export class UserModule { // módulo
  constructor() {}
}
```

### Nombramiento de Componentes.
+ Añada al nombre del componente el sufijo `Component`.
+ Esta clase sigue la convención de nombrado UpperCamelCase. Ejemplo `UserComponent`.
+ Modifica el nombre de la clase del Componente, para ajustarlo a Upper Camel Case.
#### Ejemplo.
```typescript
@Component({
  selector: 'app-user',
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent { // componente
  constructor() {}
}
export class MenuScreenComponent implements OnDestroy {

}
```

### Selectores de Componentes.
+ Utilice minúsculas para el nombre del selector de componentes y guiones para separar las palabras.
+ El nombre del selector debe ser el mismo que el nombre de la clase, pero en minúsculas y con guiones en lugar de mayúsculas.
+ El nombre del selector debe ser único en toda la aplicación y descriptivo.

#### Ejemplo
```typescript
@Component({
  selector: 'app-user', // nombre del selector
  templateUrl: './user.component.html',
  styleUrls: ['./user.component.css']
})
export class UserComponent { // componente
  constructor() {}
}
```

### Componente custom prefix.
+ Utilice un valor de selector de elemento con guión y en minúsculas; por ejemplo, admin-users.
+ Utilice un prefijo personalizado para un selector de componentes. Por ejemplo, el prefijo toh representa Tour of Heroes y el prefijo admin representa un área de características de administración.
+ Utilice un prefijo que identifique el área de características o la propia aplicación.

  + Evita las colisiones de nombres de elementos con componentes de otras aplicaciones y con elementos HTML nativos.
  + Facilita la promoción y el uso compartido del componente en otras aplicaciones.
  + Los componentes son fáciles de identificar en el DOM.

#### Ejemplos Correctos
Ruta de ubicación del ejemplo: app/heroes/hero.component.ts

```typescript
@Component({
  selector: 'toh-hero', // nombre del selector
  templateUrl: './heros.component.html',
  styleUrls: ['./heros.component.css']
})
export class HeroComponent { // componente
  constructor() {}
}
```

Ruta de ubicación del ejemplo: app/users/users.component.ts

```typescript
@Component({
  selector: 'admin-users', // nombre del selector
  templateUrl: './users.component.html',
  styleUrls: ['./users.component.css']
})
export class UsersComponent {}
```

### Nombramiento de Servicios.
+ Añada el sufijo `Service` al nombre de una clase de servicio.

#### Ejemplo
```typescript
@Injectable()
export class HeroDataService { }

@Injectable()
export class LoggerService { }
```


### Nombramiento de clase que definan directivas.

+ Añade al nombre de la clase de la directiva el sufijo `Directive`.
+ Utilice las minúsculas para nombrar los selectores de las directivas.
+ Utilice un prefijo personalizado para el selector de directivas.
+ Escribe los selectores que no son elementos en minúsculas, a menos que el selector esté destinado a coincidir con un atributo HTML nativo.

#### Ejemplo
app/shared/toh-validate.directive.ts

```typescript
@Directive({
  selector: '[tohValidate]'
})
export class ValidateDirective {}
```

> **Warning**
> No antepongas al nombre de una directiva el prefijo `ng` porque ese prefijo está reservado para Angular y su uso podría causar errores difíciles de diagnosticar.

#### ¿Por qué?

*   Previene colisión entre nombrados.
*   Las directivas se identificarán fácilmente.

### Nombramiento de clase que definan pipe.

+ Añade al nombre dee la clase el sufijo `Pipe`.
+ Utilice nombres consistentes para todas las Pipes, nombradas según su característica.
+ El nombre de la clase debe usar UpperCamelCase.
+ La cadena de nombre correspondiente debe usar lowerCamelCase.
+ La cadena de nombre **no puede** utilizar guiones ("dash-case" o "kebab-case").

### Ejemplos

```typescript
@Pipe({ name: 'ellipsis' })
export class EllipsisPipe implements PipeTransform{
}
@Pipe({ name: 'initCaps' })
export class InitCapsPipe implements PipeTransform { }
```

### Módulos.
+ Añada al nombre del módulo el sufijo `Module`.

#### Ejemplo
```typescript
@NgModule({ ... })
export class HeroesModule { }
```


### Nombramiento de clase que definan rutas en módulos.

Es un módulo dedicado exclusivamente en la configuración de rutas en Angular.

Añade al nombre de la clase el sufijo `RoutingModule`.

#### Ejemplo
```typescript
@NgModule({ ... })
export class AppRoutingModule { }

@NgModule({ ... })
export class HeroesRoutingModule { }
```

### Nombramiento de clase que definan guards.

El guard debe utilizar la siguiente convención de nombres:

*   Class Name: `NameFunctionGuard`

Cada parte está identificada como:

*   `Name` - este es el nombre de tu guard.
*   `Function` - esta es la función a la que se adjuntará su guard. Angular soporta `CanActivate`, `CanActivateChild`, `CanDeactivate`, y `Resolve`.
*   `Guard` sufijo que debe terminar la clase.

#### Ejemplo
```typescript
export class AuthCanActivateGuard { }
```

### Resolver.

### Nombramiento de clase que definan resolver.

El resolver debe utilizar la siguiente convención de nombres:

*   ClassName: `NameResolver`

### Ejemplo

```typescript
@Injectable()
export class PruebaResolver implements Resolve<Hero> { }
```

## Seguridad
Verifica los siguientes puntos del projecto:
+  **Input Validation**: Valida y sanitiza todos los user inputs.
+  **Authentication and Authorization**: Implementar mecanismos adecuados.
+  **Secure API Calls**: Ensure API calls are secure.

## Calidad de Código.

+ **Consistent Coding Conventions**: Adhere to coding standards (e.g., simple quotes for strings).
+ **Strict Typing**: Usa Tipos TypeScript correctamente; evita `any`. Al utilizar objetos tipo type en otros archivos, busca sus tipos en la dirección `src\app\type`.
+ **Linting**: Ensure code passes all linting checks.
+ Evita el uso de if else, en su lugar utiliza operador terciario.
- Evita la duplicidad de funcionalidad ya existentes.Busca siempre la reutilización de componentes cuando sea posible.
- Escribe pruebas comprensivas para toda funcionalidad nueva o modificada.
- **Nunca eliminar data o código inintencionadamente**; antes de ejecutar acciones destructitvas pide una confirmación explícita.
- Haz commits frecuentes para mantener un historial adecuado del proyecto.
- En caso de que las tareas no sean claras, siempre pide aclaraciones.
- Verifica si tiene un gestionar de errores, sino lo tiene propon uno con las mejores practicas de desarrollo.

### Inyección de Servicios y Dependencias
+ **Singleton Services**: Provee los servicios singleton en la raíz del proyecto.
+ **Dependency Injection**: Haz uso de la inyección de dependencias de manera adecuada, evitando el uso de la palabra clave `new`. Al momento de inyectar las dependencias hacerlo usando `inject()` en lugar de hacerlo en el constructor.

❌ Ejemplo Incorrecto.
```TypeScript
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class ApiService {
  constructor(private http:HttpClient){}

  getData() {
    return this.http.get('...');
  }
}
```

✅ Ejemplo Correcto.
```TypeScript
import { inject, Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";

@Injectable({
  providedIn: "root",
})
export class ApiService {
  private http: HttpClient = inject(HttpClient);

  getData() {
    return this.http.get('...');
  }
}
```


##  Optimización del Performance.

+ **Bundle Size**: Optimize bundle size; remove unnecessary dependencies.
+ **AOT Compilation**: Enable AOT for production builds.
+ **Memory Leaks**: Check for potential memory leaks.
+ **Estrategia Lazy Loading**: Verifica si es necesario usar estrategia Lazy Loading. Carga los módulos de manera diferida para reducir el tiempo de carga inicial. Solo se cargan los módulos necesarios cuando se requieren
  + `ng generate module nombreModulo --route ruta --module app.module`
+ **Estrategia de Detección**. Utiliza ChangeDetectionStrategy.OnPush para que Angular solo verifique el componente cuando sus inputs cambian, mejorando así el rendimiento. 
En caso de aplicar la estrategia "OnPush" dejar un comentario indicando que:
  - los `@Input` referenciados en el componente deben pasar referencias nuevas para que se detecte el cambio, por ejemplo si se recibe un array y se ocupa `myArray.push(newVal)` Angular no detectará el cambio, por lo que lo correcto sería `myArray = [...myArray, newVal]`, lo mismo aplica para los objetos.
  - Se puede usar el método `markForCheck()` de la clase `ChangeDetectorRef` para forzar una validación de cambios de ser necesario.
### Ejemplo:
```TypeScript
import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-mi-componente',
  templateUrl: './mi-componente.component.html',
  changeDetection: ChangeDetectionStrategy.OnPush
})
export class MiComponenteComponent {}

```

+ **Uso de TrackBy en NgFor**: Implementa trackBy en las listas iteradas con *ngFor para que Angular identifique elementos únicos y evite la creación innecesaria de elementos DOM.
```html
<div *ngFor="let item of items; trackBy: trackById">
  {{ item.nombre }}
</div>
```

+ **Evitar el uso innecesario de Pipes**: Aunque los pipes son útiles, su uso excesivo puede afectar el rendimiento. Usa pipes puros siempre que sea posible.

## Consumo de API e Interceptores.
Manejar un servicio `api.service.ts` para agrupar todos los consumos de servicios en un solo lugar.

En caso de que se repita configuraciones en el consumo de APIs, como por ejemplo agregar token, encryptar información, agregar cabeceras, etc. Hacer uso de interceptores para centralizar esta lógica y evitar código duplicado.

De ser posible centralizar también el manejo de errores, para lo cual se puede aprovaechar el interceptor.

## Interacción con Canales digitales (Banca web y Banca Movil)
Hacer uso de un servicio `channels.service.ts` que sirva como wrapper de la librería `@pichincha/channels-event-bus` ya que hay casuísticas especiales en donde se debe manejar la interacción de maneras diferentes, de esta manera se puede centralizar esta lógica para ser reutilizada en diferentes partes de la aplicación.

##  Manejo de estilos.
Hacer uso de la metodología BEM (Block Elemet Modifier) para lo cual, la posibilidad de manejar estilos anidados con SASS en los archivos `.scss` es una gran ayuda.
### Ejemplo
```scss
.footer{
  ...

  &__button{
    ...

    &--accept{
      color: green;
    }
    &--cancel{
      color: red;
    }
  }

  &__divider{
    ...
  }
}
```