# 📚 GUÍA DE ESTUDIO COMPLETA - PRÁCTICA 2 MADS
## Para Examen Práctico - TodoList Spring Boot

### 🎯 **OBJETIVO DEL EXAMEN**
Demostrar dominio completo de Spring Boot, JPA/Hibernate, Thymeleaf, testing y arquitectura MVC implementados en la práctica.

---

## 📋 **1. ARQUITECTURA Y TECNOLOGÍAS**

### **Stack Tecnológico**
- **Spring Boot 2.7.14**: Framework principal
- **JPA/Hibernate**: ORM para persistencia
- **H2 Database**: Base de datos en memoria
- **Thymeleaf**: Motor de plantillas
- **Bootstrap 4.6**: Framework CSS
- **Maven**: Gestión de dependencias
- **JUnit 5**: Framework de testing

### **Patrón Arquitectónico: MVC**
```
Controller (TareaController, LoginController, HomeController)
    ↓
Service (TareaService, UsuarioService)
    ↓
Repository (TareaRepository, UsuarioRepository)
    ↓
Model/Entity (Tarea, Usuario)
```

---

## 🗄️ **2. MODELO DE DATOS Y JPA**

### **Entidad Usuario**
```java
@Entity
@Table(name = "usuarios")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String email;
    
    private String nombre;
    private String password;
    private Date fechaNacimiento;
    
    // NUEVOS CAMPOS PRÁCTICA 2
    @Column(name = "is_admin")
    private Boolean isAdmin = false;
    
    private Boolean bloqueado = false;
    
    @OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
    private Set<Tarea> tareas = new HashSet<>();
}
```

### **Entidad Tarea**
```java
@Entity
@Table(name = "tareas")
public class Tarea {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    private String titulo;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "usuario_id")
    private Usuario usuario;
}
```

### **Relaciones JPA Críticas**
- **OneToMany**: Un Usuario tiene muchas Tareas
- **ManyToOne**: Muchas Tareas pertenecen a un Usuario
- **FetchType.LAZY**: Optimización de carga
- **Cascadas**: No utilizadas para evitar eliminaciones accidentales

---

## 🔧 **3. CAPA DE SERVICIO (BUSINESS LOGIC)**

### **UsuarioService - Métodos Clave**

#### **Gestión de Administradores**
```java
public boolean existeAdministrador() {
    return usuarioRepository.existsByIsAdminTrue();
}

public UsuarioData registrarConAdmin(RegistroData registroData, boolean esAdmin) {
    Usuario usuario = new Usuario(registroData.getEmail());
    usuario.setNombre(registroData.getNombre());
    usuario.setPassword(registroData.getPassword());
    usuario.setFechaNacimiento(registroData.getFechaNacimiento());
    usuario.setIsAdmin(esAdmin);
    usuario.setBloqueado(false);
    
    Usuario usuarioGuardado = usuarioRepository.save(usuario);
    return modelMapper.map(usuarioGuardado, UsuarioData.class);
}
```

#### **Sistema de Bloqueo**
```java
public void bloquearUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new UsuarioServiceException("Usuario no encontrado"));
    
    if (usuario.getIsAdmin()) {
        throw new UsuarioServiceException("No se puede bloquear a un administrador");
    }
    
    usuario.setBloqueado(true);
    usuarioRepository.save(usuario);
}

public void desbloquearUsuario(Long id) {
    Usuario usuario = usuarioRepository.findById(id)
        .orElseThrow(() -> new UsuarioServiceException("Usuario no encontrado"));
    
    usuario.setBloqueado(false);
    usuarioRepository.save(usuario);
}
```

