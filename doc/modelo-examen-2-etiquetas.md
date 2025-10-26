# 📝 EXAMEN PRÁCTICO 2 - MADS ToDoList

**Nombre y Apellidos:** ________________________________________________

**Usuario GitHub:** ________________________________________________

**Fecha:** 25 de octubre de 2025

**Duración:** 2 horas

---

## ⚠️ INSTRUCCIONES IMPORTANTES

### Fase 1: Configuración Inicial (10 minutos - CON INTERNET)

1. **Clonar el repositorio** que se os ha proporcionado en GitHub Classroom
2. **Verificar que funciona**:
   ```bash
   cd mads-todolist-examen
   ./mvnw test
   ./mvnw spring-boot:run
   ```
3. **Crear UN ÚNICO ISSUE** en GitHub con el título: "Examen: Implementar sistema de etiquetas en tareas"
4. **Crear rama local** para trabajar:
   ```bash
   git checkout -b examen-practica
   ```
5. **Anotar vuestro nombre** en este enunciado

### Fase 2: Desarrollo (1 hora 40 minutos - SIN INTERNET)

- Trabajar **solo en local** con commits frecuentes
- **NO se permite** acceso a internet, apuntes, ni otros recursos
- Solo podéis usar el **IDE y sus sugerencias**
- Hacer **commits pequeños y descriptivos** cada vez que completéis un test o funcionalidad

### Fase 3: Entrega (10 minutos - CON INTERNET)

1. **Push** de todos los cambios: `git push origin examen-practica`
2. **Crear Pull Request** a `main` en GitHub
3. **Hacer merge** del Pull Request
4. **Comprimir proyecto** (sin carpeta target):
   ```bash
   cd ..
   zip -r apellido-nombre-examen.zip mads-todolist-examen/ -x "*/target/*"
   ```
5. **Subir ZIP** a la tarea de Moodle correspondiente
6. **Entregar este enunciado** firmado al profesor

---

## 🎯 ENUNCIADO: Sistema de Etiquetas (Tags) para Tareas

Se desea añadir un sistema de **etiquetas (tags)** a las tareas de la aplicación ToDoList. Cada tarea podrá tener múltiples etiquetas para categorizarlas (por ejemplo: "personal", "trabajo", "urgente", "casa", etc.). Las etiquetas son **comunes a todos los usuarios**.

### Funcionalidades a Implementar

#### **Funcionalidad 1: Modelo y Persistencia (30 puntos)**

Crear una nueva entidad `Etiqueta` y establecer la relación many-to-many con `Tarea`:

**Requisitos:**

1. **Crear entidad `Etiqueta`**:
   - Campo `id` (Long): Identificador único
   - Campo `nombre` (String): Nombre de la etiqueta (único en la BD)
   - Campo `color` (String): Color en formato hexadecimal (ej: "#FF5733")
   - Relación Many-to-Many con `Tarea`

2. **Modificar entidad `Tarea`**:
   - Añadir relación Many-to-Many con `Etiqueta`
   - Una tarea puede tener múltiples etiquetas
   - Una etiqueta puede estar en múltiples tareas

3. **Tabla intermedia**:
   - Debe llamarse `tarea_etiqueta`
   - Columnas: `fk_tarea`, `fk_etiqueta`

**Tests requeridos (Repository):**
1. Test que cree una etiqueta en la BD
2. Test que añada una etiqueta a una tarea
3. Test que verifique que una tarea puede tener múltiples etiquetas
4. Test que verifique que una etiqueta puede estar en múltiples tareas

**Archivos a crear/modificar:**
- `src/main/java/madstodolist/model/Etiqueta.java` (NUEVO)
- `src/main/java/madstodolist/model/Tarea.java` (modificar)
- `src/main/java/madstodolist/repository/EtiquetaRepository.java` (NUEVO)
- `src/test/java/madstodolist/repository/EtiquetaTest.java` (NUEVO)

---

#### **Funcionalidad 2: Lógica de Negocio (30 puntos)**

Implementar en la capa de servicio las operaciones necesarias para gestionar etiquetas:

**Requisitos:**

1. **Crear `EtiquetaData` DTO**:
   - Campos: `id`, `nombre`, `color`

2. **Crear `EtiquetaService`** con los siguientes métodos:
   - `crearEtiqueta(String nombre, String color)`: Crea nueva etiqueta
   - `findAllEtiquetas()`: Devuelve todas las etiquetas ordenadas por nombre
   - `findById(Long id)`: Busca etiqueta por ID
   - `eliminarEtiqueta(Long id)`: Elimina una etiqueta

