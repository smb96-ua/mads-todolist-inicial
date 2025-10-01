# ToDoList MADS - Práctica 2

Aplicación ToDoList desarrollada para la asignatura [MADS](https://cvnet.cpd.ua.es/Guia-Docente/GuiaDocente/Index?wcodest=C203&wcodasi=34037&wlengua=es&scaca=2019-20) usando Spring Boot y plantillas Thymeleaf.

## Gestión del proyecto

- **Tablero Trello**: [ToDoList MADS](URL_DEL_TABLERO_TRELLO) (Añadir URL después de crear el tablero)
- **Repositorio GitHub**: [mads-todolist-inicial](https://github.com/smb96-ua/mads-todolist-inicial)

## Requisitos

Necesitas tener instalado en tu sistema:

- Java 8

## Ejecución

Puedes ejecutar la aplicación usando el _goal_ `run` del _plugin_ Maven 
de Spring Boot:

```
$ ./mvnw spring-boot:run 
```   

También puedes generar un `jar` y ejecutarlo:

```
$ ./mvnw package
$ java -jar target/mads-todolist-inicial-0.0.1-SNAPSHOT.jar 
```

Una vez lanzada la aplicación puedes abrir un navegador y probar la página de inicio:

- [http://localhost:8080/login](http://localhost:8080/login)
