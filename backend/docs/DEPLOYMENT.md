# 🚀 Deployment Guide

## 📋 Tabla de Contenidos

- [🎯 Visión General](#-visión-general)
- [📋 Prerrequisitos](#-prerrequisitos)
- [🐳 Despliegue Local con Docker](#-despliegue-local-con-docker)
- [☸️ Despliegue en Kubernetes](#️-despliegue-en-kubernetes)
- [☁️ Despliegue en la Nube](#️-despliegue-en-la-nube)
- [🔧 Configuración](#-configuración)
- [📊 Monitoreo](#-monitoreo)
- [🛠️ Troubleshooting](#️-troubleshooting)

## 🎯 **Visión General**

Esta guía describe cómo desplegar el sistema de microservicios bancarios en diferentes entornos:

- **🐳 Local Development**: Docker Compose para desarrollo local
- **☸️ Staging/Production**: Kubernetes para entornos productivos
- **☁️ Cloud Native**: AWS EKS, Azure AKS, Google GKE

## 📋 **Prerrequisitos**

### **Software Requerido**

| Tool               | Version | Purpose                       |
| ------------------ | ------- | ----------------------------- |
| **Docker**         | 20.10+  | Conteneurización              |
| **Docker Compose** | 2.0+    | Orquestación local            |
| **Kubernetes**     | 1.24+   | Orquestación productiva       |
| **kubectl**        | Latest  | Cliente K8s                   |
| **Helm**           | 3.8+    | Package manager K8s           |
| **Java**           | 21+     | Runtime (desarrollo local)    |
| **Gradle**         | 8.0+    | Build tool (desarrollo local) |

### **Hardware Mínimo**

#### **Desarrollo Local**

- **CPU**: 4 cores
- **RAM**: 8 GB
- **Disk**: 20 GB free space
- **Network**: Internet connection

#### **Producción**

- **CPU**: 16+ cores
- **RAM**: 32+ GB
- **Disk**: 100+ GB SSD
- **Network**: High-speed, low-latency

## 🐳 **Despliegue Local con Docker**

### **1. Preparación del Entorno**

```bash
# Clonar el repositorio
git clone <repository-url>
cd bp-challenge/backend

# Verificar Docker
docker --version
docker-compose --version

# Verificar puertos disponibles
netstat -an | grep -E ":1433|:8080|:8081|:8082|:8083"
```

### **2. Configuración de Variables de Entorno**

Crear archivo `.env`:

```bash
# .env
# Database Configuration
DB_SERVER=sqlserver
DB_PORT=1433
DB_NAME=challenge
DB_USER=sa
DB_PASSWORD=Holamundo11*

# Application Configuration
SPRING_PROFILES_ACTIVE=docker
LOG_LEVEL=INFO

# Resource Limits
JVM_MEMORY_OPTS=-Xms512m -Xmx1024m

# Monitoring
ENABLE_METRICS=true
ENABLE_HEALTH_CHECKS=true
```

### **3. Construcción y Despliegue**

```bash
# Construir todas las imágenes
docker-compose build

# Verificar imágenes construidas
docker images | grep "bp-challenge"

# Levantar servicios en background
docker-compose up -d

# Verificar estado de contenedores
docker-compose ps

# Ver logs en tiempo real
docker-compose logs -f
```

### **4. Verificación del Despliegue**

```bash
# Health checks
echo "🔍 Verificando servicios..."

# Base de datos
docker-compose exec sqlserver /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P "Holamundo11*" \
  -Q "SELECT name FROM sys.databases WHERE name='challenge'"

# Microservicios
curl -f http://localhost:8080/actuator/health || echo "❌ Client Service DOWN"
curl -f http://localhost:8081/actuator/health || echo "❌ Account Service DOWN"
curl -f http://localhost:8082/actuator/health || echo "❌ Movement Service DOWN"
curl -f http://localhost:8083/actuator/health || echo "❌ Report Service DOWN"

echo "✅ Verificación completada"
```

### **5. Docker Compose Optimizado**

```yaml
# docker-compose.prod.yml
version: "3.9"

services:
  sqlserver:
    image: mcr.microsoft.com/mssql/server:2019-latest
    container_name: sqlserver
    environment:
      SA_PASSWORD: "${DB_PASSWORD}"
      ACCEPT_EULA: "Y"
      MSSQL_PID: "Express"
    ports:
      - "${DB_PORT}:1433"
    volumes:
      - sqlserver_data:/var/opt/mssql
      - ./db-init:/db-init
    networks:
      - banking-network
    healthcheck:
      test:
        [
          "CMD-SHELL",
          "/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $$SA_PASSWORD -Q 'SELECT 1'",
        ]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s

  redis:
    image: redis:7-alpine
    container_name: redis
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    networks:
      - banking-network
    healthcheck:
      test: ["CMD", "redis-cli", "ping"]
      interval: 30s
      timeout: 10s
      retries: 3

  msa-client:
    build:
      context: ./msa-client
      dockerfile: Dockerfile
    container_name: msa-client
    environment:
      SPRING_PROFILES_ACTIVE: docker
      SPRING_R2DBC_URL: r2dbc:pool:mssql://${DB_SERVER}:${DB_PORT}/${DB_NAME}
      SPRING_R2DBC_USERNAME: ${DB_USER}
      SPRING_R2DBC_PASSWORD: ${DB_PASSWORD}
      JAVA_OPTS: "${JVM_MEMORY_OPTS}"
    ports:
      - "8080:8080"
    depends_on:
      sqlserver:
        condition: service_healthy
      redis:
        condition: service_healthy
    networks:
      - banking-network
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 30s
      timeout: 10s
      retries: 3
      start_period: 60s
    deploy:
      resources:
        limits:
          cpus: "1.0"
          memory: 1G
        reservations:
          cpus: "0.5"
          memory: 512M

volumes:
  sqlserver_data:
  redis_data:

networks:
  banking-network:
    driver: bridge
```

## ☸️ **Despliegue en Kubernetes**

### **1. Preparación del Cluster**

```bash
# Verificar conexión al cluster
kubectl cluster-info

# Crear namespace
kubectl create namespace banking-system

# Configurar como namespace por defecto
kubectl config set-context --current --namespace=banking-system
```

### **2. Secrets y ConfigMaps**

```bash
# Crear secrets para la base de datos
kubectl create secret generic db-secret \
  --from-literal=username=sa \
  --from-literal=password=Holamundo11*

# Crear ConfigMap para configuración común
kubectl apply -f - <<EOF
apiVersion: v1
kind: ConfigMap
metadata:
  name: app-config
  namespace: banking-system
data:
  spring.profiles.active: "kubernetes"
  logging.level.org.springframework.data.r2dbc: "INFO"
  management.endpoints.web.exposure.include: "health,metrics,prometheus"
EOF
```

### **3. Base de Datos (StatefulSet)**

```yaml
# k8s/sqlserver-statefulset.yaml
apiVersion: apps/v1
kind: StatefulSet
metadata:
  name: sqlserver
  namespace: banking-system
spec:
  serviceName: sqlserver-service
  replicas: 1
  selector:
    matchLabels:
      app: sqlserver
  template:
    metadata:
      labels:
        app: sqlserver
    spec:
      containers:
        - name: sqlserver
          image: mcr.microsoft.com/mssql/server:2019-latest
          ports:
            - containerPort: 1433
          env:
            - name: SA_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: password
            - name: ACCEPT_EULA
              value: "Y"
            - name: MSSQL_PID
              value: "Express"
          volumeMounts:
            - name: sqlserver-storage
              mountPath: /var/opt/mssql
          resources:
            requests:
              memory: "2Gi"
              cpu: "1000m"
            limits:
              memory: "4Gi"
              cpu: "2000m"
          livenessProbe:
            exec:
              command:
                - /opt/mssql-tools/bin/sqlcmd
                - -S
                - localhost
                - -U
                - sa
                - -P
                - $(SA_PASSWORD)
                - -Q
                - SELECT 1
            initialDelaySeconds: 60
            periodSeconds: 30
  volumeClaimTemplates:
    - metadata:
        name: sqlserver-storage
      spec:
        accessModes: ["ReadWriteOnce"]
        resources:
          requests:
            storage: 20Gi
        storageClassName: fast-ssd
---
apiVersion: v1
kind: Service
metadata:
  name: sqlserver-service
  namespace: banking-system
spec:
  selector:
    app: sqlserver
  ports:
    - port: 1433
      targetPort: 1433
  type: ClusterIP
```

### **4. Microservicios (Deployments)**

```yaml
# k8s/client-service.yaml
apiVersion: apps/v1
kind: Deployment
metadata:
  name: client-service
  namespace: banking-system
  labels:
    app: client-service
    version: v1
spec:
  replicas: 3
  selector:
    matchLabels:
      app: client-service
  template:
    metadata:
      labels:
        app: client-service
        version: v1
    spec:
      containers:
        - name: client-service
          image: bp-challenge/msa-client:latest
          ports:
            - containerPort: 8080
          env:
            - name: SPRING_PROFILES_ACTIVE
              valueFrom:
                configMapKeyRef:
                  name: app-config
                  key: spring.profiles.active
            - name: SPRING_R2DBC_URL
              value: "r2dbc:pool:mssql://sqlserver-service:1433/challenge"
            - name: SPRING_R2DBC_USERNAME
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: username
            - name: SPRING_R2DBC_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-secret
                  key: password
            - name: JAVA_OPTS
              value: "-Xms512m -Xmx1024m"
          resources:
            requests:
              memory: "512Mi"
              cpu: "500m"
            limits:
              memory: "1Gi"
              cpu: "1000m"
          livenessProbe:
            httpGet:
              path: /actuator/health/liveness
              port: 8080
            initialDelaySeconds: 60
            periodSeconds: 30
          readinessProbe:
            httpGet:
              path: /actuator/health/readiness
              port: 8080
            initialDelaySeconds: 30
            periodSeconds: 10
---
apiVersion: v1
kind: Service
metadata:
  name: client-service
  namespace: banking-system
spec:
  selector:
    app: client-service
  ports:
    - port: 80
      targetPort: 8080
  type: ClusterIP
```

### **5. Ingress Controller**

```yaml
# k8s/ingress.yaml
apiVersion: networking.k8s.io/v1
kind: Ingress
metadata:
  name: banking-ingress
  namespace: banking-system
  annotations:
    kubernetes.io/ingress.class: nginx
    nginx.ingress.kubernetes.io/ssl-redirect: "true"
    nginx.ingress.kubernetes.io/use-regex: "true"
    nginx.ingress.kubernetes.io/rewrite-target: /$2
    cert-manager.io/cluster-issuer: letsencrypt-prod
spec:
  tls:
    - hosts:
        - api.banking.com
      secretName: banking-tls
  rules:
    - host: api.banking.com
      http:
        paths:
          - path: /api/v1/clientes(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: client-service
                port:
                  number: 80
          - path: /api/v1/cuentas(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: account-service
                port:
                  number: 80
          - path: /api/v1/movimientos(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: movement-service
                port:
                  number: 80
          - path: /api/v1/reportes(/|$)(.*)
            pathType: Prefix
            backend:
              service:
                name: report-service
                port:
                  number: 80
```

### **6. Despliegue con Helm**

```bash
# Crear Helm chart
helm create banking-microservices

# Estructura del chart
banking-microservices/
├── Chart.yaml
├── values.yaml
├── templates/
│   ├── deployment.yaml
│   ├── service.yaml
│   ├── ingress.yaml
│   ├── configmap.yaml
│   └── secret.yaml
```

```yaml
# values.yaml
global:
  namespace: banking-system
  imageRegistry: "your-registry.com"
  imageTag: "latest"

database:
  enabled: true
  image: mcr.microsoft.com/mssql/server:2019-latest
  storage: 20Gi
  password: "Holamundo11*"

services:
  clientService:
    enabled: true
    replicas: 3
    image: msa-client
    port: 8080
    resources:
      requests:
        memory: "512Mi"
        cpu: "500m"
      limits:
        memory: "1Gi"
        cpu: "1000m"

  accountService:
    enabled: true
    replicas: 3
    image: msa-account
    port: 8080

  movementService:
    enabled: true
    replicas: 5
    image: msa-movement
    port: 8080

  reportService:
    enabled: true
    replicas: 2
    image: msa-report
    port: 8080

ingress:
  enabled: true
  className: nginx
  host: api.banking.com
  tls:
    enabled: true
    secretName: banking-tls

monitoring:
  prometheus:
    enabled: true
  grafana:
    enabled: true
```

```bash
# Desplegar con Helm
helm install banking-system ./banking-microservices \
  --namespace banking-system \
  --create-namespace \
  --values values.yaml

# Verificar despliegue
helm status banking-system
kubectl get pods -n banking-system
```

## ☁️ **Despliegue en la Nube**

### **AWS EKS**

```bash
# Crear cluster EKS
eksctl create cluster \
  --name banking-cluster \
  --version 1.24 \
  --region us-west-2 \
  --nodegroup-name banking-nodes \
  --node-type m5.xlarge \
  --nodes 3 \
  --nodes-min 1 \
  --nodes-max 6 \
  --managed

# Configurar kubectl
aws eks update-kubeconfig --region us-west-2 --name banking-cluster

# Instalar ALB Ingress Controller
kubectl apply -k "github.com/aws/eks-charts/stable/aws-load-balancer-controller/crds?ref=master"

helm repo add eks https://aws.github.io/eks-charts
helm install aws-load-balancer-controller eks/aws-load-balancer-controller \
  -n kube-system \
  --set clusterName=banking-cluster \
  --set serviceAccount.create=false \
  --set serviceAccount.name=aws-load-balancer-controller
```

### **Azure AKS**

```bash
# Crear resource group
az group create --name banking-rg --location eastus

# Crear cluster AKS
az aks create \
  --resource-group banking-rg \
  --name banking-cluster \
  --node-count 3 \
  --node-vm-size Standard_D4s_v3 \
  --enable-addons monitoring \
  --generate-ssh-keys

# Obtener credenciales
az aks get-credentials --resource-group banking-rg --name banking-cluster

# Habilitar NGINX Ingress
kubectl apply -f https://raw.githubusercontent.com/kubernetes/ingress-nginx/controller-v1.5.1/deploy/static/provider/cloud/deploy.yaml
```

## 🔧 **Configuración**

### **Profiles de Spring Boot**

```yaml
# application-docker.yml
spring:
  config:
    activate:
      on-profile: docker
  r2dbc:
    url: r2dbc:pool:mssql://sqlserver:1433/challenge
  data:
    redis:
      host: redis
      port: 6379

management:
  endpoints:
    web:
      exposure:
        include: health,metrics,prometheus
  endpoint:
    health:
      show-details: always

logging:
  level:
    org.springframework.data.r2dbc: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
```

```yaml
# application-kubernetes.yml
spring:
  config:
    activate:
      on-profile: kubernetes
  r2dbc:
    url: r2dbc:pool:mssql://sqlserver-service:1433/challenge

management:
  server:
    port: 8081
  endpoints:
    web:
      base-path: /actuator
      exposure:
        include: "*"

logging:
  level:
    org.springframework.data.r2dbc: INFO
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%X{traceId:-},%X{spanId:-}] [%thread] %-5level %logger{36} - %msg%n"
```

## 📊 **Monitoreo**

### **Prometheus Configuration**

```yaml
# prometheus.yml
global:
  scrape_interval: 15s
  evaluation_interval: 15s

rule_files:
  - "banking_rules.yml"

scrape_configs:
  - job_name: "banking-microservices"
    kubernetes_sd_configs:
      - role: pod
        namespaces:
          names:
            - banking-system
    relabel_configs:
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_scrape]
        action: keep
        regex: true
      - source_labels: [__meta_kubernetes_pod_annotation_prometheus_io_path]
        action: replace
        target_label: __metrics_path__
        regex: (.+)
      - source_labels:
          [__address__, __meta_kubernetes_pod_annotation_prometheus_io_port]
        action: replace
        regex: ([^:]+)(?::\d+)?;(\d+)
        replacement: $1:$2
        target_label: __address__

alerting:
  alertmanagers:
    - static_configs:
        - targets:
            - alertmanager:9093
```

### **Grafana Dashboards**

```json
{
  "dashboard": {
    "id": null,
    "title": "Banking Microservices Overview",
    "panels": [
      {
        "title": "Service Health",
        "type": "stat",
        "targets": [
          {
            "expr": "up{job=\"banking-microservices\"}",
            "legendFormat": "{{instance}}"
          }
        ]
      },
      {
        "title": "Request Rate",
        "type": "graph",
        "targets": [
          {
            "expr": "rate(http_requests_total[5m])",
            "legendFormat": "{{service}}"
          }
        ]
      },
      {
        "title": "Response Time",
        "type": "graph",
        "targets": [
          {
            "expr": "histogram_quantile(0.95, rate(http_request_duration_seconds_bucket[5m]))",
            "legendFormat": "95th percentile"
          }
        ]
      }
    ]
  }
}
```

## 🛠️ **Troubleshooting**

### **Problemas Comunes**

#### **1. Servicios no inician**

```bash
# Verificar logs
docker-compose logs msa-client
kubectl logs -f deployment/client-service -n banking-system

# Verificar conectividad a BD
docker-compose exec msa-client nc -zv sqlserver 1433
kubectl exec -it deployment/client-service -- nc -zv sqlserver-service 1433

# Verificar variables de entorno
docker-compose exec msa-client env | grep SPRING
kubectl exec -it deployment/client-service -- env | grep SPRING
```

#### **2. Base de datos no disponible**

```bash
# Verificar estado de SQL Server
docker-compose exec sqlserver /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P "Holamundo11*" \
  -Q "SELECT @@VERSION"

# Verificar que la BD existe
docker-compose exec sqlserver /opt/mssql-tools/bin/sqlcmd \
  -S localhost -U sa -P "Holamundo11*" \
  -Q "SELECT name FROM sys.databases"

# Recrear BD si es necesario
docker-compose down -v
docker-compose up -d sqlserver db_init
```

#### **3. Problemas de memoria**

```bash
# Verificar uso de memoria
docker stats
kubectl top pods -n banking-system

# Ajustar límites de memoria
export JVM_MEMORY_OPTS="-Xms256m -Xmx512m"
docker-compose up -d

# En Kubernetes, editar deployment
kubectl edit deployment client-service -n banking-system
```

### **Health Checks**

```bash
#!/bin/bash
# health-check.sh

echo "🏥 Banking System Health Check"
echo "================================"

services=(
  "http://localhost:8080/actuator/health:Client Service"
  "http://localhost:8081/actuator/health:Account Service"
  "http://localhost:8082/actuator/health:Movement Service"
  "http://localhost:8083/actuator/health:Report Service"
)

for service in "${services[@]}"; do
  url=$(echo $service | cut -d: -f1,2,3)
  name=$(echo $service | cut -d: -f4)

  if curl -f -s "$url" > /dev/null; then
    echo "✅ $name: UP"
  else
    echo "❌ $name: DOWN"
  fi
done

echo "================================"
echo "🔍 Detailed Status:"
curl -s http://localhost:8080/actuator/health | jq '.'
```

### **Performance Tuning**

```yaml
# docker-compose.override.yml para development
version: "3.9"

services:
  msa-client:
    environment:
      - JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC
      - SPRING_PROFILES_ACTIVE=docker,debug
    deploy:
      resources:
        limits:
          cpus: "0.5"
          memory: 512M

  msa-account:
    environment:
      - JAVA_OPTS=-Xms256m -Xmx512m -XX:+UseG1GC

  msa-movement:
    environment:
      - JAVA_OPTS=-Xms512m -Xmx1024m -XX:+UseG1GC
    deploy:
      resources:
        limits:
          cpus: "1.0"
          memory: 1G
```

---

> 📋 **Siguiente**: [Development Guide](./DEVELOPMENT.md) | [Monitoring Guide](./MONITORING.md)
