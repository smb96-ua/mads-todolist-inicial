# 📚 Guía de Estudio Completa - Examen Práctico MADS

## 🎯 Información Crucial del Examen

### Formato del Examen
- **Duración**: 2 horas
- **Primeros 10 minutos**: Configuración inicial (con internet)
- **1h 40m**: Desarrollo sin internet
- **Últimos 10 minutos**: Entrega (con internet)

### ⚠️ Normas Importantes
- ✅ Llegar puntual (10 min setup críticos)
- ❌ NO móviles, NO smartwatch, NO internet durante desarrollo
- ❌ NO apuntes físicos ni digitales
- ❌ NO instalar extensiones/plugins durante el examen
- ✅ Solo usar el IDE y sus sugerencias (sin internet)
- ✅ Entregar: Push GitHub + archivo ZIP en Moodle

### Metodología de Trabajo
1. **Crear UN SOLO ISSUE en GitHub** (primeros 10 min)
2. **Crear una rama local** para trabajar
3. **Commits locales pequeños** durante el desarrollo
4. **Pull Request final** en los últimos 10 minutos

---

## 📋 ¿Qué Esperar en el Examen?

### Lo Que Te Van a Dar
- Proyecto inicial Spring Boot (similar a mads-todolist-inicial)
- Enunciado en papel con funcionalidades a implementar
- Tests existentes que deben seguir pasando

### Lo Que Tienes Que Hacer
- **Añadir funcionalidad NUEVA** sobre código existente
- **Trabajar en TODAS las capas**: Repository → Service → Controller → Vista
- **Escribir tests** para cada capa (excepto vista)
- **Tests deben pasar** y probar funcionalidad real
- **Commits descriptivos** pequeños y frecuentes

---

## 🏗️ Arquitectura Spring Boot - Lo Esencial

### Capas de la Aplicación (de abajo hacia arriba)

```
┌─────────────────────────────────────┐
│         VISTA (Thymeleaf)           │  ← Usuario ve esto
├─────────────────────────────────────┤
│         CONTROLLER (@Controller)     │  ← Procesa HTTP requests
├─────────────────────────────────────┤
│         SERVICE (@Service)           │  ← Lógica de negocio
├─────────────────────────────────────┤
│         REPOSITORY (JPA)             │  ← Acceso a base de datos
├─────────────────────────────────────┤
│         MODEL (@Entity)              │  ← Entidades JPA
├─────────────────────────────────────┤
│         BASE DE DATOS (H2)           │  ← Datos persistentes
└─────────────────────────────────────┘
```

---

## 🔧 Patrón TDD - Test Driven Development

### Ciclo Red-Green-Refactor

```
1️⃣ RED: Escribir test que falla
    ↓
2️⃣ GREEN: Implementar código mínimo para que pase
    ↓
3️⃣ REFACTOR: Mejorar código (opcional)
    ↓
4️⃣ COMMIT: Guardar cambios
```

### Ejemplo Práctico Completo

Supongamos que te piden: **"Añadir la capacidad de marcar tareas como urgentes"**

---

## 📝 PASO A PASO: Añadir Funcionalidad Completa

### PASO 1: Modificar el Modelo (Entity)

```java
// src/main/java/madstodolist/model/Tarea.java

@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String titulo;
    
    // NUEVO: Añadir campo urgente
    @Column(name = "urgente")
    private Boolean urgente = false;  // Por defecto no urgente
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
    
    // Constructor vacío (requerido por JPA)
    public Tarea() {}
    
    public Tarea(String titulo) {
        this.titulo = titulo;
        this.urgente = false;
    }
    
    // Getters y Setters
    public Boolean getUrgente() {
        return urgente;
    }
    
    public void setUrgente(Boolean urgente) {
        this.urgente = urgente;
    }
}
```

**💡 Conceptos Clave:**
- `@Entity`: Indica que es una entidad JPA
- `@Table`: Nombre de la tabla en BD
- `@Column`: Mapea el campo a una columna
- `@NotNull`: Validación de que no puede ser null
- `Boolean` (objeto) permite null, `boolean` (primitivo) no