#### **Login con Validaciones**
```java
public enum LOGIN_STATUS {LOGIN_OK, USER_NOT_FOUND, ERROR_PASSWORD, USER_BLOCKED}

public LoginStatus login(String email, String password) {
    Optional<Usuario> usuario = usuarioRepository.findByEmail(email);
    if (!usuario.isPresent()) {
        return LOGIN_STATUS.USER_NOT_FOUND;
    } else if (!usuario.get().getPassword().equals(password)) {
        return LOGIN_STATUS.ERROR_PASSWORD;
    } else if (usuario.get().getBloqueado()) {
        return LOGIN_STATUS.USER_BLOCKED;
    } else {
        return LOGIN_STATUS.LOGIN_OK;
    }
}
```

### **TareaService - CRUD Completo**
```java
public TareaData nuevaTarea(Long usuarioId, String titulo) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new TareaServiceException("Usuario " + usuarioId + " no existe"));
    
    Tarea tarea = new Tarea(usuario, titulo);
    Tarea tareaGuardada = tareaRepository.save(tarea);
    return modelMapper.map(tareaGuardada, TareaData.class);
}

public TareaData modificaTarea(Long idTarea, String nuevoTitulo) {
    Tarea tarea = tareaRepository.findById(idTarea)
        .orElseThrow(() -> new TareaNotFoundException("No existe tarea con id " + idTarea));
    
    tarea.setTitulo(nuevoTitulo);
    Tarea tareaModificada = tareaRepository.save(tarea);
    return modelMapper.map(tareaModificada, TareaData.class);
}
```

---

## 🎮 **4. CONTROLADORES Y ENDPOINTS**

### **LoginController - Autenticación**
```java
@Controller
public class LoginController {
    
    @GetMapping("/")
    public String home(Model model, HttpSession session) {
        return "redirect:/login";
    }
    
    @PostMapping("/login")
    public String loginUsuario(@Valid LoginData loginData, 
                               BindingResult result, 
                               Model model, 
                               HttpSession session) {
        
        if (result.hasErrors()) {
            return "formLogin";
        }
        
        UsuarioService.LoginStatus loginStatus = usuarioService.login(
            loginData.geteMail(), loginData.getPassword());
            
        if (loginStatus == UsuarioService.LoginStatus.LOGIN_OK) {
            UsuarioData usuario = usuarioService.findByEmail(loginData.geteMail());
            managerUserSession.logearUsuario(usuario.getId());
            
            if (usuario.getIsAdmin()) {
                return "redirect:/registrados";
            } else {
                return "redirect:/usuarios/" + usuario.getId() + "/tareas";
            }
        } else if (loginStatus == UsuarioService.LoginStatus.USER_BLOCKED) {
            model.addAttribute("error", "Usuario bloqueado. Contacte al administrador.");
            return "formLogin";
        }
        // ... otros casos
    }
}
```

### **HomeController - Administración**
```java
@Controller
public class HomeController {
    
    private void verificarAccesoAdministrador(Long usuarioId) {
        UsuarioData usuario = usuarioService.findById(usuarioId);
        if (!usuario.getIsAdmin()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, 
                "Acceso denegado: se requieren privilegios de administrador");
        }
    }
    
    @GetMapping("/registrados")
    public String listaUsuarios(Model model, HttpSession session) {
        Long usuarioId = managerUserSession.usuarioLogeado();
        if (usuarioId == null) {
            return "redirect:/login";
        }
        
        verificarAccesoAdministrador(usuarioId);
        
        List<UsuarioData> usuarios = usuarioService.findAll();
        UsuarioData usuarioActual = usuarioService.findById(usuarioId);
        
        model.addAttribute("usuarios", usuarios);
        model.addAttribute("usuario", usuarioActual);
        return "listaUsuarios";
    }
    
    @PostMapping("/registrados/{id}/bloquear")
    public String bloquearUsuario(@PathVariable Long id, 
                                  HttpSession session, 
                                  RedirectAttributes redirectAttributes) {
        Long usuarioId = managerUserSession.usuarioLogeado();
        if (usuarioId == null) {
            return "redirect:/login";
        }
        
        verificarAccesoAdministrador(usuarioId);
        
        try {
            usuarioService.bloquearUsuario(id);
            redirectAttributes.addFlashAttribute("mensaje", "Usuario bloqueado correctamente");
        } catch (UsuarioServiceException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }
        
        return "redirect:/registrados";
    }
}
```

