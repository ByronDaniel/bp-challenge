## BP Challenge – Backend

### Tecnologías

- Java 21
- Spring WebFlux (reactivo)
- Spring Data R2DBC
- Spring Validation
- OpenAPI Generator
- MapStruct
- Lombok
- Docker & Docker Compose
- Microsoft SQL Server 2019

### Levantar todo (desde `backend/`)

```bash
docker compose up --build -d
```

### Detener todo

```bash
docker compose down
```

### Limpiar contenedores puntuales (forzar eliminación)

```bash
docker rm -f msa-client msa-account msa-movement msa-report sqlserver db_init
```

### Colección Postman

`backend/BP Challenge.postman_collection.json` (importar y probar).

---

## BP Challenge – Frontend