---

### PASO 2: Tests del Repository

```java
// src/test/java/madstodolist/repository/TareaTest.java

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class TareaTest {
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Test
    @Transactional
    public void crearTareaUrgente() {
        // ARRANGE: Preparar datos
        Usuario usuario = new Usuario("user@test.com");
        usuarioRepository.save(usuario);
        
        Tarea tarea = new Tarea("Tarea urgente");
        tarea.setUsuario(usuario);
        tarea.setUrgente(true);
        
        // ACT: Ejecutar acción
        tareaRepository.save(tarea);
        
        // ASSERT: Verificar resultado
        Tarea tareaRecuperada = tareaRepository.findById(tarea.getId()).orElse(null);
        assertThat(tareaRecuperada).isNotNull();
        assertThat(tareaRecuperada.getUrgente()).isTrue();
    }
    
    @Test
    @Transactional
    public void tareaNoUrgenteporDefecto() {
        Usuario usuario = new Usuario("user@test.com");
        usuarioRepository.save(usuario);
        
        Tarea tarea = new Tarea("Tarea normal");
        tarea.setUsuario(usuario);
        tareaRepository.save(tarea);
        
        Tarea tareaRecuperada = tareaRepository.findById(tarea.getId()).orElse(null);
        assertThat(tareaRecuperada.getUrgente()).isFalse();
    }
}
```

**💡 Conceptos Clave:**
- `@SpringBootTest`: Carga contexto Spring completo
- `@Sql`: Ejecuta script SQL antes del test (limpia BD)
- `@Transactional`: Hace rollback después del test
- `@Autowired`: Inyección de dependencias
- **Patrón AAA**: Arrange-Act-Assert

---

### PASO 3: DTO (Data Transfer Object)

```java
// src/main/java/madstodolist/dto/TareaData.java

public class TareaData {
    private Long id;
    private String titulo;
    private Boolean urgente;  // NUEVO
    
    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    
    public Boolean getUrgente() { return urgente; }
    public void setUrgente(Boolean urgente) { this.urgente = urgente; }
}
```

**💡 ¿Por qué DTOs?**
- Separan modelo de base de datos de lo que ve el usuario
- Evitan exponer estructura interna de la BD
- Permiten transformar datos antes de mostrarlos

---

### PASO 4: Service Layer

```java
// src/main/java/madstodolist/service/TareaService.java

@Service
public class TareaService {
    
    @Autowired
    private TareaRepository tareaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    // NUEVO: Marcar tarea como urgente
    @Transactional
    public TareaData marcarUrgenteONoUrgente(Long idTarea, Boolean urgente) {
        Tarea tarea = tareaRepository.findById(idTarea).orElse(null);
        if (tarea == null) {
            throw new TareaServiceException("No existe tarea con id " + idTarea);
        }
        tarea.setUrgente(urgente);
        tareaRepository.save(tarea);
        return modelMapper.map(tarea, TareaData.class);
    }
    
    // NUEVO: Obtener tareas urgentes de un usuario
    @Transactional(readOnly = true)
    public List<TareaData> allTareasUrgenteUsuario(Long idUsuario) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);
        if (usuario == null) {
            throw new UsuarioServiceException("No existe usuario con id " + idUsuario);
        }
        
        List<Tarea> tareas = tareaRepository.findByUsuarioId(idUsuario);
        return tareas.stream()
                .filter(t -> t.getUrgente() != null && t.getUrgente())
                .map(t -> modelMapper.map(t, TareaData.class))
                .collect(Collectors.toList());
    }
}
```

**💡 Conceptos Clave:**
- `@Service`: Marca clase como servicio Spring
- `@Transactional`: Gestión automática de transacciones
- `readOnly = true`: Optimización para consultas
- `ModelMapper`: Convierte Entity ↔ DTO automáticamente
- `stream()`: API funcional de Java para colecciones

---

### PASO 5: Tests del Service