3. **Modificar `TareaService`** añadiendo:
   - `añadirEtiquetaATarea(Long tareaId, Long etiquetaId)`: Asocia etiqueta a tarea
   - `eliminarEtiquetaDeTarea(Long tareaId, Long etiquetaId)`: Desasocia etiqueta de tarea
   - `findTareasConEtiqueta(Long usuarioId, Long etiquetaId)`: Devuelve tareas del usuario con esa etiqueta

4. **Modificar `TareaData` DTO**:
   - Añadir campo `etiquetas` (Set<EtiquetaData>)

**Tests requeridos (Service):**
1. Test que cree una etiqueta mediante el servicio
2. Test que añada una etiqueta a una tarea
3. Test que elimine una etiqueta de una tarea
4. Test que obtenga todas las tareas de un usuario con una etiqueta específica
5. Test que verifique que al eliminar una etiqueta, se elimina de todas las tareas

**Archivos a crear/modificar:**
- `src/main/java/madstodolist/dto/EtiquetaData.java` (NUEVO)
- `src/main/java/madstodolist/service/EtiquetaService.java` (NUEVO)
- `src/main/java/madstodolist/service/EtiquetaServiceException.java` (NUEVO)
- `src/main/java/madstodolist/service/TareaService.java` (modificar)
- `src/main/java/madstodolist/dto/TareaData.java` (modificar)
- `src/test/java/madstodolist/service/EtiquetaServiceTest.java` (NUEVO)
- `src/test/java/madstodolist/service/TareaServiceTest.java` (añadir tests)

---

#### **Funcionalidad 3: Capa Web (30 puntos)**

Implementar los endpoints y vistas necesarios para gestionar etiquetas desde la interfaz web:

**Requisitos:**

**A) Gestión de Etiquetas** (`EtiquetaController`):

1. **Listar etiquetas**: `GET /etiquetas`
   - Vista: `listaEtiquetas.html`
   - Muestra todas las etiquetas con su color
   - Botones: "Nueva etiqueta", "Editar", "Eliminar"

2. **Crear etiqueta**: `GET /etiquetas/nueva` y `POST /etiquetas/nueva`
   - Vista: `formNuevaEtiqueta.html`
   - Formulario con campos: nombre, color (input type="color")

3. **Eliminar etiqueta**: `DELETE /etiquetas/{id}`
   - Elimina la etiqueta
   - Usa JavaScript con fetch()

**B) Modificar listado de tareas** (`listaTareas.html`):

1. **Mostrar etiquetas de cada tarea**:
   - Badges con el color de cada etiqueta
   - Ejemplo: <span style="background:#FF5733">trabajo</span> <span style="background:#33FF57">personal</span>

2. **Botón para añadir etiqueta a tarea**:
   - Icono "+" o "🏷️" junto a cada tarea
   - Al hacer clic, muestra un modal o dropdown con las etiquetas disponibles

3. **Eliminar etiqueta de tarea**:
   - Botón "×" en cada badge de etiqueta
   - POST a `/tareas/{id}/etiquetas/{etiquetaId}/eliminar`

**C) Filtrar tareas por etiqueta**:

1. **Endpoint**: `GET /usuarios/{id}/tareas/etiqueta/{etiquetaId}`
   - Muestra solo tareas con esa etiqueta
   - Reutiliza `listaTareas.html`

2. **Añadir botones de filtro** en `listaTareas.html`:
   - Botón por cada etiqueta que tenga el usuario
   - Botón "Todas" para quitar filtro

**Tests requeridos (Controller):**
1. Test que cree una etiqueta mediante POST
2. Test que liste todas las etiquetas
3. Test que añada una etiqueta a una tarea
4. Test que elimine una etiqueta de una tarea
5. Test que filtre tareas por etiqueta

**Archivos a crear/modificar:**
- `src/main/java/madstodolist/controller/EtiquetaController.java` (NUEVO)
- `src/main/java/madstodolist/controller/TareaController.java` (modificar)
- `src/main/resources/templates/listaEtiquetas.html` (NUEVO)
- `src/main/resources/templates/formNuevaEtiqueta.html` (NUEVO)
- `src/main/resources/templates/listaTareas.html` (modificar)
- `src/test/java/madstodolist/controller/EtiquetaWebTest.java` (NUEVO)
- `src/test/java/madstodolist/controller/TareaWebTest.java` (añadir tests)

---

#### **Funcionalidad 4: Editar Etiqueta (10 puntos - BONUS)**

Implementar la funcionalidad de editar una etiqueta existente:

