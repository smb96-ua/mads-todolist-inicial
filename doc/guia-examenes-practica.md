# 📚 GUÍA DE EXÁMENES DE PRÁCTICA - MADS ToDoList

**Fecha**: 25 de octubre de 2025

---

## 🎯 RESUMEN DE EXÁMENES DISPONIBLES

Tienes **2 exámenes de práctica completos** para prepararte para el examen real:

| Examen | Funcionalidad | Dificultad | Base de Código | Estado |
|--------|--------------|------------|----------------|--------|
| **Examen 1** | Sistema de Prioridades | ⭐⭐⭐ Media | `/base` | ✅ Con solución |
| **Examen 2** | Sistema de Etiquetas | ⭐⭐⭐⭐ Alta | `/base2` | 📝 Por resolver |

---

## 📝 EXAMEN 1: Sistema de Prioridades

### 📄 Enunciado
`/doc/modelo-examen-base.md`

### 🎯 Funcionalidad
Añadir prioridades (BAJA, MEDIA, ALTA) a las tareas con:
- Campo simple en entidad (String)
- Badges de colores (gris, azul, rojo)
- Botones para cambiar prioridad
- Filtros por prioridad
- Selector en formularios

### 🔑 Conceptos Clave
- ✅ Modificar entidad existente
- ✅ DTOs y Services
- ✅ Endpoints GET/POST
- ✅ Thymeleaf: condicionales, badges
- ✅ Bootstrap: clases de colores
- ✅ Selectores HTML `<select>`

### 📊 Dificultad: **Media** ⭐⭐⭐
- Relaciones: Ninguna nueva
- Entidades nuevas: 0
- Archivos nuevos: ~5
- Tests mínimos: 9

### 📁 Recursos Disponibles
- ✅ Enunciado completo: `/doc/modelo-examen-base.md`
- ✅ Tu solución (75/100): `/base` (versión original)
- ✅ Solución perfecta (100/100): `/base` (versión actual)
- ✅ Corrección detallada: `/doc/correccion-examen.md`

### 💡 Para Qué Sirve
**Ideal para practicar:**
- Modificación de entidades existentes
- Vistas Thymeleaf con colores y badges
- Filtros simples
- Selectores de formulario
- TDD básico

---

## 🏷️ EXAMEN 2: Sistema de Etiquetas (Tags)

### 📄 Enunciado
`/doc/modelo-examen-2-etiquetas.md`

### 🎯 Funcionalidad
Añadir sistema de etiquetas para categorizar tareas:
- Nueva entidad `Etiqueta` (nombre, color)
- Relación Many-to-Many Tarea ↔ Etiqueta
- CRUD completo de etiquetas
- Asignar múltiples etiquetas a cada tarea
- Filtrar tareas por etiqueta
- Input color picker

### 🔑 Conceptos Clave
- ✅ **Relación Many-to-Many** (¡CRÍTICO!)
- ✅ Tabla intermedia JPA
- ✅ Nueva entidad completa
- ✅ CRUD completo (Create, Read, Update, Delete)
- ✅ Controller nuevo
- ✅ Múltiples vistas nuevas
- ✅ Input type="color"
- ✅ Badges con colores dinámicos
- ✅ Gestión bidireccional de relaciones

### 📊 Dificultad: **Alta** ⭐⭐⭐⭐
- Relaciones: **Many-to-Many** (compleja)
- Entidades nuevas: 1 (Etiqueta)
- Archivos nuevos: ~12
- Tests mínimos: 15

### 📁 Recursos Disponibles
- ✅ Enunciado completo: `/doc/modelo-examen-2-etiquetas.md`
- ⏳ Solución: **Por implementar** (¡tu turno!)
- 📂 Base de código: `/base2` (limpio, sin modificar)

### 💡 Para Qué Sirve
**Ideal para practicar:**
- **Relaciones Many-to-Many** (muy importante para examen)
- Crear entidades desde cero
- CRUD completo
- Controladores nuevos
- Múltiples vistas
- Colores dinámicos en CSS
- TDD avanzado con relaciones

---

## 🎓 ESTRATEGIA DE ESTUDIO RECOMENDADA

### Semana 1: Dominar Examen 1 (Prioridades)
```
Día 1-2: Hacer Examen 1 en 2 horas (sin ayuda)
Día 3: Comparar con solución perfecta
Día 4: Rehacer las partes que fallaste
Día 5: Hacer Examen 1 de nuevo (completo en 1.5h)
```

### Semana 2: Dominar Examen 2 (Etiquetas)
```
Día 1-2: Estudiar relaciones Many-to-Many
Día 3-4: Hacer Examen 2 en 2 horas (sin ayuda)
Día 5: Revisar y corregir
```

### Semana 3: Consolidar
```
Día 1: Examen 1 en 1 hora
Día 2: Examen 2 en 1.5 horas
Día 3: Crear tu propio examen mezclando conceptos
Día 4-5: Repasar conceptos débiles
```

