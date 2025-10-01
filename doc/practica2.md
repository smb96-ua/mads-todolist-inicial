# Práctica 2 - Documentación Técnica

## Introducción

Esta documentación describe las modificaciones realizadas en la aplicación ToDoList para implementar las funcionalidades opcionales de la Práctica 2, que incluyen la gestión de usuarios administradores, protección de acceso a endpoints específicos y funcionalidad de bloqueo de usuarios.

## Funcionalidades Implementadas

### 1. Usuario Administrador (2 puntos)

**Objetivo**: Permitir el registro de un único usuario administrador con acceso especial a funcionalidades administrativas.

**Modificaciones en la Base de Datos**:
- Se añadió el campo `is_admin` de tipo `boolean` a la tabla `usuarios` con valor por defecto `false`.

**Cambios en el Modelo**:
- **Clase `Usuario`**: Se agregó el atributo `isAdmin` con anotación `@Column(name = "is_admin")` y sus correspondientes métodos getter y setter.
- **Clase `UsuarioData`**: Se añadió el campo `isAdmin` para el transporte de datos entre capas.

**Modificaciones en el Servicio**:
- **Método `existeAdministrador()`**: Verifica si ya existe un administrador en el sistema mediante consulta a la base de datos.
- **Método `registrarConAdmin()`**: Permite el registro de usuarios con la posibilidad de ser administradores, aplicando la restricción de un solo administrador por sistema.

**Cambios en el Controlador**:
- **`LoginController`**: Modificado para mostrar condicionalmente el checkbox de administrador en el formulario de registro y redirigir a `/registrados` cuando un administrador inicia sesión.

**Modificaciones en las Vistas**:
- **`formRegistro.html`**: Se añadió un checkbox condicional que solo aparece cuando no existe un administrador en el sistema, utilizando la expresión Thymeleaf `th:if="${!existeAdmin}"`.

### 2. Protección de Acceso (1 punto)

**Objetivo**: Restringir el acceso a las páginas `/registrados` y `/registrados/{id}` únicamente a usuarios administradores.

**Cambios en el Controlador**:
- **`HomeController`**: Se implementó el método `verificarAccesoAdministrador()` que valida si el usuario actual está logueado y es administrador.
- Los métodos `listadoUsuarios()` y `descripcionUsuario()` fueron modificados para llamar a esta verificación antes de procesar la solicitud.
- Se utiliza `ResponseStatusException` con código HTTP 401 (Unauthorized) para usuarios no autorizados.

**Gestión de Errores**:
- Usuarios no logueados o no administradores reciben un error HTTP 401 con mensajes descriptivos.

### 3. Bloqueo de Usuarios (1 punto)

**Objetivo**: Permitir a los administradores bloquear y desbloquear usuarios, impidiendo el login de usuarios bloqueados.

**Modificaciones en la Base de Datos**:
- Se añadió el campo `bloqueado` de tipo `boolean` a la tabla `usuarios` con valor por defecto `false`.

**Cambios en el Modelo**:
- **Clase `Usuario`**: Se agregó el atributo `bloqueado` con anotación `@Column(name = "bloqueado")` y sus métodos de acceso.
- **Clase `UsuarioData`**: Se incorporó el campo `bloqueado` para el transporte de datos.

**Modificaciones en el Servicio**:
- **Enum `LoginStatus`**: Se añadió el estado `USER_BLOCKED` para manejar usuarios bloqueados.
- **Método `login()`**: Modificado para verificar el estado de bloqueo antes de permitir el acceso.
- **Método `bloquearUsuario()`**: Implementa la lógica de bloqueo con validaciones para evitar bloquear administradores.
- **Método `desbloquearUsuario()`**: Permite la reactivación de usuarios bloqueados.

**Cambios en el Controlador**:
- **`LoginController`**: Actualizado para manejar el estado `USER_BLOCKED` y mostrar el mensaje "Usuario bloqueado".
- **`HomeController`**: Se añadieron los endpoints POST `/registrados/{id}/bloquear` y `/registrados/{id}/desbloquear` para la gestión de usuarios.

**Modificaciones en las Vistas**:
- **`listaUsuarios.html`**: Se añadió una columna "Estado" que muestra badges indicando si el usuario es administrador, está bloqueado o activo.
- **`descripcionUsuario.html`**: Se incorporaron botones para bloquear/desbloquear usuarios con confirmación JavaScript y se muestra el estado actual del usuario con badges Bootstrap.

## Clases y Métodos Principales

### Nuevos Métodos en `UsuarioService`
- `existeAdministrador()`: Consulta si existe al menos un administrador en el sistema
- `registrarConAdmin(UsuarioData)`: Registra usuarios con validación de administrador único
- `bloquearUsuario(Long)`: Bloquea un usuario específico con validaciones
- `desbloquearUsuario(Long)`: Desbloquea un usuario específico

### Nuevos Endpoints en Controladores
- `POST /registrados/{id}/bloquear`: Bloquea un usuario (solo administradores)
- `POST /registrados/{id}/desbloquear`: Desbloquea un usuario (solo administradores)

### Método de Seguridad
- `verificarAccesoAdministrador()` en `HomeController`: Valida permisos de administrador

## Testing

Se implementaron 11 nuevos tests distribuidos en dos clases:

### `UsuarioServiceAdminTest` (4 tests)
- Verificación de inexistencia de administrador en BD vacía
- Registro exitoso del primer administrador
- Prevención de registro de segundo administrador
- Registro normal de usuarios con administrador existente

### `UsuarioServiceBloqueoTest` (7 tests)
- Prevención de login para usuarios bloqueados
- Permiso de login tras desbloqueo
- Actualización correcta del estado de bloqueo en BD
- Actualización correcta del estado de desbloqueo en BD
- Prevención de bloqueo de administradores
- Manejo de errores para usuarios inexistentes

Todos los tests utilizan la base de datos H2 en memoria y la anotación `@Sql(scripts = "/clean-db.sql")` para garantizar un estado limpio entre ejecuciones.

## Conclusiones

Las funcionalidades implementadas fortalecen significativamente la seguridad y capacidades administrativas de la aplicación ToDoList. El sistema de administrador único, la protección de endpoints críticos y la capacidad de gestionar el acceso de usuarios proporcionan un control granular sobre el sistema, manteniendo la integridad de los datos y la seguridad de la aplicación.

La implementación sigue las mejores prácticas de Spring Boot, utiliza anotaciones apropiadas para la persistencia JPA, mantiene la separación de responsabilidades entre capas y está completamente validada mediante tests unitarios e integración.