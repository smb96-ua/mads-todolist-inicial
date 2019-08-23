package madstodolist;

import madstodolist.model.Usuario;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UsuarioTest {

    @Test
    public void crearUsuario() throws Exception {
        Usuario usuario = new Usuario("juan.gutierrez@gmail.com");
        usuario.setNombre("Juan Gutiérrez");
        usuario.setPassword("12345678");
        assertThat(usuario.getEmail()).isEqualTo("juan.gutierrez@gmail.com");
        assertThat(usuario.getNombre()).isEqualTo("Juan Gutiérrez");
        assertThat(usuario.getPassword()).isEqualTo("12345678");
    }
}
