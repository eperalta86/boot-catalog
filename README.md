# Boot-Catalog (Backend)

API REST para Backlog, un catalogador personal de videojuegos. Permite registrar juegos, asociarlos a plataformas, gestionar imágenes y hacer seguimiento del estado (pendiente, en progreso, completado).

## Tech Stack

- Spring Boot 3.5.7
- Java 25 (Temurin via SDKMAN)
- PostgreSQL 16
- Flyway (migraciones)
- Spring Security + JWT (jjwt 0.12.6)
- Bucket4j (rate limiting)

## Requisitos

- Java 25 (via SDKMAN - se activa automáticamente con `.sdkmanrc`)
- PostgreSQL 16+ corriendo en `localhost:5432`

## Levantar

```bash
./mvnw spring-boot:run
```

Corre en `http://localhost:8080` con perfil `dev` por defecto.

## Endpoints principales

| Método | Ruta | Auth | Descripción |
|--------|------|------|-------------|
| GET | /api/media | No | Listar catálogo (paginado) |
| POST | /api/media | JWT | Crear media item |
| PUT | /api/media/{id} | JWT | Actualizar media item |
| DELETE | /api/media/{id} | JWT | Eliminar media item |
| POST | /api/media/{id}/images | JWT | Subir imagen (JPEG, PNG, WebP) |
| POST | /api/auth/login | No | Iniciar sesión |
| POST | /api/auth/register | No | Registrar usuario |

## Estado del desarrollo

| Fase | Descripción | Estado |
|------|-------------|--------|
| 1 | Fundamentos (CRUD, entidades, DTOs, validación) | Completada |
| 2 | Producción (Profiles, Flyway, paginación, auditoría) | Completada |
| 3 | Seguridad (JWT, registro, MIME validation, rate limiting) | Completada |
