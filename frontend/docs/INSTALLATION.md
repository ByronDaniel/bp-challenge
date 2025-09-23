# 🚀 Guía de Instalación y Despliegue - BP Challenge

## 📋 Requisitos Previos

### Software Requerido

| Software        | Versión Mínima | Versión Recomendada | Instalación                         |
| --------------- | -------------- | ------------------- | ----------------------------------- |
| **Node.js**     | 18.0.0         | 20.x.x              | [nodejs.org](https://nodejs.org/)   |
| **npm**         | 9.0.0          | 10.x.x              | Incluido con Node.js                |
| **Angular CLI** | 17.0.0         | 17.x.x              | `npm install -g @angular/cli`       |
| **Git**         | 2.30.0         | Latest              | [git-scm.com](https://git-scm.com/) |

### Verificación de Instalación

```bash
# Verificar versiones
node --version          # v20.x.x
npm --version           # 10.x.x
ng version             # Angular CLI: 17.x.x
git --version          # git version 2.x.x
```

## 🔽 Instalación del Proyecto

### 1. Clonar el Repositorio

```bash
# HTTPS
git clone https://github.com/tu-usuario/bp-challenge-frontend.git

# SSH
git clone git@github.com:tu-usuario/bp-challenge-frontend.git

# Cambiar al directorio del proyecto
cd bp-challenge-frontend
```

### 2. Instalar Dependencias

```bash
# Instalación estándar
npm install

# Instalación limpia (recomendado)
npm ci

# Instalación con cache limpio (si hay problemas)
npm cache clean --force && npm install
```

### 3. Configurar Variables de Entorno

```bash
# Copiar archivo de ejemplo
cp src/environments/environment.example.ts src/environments/environment.ts
```

**Editar `src/environments/environment.ts`:**

```typescript
export const environment = {
  production: false,
  apiUrl: "https://9e7043da-4f4b-431d-afdf-d300eafc5e90.mock.pstmn.io",
};
```

**Editar `src/environments/environment.prod.ts`:**

```typescript
export const environment = {
  production: true,
  apiUrl: "https://api.tu-dominio.com",
};
```

### 4. Ejecutar en Desarrollo

```bash
# Servidor de desarrollo
npm start

# O usando Angular CLI directamente
ng serve

# Con puerto específico
ng serve --port 4300

# Con host específico
ng serve --host 0.0.0.0 --port 4200
```

**Acceder a la aplicación:**

- URL: http://localhost:4200
- La aplicación se recargará automáticamente al hacer cambios

## 🏗️ Comandos de Build

### Build de Desarrollo

```bash
npm run build:dev
# O
ng build --configuration development
```

### Build de Producción

```bash
npm run build
# O
ng build --configuration production
```

### Build con Optimizaciones Adicionales

```bash
# Build con análisis de bundle
ng build --stats-json
npx webpack-bundle-analyzer dist/bp-challenge-frontend/stats.json

# Build con source maps
ng build --source-map

# Build con AOT compilation
ng build --aot
```

## 🧪 Testing

### Tests Unitarios

```bash
# Ejecutar tests una vez
npm test

# Tests en modo watch
ng test

# Tests con coverage
ng test --code-coverage

# Tests headless (CI/CD)
ng test --watch=false --browsers=ChromeHeadless
```

### Tests E2E

```bash
# Instalar Cypress (si no está instalado)
npm install cypress --save-dev

# Ejecutar tests E2E
npm run e2e

# Abrir interfaz de Cypress
npx cypress open
```

### Linting

```bash
# Ejecutar ESLint
npm run lint

# Auto-fix problemas de linting
npm run lint:fix

# Formatear código con Prettier
npm run format
```

## 🐳 Docker

### Dockerfile para Desarrollo

```dockerfile
# Dockerfile.dev
FROM node:20-alpine

WORKDIR /app

COPY package*.json ./
RUN npm ci

COPY . .

EXPOSE 4200

CMD ["npm", "start"]
```

### Dockerfile para Producción

```dockerfile
# Multi-stage build
FROM node:20-alpine AS builder

WORKDIR /app
COPY package*.json ./
RUN npm ci

COPY . .
RUN npm run build

# Production stage
FROM nginx:alpine

COPY --from=builder /app/dist/bp-challenge-frontend /usr/share/nginx/html
COPY nginx.conf /etc/nginx/nginx.conf

EXPOSE 80

CMD ["nginx", "-g", "daemon off;"]
```

### Docker Compose

```yaml
# docker-compose.yml
version: "3.8"

services:
  frontend:
    build:
      context: .
      dockerfile: Dockerfile
    ports:
      - "4200:80"
    environment:
      - NODE_ENV=production
    restart: unless-stopped
```

### Comandos Docker

```bash
# Build imagen
docker build -t bp-challenge-frontend .

# Ejecutar contenedor
docker run -p 4200:80 bp-challenge-frontend

# Con Docker Compose
docker-compose up -d
```

## 🌐 Despliegue en Producción

### 1. Nginx Configuration

```nginx
# nginx.conf
events {
    worker_connections 1024;
}

http {
    include       /etc/nginx/mime.types;
    default_type  application/octet-stream;

    # Compression
    gzip on;
    gzip_types
        text/plain
        text/css
        text/js
        text/xml
        text/javascript
        application/javascript
        application/xml+rss
        application/json;

    server {
        listen 80;
        server_name yourdomain.com;
        root /usr/share/nginx/html;
        index index.html;

        # Handle Angular routing
        location / {
            try_files $uri $uri/ /index.html;
        }

        # Cache static assets
        location ~* \.(js|css|png|jpg|jpeg|gif|ico|svg)$ {
            expires 1y;
            add_header Cache-Control "public, immutable";
        }

        # Security headers
        add_header X-Frame-Options "SAMEORIGIN" always;
        add_header X-Content-Type-Options "nosniff" always;
        add_header X-XSS-Protection "1; mode=block" always;
    }
}
```

### 2. Apache Configuration

```apache
# .htaccess
<IfModule mod_rewrite.c>
    RewriteEngine On

    # Handle Angular routing
    RewriteBase /
    RewriteRule ^index\.html$ - [L]
    RewriteCond %{REQUEST_FILENAME} !-f
    RewriteCond %{REQUEST_FILENAME} !-d
    RewriteRule . /index.html [L]
</IfModule>

# Compression
<IfModule mod_deflate.c>
    AddOutputFilterByType DEFLATE text/plain
    AddOutputFilterByType DEFLATE text/html
    AddOutputFilterByType DEFLATE text/xml
    AddOutputFilterByType DEFLATE text/css
    AddOutputFilterByType DEFLATE application/xml
    AddOutputFilterByType DEFLATE application/xhtml+xml
    AddOutputFilterByType DEFLATE application/rss+xml
    AddOutputFilterByType DEFLATE application/javascript
    AddOutputFilterByType DEFLATE application/x-javascript
</IfModule>

# Cache control
<IfModule mod_expires.c>
    ExpiresActive on
    ExpiresByType text/css "access plus 1 year"
    ExpiresByType application/javascript "access plus 1 year"
    ExpiresByType image/png "access plus 1 year"
    ExpiresByType image/jpg "access plus 1 year"
    ExpiresByType image/jpeg "access plus 1 year"
    ExpiresByType image/gif "access plus 1 year"
    ExpiresByType image/ico "access plus 1 year"
</IfModule>
```

## ☁️ Despliegue en Cloud

### Netlify

```bash
# Instalar Netlify CLI
npm install -g netlify-cli

# Login
netlify login

# Deploy
netlify deploy --prod --dir=dist/bp-challenge-frontend
```

**netlify.toml:**

```toml
[build]
  publish = "dist/bp-challenge-frontend"
  command = "npm run build"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

### Vercel

```bash
# Instalar Vercel CLI
npm install -g vercel

# Deploy
vercel --prod
```

**vercel.json:**

```json
{
  "version": 2,
  "builds": [
    {
      "src": "package.json",
      "use": "@vercel/static-build"
    }
  ],
  "routes": [
    {
      "src": "/(.*)",
      "dest": "/index.html"
    }
  ]
}
```

### AWS S3 + CloudFront

```bash
# Instalar AWS CLI
pip install awscli

# Configurar credenciales
aws configure

# Sync a S3
aws s3 sync dist/bp-challenge-frontend/ s3://your-bucket-name --delete

# Invalidar cache de CloudFront
aws cloudfront create-invalidation --distribution-id YOUR_DISTRIBUTION_ID --paths "/*"
```

### Firebase Hosting

```bash
# Instalar Firebase CLI
npm install -g firebase-tools

# Login
firebase login

# Inicializar proyecto
firebase init hosting

# Deploy
firebase deploy
```

**firebase.json:**

```json
{
  "hosting": {
    "public": "dist/bp-challenge-frontend",
    "ignore": ["firebase.json", "**/.*", "**/node_modules/**"],
    "rewrites": [
      {
        "source": "**",
        "destination": "/index.html"
      }
    ]
  }
}
```

## 🔄 CI/CD con GitHub Actions

### Workflow de Deploy

```yaml
# .github/workflows/deploy.yml
name: Deploy to Production

on:
  push:
    branches: [main]

jobs:
  build-and-deploy:
    runs-on: ubuntu-latest

    steps:
      - name: Checkout
        uses: actions/checkout@v4

      - name: Setup Node.js
        uses: actions/setup-node@v4
        with:
          node-version: "20"
          cache: "npm"

      - name: Install dependencies
        run: npm ci

      - name: Run tests
        run: npm test -- --watch=false --browsers=ChromeHeadless

      - name: Build application
        run: npm run build

      - name: Deploy to S3
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        run: |
          aws s3 sync dist/bp-challenge-frontend/ s3://your-bucket-name --delete

      - name: Invalidate CloudFront
        env:
          AWS_ACCESS_KEY_ID: ${{ secrets.AWS_ACCESS_KEY_ID }}
          AWS_SECRET_ACCESS_KEY: ${{ secrets.AWS_SECRET_ACCESS_KEY }}
        run: |
          aws cloudfront create-invalidation --distribution-id ${{ secrets.CLOUDFRONT_DISTRIBUTION_ID }} --paths "/*"
```

## 🔧 Optimizaciones de Performance

### 1. Angular Build Optimizations

```json
// angular.json
{
  "configurations": {
    "production": {
      "budgets": [
        {
          "type": "initial",
          "maximumWarning": "500kb",
          "maximumError": "1mb"
        }
      ],
      "optimization": true,
      "outputHashing": "all",
      "sourceMap": false,
      "namedChunks": false,
      "extractLicenses": true,
      "vendorChunk": false,
      "buildOptimizer": true
    }
  }
}
```

### 2. Service Worker (PWA)

```bash
# Agregar Service Worker
ng add @angular/pwa
```

### 3. Lazy Loading

```typescript
// app.routes.ts
export const routes: Routes = [
  {
    path: "clientes",
    loadComponent: () => import("./views/clients-view/clients-view.component").then((c) => c.ClientsViewComponent),
  },
];
```

## 🔍 Monitoreo y Analytics

### 1. Bundle Analyzer

```bash
npm install --save-dev webpack-bundle-analyzer
ng build --stats-json
npx webpack-bundle-analyzer dist/bp-challenge-frontend/stats.json
```

### 2. Lighthouse CI

```yaml
# .github/workflows/lighthouse.yml
- name: Lighthouse CI
  uses: treosh/lighthouse-ci-action@v10
  with:
    configPath: "./lighthouserc.json"
    uploadArtifacts: true
```

### 3. Error Tracking

```typescript
// Sentry integration
import * as Sentry from "@sentry/angular";

Sentry.init({
  dsn: "YOUR_DSN_HERE",
  environment: environment.production ? "production" : "development",
});
```

## 🚨 Troubleshooting

### Problemas Comunes

1. **Error de memoria en build:**

   ```bash
   NODE_OPTIONS="--max-old-space-size=8192" npm run build
   ```

2. **Problemas de caché:**

   ```bash
   npm cache clean --force
   rm -rf node_modules package-lock.json
   npm install
   ```

3. **Problemas de CORS en desarrollo:**

   ```json
   // proxy.conf.json
   {
     "/api/*": {
       "target": "http://localhost:3000",
       "secure": true,
       "changeOrigin": true
     }
   }
   ```

4. **Angular CLI no actualizada:**
   ```bash
   npm uninstall -g @angular/cli
   npm install -g @angular/cli@latest
   ```

---

**Con esta guía tienes todo lo necesario para instalar, desarrollar y desplegar el proyecto BP Challenge de manera profesional.**
