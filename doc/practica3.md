# Práctica 3 - Sistema de Gestión de Equipos

## Resumen

Esta práctica implementa la funcionalidad básica de gestión de equipos en la aplicación ToDoList, utilizando metodología TDD (Test-Driven Development) y configuración de CI/CD con GitHub Actions y PostgreSQL.

## Versión

**Versión:** 1.2.0-SNAPSHOT (en desarrollo)

## Características Implementadas

### 1. Infraestructura CI/CD

#### GitHub Actions Workflows

Se han creado dos workflows de integración continua:

- **developer-tests.yml**: Ejecuta todos los tests en cada push utilizando H2 en memoria
- **integration-tests.yml**: Ejecuta tests de integración con PostgreSQL en contenedor Docker

#### Configuración de Perfiles PostgreSQL

Se han añadido archivos de configuración para el perfil PostgreSQL:
- `src/main/resources/application-postgres.properties`: Configuración para base de datos de desarrollo (mads)
- `src/test/resources/application-postgres.properties`: Configuración para base de datos de tests (mads_test)

La aplicación utiliza PostgreSQL 13 en producción y H2 en desarrollo por defecto.

### 2. Historia de Usuario 008: Listado de Equipos

Se ha implementado la funcionalidad básica de equipos siguiendo TDD con 5 ciclos completos:

#### Ciclo 1: Entidad Equipo
- Creación de la clase `Equipo` con atributos id y nombre
- Test básico de creación de equipo
- Persistencia JPA con tabla `equipos`

#### Ciclo 2: Relación Usuario-Equipo
- Implementación de relación many-to-many entre Usuario y Equipo
- Tabla intermedia `equipo_usuario`
- Método `addUsuario()` para gestionar la relación bidireccional
- Tests de igualdad y gestión de usuarios en equipos

#### Ciclo 3: EquipoRepository
- Creación del repositorio JPA `EquipoRepository`
- Tests de persistencia: crear, buscar por ID, listar todos
- Tests de relación Usuario-Equipo en base de datos
- Actualización de `clean-db.sql` para incluir tablas de equipos

#### Ciclo 4: EquipoService
- Creación del DTO `EquipoData`
- Implementación de `EquipoService` con operaciones:
  - `crearEquipo(String nombre)`
  - `findById(Long id)`
  - `findAllOrderedByName()`
  - `añadirUsuarioAEquipo(Long equipoId, Long usuarioId)`
- Tests de servicio con transacciones

#### Ciclo 5: EquipoController y Vistas
- Controlador `EquipoController` con endpoints:
  - `GET /equipos`: Lista todos los equipos
  - `GET /equipos/{id}`: Muestra detalle de un equipo
- Templates Thymeleaf:
  - `listaEquipos.html`: Vista de listado con tabla Bootstrap
  - `detalleEquipo.html`: Vista de detalle del equipo
- Integración con navbar (enlace "Equipos")
- Tests web con MockMvc
- Control de acceso (requiere login)

## Modelo de Datos

```
USUARIOS (1) ←→ (N) EQUIPO_USUARIO (N) ←→ (1) EQUIPOS
```

### Tablas Creadas

- **equipos**: id (PK), nombre (NOT NULL)
- **equipo_usuario**: fk_equipo (FK), fk_usuario (FK)

### Relaciones

- Usuario-Equipo: Many-to-Many
- Usuario-Tarea: One-to-Many (existente)

## Metodología TDD

Se ha seguido estrictamente el ciclo Red-Green-Refactor:

1. **Red**: Escribir test que falla
2. **Green**: Implementar código mínimo para pasar el test
3. **Refactor**: Mejorar el código manteniendo los tests verdes

Cada ciclo se ha confirmado en un commit separado con mensaje descriptivo:
- "TDD Ciclo 1: Test y entidad Equipo básica"
- "TDD Ciclo 2: Relación many-to-many Usuario-Equipo"
- "TDD Ciclo 3: EquipoRepository y tests de persistencia"
- "TDD Ciclo 4: EquipoService con DTO y operaciones básicas"
- "TDD Ciclo 5: EquipoController y vistas de listado"

## Tecnologías Utilizadas

- **Spring Boot 2.7.14**: Framework principal
- **JPA/Hibernate**: ORM con soporte PostgreSQL9Dialect
- **H2 Database**: Base de datos en memoria para desarrollo
- **PostgreSQL 13**: Base de datos de producción
- **Thymeleaf**: Motor de plantillas
- **Bootstrap 4.6**: Framework CSS
- **JUnit 5**: Framework de testing
- **MockMvc**: Testing de controladores
- **GitHub Actions**: CI/CD
- **Docker**: Contenedores PostgreSQL para tests

## Testing

Total de tests ejecutados: **67 tests**
- Tests de modelo: 7 tests
- Tests de repositorio: 14 tests  
- Tests de servicio: 12 tests
- Tests de controlador: 8 tests
- Tests web integración: 26 tests

Todos los tests pasan correctamente tanto en H2 como en PostgreSQL.

## Próximas Funcionalidades (Historias 009 y 010)

- Historia 009: Crear equipos, añadir/eliminar usuarios
- Historia 010: Administración de equipos (renombrar, eliminar)

## Ejecución

### Desarrollo (H2)
```bash
./mvnw spring-boot:run
```

### Producción (PostgreSQL)
```bash
# Iniciar PostgreSQL
docker run -d --rm -p 5432:5432 --name postgres-mads -e POSTGRES_PASSWORD=mads postgres:13

# Crear base de datos
docker exec -it postgres-mads psql -U postgres -c "CREATE DATABASE mads;"
docker exec -it postgres-mads psql -U postgres -c "CREATE USER mads WITH PASSWORD 'mads';"
docker exec -it postgres-mads psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE mads TO mads;"

# Ejecutar aplicación
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Tests
```bash
# Tests con H2
./mvnw test

# Tests con PostgreSQL
./mvnw test -Dspring.profiles.active=postgres
```

## Autor

Sergio Martínez Blanes (smb96@alu.ua.es)

## Fecha

Octubre 2025
