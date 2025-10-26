# 📚 Guía de Estudio - Práctica 3: Sistema de Gestión de Equipos

## 🎯 Objetivo del Examen
Demostrar dominio en desarrollo de aplicaciones Spring Boot siguiendo TDD, integrando CI/CD, y gestionando relaciones many-to-many en JPA.

---

## 1️⃣ CONCEPTOS FUNDAMENTALES

### 1.1 Metodología TDD (Test-Driven Development)

#### ¿Qué es TDD?
Metodología de desarrollo donde **primero escribes el test** que falla (Red), luego implementas el código mínimo para que pase (Green), y finalmente refactorizas (Refactor).

#### Ciclo Red-Green-Refactor
```
🔴 RED → Escribir test que falla
🟢 GREEN → Implementar código mínimo para pasar test
♻️ REFACTOR → Mejorar código manteniendo tests verdes
```

#### ¿Por qué es importante?
- ✅ **Calidad del código**: Menos bugs en producción
- ✅ **Documentación viva**: Los tests documentan el comportamiento esperado
- ✅ **Refactoring seguro**: Puedes cambiar código sin miedo
- ✅ **Diseño mejorado**: Te obliga a pensar en la API antes de implementar

#### En la Práctica 3:
Realizamos **7 ciclos TDD completos**:
1. Entidad Equipo básica
2. Relación many-to-many Usuario-Equipo
3. EquipoRepository
4. EquipoService con operaciones básicas
5. EquipoController y vistas
6. Gestión de usuarios en equipos
7. Renombrar y eliminar equipos

**Cada ciclo = 1 commit en Git** 📝

---

### 1.2 Spring Boot - Arquitectura en Capas

```
┌─────────────────────────────────┐
│   VISTA (Templates Thymeleaf)   │ ← HTML + Bootstrap
├─────────────────────────────────┤
│   CONTROLLER (@Controller)       │ ← Endpoints REST/Web
├─────────────────────────────────┤
│   SERVICE (@Service)             │ ← Lógica de negocio
├─────────────────────────────────┤
│   REPOSITORY (CrudRepository)    │ ← Acceso a datos
├─────────────────────────────────┤
│   MODELO (@Entity)               │ ← Entidades JPA
├─────────────────────────────────┤
│   BASE DE DATOS (H2/PostgreSQL) │
└─────────────────────────────────┘
```

#### Responsabilidades de cada capa:

**MODELO (Entity)**
- Define estructura de datos
- Anotaciones JPA: `@Entity`, `@Table`, `@Id`, `@ManyToMany`
- Relaciones entre entidades
- Métodos `equals()` y `hashCode()`

**REPOSITORY (Interface)**
- Hereda de `CrudRepository<Entidad, Long>`
- Spring Data JPA genera implementación automáticamente
- Métodos CRUD sin escribir SQL: `save()`, `findById()`, `findAll()`, `delete()`

**SERVICE (Clase)**
- `@Service` para inyección de dependencias
- `@Transactional` para operaciones de BD
- Lógica de negocio
- Usa DTOs para comunicarse con el controller
- Usa ModelMapper para convertir Entity ↔ DTO

**CONTROLLER (Clase)**
- `@Controller` para vistas web
- `@RestController` para API REST
- `@GetMapping`, `@PostMapping`, `@DeleteMapping`
- Gestiona sesión con `ManagerUserSession`
- Devuelve nombres de templates o redirecciones

**VISTA (Templates)**
- Thymeleaf: `th:text`, `th:href`, `th:each`, `th:if`
- Bootstrap para estilos CSS
- JavaScript para DELETE requests con `fetch()`

---

### 1.3 JPA - Relaciones Many-to-Many

#### ¿Qué es una relación Many-to-Many?
Un usuario puede pertenecer a **muchos equipos**, y un equipo puede tener **muchos usuarios**.

#### Implementación en JPA:

**Lado propietario (Equipo.java)**
```java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(name = "equipo_usuario",
    joinColumns = { @JoinColumn(name = "fk_equipo") },
    inverseJoinColumns = { @JoinColumn(name = "fk_usuario") })
Set<Usuario> usuarios = new HashSet<>();
```

