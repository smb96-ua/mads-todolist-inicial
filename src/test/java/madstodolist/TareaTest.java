package madstodolist;


import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.model.UsuarioRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest
public class TareaTest {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Test
    public void crearTarea() throws Exception {
        // GIVEN
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");

        // WHEN

        Tarea tarea = new Tarea(usuario, "Práctica 1 de MADS");

        // THEN

        assertThat(tarea.getTitulo()).isEqualTo("Práctica 1 de MADS");
        assertThat(tarea.getUsuario()).isEqualTo(usuario);
    }

    @Test
    public void comprobarIgualdadSinId() {
        // GIVEN

        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        Tarea tarea1 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea2 = new Tarea(usuario, "Práctica 1 de MADS");
        Tarea tarea3 = new Tarea(usuario, "Pagar el alquiler");

        // THEN

        assertThat(tarea1).isEqualTo(tarea2);
        assertThat(tarea1).isNotEqualTo(tarea3);
    }

}