### **TareaController - CRUD Tareas**
```java
@Controller
public class TareaController {
    
    @GetMapping("/usuarios/{id}/tareas")
    public String listaTareas(@PathVariable(value="id") Long idUsuario, 
                              Model model, HttpSession session) {
        
        Long usuarioLogeado = managerUserSession.usuarioLogeado();
        if (usuarioLogeado == null) {
            return "redirect:/login";
        }
        
        if (!usuarioLogeado.equals(idUsuario)) {
            throw new UsuarioNoLogeadoException();
        }
        
        UsuarioData usuario = usuarioService.findById(idUsuario);
        List<TareaData> tareas = tareaService.allTareasUsuario(idUsuario);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("tareas", tareas);
        return "listaTareas";
    }
    
    @PostMapping("/usuarios/{usuarioId}/tareas/nueva")
    public String nuevaTarea(@PathVariable(value="usuarioId") Long idUsuario,
                             @ModelAttribute TareaData tareaData,
                             Model model, HttpSession session,
                             RedirectAttributes flash) {
        
        Long usuarioLogeado = managerUserSession.usuarioLogeado();
        if (usuarioLogeado == null) {
            return "redirect:/login";
        }
        
        if (!usuarioLogeado.equals(idUsuario)) {
            throw new UsuarioNoLogeadoException();
        }
        
        tareaService.nuevaTarea(idUsuario, tareaData.getTitulo());
        flash.addFlashAttribute("mensaje", "Tarea creada correctamente");
        return "redirect:/usuarios/" + idUsuario + "/tareas";
    }
}
```

---

## 🎨 **5. TEMPLATES THYMELEAF**

### **Fragmentos Reutilizables**
```html
<!-- fragments.html -->
<nav th:fragment="navbar" class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container">
        <a class="navbar-brand" href="#">TodoList</a>
        
        <div class="navbar-nav ml-auto">
            <div class="nav-item dropdown">
                <a class="nav-link dropdown-toggle" href="#" 
                   data-toggle="dropdown" th:text="${usuario.nombre}">
                </a>
                <div class="dropdown-menu">
                    <a class="dropdown-item" 
                       th:href="@{/usuarios/{id}/tareas(id=${usuario.id})}">
                       Mis Tareas
                    </a>
                    <a class="dropdown-item" 
                       th:href="@{/registrados}" 
                       th:if="${usuario.isAdmin}">
                       Usuarios
                    </a>
                    <div class="dropdown-divider"></div>
                    <a class="dropdown-item" href="/logout">Cerrar Sesión</a>
                </div>
            </div>
        </div>
    </div>
</nav>
```

### **Renderizado Condicional**
```html
<!-- listaUsuarios.html -->
<tr th:each="user : ${usuarios}">
    <td th:text="${user.nombre}"></td>
    <td th:text="${user.email}"></td>
    <td>
        <span th:if="${user.isAdmin}" 
              class="badge badge-primary">Admin</span>
        <span th:unless="${user.isAdmin}" 
              class="badge badge-secondary">Normal</span>
    </td>
    <td>
        <span th:if="${user.bloqueado}" 
              class="badge badge-danger">Bloqueado</span>
        <span th:unless="${user.bloqueado}" 
              class="badge badge-success">Activo</span>
    </td>
    <td>
        <div th:unless="${user.isAdmin}">
            <form th:if="${!user.bloqueado}" 
                  th:action="@{/registrados/{id}/bloquear(id=${user.id})}" 
                  method="post" style="display: inline;">
                <button type="submit" 
                        class="btn btn-warning btn-sm"
                        onclick="return confirm('¿Seguro que quiere bloquear este usuario?')">
                    Bloquear
                </button>
            </form>
            <form th:if="${user.bloqueado}" 
                  th:action="@{/registrados/{id}/desbloquear(id=${user.id})}" 
                  method="post" style="display: inline;">
                <button type="submit" class="btn btn-success btn-sm">
                    Desbloquear
                </button>
            </form>
        </div>
    </td>
</tr>
```