**Lado inverso (Usuario.java)**
```java
@ManyToMany(mappedBy = "usuarios", fetch = FetchType.LAZY)
Set<Equipo> equipos = new HashSet<>();
```

#### Tabla intermedia generada:
```sql
CREATE TABLE equipo_usuario (
    fk_equipo BIGINT NOT NULL,
    fk_usuario BIGINT NOT NULL,
    PRIMARY KEY (fk_equipo, fk_usuario),
    FOREIGN KEY (fk_equipo) REFERENCES equipos(id),
    FOREIGN KEY (fk_usuario) REFERENCES usuarios(id)
);
```

#### Gestión bidireccional:
```java
// Método en Equipo.java
public void addUsuario(Usuario usuario) {
    this.usuarios.add(usuario);      // Añade a este lado
    usuario.getEquipos().add(this);  // Añade al otro lado
}
```

**⚠️ MUY IMPORTANTE**: Siempre gestionar ambos lados de la relación.

---

### 1.4 DTOs (Data Transfer Objects)

#### ¿Por qué usar DTOs?
- ✅ Separar capa de persistencia de capa de presentación
- ✅ Evitar lazy loading exceptions
- ✅ Controlar qué datos se exponen
- ✅ Facilitar serialización JSON

#### Conversión Entity ↔ DTO con ModelMapper:

```java
// En EquipoService.java
@Autowired
private ModelMapper modelMapper;

// Entity → DTO
EquipoData dto = modelMapper.map(equipo, EquipoData.class);

// Lista de Entity → Lista de DTO
return equipos.stream()
    .map(equipo -> modelMapper.map(equipo, EquipoData.class))
    .collect(Collectors.toList());
```

---

## 2️⃣ CI/CD CON GITHUB ACTIONS

### 2.1 ¿Qué es CI/CD?

**CI (Continuous Integration)**
- Integración continua del código
- Ejecuta tests automáticamente en cada push
- Detecta errores temprano

**CD (Continuous Deployment)**
- Despliegue continuo a producción
- Automatiza el proceso de release

### 2.2 Workflows en GitHub Actions

#### developer-tests.yml
```yaml
name: Developer tests
on: push  # Se ejecuta en CADA push

jobs:
  launch-tests:
    runs-on: ubuntu-latest
    
    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
      
      - name: Launch tests with Maven
        run: ./mvnw test
```

**Ejecuta**: Todos los tests con H2 en memoria

#### integration-tests.yml
```yaml
name: Integration Tests
on:
  push:
    branches:
      - main  # Solo en push a main

jobs:
  integration-tests:
    runs-on: ubuntu-latest
    
    services:
      postgres:
        image: postgres:13
        env:
          POSTGRES_USER: mads
          POSTGRES_PASSWORD: mads
          POSTGRES_DB: mads_test
        ports:
          - 5432:5432
        options: >-
          --health-cmd pg_isready
          --health-interval 10s
          --health-timeout 5s
          --health-retries 5

    steps:
      - uses: actions/checkout@v2
      
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
          distribution: 'temurin'
          
      - name: Run integration tests with PostgreSQL
        run: ./mvnw test -Dspring.profiles.active=postgres
        env:
          POSTGRES_HOST: localhost
          POSTGRES_PORT: 5432
          DB_USER: mads
          DB_PASSWD: mads
```

**Ejecuta**: Tests con PostgreSQL real en Docker

### 2.3 Perfiles de Spring Boot

#### application.properties (default - H2)
```properties
spring.datasource.url=jdbc:h2:mem:dev
spring.jpa.hibernate.ddl-auto=create
```

#### application-postgres.properties (producción)
```properties
POSTGRES_HOST=localhost
POSTGRES_PORT=5432
DB_USER=mads
DB_PASSWD=mads
spring.datasource.url=jdbc:postgresql://${POSTGRES_HOST}:${POSTGRES_PORT}/mads
spring.datasource.username=${DB_USER}
spring.datasource.password=${DB_PASSWD}
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQL9Dialect
```

#### Activar perfil:
```bash
# Desarrollo (H2)
./mvnw spring-boot:run

# Producción (PostgreSQL)
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres

# Tests con PostgreSQL
./mvnw test -Dspring.profiles.active=postgres
```

---

## 3️⃣ IMPLEMENTACIÓN PASO A PASO