---

## 📊 COMPARATIVA DE EXÁMENES

| Aspecto | Examen 1: Prioridades | Examen 2: Etiquetas |
|---------|----------------------|-------------------|
| **Tiempo estimado** | 1h 30min - 2h | 2h - 2h 30min |
| **Entidades nuevas** | 0 | 1 |
| **Relaciones JPA** | Ninguna | Many-to-Many |
| **Controllers nuevos** | 0 | 1 |
| **Vistas nuevas** | 0 | 2 |
| **Tests mínimos** | 9 | 15 |
| **Input especial** | `<select>` | `<input type="color">` |
| **Badges dinámicos** | 3 colores fijos | Colores variables |
| **CRUD** | Parcial (Update) | Completo |
| **Dificultad** | ⭐⭐⭐ | ⭐⭐⭐⭐ |

---

## 🔥 CONCEPTOS QUE CUBREN AMBOS EXÁMENES

### Examen 1 (Prioridades) te prepara para:
- ✅ Modificar entidades
- ✅ Añadir campos simples
- ✅ DTOs básicos
- ✅ Services con métodos nuevos
- ✅ Endpoints simples
- ✅ Vistas con condicionales
- ✅ Badges de Bootstrap
- ✅ Formularios con select
- ✅ Filtros GET

### Examen 2 (Etiquetas) te prepara para:
- ✅ **Relaciones Many-to-Many** ⭐ MUY IMPORTANTE
- ✅ Crear entidades desde cero
- ✅ Tabla intermedia JPA
- ✅ CRUD completo
- ✅ Controllers nuevos
- ✅ Múltiples vistas
- ✅ Estilos dinámicos CSS
- ✅ Input color picker
- ✅ Gestión bidireccional
- ✅ Endpoints complejos

### Juntos cubren el **95%** de lo que puede caer en el examen real ✅

---

## 📚 RECURSOS DE APOYO

### Documentación Disponible

| Documento | Ubicación | Contenido |
|-----------|-----------|-----------|
| **Guía de Estudio General** | `/doc/guia-estudio-examen.md` | Conceptos, ejemplos, comandos |
| **Enunciado Examen 1** | `/doc/modelo-examen-base.md` | Sistema de prioridades |
| **Corrección Examen 1** | `/doc/correccion-examen.md` | Tu solución vs perfecta |
| **Enunciado Examen 2** | `/doc/modelo-examen-2-etiquetas.md` | Sistema de etiquetas |
| **Solución Examen 1** | `/base/` (código) | Implementación 100/100 |
| **Base Examen 2** | `/base2/` (código) | Para que practiques |

---

## 🚀 CÓMO USAR ESTOS EXÁMENES

### Opción 1: Práctica Simulada Real (Recomendado)

```bash
# 1. Preparar entorno
cd base2
git checkout -b examen-etiquetas

# 2. Leer enunciado (5 min)
cat ../doc/modelo-examen-2-etiquetas.md

# 3. Poner temporizador: 2 horas
# 4. Cerrar TODA documentación, navegador, apuntes
# 5. Solo IDE + enunciado en papel
# 6. ¡A trabajar!

# 7. Después de 2 horas: parar
git add .
git commit -m "Fin de examen"

# 8. Revisar lo que faltó
# 9. Estudiar conceptos débiles
# 10. Repetir en 1 semana
```

### Opción 2: Práctica Guiada

```bash
# 1. Hacer Funcionalidad 1 (30 min)
#    - Leer solo esa parte del enunciado
#    - Implementar modelo + tests
#    - Commit

# 2. Hacer Funcionalidad 2 (40 min)
#    - Leer solo esa parte
#    - Implementar services + tests
#    - Commit

# 3. Hacer Funcionalidad 3 (50 min)
#    - Leer solo esa parte
#    - Implementar controllers + vistas + tests
#    - Commit

# 4. Bonus si queda tiempo (15 min)
```

### Opción 3: Estudio de Conceptos

```bash
# Estudiar un concepto específico del examen:

# Relación Many-to-Many:
grep -r "@ManyToMany" base/
# Ver cómo se implementa en la solución del Examen 1

# Badges con colores:
grep -r "badge" base/src/main/resources/templates/
# Ver ejemplos en las vistas

# Tests de controller:
cat base/src/test/java/madstodolist/controller/TareaWebTest.java
# Entender estructura de tests web
```

---

## 🎯 OBJETIVOS DE APRENDIZAJE

### Después del Examen 1 deberías dominar:
- ✅ Modificar entidades existentes
- ✅ Añadir campos simples a DTOs
- ✅ Crear métodos en Services
- ✅ Añadir endpoints a Controllers existentes
- ✅ Modificar vistas Thymeleaf
- ✅ Usar badges de Bootstrap
- ✅ Crear selectores HTML
- ✅ Escribir tests básicos

