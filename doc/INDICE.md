# 📚 ÍNDICE COMPLETO - Preparación Examen MADS

**Última actualización**: 25 de octubre de 2025

---

## 🎯 RESUMEN EJECUTIVO

Tienes todo lo necesario para preparar el examen práctico de MADS:

✅ **2 exámenes de práctica completos**  
✅ **1 examen con solución perfecta (100/100)**  
✅ **Guías de estudio completas**  
✅ **Código base limpio para practicar**

---

## 📁 ESTRUCTURA DEL PROYECTO

```
mads-todolist-inicial/
├── base/                    ← Examen 1 CON SOLUCIÓN (100/100)
│   ├── src/                 ← Código perfecto del Sistema de Prioridades
│   └── README.md            ← Cómo usar esta solución
│
├── base2/                   ← Examen 2 SIN RESOLVER (para practicar)
│   ├── src/                 ← Código base limpio
│   └── README.md            ← Instrucciones del examen 2
│
└── doc/                     ← TODA LA DOCUMENTACIÓN
    ├── modelo-examen-1-prioridades.md      ← Enunciado Examen 1
    ├── modelo-examen-2-etiquetas.md        ← Enunciado Examen 2
    ├── correccion-examen.md                ← Corrección Examen 1 (75→100)
    ├── guia-estudio-examen.md              ← Guía completa (1000+ líneas)
    ├── guia-examenes-practica.md           ← Comparativa de exámenes
    └── INDICE.md                           ← Este archivo
```

---

## 📝 DOCUMENTOS DISPONIBLES

### 1. 🎓 Guías de Estudio

| Documento | Tamaño | Propósito | Cuándo usarlo |
|-----------|--------|-----------|---------------|
| **guia-estudio-examen.md** | 1000+ líneas | Conceptos teóricos y ejemplos completos | Antes de hacer exámenes |
| **guia-examenes-practica.md** | 400 líneas | Comparativa de los 2 exámenes | Para planificar tu estudio |
| **INDICE.md** | Este archivo | Navegación rápida | Cuando no sepas por dónde empezar |

### 2. 📋 Enunciados de Exámenes

| Examen | Documento | Funcionalidad | Dificultad | Base de Código |
|--------|-----------|--------------|------------|----------------|
| **Examen 1** | `modelo-examen-1-prioridades.md` | Sistema de Prioridades | ⭐⭐⭐ | `/base` |
| **Examen 2** | `modelo-examen-2-etiquetas.md` | Sistema de Etiquetas | ⭐⭐⭐⭐ | `/base2` |

### 3. ✅ Soluciones y Correcciones

| Documento | Contenido | Utilidad |
|-----------|-----------|----------|
| **correccion-examen.md** | Corrección detallada de tu Examen 1 (75/100 → 100/100) | Ver qué errores cometiste |
| `/base` (código) | Solución perfecta del Examen 1 | Referencia de código correcto |

---

## 🗺️ ROADMAP DE ESTUDIO

### Fase 1: Fundamentos (Días 1-3)
```
📖 Leer: guia-estudio-examen.md (secciones 1-5)
🎯 Objetivo: Entender arquitectura Spring Boot y TDD
✅ Checklist:
   - Entiendes capas: Model → Repository → Service → Controller → Vista
   - Sabes qué es TDD (Test-Driven Development)
   - Conoces anotaciones JPA básicas
```

### Fase 2: Examen 1 - Prioridades (Días 4-7)
```
📖 Leer: modelo-examen-1-prioridades.md
⏱️ Hacer: Examen 1 completo en 2 horas (SIN AYUDA)
📊 Comparar: Tu código vs /base (solución perfecta)
📖 Leer: correccion-examen.md
🔄 Repetir: Examen 1 en menos de 1.5 horas
```

### Fase 3: Examen 2 - Etiquetas (Días 8-12)
```
📖 Leer: modelo-examen-2-etiquetas.md
📚 Estudiar: Relaciones Many-to-Many (guia-estudio-examen.md sección 3)
⏱️ Hacer: Examen 2 completo en 2 horas (SIN AYUDA)
🔍 Revisar: Tu implementación
🔄 Repetir: Partes difíciles
```

### Fase 4: Consolidación (Días 13-15)
```
🏃 Examen 1 en 1 hora
🏃 Examen 2 en 1.5 horas
📝 Crear tu propio mini-examen
🎯 Repasar conceptos débiles
```

---

## 🎯 RUTA RÁPIDA SEGÚN TU SITUACIÓN

### 😰 "El examen es en 3 días"
```
Día 1: Lee guia-estudio-examen.md (secciones críticas)
Día 2: Haz Examen 1 completo (2h) + compara con solución
Día 3: Repasa conceptos del Examen 1 + lee Examen 2
```