### 3.1 Crear una nueva Entidad (TDD Ciclo 1)

#### 1. Escribir el test primero (RED)
```java
// src/test/java/madstodolist/model/EquipoTest.java
@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoTest {
    
    @Test
    public void crearEquipo() {
        Equipo equipo = new Equipo("Proyecto A");
        
        assertThat(equipo.getNombre()).isEqualTo("Proyecto A");
    }
}
```

**Ejecutar**: `./mvnw test -Dtest=EquipoTest` → ❌ FALLA (no existe Equipo)

#### 2. Implementar el código (GREEN)
```java
// src/main/java/madstodolist/model/Equipo.java
@Entity
@Table(name = "equipos")
public class Equipo implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String nombre;
    
    public Equipo() {}
    
    public Equipo(String nombre) {
        this.nombre = nombre;
    }
    
    // Getters, setters, equals, hashCode
}
```

**Ejecutar**: `./mvnw test -Dtest=EquipoTest` → ✅ PASA

#### 3. Commit
```bash
git add .
git commit -m "TDD Ciclo 1: Test y entidad Equipo básica"
```

---

### 3.2 Crear Repository (TDD Ciclo 2)

#### 1. Test primero (RED)
```java
@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoTest {
    
    @Autowired
    private EquipoRepository equipoRepository;
    
    @Test
    public void crearEquipoEnBD() {
        Equipo equipo = new Equipo("Proyecto A");
        equipoRepository.save(equipo);
        
        assertThat(equipo.getId()).isNotNull();
    }
    
    @Test
    @Transactional
    public void listarTodosLosEquipos() {
        equipoRepository.save(new Equipo("Proyecto A"));
        equipoRepository.save(new Equipo("Proyecto B"));
        
        Iterable<Equipo> equipos = equipoRepository.findAll();
        
        assertThat(equipos).hasSize(2);
    }
}
```

#### 2. Implementar (GREEN)
```java
// src/main/java/madstodolist/repository/EquipoRepository.java
public interface EquipoRepository extends CrudRepository<Equipo, Long> {
    // Spring Data JPA genera la implementación automáticamente
}
```

#### 3. Actualizar clean-db.sql
```sql
DELETE FROM equipo_usuario;
DELETE FROM equipos;
DELETE FROM tareas;
DELETE FROM usuarios;
```

---

### 3.3 Crear Service (TDD Ciclo 3)

#### 1. Test primero (RED)
```java
@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoServiceTest {
    
    @Autowired
    private EquipoService equipoService;
    
    @Test
    public void crearNuevoEquipo() {
        EquipoData equipo = equipoService.crearEquipo("Proyecto A");
        
        assertThat(equipo.getId()).isNotNull();
        assertThat(equipo.getNombre()).isEqualTo("Proyecto A");
    }
    
    @Test
    public void listarEquipos() {
        equipoService.crearEquipo("Proyecto A");
        equipoService.crearEquipo("Proyecto B");
        
        List<EquipoData> equipos = equipoService.findAllOrderedByName();
        
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto A");
    }
}
```

#### 2. Crear DTO
```java
// src/main/java/madstodolist/dto/EquipoData.java
public class EquipoData implements Serializable {
    private Long id;
    private String nombre;
    
    // Getters, setters, equals, hashCode
}
```

#### 3. Implementar Service (GREEN)
```java
@Service
public class EquipoService {
    
    @Autowired
    private EquipoRepository equipoRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    @Transactional
    public EquipoData crearEquipo(String nombre) {
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return modelMapper.map(equipo, EquipoData.class);
    }
    
    @Transactional(readOnly = true)
    public List<EquipoData> findAllOrderedByName() {
        List<Equipo> equipos = new ArrayList<>();
        equipoRepository.findAll().forEach(equipos::add);
        Collections.sort(equipos, (a, b) -> a.getNombre().compareTo(b.getNombre()));
        return equipos.stream()
                .map(e -> modelMapper.map(e, EquipoData.class))
                .toList();
    }
    
    @Transactional(readOnly = true)
    public EquipoData findById(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if (equipo == null) return null;
        return modelMapper.map(equipo, EquipoData.class);
    }
}
```

**Anotaciones importantes**:
- `@Transactional`: Gestiona transacciones de BD automáticamente
- `@Transactional(readOnly = true)`: Optimización para consultas