### **Gestión de Mensajes**
```html
<!-- Mensajes Flash -->
<div th:if="${mensaje}" class="alert alert-success alert-dismissible">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <span th:text="${mensaje}"></span>
</div>

<div th:if="${error}" class="alert alert-danger alert-dismissible">
    <button type="button" class="close" data-dismiss="alert">&times;</button>
    <span th:text="${error}"></span>
</div>
```

---

## 🧪 **6. TESTING ESTRATÉGICO**

### **Tests de Repositorio**
```java
@SpringBootTest
@Transactional
class UsuarioTest {
    
    @Test
    public void crearUsuarioConRolAdmin() {
        Usuario usuario = new Usuario("admin@example.com");
        usuario.setIsAdmin(true);
        usuario.setBloqueado(false);
        
        usuarioRepository.save(usuario);
        
        assertThat(usuario.getId()).isNotNull();
        assertThat(usuario.getIsAdmin()).isTrue();
        assertThat(usuario.getBloqueado()).isFalse();
    }
}
```

### **Tests de Servicio**
```java
@SpringBootTest
@Transactional
class UsuarioServiceAdminTest {
    
    @Test
    public void primerUsuarioRegistradoEsAutomaticamenteAdmin() {
        // Given: No hay usuarios en el sistema
        assertThat(usuarioService.existeAdministrador()).isFalse();
        
        // When: Se registra el primer usuario
        RegistroData registroData = new RegistroData();
        registroData.setEmail("admin@example.com");
        registroData.setNombre("Admin");
        registroData.setPassword("admin123");
        
        UsuarioData usuario = usuarioService.registrar(registroData);
        
        // Then: Debe ser administrador automáticamente
        assertThat(usuario.getIsAdmin()).isTrue();
        assertThat(usuarioService.existeAdministrador()).isTrue();
    }
    
    @Test
    public void noSePuedeBloquearAdministrador() {
        // Given: Usuario administrador
        UsuarioData admin = crearUsuarioAdmin();
        
        // When & Then: Intentar bloquear debe lanzar excepción
        assertThatThrownBy(() -> usuarioService.bloquearUsuario(admin.getId()))
            .isInstanceOf(UsuarioServiceException.class)
            .hasMessageContaining("No se puede bloquear a un administrador");
    }
}
```

### **Tests Web/Controller**
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Transactional
public class HomeControllerWebTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    public void usuarioNormalNoPuedeAccederAListaUsuarios() throws Exception {
        // Given: Usuario normal logeado
        UsuarioData usuario = usuarioService.registrar(crearRegistroData());
        
        // When & Then: Acceso denegado a /registrados
        mockMvc.perform(get("/registrados")
                .sessionAttr("idUsuarioLogeado", usuario.getId()))
                .andExpect(status().isUnauthorized());
    }
    
    @Test
    public void administradorPuedeBloquearUsuarios() throws Exception {
        // Given: Admin y usuario normal
        UsuarioData admin = crearUsuarioAdmin();
        UsuarioData usuario = crearUsuarioNormal();
        
        // When: Admin bloquea usuario
        mockMvc.perform(post("/registrados/{id}/bloquear", usuario.getId())
                .sessionAttr("idUsuarioLogeado", admin.getId()))
                .andExpected(status().is3xxRedirection())
                .andExpected(redirectedUrl("/registrados"));
        
        // Then: Usuario debe estar bloqueado
        UsuarioData usuarioBloqueado = usuarioService.findById(usuario.getId());
        assertThat(usuarioBloqueado.getBloqueado()).isTrue();
    }
}
```

---

## 🔐 **7. GESTIÓN DE SESIONES Y SEGURIDAD**

### **ManagerUserSession**
```java
@Component
public class ManagerUserSession {
    