### 😊 "Tengo 1 semana"
```
Día 1-2: Lee guía de estudio + Haz Examen 1
Día 3: Compara con solución + corrige errores
Día 4-5: Haz Examen 2
Día 6: Repite ambos exámenes más rápido
Día 7: Repasa conceptos débiles
```

### 😎 "Tengo 2 semanas" (Ideal)
```
Semana 1:
  - Estudia teoría con calma
  - Haz Examen 1 (2h)
  - Analiza solución perfecta
  - Rehaz Examen 1 (1.5h)
  
Semana 2:
  - Estudia Many-to-Many
  - Haz Examen 2 (2h)
  - Revisa y corrige
  - Practica ambos exámenes en menos tiempo
```

---

## 🔍 BUSCAR INFORMACIÓN ESPECÍFICA

### "¿Cómo hago X?"

| Quiero hacer... | Buscar en... | Sección |
|----------------|-------------|---------|
| **Relación Many-to-Many** | `guia-estudio-examen.md` | "1.3 JPA - Relaciones Many-to-Many" |
| **Badges de colores** | `/base/src/main/resources/templates/listaTareas.html` | Línea ~40 |
| **Selector de formulario** | `/base/src/main/resources/templates/formNuevaTarea.html` | Línea ~15 |
| **Tests de Service** | `/base/src/test/java/madstodolist/service/TareaServiceTest.java` | Todo el archivo |
| **Tests de Controller** | `/base/src/test/java/madstodolist/controller/TareaWebTest.java` | Todo el archivo |
| **Input color picker** | `modelo-examen-2-etiquetas.md` | "Ejemplo de Input Color" |
| **Filtros GET** | `/base/src/main/java/madstodolist/controller/TareaController.java` | Método `listadoTareasPorPrioridad` |

### Comandos útiles para buscar código

```bash
# Buscar todas las relaciones Many-to-Many
grep -r "@ManyToMany" base/src/

# Ver ejemplos de badges
grep -r "badge" base/src/main/resources/templates/

# Ver todos los tests
find base/src/test -name "*Test.java"

# Buscar un concepto en la guía
grep -n "Many-to-Many" doc/guia-estudio-examen.md
```

---

## 📊 COMPARATIVA RÁPIDA DE EXÁMENES

| Aspecto | Examen 1: Prioridades | Examen 2: Etiquetas |
|---------|----------------------|-------------------|
| **Concepto principal** | Campo simple + filtros | Relación Many-to-Many |
| **Dificultad** | ⭐⭐⭐ Media | ⭐⭐⭐⭐ Alta |
| **Tiempo estimado** | 1.5 - 2h | 2 - 2.5h |
| **Entidades nuevas** | 0 | 1 |
| **Controllers nuevos** | 0 | 1 |
| **Vistas nuevas** | 0 | 2 |
| **Tests mínimos** | 9 | 15 |
| **Lo más difícil** | Badges con colores | Gestión bidireccional |
| **Perfecto para practicar** | Vistas, filtros, selectores | Relaciones, CRUD completo |

---

## ✅ CHECKLIST PRE-EXAMEN

Antes del examen real, verifica que puedes:

### Técnico
- [ ] Crear una entidad JPA con anotaciones
- [ ] Implementar relación Many-to-Many
- [ ] Crear un Repository (interface)
- [ ] Crear un Service con @Transactional
- [ ] Crear un Controller con endpoints GET/POST
- [ ] Modificar vistas Thymeleaf
- [ ] Usar badges de Bootstrap
- [ ] Escribir tests de repository
- [ ] Escribir tests de service
- [ ] Escribir tests de controller con MockMvc

### Metodología
- [ ] Entiendes el ciclo TDD: Red → Green → Refactor
- [ ] Sabes hacer commits pequeños y descriptivos
- [ ] Puedes leer un enunciado y planificar
- [ ] Sabes gestionar el tiempo (no quedarte atascado)

### Herramientas
- [ ] `./mvnw test` - Ejecutar todos los tests
- [ ] `./mvnw spring-boot:run` - Arrancar app
- [ ] `git add . && git commit -m "..."` - Commit
- [ ] Crear Pull Request en GitHub

---

## 🚀 CÓMO EMPEZAR AHORA MISMO

### Si nunca has hecho un examen de práctica:

```bash
# 1. Ve a la carpeta del proyecto
cd /home/sergio/Escritorio/CUARTO\ CARRERA/MADS/mads-todolist-inicial

# 2. Lee la guía de estudio (al menos las primeras 200 líneas)
less doc/guia-estudio-examen.md

# 3. Lee el enunciado del Examen 1
less doc/modelo-examen-1-prioridades.md

# 4. Crea una rama para trabajar
cd base
git checkout -b mi-intento-examen1

# 5. Pon temporizador de 2 horas
# 6. Cierra TODO: navegador, apuntes, documentación
# 7. Solo deja: IDE + enunciado en papel
# 8. ¡Empieza!

# 9. Después de 2 horas (aunque no termines):
git add .
git commit -m "Fin de mi primer intento - Examen 1"

# 10. Compara con la solución perfecta (está en la misma carpeta base/)
# 11. Lee doc/correccion-examen.md para ver errores comunes
```