```java
// src/test/java/madstodolist/service/TareaServiceTest.java

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class TareaServiceTest {
    
    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Test
    public void marcarTareaComoUrgente() {
        // Crear usuario
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@test.com");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        // Crear tarea
        TareaData tarea = tareaService.nuevaTareaUsuario(usuario.getId(), "Tarea test");
        assertThat(tarea.getUrgente()).isFalse();
        
        // Marcar como urgente
        TareaData tareaUrgente = tareaService.marcarUrgenteONoUrgente(tarea.getId(), true);
        
        // Verificar
        assertThat(tareaUrgente.getUrgente()).isTrue();
    }
    
    @Test
    public void obtenerSoloTareasUrgentes() {
        // Crear usuario
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@test.com");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        // Crear tareas (2 urgentes, 1 normal)
        TareaData tarea1 = tareaService.nuevaTareaUsuario(usuario.getId(), "Urgente 1");
        tareaService.marcarUrgenteONoUrgente(tarea1.getId(), true);
        
        TareaData tarea2 = tareaService.nuevaTareaUsuario(usuario.getId(), "Normal");
        
        TareaData tarea3 = tareaService.nuevaTareaUsuario(usuario.getId(), "Urgente 2");
        tareaService.marcarUrgenteONoUrgente(tarea3.getId(), true);
        
        // Obtener solo urgentes
        List<TareaData> urgentes = tareaService.allTareasUrgenteUsuario(usuario.getId());
        
        // Verificar
        assertThat(urgentes).hasSize(2);
        assertThat(urgentes.stream().allMatch(t -> t.getUrgente())).isTrue();
    }
}
```

---

### PASO 6: Controller

```java
// src/main/java/madstodolist/controller/TareaController.java

@Controller
public class TareaController {
    
    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Autowired
    private ManagerUserSession managerUserSession;
    
    // NUEVO: Marcar tarea como urgente
    @PostMapping("/tareas/{id}/marcar-urgente")
    public String marcarUrgenteONoUrgente(@PathVariable(value="id") Long idTarea,
                                          @RequestParam(value="urgente") Boolean urgente,
                                          HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }
        
        tareaService.marcarUrgenteONoUrgente(idTarea, urgente);
        return "redirect:/usuarios/" + idUsuarioLogeado + "/tareas";
    }
    
    // NUEVO: Ver solo tareas urgentes
    @GetMapping("/usuarios/{id}/tareas/urgentes")
    public String listadoTareasUrgentes(@PathVariable(value="id") Long idUsuario,
                                        Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }
        
        if (!idUsuario.equals(idUsuarioLogeado)) {
            throw new UsuarioNoLogeadoException();
        }
        
        UsuarioData usuario = usuarioService.findById(idUsuario);
        List<TareaData> tareas = tareaService.allTareasUrgenteUsuario(idUsuario);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        model.addAttribute("soloUrgentes", true);
        
        return "listaTareas";
    }
}
```

**💡 Conceptos Clave:**
- `@Controller`: Marca clase como controlador Spring MVC
- `@GetMapping`: Maneja peticiones HTTP GET
- `@PostMapping`: Maneja peticiones HTTP POST
- `@PathVariable`: Extrae variable de la URL
- `@RequestParam`: Extrae parámetro de query string
- `Model`: Para pasar datos a la vista
- `HttpSession`: Para manejar sesión del usuario

---

### PASO 7: Vista (Thymeleaf)

