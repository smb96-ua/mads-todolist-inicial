# Práctica 3 - Sistema de Gestión de Equipos

## Resumen

Esta práctica implementa la funcionalidad básica de gestión de equipos en la aplicación ToDoList, utilizando metodología TDD (Test-Driven Development) y configuración de CI/CD con GitHub Actions y PostgreSQL.

## Versión

**Versión:** 1.2.0-SNAPSHOT (en desarrollo)

## Características Implementadas

### 1. Infraestructura CI/CD

#### GitHub Actions Workflows

Se han creado dos workflows de integración continua:

- **developer-tests.yml**: Ejecuta todos los tests en cada push utilizando Java 17 y H2 en memoria
- **integration-tests.yml**: Ejecuta tests de integración con PostgreSQL 13 en contenedor Docker

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

### 3. Historia de Usuario 009: Gestionar Pertenencia a Equipos

Se ha implementado la funcionalidad completa para gestionar la pertenencia de usuarios a equipos:

#### Ciclo 6: Gestión de Usuarios en Equipos
- Nuevos métodos en `EquipoService`:
  - `usuariosEquipo(Long equipoId)`: Obtener lista de usuarios de un equipo
  - `eliminarUsuarioDeEquipo(Long equipoId, Long usuarioId)`: Eliminar usuario de equipo
  - `equiposUsuario(Long usuarioId)`: Obtener lista de equipos de un usuario
- Tests completos para todas las operaciones

#### Funcionalidad Web Historia 009
- **Crear equipos**: Formulario para crear nuevos equipos desde la interfaz
  - `GET /equipos/nuevo`: Formulario de creación
  - `POST /equipos`: Crear equipo
  - Template: `formNuevoEquipo.html`
- **Gestionar usuarios**: 
  - `POST /equipos/{id}/usuarios/{usuarioId}/agregar`: Añadir usuario a equipo
  - `DELETE /equipos/{id}/usuarios/{usuarioId}`: Eliminar usuario de equipo
  - Vista actualizada en `detalleEquipo.html` con tabla de usuarios
  - Eliminación con confirmación JavaScript

### 4. Historia de Usuario 010: Administración de Equipos

Se ha implementado la funcionalidad de administración completa de equipos:

#### Ciclo 7: Renombrar y Eliminar Equipos
- Nuevos métodos en `EquipoService`:
  - `renombrarEquipo(Long equipoId, String nuevoNombre)`: Cambiar nombre del equipo
  - `eliminarEquipo(Long equipoId)`: Eliminar equipo y todas sus relaciones
- Tests completos de renombrado y eliminación

#### Funcionalidad Web Historia 010
- **Editar equipo**: Formulario para renombrar equipos
  - `GET /equipos/{id}/editar`: Formulario de edición
  - `POST /equipos/{id}/editar`: Actualizar nombre
  - Template: `formEditarEquipo.html`
- **Eliminar equipo**: Eliminar equipo completo
  - `DELETE /equipos/{id}`: Eliminar equipo
  - Confirmación JavaScript con mensaje de advertencia
  - Limpieza automática de todas las relaciones usuario-equipo

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

Se ha seguido estrictamente el ciclo Red-Green-Refactor en **7 ciclos completos**:

1. **Red**: Escribir test que falla
2. **Green**: Implementar código mínimo para pasar el test
3. **Refactor**: Mejorar el código manteniendo los tests verdes

Cada ciclo se ha confirmado en un commit separado con mensaje descriptivo:
- **TDD Ciclo 1**: Test y entidad Equipo básica
- **TDD Ciclo 2**: Relación many-to-many Usuario-Equipo
- **TDD Ciclo 3**: EquipoRepository y tests de persistencia
- **TDD Ciclo 4**: EquipoService con DTO y operaciones básicas
- **TDD Ciclo 5**: EquipoController y vistas de listado
- **TDD Ciclo 6**: Métodos gestión usuarios en equipos
- **TDD Ciclo 7**: Métodos para renombrar y eliminar equipos

## Tecnologías Utilizadas

- **Spring Boot 2.7.14**: Framework principal (Java 17)
- **JPA/Hibernate**: ORM con soporte PostgreSQL9Dialect
- **H2 Database**: Base de datos en memoria para desarrollo
- **PostgreSQL 13**: Base de datos de producción
- **Thymeleaf**: Motor de plantillas
- **Bootstrap 4.6**: Framework CSS
- **JUnit 5**: Framework de testing
- **MockMvc**: Testing de controladores
- **GitHub Actions**: CI/CD con Java 17
- **Docker**: Contenedores PostgreSQL para tests

## Testing

Total de tests ejecutados: **72 tests** (todos pasan ✅)
- Tests de modelo: 7 tests
- Tests de repositorio: 18 tests  
- Tests de servicio: 16 tests (9 en EquipoServiceTest)
- Tests de controlador: 8 tests
- Tests web integración: 23 tests

Todos los tests pasan correctamente tanto en H2 como en PostgreSQL.

## Funcionalidades Completas

**Historia 008 - Listado de equipos**: ✅ Completa
- Ver lista de todos los equipos
- Ver detalle de cada equipo
- Navegación desde navbar

**Historia 009 - Gestionar pertenencia**: ✅ Completa
- Crear nuevos equipos desde interfaz web
- Añadir usuarios a equipos
- Eliminar usuarios de equipos
- Ver usuarios de cada equipo

**Historia 010 - Administración de equipos**: ✅ Completa
- Renombrar equipos
- Eliminar equipos (con limpieza de relaciones)
- Formularios de edición
- Confirmaciones JavaScript

## Endpoints REST Implementados

### Equipos
- `GET /equipos` - Listar todos los equipos
- `GET /equipos/nuevo` - Formulario crear equipo
- `POST /equipos` - Crear equipo
- `GET /equipos/{id}` - Ver detalle equipo
- `GET /equipos/{id}/editar` - Formulario editar equipo
- `POST /equipos/{id}/editar` - Actualizar nombre equipo
- `DELETE /equipos/{id}` - Eliminar equipo

### Gestión de Usuarios en Equipos
- `POST /equipos/{id}/usuarios/{usuarioId}/agregar` - Añadir usuario
- `DELETE /equipos/{id}/usuarios/{usuarioId}` - Eliminar usuario

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
