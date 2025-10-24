# 📝 EXAMEN PRÁCTICO - MADS ToDoList

**Nombre y Apellidos:** ________________________________________________

**Usuario GitHub:** ________________________________________________

**Fecha:** 22 de octubre de 2025

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
3. **Crear UN ÚNICO ISSUE** en GitHub con el título: "Examen: Implementar gestión de prioridades en tareas"
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

## 🎯 ENUNCIADO: Sistema de Prioridades para Tareas

Se desea añadir un sistema de **prioridades** a las tareas de la aplicación ToDoList. Cada tarea podrá tener una prioridad (BAJA, MEDIA, ALTA) que permita al usuario organizar mejor su trabajo.

### Funcionalidades a Implementar

#### **Funcionalidad 1: Modelo y Persistencia (30 puntos)**

Modificar la entidad `Tarea` para incluir el concepto de prioridad:

**Requisitos:**
- Añadir un campo `prioridad` de tipo String a la entidad `Tarea`
- Las prioridades válidas son: "BAJA", "MEDIA", "ALTA"
- Por defecto, una tarea tiene prioridad "MEDIA"
- El campo debe persistirse en la base de datos en la columna `prioridad`

**Tests requeridos (Repository):**
1. Test que verifique que se puede crear una tarea con prioridad ALTA
2. Test que verifique que una tarea sin prioridad especificada tiene prioridad MEDIA por defecto
3. Test que verifique que se puede modificar la prioridad de una tarea existente

**Archivos a modificar/crear:**
- `src/main/java/madstodolist/model/Tarea.java`
- `src/test/java/madstodolist/repository/TareaTest.java`

---

#### **Funcionalidad 2: Lógica de Negocio (30 puntos)**

Implementar en la capa de servicio las operaciones necesarias para gestionar prioridades:

**Requisitos:**
- Añadir campo `prioridad` al DTO `TareaData`
- Implementar método `cambiarPrioridad(Long idTarea, String nuevaPrioridad)` en `TareaService`
- Implementar método `allTareasPrioridadUsuario(Long idUsuario, String prioridad)` que devuelva las tareas de un usuario filtradas por prioridad

**Tests requeridos (Service):**
1. Test que cambie la prioridad de una tarea de MEDIA a ALTA
2. Test que obtenga solo las tareas con prioridad ALTA de un usuario
3. Test que verifique que cambiar prioridad de una tarea inexistente lanza excepción

**Archivos a modificar/crear:**
- `src/main/java/madstodolist/dto/TareaData.java`
- `src/main/java/madstodolist/service/TareaService.java`
- `src/test/java/madstodolist/service/TareaServiceTest.java`

---

#### **Funcionalidad 3: Capa Web (30 puntos)**

Implementar los endpoints y vistas necesarios para gestionar prioridades desde la interfaz web:

**Requisitos:**

**A) Modificar listado de tareas** (`listaTareas.html`):
- Mostrar la prioridad de cada tarea con un badge de color:
  - BAJA: badge-secondary (gris)
  - MEDIA: badge-primary (azul)
  - ALTA: badge-danger (rojo)
- Añadir botones junto a cada tarea para cambiar su prioridad

**B) Añadir filtro de prioridades**:
- Crear endpoint `GET /usuarios/{id}/tareas/prioridad/{prioridad}` que muestre solo tareas de esa prioridad
- Añadir botones en la vista para filtrar por BAJA, MEDIA o ALTA
- Añadir botón "Todas" para ver todas las tareas sin filtro

**C) Cambiar prioridad**:
- Crear endpoint `POST /tareas/{id}/cambiar-prioridad` que reciba el parámetro `prioridad`
- Debe redirigir a la lista de tareas después de cambiar la prioridad

**Tests requeridos (Controller):**
1. Test que verifique que se puede cambiar la prioridad de una tarea mediante POST
2. Test que verifique que el filtro por prioridad ALTA muestra solo tareas de esa prioridad
3. Test que verifique que el listado muestra correctamente las 3 prioridades

**Archivos a modificar/crear:**
- `src/main/java/madstodolist/controller/TareaController.java`
- `src/main/resources/templates/listaTareas.html`
- `src/test/java/madstodolist/controller/TareaWebTest.java`

---

#### **Funcionalidad 4: Formulario de Nueva Tarea (10 puntos - BONUS)**

Modificar el formulario de creación de tareas para permitir seleccionar la prioridad:

**Requisitos:**
- Modificar `formNuevaTarea.html` para incluir un selector de prioridad
- Las opciones deben ser: BAJA, MEDIA (seleccionada por defecto), ALTA
- El método `nuevaTarea` del controlador debe recibir y asignar la prioridad

**Archivos a modificar:**
- `src/main/resources/templates/formNuevaTarea.html`
- `src/main/java/madstodolist/controller/TareaController.java`

---

## ✅ CRITERIOS DE EVALUACIÓN

### Distribución de Puntos

| Aspecto | Puntos | Descripción |
|---------|--------|-------------|
| **Funcionalidad 1** | 30 | Modelo, persistencia y tests de repository |
| **Funcionalidad 2** | 30 | Service, DTO y tests de servicio |
| **Funcionalidad 3** | 30 | Controller, vistas y tests web |
| **Funcionalidad 4** | 10 | Bonus: Formulario de nueva tarea |
| **TOTAL** | **100** | (90 puntos + 10 bonus) |