```html
<!-- src/main/resources/templates/listaTareas.html -->

<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">

<head th:replace="fragments :: head (titulo='Tareas')"></head>

<body>
<div th:replace="fragments :: navbar (usuario=${usuario})"></div>

<div class="container-fluid">
    
    <div class="row mt-3">
        <div class="col">
            <h2 th:text="'Listado de tareas de ' + ${usuario.nombre}"></h2>
        </div>
    </div>
    
    <!-- NUEVO: Filtros -->
    <div class="row mt-3">
        <div class="col">
            <a class="btn btn-primary" th:href="@{'/usuarios/' + ${usuario.id} + '/tareas'}">
                Todas las tareas
            </a>
            <a class="btn btn-warning" th:href="@{'/usuarios/' + ${usuario.id} + '/tareas/urgentes'}">
                Solo urgentes
            </a>
            <a class="btn btn-success" th:href="@{'/usuarios/' + ${usuario.id} + '/tareas/nueva'}">
                Nueva tarea
            </a>
        </div>
    </div>
    
    <table class="table table-striped mt-3">
        <thead>
        <tr>
            <th>Id</th>
            <th>Tarea</th>
            <th>Urgente</th> <!-- NUEVO -->
            <th>Acción</th>
        </tr>
        </thead>
        <tbody>
        <tr th:each="tarea: ${tareas}">
            <td th:text="${tarea.id}"></td>
            <td th:text="${tarea.titulo}"></td>
            
            <!-- NUEVO: Mostrar si es urgente -->
            <td>
                <span th:if="${tarea.urgente}" class="badge badge-danger">URGENTE</span>
                <span th:unless="${tarea.urgente}" class="badge badge-secondary">Normal</span>
            </td>
            
            <td>
                <a class="btn btn-primary btn-sm" 
                   th:href="@{/tareas/{id}/editar(id=${tarea.id})}">Editar</a>
                
                <!-- NUEVO: Botón marcar urgente -->
                <form th:if="${!tarea.urgente}" method="post" 
                      th:action="@{/tareas/{id}/marcar-urgente(id=${tarea.id})}" 
                      style="display: inline;">
                    <input type="hidden" name="urgente" value="true">
                    <button type="submit" class="btn btn-warning btn-sm">⚠️ Marcar urgente</button>
                </form>
                
                <!-- NUEVO: Botón quitar urgente -->
                <form th:if="${tarea.urgente}" method="post" 
                      th:action="@{/tareas/{id}/marcar-urgente(id=${tarea.id})}" 
                      style="display: inline;">
                    <input type="hidden" name="urgente" value="false">
                    <button type="submit" class="btn btn-secondary btn-sm">✓ Quitar urgente</button>
                </form>
                
                <a class="btn btn-danger btn-sm" 
                   th:href="@{/tareas/{id}(id=${tarea.id})}"
                   onclick="event.preventDefault(); del(this.href);">Borrar</a>
            </td>
        </tr>
        </tbody>
    </table>
</div>

<script th:inline="javascript">
    function del(urlBorrar) {
        if (confirm('¿Estás seguro de borrar esta tarea?')) {
            fetch(urlBorrar, {
                method: 'DELETE'
            }).then(() => location.reload());
        }
    }
</script>

<div th:replace="fragments::javascript"/>

</body>
</html>
```

**💡 Conceptos Clave Thymeleaf:**
- `th:text`: Renderiza texto escapado
- `th:href`: Genera URL con `@{...}`
- `th:if` / `th:unless`: Condicionales
- `th:each`: Bucle sobre colección
- `${variable}`: Acceso a variable del Model
- `@{/ruta}`: Genera URL correcta con context path

---

## 🧪 Tests del Controller (con MockMvc)

```java
// src/test/java/madstodolist/controller/TareaWebTest.java

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql")
public class TareaWebTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Autowired
    private TareaService tareaService;
    
    @Autowired
    private UsuarioService usuarioService;
    
    @Test
    public void marcarTareaComoUrgenteDesdeWeb() throws Exception {
        // Crear usuario y tarea
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@test.com");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        TareaData tarea = tareaService.nuevaTareaUsuario(usuario.getId(), "Test");
        
        // Marcar como urgente mediante POST
        this.mockMvc.perform(post("/tareas/{id}/marcar-urgente", tarea.getId())
                        .param("urgente", "true")
                        .sessionAttr("idUsuarioLogeado", usuario.getId()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/" + usuario.getId() + "/tareas"));
        
        // Verificar que se marcó
        TareaData tareaActualizada = tareaService.findById(tarea.getId());
        assertThat(tareaActualizada.getUrgente()).isTrue();
    }
    
    @Test
    public void verSoloTareasUrgentes() throws Exception {
        // Crear usuario
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@test.com");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        // Crear tareas
        TareaData urgente = tareaService.nuevaTareaUsuario(usuario.getId(), "Urgente");
        tareaService.marcarUrgenteONoUrgente(urgente.getId(), true);
        
        TareaData normal = tareaService.nuevaTareaUsuario(usuario.getId(), "Normal");
        
        // Ver solo urgentes
        this.mockMvc.perform(get("/usuarios/{id}/tareas/urgentes", usuario.getId())
                        .sessionAttr("idUsuarioLogeado", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Urgente")))
                .andExpect(content().string(not(containsString("Normal"))));
    }
}
```