---

### 3.4 Crear Controller y Vistas (TDD Ciclo 4)

#### 1. Test del Controller
```java
@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql")
public class EquipoWebTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private EquipoService equipoService;
    
    @Test
    public void listarEquiposSinLogearse() throws Exception {
        this.mockMvc.perform(get("/equipos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }
    
    @Test
    public void listarEquiposLogeado() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        equipoService.crearEquipo("Proyecto A");
        
        this.mockMvc.perform(get("/equipos")
                        .sessionAttr("idUsuarioLogeado", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Proyecto A")));
    }
}
```

#### 2. Implementar Controller
```java
@Controller
public class EquipoController {
    
    @Autowired
    private EquipoService equipoService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ManagerUserSession managerUserSession;
    
    @GetMapping("/equipos")
    public String listarEquipos(Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }
        
        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        List<EquipoData> equipos = equipoService.findAllOrderedByName();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipos", equipos);
        return "listaEquipos";
    }
    
    @GetMapping("/equipos/{id}")
    public String mostrarEquipo(@PathVariable("id") Long idEquipo, 
                                Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }
        
        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        EquipoData equipo = equipoService.findById(idEquipo);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        return "detalleEquipo";
    }
}
```

#### 3. Crear Vista Thymeleaf
```html
<!-- src/main/resources/templates/listaEquipos.html -->
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head th:replace="fragments :: head (titulo='Lista de Equipos')"></head>

<body>
<div th:replace="fragments :: navbar (usuario=${usuario})"></div>

<div class="container-fluid">
    <div class="row mt-3">
        <div class="col">
            <h1>Lista de Equipos</h1>
        </div>
    </div>
    
    <table class="table table-striped mt-3">
        <thead>
            <tr>
                <th>Nombre del Equipo</th>
                <th>Acciones</th>
            </tr>
        </thead>
        <tbody>
            <tr th:each="equipo: ${equipos}">
                <td th:text="${equipo.nombre}"></td>
                <td>
                    <a class="btn btn-primary btn-sm" 
                       th:href="@{/equipos/{id}(id=${equipo.id})}">
                        Ver detalle
                    </a>
                </td>
            </tr>
        </tbody>
    </table>
</div>

<div th:replace="fragments::javascript"/>
</body>
</html>
```

**Thymeleaf importante**:
- `th:text="${variable}"`: Insertar texto
- `th:href="@{/ruta}"`: URLs relativas
- `th:each="item: ${lista}"`: Bucle for
- `th:if="${condicion}"`: Condicional

---

### 3.5 Operaciones DELETE con JavaScript

#### Controller
```java
@DeleteMapping("/equipos/{id}")
@ResponseBody
public String eliminarEquipo(@PathVariable("id") Long idEquipo, HttpSession session) {
    Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
    if (idUsuarioLogeado == null) {
        return "redirect:/login";
    }
    
    equipoService.eliminarEquipo(idEquipo);
    return "";
}
```

#### Vista con JavaScript
```html
<a class="btn btn-danger" href="#" 
   th:onclick="'delEquipo(\'/equipos/' + ${equipo.id} + '\');'">
    Eliminar equipo
</a>

<script>
function delEquipo(urlBorrar) {
    if (confirm('¿Está seguro?')) {
        fetch(urlBorrar, {
            method: 'DELETE'
        }).then(response => {
            if (response.ok) {
                window.location.href = '/equipos';
            }
        });
    }
}
</script>
```

---

## 4️⃣ COMANDOS ESENCIALES

### Git
```bash
# Ver estado
git status

# Añadir archivos
git add .
git add archivo.java

# Commit
git commit -m "Mensaje descriptivo"

# Push
git push origin main

# Ver historial
git log --oneline

# Ver cambios
git diff
```

### Maven
```bash
# Compilar
./mvnw compile

# Ejecutar tests
./mvnw test

# Ejecutar test específico
./mvnw test -Dtest=EquipoTest

# Limpiar y compilar
./mvnw clean compile

# Ejecutar aplicación
./mvnw spring-boot:run

# Ejecutar con perfil PostgreSQL
./mvnw spring-boot:run -Dspring-boot.run.profiles=postgres
```

