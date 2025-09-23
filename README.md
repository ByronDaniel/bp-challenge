# BP Challenge

Microservicios (`msa-client`, `msa-account`, `msa-movement`, `msa-report`) 
Base de datos SQL Server (`challenge`).  
Se utiliza **Docker y docker-compose** para levantar todos los servicios y ejecutar automáticamente el script de creación de la base de datos.

---

## 🛠 Requisitos

- Docker >= 20.x  
- docker-compose >= 1.29.x  
- Postman

---

## Levantar el proyecto

1. Descargar la imagen de SQL Server:

docker pull mcr.microsoft.com/mssql/server:2019-latest

Limpiar contenedores antiguos (opcional, para evitar conflictos):
docker rm -f msa-client msa-account msa-movement msa-report sqlserver db_init 2>/dev/null || true


Levantar todos los servicios:
docker-compose up --build -d --force-recreate

Esto hará que:

SQL Server se levante en sqlserver:1433.

Se ejecute automáticamente el script db-init/basedatos.sql.

Se levanten los 4 microservicios conectándose a la DB.

Microservicios y puertos
Servicio	URL local	Puerto
msa-client	http://localhost	8080
msa-account	http://localhost	8081
msa-movement	http://localhost	8082
msa-report	http://localhost	8083

Colección de Postman
Se incluye la colección para probar los endpoints:
BP Challenge.postman_collection.json

Puedes importarla directamente en Postman para testear los servicios.

Incluye todos los endpoints de los microservicios, con ejemplos de request y response.

🔍 Notas
El script db-init/basedatos.sql crea la base de datos challenge y todas las tablas necesarias para los microservicios.

Para detener todos los contenedores:
docker-compose down

Para ver logs de un servicio específico:
docker-compose logs -f msa-client

Testing rápido
Levantar todo con docker-compose up -d.

Importar la colección de Postman y probar endpoints.

Validar que la DB challenge existe y tiene las tablas (persons, clients, accounts, movements).

Probar flujos de creación de clientes, cuentas y movimientos.