**Requisitos:**
- Endpoint `GET /etiquetas/{id}/editar` que muestre formulario
- Endpoint `POST /etiquetas/{id}/editar` que actualice la etiqueta
- Vista `formEditarEtiqueta.html` con campos prellenados
- Al cambiar nombre o color, se actualiza en todas las tareas que la usen

**Archivos a crear/modificar:**
- `src/main/java/madstodolist/controller/EtiquetaController.java`
- `src/main/java/madstodolist/service/EtiquetaService.java`
- `src/main/resources/templates/formEditarEtiqueta.html` (NUEVO)

---

## ✅ CRITERIOS DE EVALUACIÓN

### Distribución de Puntos

| Aspecto | Puntos | Descripción |
|---------|--------|-------------|
| **Funcionalidad 1** | 30 | Modelo Many-to-Many y tests de repository |
| **Funcionalidad 2** | 30 | Services, DTOs y tests de servicio |
| **Funcionalidad 3** | 30 | Controllers, vistas y tests web |
| **Funcionalidad 4** | 10 | Bonus: Editar etiqueta |
| **TOTAL** | **100** | (90 puntos + 10 bonus) |

### Desglose Detallado

#### Funcionalidad del Código (50%)
- ✅ La aplicación arranca sin errores
- ✅ Se pueden crear etiquetas
- ✅ Se pueden añadir etiquetas a tareas
- ✅ Se pueden eliminar etiquetas de tareas
- ✅ El filtro por etiqueta funciona correctamente
- ✅ La interfaz muestra correctamente las etiquetas con colores

#### Tests (40%)
- ✅ Todos los tests existentes siguen pasando
- ✅ Los nuevos tests de repository están implementados y pasan
- ✅ Los nuevos tests de service están implementados y pasan
- ✅ Los nuevos tests de controller están implementados y pasan
- ✅ Los tests prueban funcionalidad real (no son triviales)
- ✅ Mínimo 15 tests nuevos

#### Calidad del Código (10%)
- ✅ Commits pequeños y descriptivos (estilo TDD)
- ✅ Nombres de métodos y variables descriptivos
- ✅ Código bien estructurado y coherente
- ✅ Uso correcto de anotaciones JPA (@ManyToMany, @JoinTable)
- ✅ Manejo adecuado de casos límite

---

## 📋 CHECKLIST DE ENTREGA

Antes de entregar, verifica que has completado:

**Modelo y Persistencia:**
- [ ] Entidad `Etiqueta` creada con id, nombre, color
- [ ] Relación Many-to-Many entre Tarea y Etiqueta
- [ ] EtiquetaRepository creado
- [ ] 4 tests de repository pasando

**Lógica de Negocio:**
- [ ] EtiquetaData DTO creado
- [ ] EtiquetaService con 4 métodos
- [ ] TareaService con 3 métodos nuevos para etiquetas
- [ ] TareaData modificado con campo etiquetas
- [ ] 5 tests de service pasando

**Capa Web:**
- [ ] EtiquetaController con endpoints CRUD
- [ ] Vista listaEtiquetas.html funcionando
- [ ] Vista formNuevaEtiqueta.html con input color
- [ ] listaTareas.html muestra badges de etiquetas con colores
- [ ] Botones para añadir/eliminar etiquetas en tareas
- [ ] Filtro por etiqueta funcionando
- [ ] 5 tests de controller pasando

**General:**
- [ ] Todos los tests pasan: `./mvnw test`
- [ ] Commits: Al menos 8-10 commits descriptivos
- [ ] GitHub: Push realizado y Pull Request creado
- [ ] Moodle: ZIP subido correctamente
- [ ] Papel: Enunciado entregado con nombre y usuario

---

## 💡 CONSEJOS Y AYUDA

### Orden Recomendado de Implementación

1. **Empezar por el modelo** (15-20 min)
   - Crear entidad `Etiqueta`
   - Modificar entidad `Tarea` con relación Many-to-Many
   - Crear `EtiquetaRepository`
   - Tests de repository
   - Commit: "TDD: Entidad Etiqueta y relación many-to-many"

2. **Continuar con DTOs y Service** (40-50 min)
   - Crear `EtiquetaData`
   - Modificar `TareaData`
   - Crear `EtiquetaService`
   - Modificar `TareaService`
   - Tests de service
   - Commit: "TDD: Services para gestión de etiquetas"

3. **Luego Controller** (30-40 min)
   - Crear `EtiquetaController`
   - Modificar `TareaController`
   - Tests de controller
   - Commit: "TDD: Controllers para etiquetas"

4. **Finalmente Vistas** (30-40 min)
   - Crear `listaEtiquetas.html`
   - Crear `formNuevaEtiqueta.html`
   - Modificar `listaTareas.html`
   - Commit: "Vistas: Gestión completa de etiquetas"