### Docker (PostgreSQL)
```bash
# Iniciar contenedor PostgreSQL
docker run -d --rm -p 5432:5432 --name postgres-mads \
  -e POSTGRES_PASSWORD=mads postgres:13

# Crear base de datos
docker exec -it postgres-mads psql -U postgres \
  -c "CREATE DATABASE mads;"

# Crear usuario
docker exec -it postgres-mads psql -U postgres \
  -c "CREATE USER mads WITH PASSWORD 'mads';"

# Dar permisos
docker exec -it postgres-mads psql -U postgres \
  -c "GRANT ALL PRIVILEGES ON DATABASE mads TO mads;"

# Conectar a PostgreSQL
docker exec -it postgres-mads psql -U mads -d mads

# Parar contenedor
docker stop postgres-mads

# Listar contenedores
docker ps
```

---

## 5️⃣ PREGUNTAS TIPO EXAMEN

### Teoría

**1. ¿Qué es TDD y cuál es su ciclo?**
- Respuesta: Test-Driven Development. Ciclo Red-Green-Refactor:
  1. Red: Escribir test que falla
  2. Green: Implementar código mínimo para pasar
  3. Refactor: Mejorar código sin romper tests

**2. ¿Qué ventajas tiene usar TDD?**
- Menos bugs, código más limpio, refactoring seguro, mejor diseño, documentación viva

**3. ¿Qué es una relación Many-to-Many en JPA?**
- Relación donde múltiples instancias de una entidad se relacionan con múltiples instancias de otra. Requiere tabla intermedia.

**4. ¿Diferencia entre @ManyToMany y @ManyToMany(mappedBy)?**
- Sin mappedBy: Lado propietario (crea la tabla intermedia)
- Con mappedBy: Lado inverso (no crea tabla, solo referencia)

**5. ¿Para qué sirven los DTOs?**
- Separar capas, evitar lazy loading exceptions, controlar datos expuestos, facilitar serialización

**6. ¿Qué hace @Transactional?**
- Gestiona transacciones de BD automáticamente. Si hay error, hace rollback.

**7. ¿Diferencia entre @Controller y @RestController?**
- @Controller: Devuelve vistas (HTML)
- @RestController: Devuelve datos (JSON/XML), equivale a @Controller + @ResponseBody

**8. ¿Qué es CI/CD?**
- CI: Continuous Integration (integración continua, tests automáticos)
- CD: Continuous Deployment (despliegue continuo automático)

**9. ¿Para qué sirven los perfiles de Spring?**
- Configurar diferentes entornos (desarrollo, testing, producción) con distintas bases de datos y configuraciones

**10. ¿Qué hace CrudRepository?**
- Interface de Spring Data JPA que proporciona métodos CRUD automáticos sin escribir SQL

### Práctica

**1. Implementa un test para crear un equipo**
```java
@Test
public void crearEquipo() {
    Equipo equipo = new Equipo("Proyecto A");
    assertThat(equipo.getNombre()).isEqualTo("Proyecto A");
}
```

**2. Implementa la relación many-to-many entre Usuario y Equipo**
```java
// En Equipo.java
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(name = "equipo_usuario",
    joinColumns = @JoinColumn(name = "fk_equipo"),
    inverseJoinColumns = @JoinColumn(name = "fk_usuario"))
Set<Usuario> usuarios = new HashSet<>();

// En Usuario.java
@ManyToMany(mappedBy = "usuarios")
Set<Equipo> equipos = new HashSet<>();
```

**3. Implementa un método para añadir usuario a equipo**
```java
// En EquipoService.java
@Transactional
public void añadirUsuarioAEquipo(Long equipoId, Long usuarioId) {
    Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
    Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
    if (equipo == null || usuario == null) return;
    equipo.addUsuario(usuario);
}
```

**4. Crea un endpoint GET para listar equipos**
```java
@GetMapping("/equipos")
public String listarEquipos(Model model, HttpSession session) {
    Long id = managerUserSession.usuarioLogeado();
    if (id == null) return "redirect:/login";
    
    UsuarioData usuario = usuarioService.findById(id);
    List<EquipoData> equipos = equipoService.findAllOrderedByName();
    
    model.addAttribute("usuario", usuario);
    model.addAttribute("equipos", equipos);
    return "listaEquipos";
}
```