    public void logearUsuario(Long idUsuario) {
        session.setAttribute("idUsuarioLogeado", idUsuario);
    }
    
    public void logoutUsuario() {
        session.removeAttribute("idUsuarioLogeado");
    }
    
    public Long usuarioLogeado() {
        return (Long) session.getAttribute("idUsuarioLogeado");
    }
}
```

### **Patrón de Seguridad**
1. **Verificación de Sesión**: Todos los endpoints protegidos verifican `usuarioLogeado()`
2. **Verificación de Permisos**: Endpoints admin verifican `isAdmin`
3. **Verificación de Identidad**: Solo el propio usuario puede acceder a sus tareas
4. **Estados de Bloqueo**: Login rechaza usuarios bloqueados

---

## 📊 **8. DTOs Y TRANSFERENCIA DE DATOS**

### **Patrón DTO**
```java
public class UsuarioData {
    private Long id;
    private String email;
    private String nombre;
    private Date fechaNacimiento;
    private Boolean isAdmin;        // Nuevo campo
    private Boolean bloqueado;      // Nuevo campo
    
    // Constructor, getters, setters
}

public class RegistroData {
    @NotBlank(message = "El email no puede estar vacío")
    private String email;
    
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;
    
    @Size(min = 6, max = 20, message = "La contraseña debe tener entre 6 y 20 caracteres")
    private String password;
    
    private Date fechaNacimiento;
}
```

### **ModelMapper Configuration**
```java
@Configuration
public class ModelMapperConfig {
    