### Después del Examen 2 deberías dominar:
- ✅ **Crear relaciones Many-to-Many**
- ✅ Gestionar tablas intermedias
- ✅ Crear entidades desde cero
- ✅ Implementar CRUD completo
- ✅ Crear controllers nuevos
- ✅ Crear múltiples vistas
- ✅ Estilos CSS dinámicos
- ✅ Gestión bidireccional de relaciones
- ✅ Tests avanzados con relaciones

### Después de ambos estarás preparado para:
- ✅ **Aprobar el examen real con nota alta**
- ✅ Implementar cualquier funcionalidad nueva
- ✅ Trabajar con cualquier tipo de relación JPA
- ✅ Crear vistas profesionales con Bootstrap
- ✅ Seguir metodología TDD correctamente
- ✅ Gestionar el tiempo eficientemente

---

## ⏱️ GESTIÓN DEL TIEMPO

### Distribución Recomendada (2 horas)

| Fase | Examen 1 | Examen 2 | Actividad |
|------|----------|----------|-----------|
| **Setup** | 5 min | 5 min | Leer enunciado completo, planificar |
| **Modelo** | 15 min | 20 min | Entidad + Repository + Tests |
| **Service** | 35 min | 45 min | DTOs + Service + Tests |
| **Controller** | 25 min | 35 min | Endpoints + Tests |
| **Vista** | 30 min | 35 min | HTML + CSS + JavaScript |
| **Verificación** | 10 min | 10 min | Ejecutar todos los tests, probar app |
| **TOTAL** | **120 min** | **150 min** | |

### Si vas retrasado:
1. **Prioriza**: Modelo → Service → Controller básico
2. **Simplifica** vistas: Funcionalidad antes que estética
3. **Salta** el bonus si no da tiempo
4. **Deja** tests de controller para el final si necesario

---

## 🏆 CHECKLIST ANTES DEL EXAMEN REAL

Antes del examen, asegúrate de que puedes:

### Conceptos Técnicos
- [ ] Crear una entidad JPA desde cero
- [ ] Implementar relación Many-to-Many
- [ ] Crear y usar DTOs
- [ ] Implementar métodos @Transactional en Services
- [ ] Crear endpoints GET/POST/DELETE
- [ ] Modificar vistas Thymeleaf
- [ ] Usar badges de Bootstrap
- [ ] Implementar filtros/búsquedas
- [ ] Escribir tests de repository
- [ ] Escribir tests de service
- [ ] Escribir tests de controller con MockMvc

### Metodología
- [ ] Seguir ciclo TDD (Red-Green-Refactor)
- [ ] Hacer commits pequeños y descriptivos
- [ ] Leer enunciado completo antes de empezar
- [ ] Planificar tiempo por funcionalidad
- [ ] Ejecutar `./mvnw test` frecuentemente
- [ ] Probar en navegador antes de entregar

### Comandos
- [ ] `./mvnw test` - Ejecutar tests
- [ ] `./mvnw spring-boot:run` - Arrancar aplicación
- [ ] `git add . && git commit -m "..."` - Commit
- [ ] `git push origin rama` - Push
- [ ] Crear Pull Request en GitHub

---

## 💡 CONSEJOS FINALES

### Para el Examen 1 (Prioridades):
1. **Fácil de empezar**: Solo añadir un campo String
2. **Cuidado con**: Los badges de colores (condicionales Thymeleaf)
3. **No olvides**: El selector `<select>` en formularios
4. **Punto clave**: Filtros GET con parámetros en URL

### Para el Examen 2 (Etiquetas):
1. **Crítico**: Relación Many-to-Many bien implementada
2. **Cuidado con**: Gestión bidireccional (ambos lados)
3. **No olvides**: Input `type="color"` para el color picker
4. **Punto clave**: Badges con `style` dinámico

### General:
- ✅ **Tests primero**, implementación después
- ✅ **Commits frecuentes**: cada test que pase
- ✅ **No te bloquees**: salta y vuelve después
- ✅ **Prioriza**: funcionalidad > estética
- ✅ **Verifica**: ejecuta tests antes de cada commit

---

## 🎓 PRÓXIMOS PASOS

### Ahora mismo:
1. ✅ Lee el enunciado del Examen 2 completo
2. ⏱️ Agenda 2 horas libres para hacerlo
3. 📝 Prepara: papel, bolígrafo, enunciado impreso

### Esta semana:
1. 🔄 Repite Examen 1 en menos tiempo
2. 🆕 Haz Examen 2 completo
3. 📚 Repasa conceptos débiles

### Próxima semana:
1. 🏃 Examen 1 en 60 minutos
2. 🏃 Examen 2 en 90 minutos
3. 🎯 Crea tu propio mini-examen

---

**Fecha de creación**: 25 de octubre de 2025  
**Autor**: Guía de preparación para examen MADS  
**Versión**: 2.0 - Con 2 exámenes completos

**¡Mucha suerte con tu preparación! 🚀**