**5. Implementa eliminación con JavaScript**
```javascript
function delEquipo(url) {
    if (confirm('¿Seguro?')) {
        fetch(url, { method: 'DELETE' })
            .then(r => r.ok && (window.location.href = '/equipos'));
    }
}
```

---

## 6️⃣ ERRORES COMUNES Y SOLUCIONES

### Error 1: LazyInitializationException
**Problema**: Intentar acceder a relación lazy fuera de transacción
**Solución**: 
- Añadir `@Transactional` al método
- Usar DTOs en lugar de entities

### Error 2: Tests fallan por datos anteriores
**Problema**: Tests no limpian BD entre ejecuciones
**Solución**: 
- Añadir `@Sql(scripts = "/clean-db.sql")` a clase de test
- Incluir todas las tablas en clean-db.sql

### Error 3: GitHub Actions falla
**Problema**: Java version mismatch
**Solución**: 
- Verificar que workflow use Java 17
- Verificar que pom.xml use Spring Boot 2.7+

### Error 4: No se guarda relación many-to-many
**Problema**: No gestionar ambos lados de la relación
**Solución**:
```java
public void addUsuario(Usuario usuario) {
    this.usuarios.add(usuario);      // Lado A
    usuario.getEquipos().add(this);  // Lado B ← IMPORTANTE
}
```

### Error 5: NullPointerException en vista
**Problema**: No añadir atributo al Model
**Solución**:
```java
model.addAttribute("equipos", equipos); // SIEMPRE añadir
```

---

## 7️⃣ CHECKLIST PARA EL EXAMEN

### Antes de empezar
- [ ] Entiendo el enunciado completamente
- [ ] Identifico las entidades necesarias
- [ ] Identifico las relaciones entre entidades
- [ ] Planifico los ciclos TDD necesarios

### Implementación
- [ ] Test primero (RED)
- [ ] Ejecuto test y verifico que falla
- [ ] Implemento código mínimo (GREEN)
- [ ] Ejecuto test y verifico que pasa
- [ ] Refactorizo si necesario
- [ ] Commit con mensaje descriptivo
- [ ] Repito para siguiente funcionalidad

### Testing
- [ ] Tests de modelo (@SpringBootTest)
- [ ] Tests de repository (@SpringBootTest)
- [ ] Tests de service (@SpringBootTest)
- [ ] Tests de controller (@SpringBootTest @AutoConfigureMockMvc)
- [ ] Todos los tests pasan: `./mvnw test`

### Código
- [ ] Entities con anotaciones JPA correctas
- [ ] Repository extiende CrudRepository
- [ ] Service con @Service y @Transactional
- [ ] Controller con @Controller/@RestController
- [ ] DTOs para transferir datos
- [ ] ModelMapper para conversiones
- [ ] Gestión bidireccional de relaciones many-to-many

### Vistas (si aplica)
- [ ] Templates Thymeleaf con sintaxis correcta
- [ ] Bootstrap para estilos
- [ ] JavaScript para DELETE requests
- [ ] Navegación entre vistas funcional

### Git
- [ ] Commits atómicos (1 funcionalidad = 1 commit)
- [ ] Mensajes descriptivos
- [ ] Push a GitHub

### CI/CD (si aplica)
- [ ] Workflows de GitHub Actions configurados
- [ ] Tests pasan en GitHub Actions
- [ ] Perfiles de Spring configurados

---

## 8️⃣ RECURSOS ADICIONALES

### Documentación Oficial
- Spring Boot: https://spring.io/projects/spring-boot
- Spring Data JPA: https://spring.io/projects/spring-data-jpa
- Thymeleaf: https://www.thymeleaf.org/
- JUnit 5: https://junit.org/junit5/
- GitHub Actions: https://docs.github.com/en/actions

### Comandos de consulta rápida
```bash
# Ver qué tests hay
find src/test -name "*Test.java"

# Ver estructura del proyecto
tree src/

# Buscar en archivos
grep -r "palabra" src/

# Ver logs de Git
git log --oneline --graph

# Ver estado de BD PostgreSQL
docker exec -it postgres-mads psql -U mads -d mads -c "\dt"
```

---