    @Bean
    public ModelMapper modelMapper() {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration()
              .setMatchingStrategy(MatchingStrategies.STRICT)
              .setFieldMatchingEnabled(true)
              .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE);
        return mapper;
    }
}
```

---

## 🚀 **9. FLUJOS DE TRABAJO CLAVE**

### **Flujo de Registro**
1. Usuario llena `formRegistro.html`
2. `LoginController.registroUsuario()` recibe datos
3. Validación de `@Valid RegistroData`
4. `UsuarioService.registrar()` determina si es primer usuario (admin)
5. Persistencia en BD con `usuarioRepository.save()`
6. Redirección según rol

### **Flujo de Login**
1. Usuario llena `formLogin.html`
2. `LoginController.loginUsuario()` recibe credenciales
3. `UsuarioService.login()` valida credenciales y estado
4. Si OK: `ManagerUserSession.logearUsuario()`
5. Redirección: Admin → `/registrados`, Usuario → `/usuarios/{id}/tareas`

### **Flujo de Gestión de Usuarios (Admin)**
1. Admin accede `/registrados`
2. `HomeController` verifica permisos admin
3. `UsuarioService.findAll()` obtiene lista
4. Render `listaUsuarios.html` con datos
5. Admin puede bloquear/desbloquear usuarios
6. POST requests actualizan estado en BD

### **Flujo de Gestión de Tareas**
1. Usuario accede `/usuarios/{id}/tareas`
2. `TareaController` verifica identidad
3. `TareaService.allTareasUsuario()` obtiene tareas
4. CRUD operations: crear, editar, eliminar
5. Validaciones de negocio en capa Service

---

## 🎯 **10. PREGUNTAS TÍPICAS DE EXAMEN**

### **Conceptuales**
1. **¿Qué patrón arquitectónico sigue la aplicación?**
   - MVC (Model-View-Controller) con capas adicionales de Service y Repository

2. **¿Por qué usar DTOs en lugar de entidades directamente?**
   - Separación de capas, seguridad, control de datos expuestos, versionado de API

3. **¿Qué es el patrón Repository?**
   - Abstracción de la capa de acceso a datos, facilita testing y cambios de BD

### **Técnicas**
1. **¿Cómo implementar una nueva funcionalidad?**
   - Entidad → Repository → Service → Controller → Template → Tests

2. **¿Cómo agregar validaciones?**
   - Bean Validation en DTOs, validaciones de negocio en Services

3. **¿Cómo proteger un endpoint?**
   - Verificar sesión, verificar permisos, lanzar excepciones apropiadas

### **Debugging**
1. **Usuario no puede hacer login**
   - Verificar: credenciales, estado bloqueado, sesión, redirecciones

2. **Error 500 en endpoint**
   - Verificar: parámetros, entidades existentes, transacciones, excepciones

3. **Template no renderiza datos**
   - Verificar: modelo en controller, sintaxis Thymeleaf, bootstrap/CSS

---

## 🏆 **11. MEJORES PRÁCTICAS IMPLEMENTADAS**

### **Seguridad**
- Validación de sesiones en todos los endpoints protegidos
- Verificación de permisos por rol
- Validación de identidad para acceso a recursos propios
- Manejo seguro de estados de usuario (bloqueo)

### **Arquitectura**
- Separación clara de responsabilidades por capas
- DTOs para transferencia de datos
- Exception handling centralizado
- Transacciones apropiadas

### **Frontend**
- Templates reutilizables (fragments)
- Renderizado condicional
- Feedback visual (mensajes, badges)
- Confirmaciones JavaScript para acciones críticas

### **Testing**
- Cobertura completa: Unit, Integration, Web
- Tests por capa (Repository, Service, Controller)
- Casos edge y validaciones de errores
- Transaccional para aislamiento

---

## 📝 **12. COMANDOS ESENCIALES**

### **Maven**
```bash
./mvnw test                    # Ejecutar todos los tests
./mvnw test -Dtest=NombreTest  # Ejecutar test específico
./mvnw spring-boot:run         # Ejecutar aplicación
./mvnw clean compile           # Limpiar y compilar
```

### **Git**
```bash
git status                     # Ver estado
git add .                      # Agregar cambios
git commit -m "mensaje"        # Commit con mensaje
git log --oneline              # Ver historial
git tag v1.1.0                 # Crear tag
```

### **Base de Datos H2**
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- User: `sa`
- Password: (vacío)

---

## 🎯 **PUNTOS CLAVE PARA EL EXAMEN**

1. **Dominar el flujo MVC completo**
2. **Entender las relaciones JPA y lazy loading**
3. **Saber implementar validaciones en múltiples capas**
4. **Conocer los patrones de seguridad implementados**
5. **Poder escribir tests efectivos para cada capa**
6. **Entender el ciclo de vida de las sesiones web**
7. **Dominar Thymeleaf para renderizado dinámico**
8. **Conocer las mejores prácticas de Spring Boot**

### **🚨 ERRORES COMUNES A EVITAR**
- No verificar sesiones en endpoints protegidos
- Usar entidades en lugar de DTOs en controllers
- No manejar excepciones apropiadamente
- Olvider transacciones en métodos de service
- No validar permisos antes de operaciones críticas
- Tests sin aislamiento (no transaccional)
- Renderizado sin verificar null values

---

## 🎉 **RESULTADO ESPERADO**
Con esta guía deberías poder:
- Implementar nuevas funcionalidades siguiendo los patrones establecidos
- Debuggear problemas comunes
- Escribir tests efectivos
- Explicar decisiones arquitectónicas
- **¡SACAR UN 10 EN EL EXAMEN! 🏆**

---

## 🎯 **13. PREPARACIÓN ESPECÍFICA PARA EXAMEN PRÁCTICO**

### **Conocimientos Imprescindibles**

#### **A. Arquitectura Spring Boot (25% del examen)**
- **Spring Boot Starters**: Comprender qué hace cada starter en `pom.xml`
- **Autoconfiguration**: Cómo Spring Boot configura automáticamente beans
- **Application Properties**: Configuración de BD, logging, puertos
- **Component Scanning**: `@ComponentScan`, `@SpringBootApplication`

#### **B. JPA/Hibernate (25% del examen)**
- **Entidades**: `@Entity`, `@Table`, `@Id`, `@GeneratedValue`
- **Relaciones**: `@OneToMany`, `@ManyToOne`, `@JoinColumn`
- **Fetch Types**: LAZY vs EAGER loading
- **Repository Pattern**: JpaRepository métodos automáticos

#### **C. Controllers y MVC (20% del examen)**
- **Mapping Annotations**: `@GetMapping`, `@PostMapping`, `@PathVariable`
- **Model Handling**: Agregar atributos al modelo
- **Session Management**: HttpSession, atributos de sesión
- **Exception Handling**: `@ExceptionHandler`, `ResponseStatusException`

#### **D. Templates Thymeleaf (15% del examen)**
- **Sintaxis básica**: `th:text`, `th:if`, `th:each`, `th:href`
- **Condicionales**: `th:if`, `th:unless`
- **Fragmentos**: `th:fragment`, `th:insert`
- **Forms**: `th:action`, `th:object`, `th:field`

#### **E. Testing (15% del examen)**
- **Unit Tests**: `@Test`, assertions con AssertJ
- **Integration Tests**: `@SpringBootTest`, `@Transactional`
- **Web Tests**: `MockMvc`, `@AutoConfigureTestDatabase`

### **Comandos que DEBES dominar**

#### **Maven**
```bash
./mvnw test                    # Ejecutar todos los tests
./mvnw spring-boot:run         # Ejecutar aplicación
./mvnw clean package           # Limpiar y crear JAR
./mvnw test -Dtest=ClaseTest   # Ejecutar test específico
```

#### **Git**
```bash
git status                     # Ver estado actual
git add .                      # Agregar cambios
git commit -m "mensaje"        # Confirmar cambios
git log --oneline              # Ver historial compacto
git branch -a                  # Ver todas las ramas
```

#### **H2 Console (Debugging BD)**
```
URL: http://localhost:8080/h2-console
JDBC URL: jdbc:h2:mem:testdb
Usuario: sa
Password: (vacío)
```

### **Problemas Típicos y Soluciones**

#### **Error: "WhitelabelErrorPage"**
- **Causa**: Controlador no mapeado correctamente
- **Solución**: Verificar `@GetMapping` y rutas en controller

#### **Error: "Template not found"**
- **Causa**: Nombre de template incorrecto o ubicación incorrecta
- **Solución**: Verificar que el template esté en `src/main/resources/templates/`

#### **Error: "No qualifying bean"**
- **Causa**: Dependencia no inyectada correctamente
- **Solución**: Verificar `@Autowired` y `@Component`/`@Service`

#### **Error en Tests: "Transaction rolled back"**
- **Causa**: Excepción no manejada en test
- **Solución**: Revisar datos de test y validaciones

#### **Error: "Session not found"**
- **Causa**: Usuario no logeado intentando acceder a endpoint protegido
- **Solución**: Verificar `ManagerUserSession.usuarioLogeado()`

### **Patrones de Implementación Clave**

#### **1. Implementar nuevo endpoint**
```java
// 1. Controller
@GetMapping("/nuevo-endpoint")
public String nuevoEndpoint(Model model, HttpSession session) {
    Long usuarioId = managerUserSession.usuarioLogeado();
    if (usuarioId == null) return "redirect:/login";
    
    // Lógica del endpoint
    model.addAttribute("datos", datos);
    return "template";
}