**💡 Conceptos Clave MockMvc:**
- `mockMvc.perform()`: Simula petición HTTP
- `.param()`: Añade parámetro a la petición
- `.sessionAttr()`: Simula atributo en sesión
- `.andExpect()`: Verifica el resultado
- `status()`: Verifica código HTTP
- `content().string()`: Verifica contenido HTML

---

## 📊 Commits Durante el Examen

### Estructura de Commits Recomendada

```bash
# 1. Test del modelo
git add src/main/java/madstodolist/model/Tarea.java
git add src/test/java/madstodolist/repository/TareaTest.java
git commit -m "TDD: Test y modelo para tarea urgente"

# 2. Test del servicio
git add src/test/java/madstodolist/service/TareaServiceTest.java
git commit -m "TDD: Test servicio marcar tarea urgente"

# 3. Implementación del servicio
git add src/main/java/madstodolist/service/TareaService.java
git add src/main/java/madstodolist/dto/TareaData.java
git commit -m "TDD: Implementación servicio marcar urgente"

# 4. Test del controller
git add src/test/java/madstodolist/controller/TareaWebTest.java
git commit -m "TDD: Test controller para tarea urgente"

# 5. Implementación controller
git add src/main/java/madstodolist/controller/TareaController.java
git commit -m "TDD: Controller para marcar tarea urgente"

# 6. Vista
git add src/main/resources/templates/listaTareas.html
git commit -m "Vista: Añadir botones y filtro tareas urgentes"

# 7. Ejecutar todos los tests
./mvnw test
git commit -m "Tests: Todos los tests pasan (72 tests OK)"
```

---

## 🎓 Conceptos Teóricos Importantes

### 1. JPA (Java Persistence API)

**Anotaciones esenciales:**
```java
@Entity              // Marca clase como entidad de BD
@Table(name = "x")   // Nombre de tabla en BD
@Id                  // Clave primaria
@GeneratedValue      // Autoincremental
@Column              // Mapeo de columna
@NotNull             // No puede ser null
@ManyToOne           // Relación muchos a uno
@OneToMany           // Relación uno a muchos
@ManyToMany          // Relación muchos a muchos
@JoinColumn          // Columna de join
@JoinTable           // Tabla intermedia
```

### 2. Spring Annotations

```java
@SpringBootApplication  // Clase principal Spring Boot
@Service               // Capa de servicio
@Controller            // Controlador web MVC
@Repository            // Acceso a datos (interfaces JPA)
@Component             // Componente genérico Spring
@Autowired             // Inyección de dependencias
@Transactional         // Transacción automática
@GetMapping            // HTTP GET
@PostMapping           // HTTP POST
@DeleteMapping         // HTTP DELETE
@PathVariable          // Variable en URL
@RequestParam          // Parámetro query string
```

### 3. Testing Annotations

```java
@SpringBootTest              // Test con contexto Spring completo
@Test                        // Marca método como test
@Autowired                   // Inyectar dependencias en tests
@Sql(scripts = "...")        // Ejecutar SQL antes del test
@Transactional              // Rollback automático después del test
@AutoConfigureMockMvc       // Configurar MockMvc
```

### 4. AssertJ (Assertions)

```java
assertThat(valor).isNotNull();
assertThat(valor).isNull();
assertThat(valor).isTrue();
assertThat(valor).isFalse();
assertThat(valor).isEqualTo(otro);
assertThat(lista).hasSize(3);
assertThat(lista).contains(elemento);
assertThat(lista).isEmpty();
assertThat(string).containsString("texto");
```