### Desglose Detallado

#### Funcionalidad del Código (50%)
- ✅ La aplicación arranca sin errores
- ✅ Se puede crear una tarea con prioridad
- ✅ Se puede cambiar la prioridad de una tarea
- ✅ El filtro por prioridad funciona correctamente
- ✅ La interfaz muestra correctamente las prioridades

#### Tests (40%)
- ✅ Todos los tests existentes siguen pasando
- ✅ Los nuevos tests de repository están implementados y pasan
- ✅ Los nuevos tests de service están implementados y pasan
- ✅ Los nuevos tests de controller están implementados y pasan
- ✅ Los tests prueban funcionalidad real (no son triviales)

#### Calidad del Código (10%)
- ✅ Commits pequeños y descriptivos (estilo TDD)
- ✅ Nombres de métodos y variables descriptivos
- ✅ Código bien estructurado y coherente
- ✅ Uso correcto de anotaciones JPA y Spring
- ✅ Manejo adecuado de casos límite

---

## 📋 CHECKLIST DE ENTREGA

Antes de entregar, verifica que has completado:

- [ ] **Modelo**: Campo `prioridad` añadido a `Tarea` con valor por defecto
- [ ] **Tests Repository**: 3 tests implementados y pasando
- [ ] **DTO**: Campo `prioridad` añadido a `TareaData`
- [ ] **Service**: Métodos `cambiarPrioridad` y `allTareasPrioridadUsuario` implementados
- [ ] **Tests Service**: 3 tests implementados y pasando
- [ ] **Controller**: Endpoints para cambiar prioridad y filtrar implementados
- [ ] **Vista**: Badges de colores y botones de cambio de prioridad
- [ ] **Vista**: Botones de filtro por prioridad funcionando
- [ ] **Tests Controller**: 3 tests implementados y pasando
- [ ] **Todos los tests pasan**: `./mvnw test` ejecutado correctamente
- [ ] **Commits**: Al menos 6-8 commits descriptivos realizados
- [ ] **GitHub**: Push realizado y Pull Request creado
- [ ] **Moodle**: ZIP subido correctamente
- [ ] **Papel**: Enunciado entregado con nombre y usuario

---

## 💡 CONSEJOS Y AYUDA

### Orden Recomendado de Implementación

1. **Empezar por el modelo** (10-15 min)
   - Añadir campo a `Tarea`
   - Escribir y pasar tests de repository
   - Commit: "Test y modelo: campo prioridad en Tarea"

2. **Continuar con DTO y Service** (30-40 min)
   - Modificar `TareaData`
   - Implementar métodos en `TareaService`
   - Escribir y pasar tests de service
   - Commit: "Test servicio: cambiar prioridad"
   - Commit: "Implementación servicio: gestión prioridades"

3. **Luego Controller** (20-30 min)
   - Añadir endpoints en `TareaController`
   - Escribir y pasar tests de controller
   - Commit: "Test controller: endpoints prioridad"
   - Commit: "Controller: cambiar y filtrar por prioridad"

4. **Finalmente Vista** (30-40 min)
   - Modificar `listaTareas.html`
   - Añadir badges y botones
   - Probar manualmente en el navegador
   - Commit: "Vista: mostrar y cambiar prioridades"

5. **Bonus si queda tiempo** (10-15 min)
   - Modificar formulario nueva tarea
   - Commit: "Bonus: selector prioridad en formulario"

### Ejemplo de Código para Badges (Thymeleaf)

```html
<!-- Badge con color según prioridad -->
<span th:class="'badge badge-' + 
    ${tarea.prioridad == 'ALTA' ? 'danger' : 
     (tarea.prioridad == 'BAJA' ? 'secondary' : 'primary')}"
    th:text="${tarea.prioridad}">
</span>
```

### Ejemplo de Endpoint Controller

```java
@PostMapping("/tareas/{id}/cambiar-prioridad")
public String cambiarPrioridad(@PathVariable(value="id") Long idTarea,
                                @RequestParam("prioridad") String prioridad,
                                HttpSession session) {
    // 1. Verificar usuario logeado
    // 2. Llamar al servicio
    // 3. Redirigir
}
```

### Valores de Prioridad Válidos

```java
// En el código Java
"BAJA"
"MEDIA"
"ALTA"
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
- Todos los tests pasan y prueban funcionalidad real
- Código limpio y bien estructurado
- Commits descriptivos y frecuentes
- Bonus implementado

### Notable (7-8):
- Funcionalidades 1, 2 y 3 completas
- Algunos tests faltan o son demasiado simples
- Código funcional pero mejorable
- Commits presentes pero poco descriptivos

### Aprobado (5-6):
- Funcionalidades 1 y 2 completas, 3 parcial
- Tests básicos que pasan
- Código funcional con algunos errores
- Commits mínimos

### Suspenso (0-4):
- Funcionalidades incompletas
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

**¡BUENA SUERTE! 🍀**

---

**Firma del alumno:** _________________________ **Fecha:** _____________