5. **Bonus si queda tiempo** (10-15 min)
   - Implementar edición de etiquetas
   - Commit: "Bonus: Editar etiquetas"

### Ejemplo de Relación Many-to-Many (JPA)

```java
// En Etiqueta.java (lado propietario)
@ManyToMany(fetch = FetchType.LAZY)
@JoinTable(
    name = "tarea_etiqueta",
    joinColumns = @JoinColumn(name = "fk_etiqueta"),
    inverseJoinColumns = @JoinColumn(name = "fk_tarea")
)
private Set<Tarea> tareas = new HashSet<>();

// En Tarea.java (lado inverso)
@ManyToMany(mappedBy = "tareas", fetch = FetchType.LAZY)
private Set<Etiqueta> etiquetas = new HashSet<>();
```

### Ejemplo de Input Color en HTML

```html
<!-- Input para seleccionar color -->
<label for="color">Color de la etiqueta:</label>
<input type="color" id="color" name="color" 
       class="form-control" value="#3498db" required>
```

### Ejemplo de Badge con Color Dinámico (Thymeleaf)

```html
<!-- Badge con color personalizado -->
<span th:each="etiqueta : ${tarea.etiquetas}"
      class="badge mr-1"
      th:style="'background-color: ' + ${etiqueta.color} + '; color: white;'"
      th:text="${etiqueta.nombre}">
</span>
```

### Ejemplo de Método para Añadir Etiqueta a Tarea

```java
// En Etiqueta.java
public void addTarea(Tarea tarea) {
    this.tareas.add(tarea);
    tarea.getEtiquetas().add(this);
}

// En TareaService.java
@Transactional
public void añadirEtiquetaATarea(Long tareaId, Long etiquetaId) {
    Tarea tarea = tareaRepository.findById(tareaId).orElse(null);
    Etiqueta etiqueta = etiquetaRepository.findById(etiquetaId).orElse(null);
    if (tarea == null || etiqueta == null) {
        throw new TareaServiceException("Tarea o etiqueta no existe");
    }
    etiqueta.addTarea(tarea);
}
```

### Ejemplo de Controller para Añadir Etiqueta

```java
@PostMapping("/tareas/{id}/etiquetas/{etiquetaId}/añadir")
public String añadirEtiqueta(@PathVariable(value="id") Long tareaId,
                             @PathVariable(value="etiquetaId") Long etiquetaId,
                             HttpSession session) {
    // Verificar usuario logeado
    // Añadir etiqueta
    // Redirigir
}
```

---

## ⚠️ IMPORTANTE

- **NO se permite copiar código** de compañeros
- **NO se permite usar ChatGPT, Copilot** u otras IA
- **NO se permite consultar apuntes** físicos o digitales
- **SÍ se permite** usar autocompletado del IDE
- **SÍ se permite** consultar la documentación del IDE (sin internet)

**Cualquier acción sospechosa será causa de suspenso inmediato.**

---

## 📊 RÚBRICA DE CORRECCIÓN

### Excelente (9-10): 
- Todas las funcionalidades implementadas y funcionando
- Relación Many-to-Many correctamente implementada
- Todos los tests pasan y prueban funcionalidad real
- Etiquetas se muestran con colores correctos
- Código limpio y bien estructurado
- Commits descriptivos y frecuentes
- Bonus implementado

### Notable (7-8):
- Funcionalidades 1, 2 y 3 completas
- Algunos tests faltan o son demasiado simples
- Código funcional pero mejorable
- Colores de etiquetas funcionan parcialmente
- Commits presentes pero poco descriptivos

### Aprobado (5-6):
- Funcionalidades 1 y 2 completas, 3 parcial
- Tests básicos que pasan
- Código funcional con algunos errores
- Etiquetas se crean pero no se muestran bien
- Commits mínimos

### Suspenso (0-4):
- Funcionalidades incompletas
- Relación Many-to-Many mal implementada
- Tests no pasan o no existen
- Código no compila o no funciona
- No se siguió la metodología TDD

---

## 🔚 FIN DEL ENUNCIADO

**Recuerda:**
1. Gestiona bien tu tiempo
2. Haz commits frecuentes
3. Tests primero, implementación después
4. Si te bloqueas, pasa a otra funcionalidad
5. Prioriza que funcione antes que esté perfecto
6. **La relación Many-to-Many es clave**: Asegúrate de gestionarla correctamente

**¡BUENA SUERTE! 🍀**

---

**Firma del alumno:** _________________________ **Fecha:** _____________