## 9️⃣ SIMULACRO DE EXAMEN

### Ejercicio Completo: Sistema de Proyectos

**Enunciado**: Implementa un sistema para gestionar proyectos y tareas con las siguientes características:

1. Un proyecto tiene un nombre y puede tener muchas tareas
2. Una tarea tiene un título y pertenece a un proyecto
3. Implementa las siguientes operaciones:
   - Crear proyecto
   - Listar todos los proyectos
   - Ver detalles de un proyecto con sus tareas
   - Añadir tarea a un proyecto
   - Eliminar tarea de un proyecto

**Solución paso a paso**:

#### Paso 1: Crear entidad Proyecto (TDD Ciclo 1)
```java
// Test
@Test
public void crearProyecto() {
    Proyecto p = new Proyecto("Web App");
    assertThat(p.getNombre()).isEqualTo("Web App");
}

// Implementación
@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String nombre;
    
    @OneToMany(mappedBy = "proyecto", fetch = FetchType.LAZY)
    Set<TareaProyecto> tareas = new HashSet<>();
    
    // Constructor, getters, setters
}
```

#### Paso 2: Crear entidad TareaProyecto
```java
@Entity
@Table(name = "tareas_proyecto")
public class TareaProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String titulo;
    
    @ManyToOne
    @JoinColumn(name = "proyecto_id")
    private Proyecto proyecto;
    
    // Constructor, getters, setters
}
```

#### Paso 3: Repository
```java
public interface ProyectoRepository extends CrudRepository<Proyecto, Long> {}
public interface TareaProyectoRepository extends CrudRepository<TareaProyecto, Long> {}
```

#### Paso 4: Service
```java
@Service
public class ProyectoService {
    @Autowired ProyectoRepository proyectoRepository;
    @Autowired TareaProyectoRepository tareaRepository;
    @Autowired ModelMapper modelMapper;
    
    @Transactional
    public ProyectoData crearProyecto(String nombre) {
        Proyecto p = new Proyecto(nombre);
        proyectoRepository.save(p);
        return modelMapper.map(p, ProyectoData.class);
    }
    
    @Transactional
    public void añadirTarea(Long proyectoId, String titulo) {
        Proyecto p = proyectoRepository.findById(proyectoId).orElse(null);
        if (p == null) return;
        
        TareaProyecto t = new TareaProyecto(titulo);
        t.setProyecto(p);
        tareaRepository.save(t);
    }
}
```

**Continúa tú con el Controller y las vistas...**

---

## 🎓 CONSEJOS FINALES PARA EL EXAMEN

1. **Lee TODO el enunciado** antes de empezar a codificar
2. **Planifica los ciclos TDD** en papel
3. **Sigue el orden**: Model → Repository → Service → Controller → Vista
4. **No te saltes los tests**: Es TDD, el test va PRIMERO
5. **Commits frecuentes**: Cada ciclo TDD = 1 commit
6. **Ejecuta tests constantemente**: `./mvnw test` es tu amigo
7. **Si te atascas**: Vuelve a los ejemplos de esta guía
8. **Gestiona el tiempo**: Divide el examen en bloques (30% Model, 30% Service, 40% Controller+Vista)
9. **Revisa al final**: Ejecuta `./mvnw clean test` antes de entregar
10. **Mantén la calma**: Sabes más de lo que crees 💪

---

## ✅ RESUMEN ULTRA-RÁPIDO (5 minutos antes del examen)

```
TDD: Test → Falla → Código → Pasa → Commit

Entidad:
@Entity @Table @Id @GeneratedValue @ManyToMany @OneToMany

Repository:
extends CrudRepository<Entity, Long>

Service:
@Service @Transactional @Autowired ModelMapper

Controller:
@Controller @GetMapping @PostMapping @DeleteMapping
model.addAttribute() return "vista"

Vista:
th:text th:href th:each th:if
fetch(url, {method: 'DELETE'})

Git:
git add . → git commit -m "..." → git push

Maven:
./mvnw test -Dtest=TestClass
./mvnw spring-boot:run
```

---

**¡Mucha suerte en el examen! 🍀**

Si sigues esta guía y practicas los ejemplos, sacarás un 10 seguro. Recuerda: TDD es disciplina, no magia. Test primero, siempre. 🚀
