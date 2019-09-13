package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaServiceTest {

    @Autowired
    UsuarioService usuarioService;

    @Autowired
    TareaService tareaService;


    @Test
    public void testNuevaTareaUsuario() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        // WHEN
        Tarea tarea = tareaService.nuevaTareaUsuario(1L, "Práctica 1 de MADS");

        // THEN

        Usuario usuario = usuarioService.findByEmail("ana.garcia@gmail.com");
        assertThat(usuario.getTareas()).contains(tarea);
    }

    @Test
    public void testListadoTareas() {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        Usuario usuario = new Usuario("ana.garcia@gmail.com");
        usuario.setId(1L);

        Tarea lavarCoche = new Tarea(usuario, "Lavar coche");
        lavarCoche.setId(1L);

        // WHEN

        List<Tarea> tareas = tareaService.allTareasUsuario(1L);

        // THEN

        assertThat(tareas).contains(lavarCoche);
    }
}