---

## ⚡ Comandos Maven Esenciales

```bash
# Compilar proyecto
./mvnw compile

# Ejecutar todos los tests
./mvnw test

# Ejecutar un test específico
./mvnw test -Dtest=TareaServiceTest

# Ejecutar un método de test específico
./mvnw test -Dtest=TareaServiceTest#marcarTareaComoUrgente

# Limpiar y compilar
./mvnw clean compile

# Ejecutar aplicación
./mvnw spring-boot:run

# Ver info de dependencias
./mvnw dependency:tree
```

---

## 🚨 Errores Comunes y Soluciones

### Error 1: "Cannot find symbol"
```
❌ ERROR: cannot find symbol: class TareaData
✅ SOLUCIÓN: 
   - Verificar imports
   - Compilar primero: ./mvnw compile
   - Verificar que la clase existe en el paquete correcto
```

### Error 2: "NullPointerException"
```
❌ ERROR: java.lang.NullPointerException
✅ SOLUCIÓN:
   - Verificar @Autowired está presente
   - Verificar que el test tiene @SpringBootTest
   - Usar .orElse(null) en Optional
   - Comprobar que el objeto no es null antes de usarlo
```

### Error 3: "Test fails - Database not cleaned"
```
❌ ERROR: Expected size: 1 but was: 3
✅ SOLUCIÓN:
   - Añadir @Sql(scripts = "/clean-db.sql") al test
   - O usar @Transactional para rollback automático
```

### Error 4: "Redirect not followed"
```
❌ ERROR: Expected status 200 but was 302
✅ SOLUCIÓN:
   - Es correcto! 302 = redirect
   - Usar .andExpect(status().is3xxRedirection())
   - Verificar URL con .andExpect(redirectedUrl("/ruta"))
```

### Error 5: "Template not found"
```
❌ ERROR: Error resolving template "listaTareas"
✅ SOLUCIÓN:
   - Verificar que el archivo está en src/main/resources/templates/
   - Verificar nombre exacto (sin .html en el controller)
   - Verificar sintaxis Thymeleaf en la plantilla
```

---

## 📝 Checklist Pre-Examen

### 3 Días Antes
- [ ] Repasar estructura del proyecto mads-todolist-inicial
- [ ] Repasar todos los tests de la Práctica 3
- [ ] Ejecutar `./mvnw test` y entender cada test
- [ ] Repasar anotaciones JPA y Spring
- [ ] Practicar crear una entidad nueva desde cero

### 1 Día Antes
- [ ] Configurar Git (nombre, email)
- [ ] Verificar que IntelliJ/VSCode funciona correctamente
- [ ] Practicar clonar repositorio y ejecutar tests
- [ ] Repasar comandos Maven
- [ ] Repasar estructura de controlador y vista

### Día del Examen
- [ ] Llegar 15 minutos antes
- [ ] Traer enunciado en papel para entregar
- [ ] **NO traer** móvil, smartwatch, apuntes
- [ ] Tener GitHub configurado (usuario/contraseña o token)
- [ ] Café ☕ (opcional pero recomendado)

---

## 🎯 Estrategia para el Examen

### Primeros 10 Minutos (CON internet)
1. **Clonar repositorio** proporcionado
2. **Ejecutar `./mvnw test`** para verificar que funciona
3. **Ejecutar aplicación** (`./mvnw spring-boot:run`)
4. **Crear ISSUE en GitHub** con descripción de la funcionalidad
5. **Crear rama local**: `git checkout -b practica-examen`
6. **Leer enunciado** completo y planificar

### Durante Desarrollo (SIN internet - 1h 40m)
1. **No entres en pánico** si algo no funciona inmediatamente
2. **Trabaja en orden**: Model → Repository → Service → Controller → Vista
3. **Haz commits pequeños** cada vez que pases un test
4. **Si te atascas**, pasa a otra parte y vuelve después
5. **Prioriza**: Mejor tener algo funcionando parcialmente que nada completo
6. **Tests primero**: Escribe test → implementa → commit
7. **Ejecuta `./mvnw test`** frecuentemente