// 2. Service (si necesita lógica de negocio)
@Service
public class MiService {
    public List<Datos> obtenerDatos() {
        return repository.findAll();
    }
}

// 3. Template (si necesita nueva vista)
<div th:each="dato : ${datos}">
    <span th:text="${dato.propiedad}"></span>
</div>
```

#### **2. Agregar nuevo campo a entidad**
```java
// 1. Entidad
@Entity
public class Usuario {
    // ... campos existentes
    
    @Column(name = "nuevo_campo")
    private String nuevoCampo;
    
    // getter y setter
}

// 2. DTO
public class UsuarioData {
    // ... campos existentes
    private String nuevoCampo;
    
    // getter y setter
}

// 3. Template (mostrar nuevo campo)
<span th:text="${usuario.nuevoCampo}"></span>
```

#### **3. Implementar validación de negocio**
```java
// Service
public void operacionValidada(Long usuarioId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new ServiceException("Usuario no encontrado"));
    
    if (condicionInvalida) {
        throw new ServiceException("Operación no permitida");
    }
    
    // Realizar operación
    usuarioRepository.save(usuario);
}

// Controller
try {
    service.operacionValidada(usuarioId);
    redirectAttributes.addFlashAttribute("mensaje", "Operación exitosa");
} catch (ServiceException e) {
    redirectAttributes.addFlashAttribute("error", e.getMessage());
}
```

### **Estructura de Respuesta para Examen**

#### **Pregunta: "Implementa funcionalidad X"**
1. **Análisis**: ¿Qué capas necesito modificar?
2. **Entidad**: ¿Necesito nuevos campos en BD?
3. **Repository**: ¿Necesito nuevas consultas?
4. **Service**: ¿Qué lógica de negocio implemento?
5. **Controller**: ¿Qué endpoints necesito?
6. **Template**: ¿Qué vistas necesito crear/modificar?
7. **Tests**: ¿Qué casos debo probar?

#### **Pregunta: "Explica cómo funciona X"**
1. **Arquitectura**: Mencionar patrón MVC y capas
2. **Flujo**: Describir paso a paso desde request hasta response
3. **Tecnologías**: Explicar qué hace cada componente Spring
4. **Ejemplos**: Dar código específico del proyecto

#### **Pregunta: "Debug error X"**
1. **Síntomas**: Describir qué error aparece
2. **Causas posibles**: Listar 2-3 causas probables
3. **Pasos de debugging**: Cómo investigar paso a paso
4. **Solución**: Implementar fix específico

### **Timing del Examen**
- **20% del tiempo**: Leer y planificar
- **60% del tiempo**: Implementar código
- **20% del tiempo**: Testing y revisión

### **Checklist Pre-Examen**
- [ ] Proyecto compila sin errores (`./mvnw clean compile`)
- [ ] Todos los tests pasan (`./mvnw test`)
- [ ] Aplicación arranca correctamente (`./mvnw spring-boot:run`)
- [ ] Base de datos H2 accesible
- [ ] Git en estado limpio (`git status`)

### **Frases Clave para Impresionar**
- "Siguiendo el patrón MVC de Spring Boot..."
- "Implementando separación de responsabilidades..."
- "Utilizando inyección de dependencias..."
- "Aplicando el patrón Repository..."
- "Con validaciones en múltiples capas..."
- "Siguiendo las convenciones de Spring Boot..."

---

## 🏆 **RESULTADO FINAL**

Con esta guía completa tienes todos los conocimientos necesarios para:
- ✅ **Implementar cualquier funcionalidad** siguiendo patrones Spring Boot
- ✅ **Debuggear errores comunes** de forma sistemática  
- ✅ **Explicar arquitectura y decisiones** técnicas con propiedad
- ✅ **Escribir tests efectivos** para cada capa de la aplicación
- ✅ **Demostrar dominio completo** de las tecnologías implementadas

### 🎯 **¡PREPARADO PARA SACAR UN 10! 🏆**

---

*Documento generado para práctica MADS - TodoList Spring Boot*
*Versión: 1.1.0 | Fecha: Octubre 2025*