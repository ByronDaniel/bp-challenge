## BP Challenge

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