### Últimos 10 Minutos (CON internet)
1. **Git push** de tu rama: `git push origin practica-examen`
2. **Crear Pull Request** a main en GitHub
3. **Merge Pull Request** (integrar en main)
4. **Comprimir proyecto**:
   ```bash
   cd ..
   zip -r practica-examen.zip mads-todolist-inicial/ -x "*/target/*" "*/node_modules/*"
   ```
5. **Subir ZIP a Moodle**
6. **Entregar enunciado en papel** con nombre y usuario

---

## 💪 Ejercicios de Práctica

### Ejercicio 1: Añadir "Fecha de Vencimiento" a Tareas
**Objetivo**: Practicar todo el flujo completo

1. Añadir campo `fechaVencimiento` (tipo `Date`) a `Tarea`
2. Modificar DTO `TareaData`
3. Añadir método `findTareasVencidas(Long usuarioId)` en Service
4. Crear endpoint `/usuarios/{id}/tareas/vencidas` en Controller
5. Modificar vista para mostrar fecha
6. Escribir tests para cada capa

### Ejercicio 2: Añadir "Categorías" a Tareas
**Objetivo**: Practicar relaciones ManyToOne

1. Crear entidad `Categoria` con campos `id`, `nombre`, `color`
2. Añadir relación `@ManyToOne` en `Tarea` → `Categoria`
3. Crear `CategoriaRepository`
4. Añadir métodos en `TareaService` para asignar categoría
5. Crear vista para gestionar categorías
6. Tests completos

### Ejercicio 3: Añadir "Comentarios" a Tareas
**Objetivo**: Practicar relaciones OneToMany

1. Crear entidad `Comentario` con `id`, `texto`, `fecha`
2. Relación `@OneToMany` en `Tarea` → `Comentario`
3. Repositorio, Service, Controller para comentarios
4. Vista para añadir/ver comentarios
5. Tests de la funcionalidad completa

---

## 🔥 Consejos de Oro

### Durante el Desarrollo
1. **Lee el enunciado 2 veces** antes de empezar
2. **No optimices prematuramente**: Primero haz que funcione, luego mejora
3. **Nombres descriptivos**: `marcarTareaComoUrgente()` mejor que `cambiar()`
4. **Si compilaerror, lee el mensaje**: Maven te dice exactamente qué falta
5. **Un test a la vez**: No escribas 5 tests y luego implementes

### En Caso de Bloqueo
1. **Respira hondo** y lee el error con calma
2. **Divide el problema**: ¿Qué capa está fallando?
3. **Simplifica**: ¿Puedes hacer una versión más simple primero?
4. **Salta temporalmente**: Avanza en otra parte y vuelve
5. **No borres código que funciona**: Comenta en lugar de borrar

### Gestión del Tiempo
- **20%** tiempo: Leer y planificar
- **40%** tiempo: Model, Repository, Service + Tests
- **30%** tiempo: Controller, Vista
- **10%** tiempo: Revisar, corregir, pulir

---

## 📚 Recursos de Referencia Rápida

### Sintaxis Thymeleaf Esencial
```html
<!-- Variables -->
<span th:text="${variable}"></span>
<span th:text="${objeto.propiedad}"></span>

<!-- Condicionales -->
<div th:if="${condicion}">Mostrar si true</div>
<div th:unless="${condicion}">Mostrar si false</div>

<!-- Bucles -->
<tr th:each="item : ${lista}">
    <td th:text="${item.nombre}"></td>
</tr>

<!-- URLs -->
<a th:href="@{/ruta}">Link</a>
<a th:href="@{/ruta/{id}(id=${variable})}">Link con variable</a>
<a th:href="@{/ruta(param=${valor})}">Link con parámetro</a>

<!-- Formularios -->
<form th:action="@{/ruta}" method="post">
    <input type="text" name="campo" th:value="${valor}">
    <button type="submit">Enviar</button>
</form>

<!-- Clases CSS condicionales -->
<span th:class="${urgente} ? 'badge-danger' : 'badge-secondary'"></span>
```

