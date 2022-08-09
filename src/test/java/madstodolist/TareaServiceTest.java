package madstodolist;

import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;

// Hemos eliminado todos los @Transactional de los tests
// y usado un script para limpiar la BD de test después de
// cada test
// https://dev.to/henrykeys/don-t-use-transactional-in-tests-40eb

@SpringBootTest
@Sql("/datos-test.sql")
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;

    @Test
    public void testNuevaTareaUsuario() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN
        // creamos una nueva tarea asociada a un usuario,
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS");

        // THEN
        // al recuperar el usuario usando el método findByEmail la tarea creada
        // está en la lista de tareas del usuario.

        Usuario usuario = usuarioService.findByEmail("user@ua");
        assertThat(usuario.getTareas().size() == 3);
        assertThat(usuario.getTareas()).contains(tarea);
    }

    @Test
    public void testBuscarTarea() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN
        // recuperamos una tarea de la base de datos a partir de su ID,

        Tarea lavarCoche = tareaService.findById(1L);

        // THEN
        // los datos de la tarea recuperada son correctos.

        assertThat(lavarCoche).isNotNull();
        assertThat(lavarCoche.getTitulo()).isEqualTo("Lavar coche");
    }

    @Test
    public void testModificarTarea() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,
        // creada una tarea nueva de un usuario y obtenido su identificador,

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Pagar el recibo");
        Long idNuevaTarea = tarea.getId();

        // WHEN
        // modificamos la tarea correspondiente a ese identificador,

        Tarea tareaModificada = tareaService.modificaTarea(idNuevaTarea, "Pagar la matrícula");

        // THEN
        // al buscar por el identificador en la base de datos se devuelve la tarea modificada

        Tarea tareaBD = tareaService.findById(idNuevaTarea);
        assertThat(tareaBD.getTitulo()).isEqualTo("Pagar la matrícula");

        // y el usuario tiene también esa tarea modificada.
        Usuario usuario = usuarioService.findById(1L);
        usuario.getTareas().contains(tareaBD);
    }

    @Test
    public void testBorrarTarea() {
        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql, añadida
        // una tarea nueva al usuario 1L y obtenido el identificador de la tarea,

        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Estudiar MADS");
        Long idNuevaTarea = tarea.getId();

        // WHEN
        // borramos la tarea correspondiente al identificador,

        tareaService.borraTarea(idNuevaTarea);

        // THEN
        // la tarea ya no está en la base de datos ni en las tareas del usuario.

        assertThat(tareaService.findById(tarea.getId())).isNull();
        assertThat(usuarioService.findById(1L).getTareas().size() == 2);
    }
}