### Si ya hiciste el Examen 1:

```bash
# 1. Ve a la carpeta del Examen 2
cd /home/sergio/Escritorio/CUARTO\ CARRERA/MADS/mads-todolist-inicial/base2

# 2. Lee el enunciado
less ../doc/modelo-examen-2-etiquetas.md

# 3. Lee sobre relaciones Many-to-Many
grep -A 50 "Many-to-Many" ../doc/guia-estudio-examen.md

# 4. Prepárate y lánzate
git checkout -b examen-etiquetas

# 5. Temporizador: 2 horas
# 6. ¡A trabajar!
```

---

## 💡 CONSEJOS DE ORO

### Para Estudiar
1. **No memorices código**: Entiende los conceptos
2. **Practica escribiendo**: No solo leas, implementa
3. **Haz tests de verdad**: No copies sin entender
4. **Commits frecuentes**: Te ayudan a organizarte

### Para el Examen
1. **Lee TODO el enunciado** antes de empezar
2. **Planifica 5 minutos**: Qué harás y en qué orden
3. **Tests primero**: Red → Green → Refactor
4. **No te bloquees**: Si algo no sale, salta y vuelve
5. **Prioriza funcionalidad**: Primero que funcione, luego que se vea bonito
6. **Verifica frecuentemente**: `./mvnw test` cada 15 minutos

### Si te Atascas
1. **Respira** hondo 3 veces
2. **Lee el error** con calma (Maven te dice qué falta)
3. **Busca en el código** de /base (Examen 1 resuelto)
4. **Simplifica**: ¿Puedes hacer una versión más simple primero?
5. **Salta temporalmente**: Avanza en otra funcionalidad

---

## 📞 PREGUNTAS FRECUENTES

### "¿Por dónde empiezo?"
Lee `guia-estudio-examen.md` (al menos 30 minutos) y luego haz el Examen 1.

### "¿Cuánto tiempo necesito?"
Mínimo 1 semana practicando 2 horas al día. Ideal: 2 semanas.

### "¿Qué es más importante?"
Relaciones JPA (Many-to-Many), TDD, y saber escribir tests.

### "¿Debo memorizar código?"
No. Entiende los conceptos. El código sale solo.

### "¿Es normal no terminar en 2 horas?"
Sí, en tu primer intento. Con práctica lo harás en 1.5h.

### "¿Qué hago si los tests fallan?"
1. Lee el error completo
2. Verifica que compilas sin errores
3. Revisa anotaciones JPA
4. Compara con el código de /base

---

## 🏆 OBJETIVO FINAL

Después de practicar con estos 2 exámenes, deberías poder:

✅ Implementar **cualquier funcionalidad nueva** en una app Spring Boot  
✅ Gestionar **relaciones JPA** (Many-to-One, Many-to-Many)  
✅ Crear **entidades, services, controllers y vistas** desde cero  
✅ Escribir **tests significativos** que realmente prueben funcionalidad  
✅ Seguir **metodología TDD** correctamente  
✅ **Gestionar el tiempo** eficientemente en 2 horas  
✅ **Aprobar el examen real con nota alta** 🎯

---

## 📅 ÚLTIMA REVISIÓN ANTES DEL EXAMEN

**1 día antes del examen:**

```bash
# Revisa estos archivos:
1. doc/guia-estudio-examen.md (secciones "Errores Comunes")
2. doc/modelo-examen-1-prioridades.md (repasa requisitos)
3. /base/src/main/java/madstodolist/model/Tarea.java (ejemplo entidad)
4. /base/src/main/java/madstodolist/service/TareaService.java (ejemplo service)
5. /base/src/test/java/madstodolist/service/TareaServiceTest.java (ejemplo tests)

# Ejecuta una vez para recordar comandos:
cd base
./mvnw test
./mvnw spring-boot:run
# Ctrl+C para parar
```

**Día del examen:**

- ☕ Café (opcional pero recomendado)
- 📝 Papel y bolígrafo
- 🖥️ IDE configurado y funcionando
- 🧘 Mente tranquila y positiva
- 💪 Confianza: ¡Has practicado suficiente!

---

**¡MUCHA SUERTE EN TU EXAMEN! 🍀🚀**

**Recuerda**: No es sobre memorizar código, es sobre entender conceptos y aplicar TDD.

---

**Creado**: 25 de octubre de 2025  
**Versión**: 1.0  
**Autor**: Guía de navegación para preparación examen MADS