### Métodos Útiles de Collections
```java
// Stream API
lista.stream()
    .filter(x -> x.getUrgente())         // Filtrar
    .map(x -> x.getTitulo())             // Transformar
    .collect(Collectors.toList());       // Recolectar

// Operaciones básicas
lista.add(elemento);
lista.remove(elemento);
lista.contains(elemento);
lista.size();
lista.isEmpty();
```

### Optional Pattern
```java
// Obtener o null
Tarea tarea = repository.findById(id).orElse(null);

// Obtener o lanzar excepción
Tarea tarea = repository.findById(id)
    .orElseThrow(() -> new TareaServiceException("No existe"));

// Verificar si existe
if (repository.findById(id).isPresent()) {
    // Existe
}
```

---

## 🏁 Resumen Final: Los 10 Mandamientos del Examen

1. **SÉ PUNTUAL**: Los 10 minutos iniciales son cruciales
2. **LEE TODO**: Enunciado completo antes de escribir código
3. **TDD SIEMPRE**: Test primero, implementación después
4. **COMMITS PEQUEÑOS**: Cada test que pasa = commit
5. **NOMBRES CLAROS**: Métodos y variables descriptivos
6. **TESTS REALES**: No tests tontos, prueban funcionalidad real
7. **TODAS LAS CAPAS**: Model → Repository → Service → Controller → Vista
8. **EJECUTA TESTS**: `./mvnw test` frecuentemente
9. **NO TE BLOQUEES**: Simplifica o salta temporalmente
10. **ENTREGA TODO**: GitHub Push + ZIP Moodle + Papel

---

## ✅ Autoevaluación: ¿Estás Preparado?

Responde SÍ/NO. Necesitas al menos 8 SÍ para estar preparado.

- [ ] ¿Puedes crear una entidad JPA desde cero?
- [ ] ¿Sabes escribir un test de repository?
- [ ] ¿Puedes implementar un método en Service?
- [ ] ¿Sabes escribir tests de Service?
- [ ] ¿Puedes crear un endpoint en Controller?
- [ ] ¿Sabes modificar una vista Thymeleaf?
- [ ] ¿Conoces los comandos Git básicos?
- [ ] ¿Puedes ejecutar `./mvnw test` e interpretar errores?
- [ ] ¿Entiendes qué hace `@Transactional`?
- [ ] ¿Puedes crear y hacer merge de un Pull Request?

---

## 🎓 Última Palabra

**Recuerda**: El profesor no busca código perfecto, busca:
- ✅ Que **funcione** lo que pides
- ✅ Que los **tests pasen**
- ✅ Que los tests **prueben funcionalidad real**
- ✅ Que el código tenga **sentido**
- ✅ Que esté en **todas las capas**

**No necesitas**:
- ❌ La solución más elegante
- ❌ Patrones de diseño complejos
- ❌ Optimizaciones prematuras
- ❌ Código perfecto sin bugs

**Prioridad**: 
1. **Funciona** → 2. **Tests pasan** → 3. **Código limpio**

---

## 📞 El Día del Examen

**Antes de empezar:**
- Respira hondo
- Lee el enunciado completo
- Haz un plan mental rápido
- Empieza por lo que te resulte más fácil

**Durante:**
- Mantén la calma
- Si te bloqueas, pasa a otra cosa
- Haz commits frecuentes
- No te compares con otros

**Al finalizar:**
- Verifica que `./mvnw test` pasa
- Push a GitHub
- ZIP a Moodle
- Entregar papel
- **RESPIRA**: Ya está hecho ✅

---

¡MUCHA SUERTE! 🍀 

Recuerda: Has practicado todo esto en la Práctica 3. 
**TÚ PUEDES HACERLO** 💪

---

**Última actualización**: 22 de octubre de 2025
**Autor**: Guía basada en Práctica 3 MADS 2024-2025
