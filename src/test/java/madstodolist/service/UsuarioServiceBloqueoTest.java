package madstodolist.service;

import madstodolist.dto.UsuarioData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class UsuarioServiceBloqueoTest {

    @Autowired
    UsuarioService usuarioService;

    @Test
    public void usuarioBloqueadoNoPuedeLoguear() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        usuarioService.bloquearUsuario(usuario.getId());
        UsuarioService.LoginStatus status = usuarioService.login("user@ua.es", "123");

        assertThat(status).isEqualTo(UsuarioService.LoginStatus.USER_BLOCKED);
    }

    @Test
    public void usuarioDesbloqueadoPuedeLoguear() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        usuarioService.bloquearUsuario(usuario.getId());
        usuarioService.desbloquearUsuario(usuario.getId());

        UsuarioService.LoginStatus status = usuarioService.login("user@ua.es", "123");

        assertThat(status).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);
    }

    @Test
    public void bloquearUsuarioActualizaEstado() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        UsuarioData usuarioBloqueado = usuarioService.bloquearUsuario(usuario.getId());

        assertThat(usuarioBloqueado.getBloqueado()).isTrue();
        
        UsuarioData usuarioEnBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioEnBD.getBloqueado()).isTrue();
    }

    @Test
    public void desbloquearUsuarioActualizaEstado() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        usuarioService.bloquearUsuario(usuario.getId());

        UsuarioData usuarioDesbloqueado = usuarioService.desbloquearUsuario(usuario.getId());

        assertThat(usuarioDesbloqueado.getBloqueado()).isFalse();
        
        UsuarioData usuarioEnBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioEnBD.getBloqueado()).isFalse();
    }

    @Test
    public void noSePuedeBloquearAdministrador() {
        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@ua.es");
        admin.setNombre("Admin Test");
        admin.setPassword("123");
        admin.setIsAdmin(true);
        UsuarioData adminRegistrado = usuarioService.registrarConAdmin(admin);

        assertThatThrownBy(() -> usuarioService.bloquearUsuario(adminRegistrado.getId()))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessageContaining("No se puede bloquear a un administrador");
    }

    @Test
    public void bloquearUsuarioInexistenteLanzaExcepcion() {
        assertThatThrownBy(() -> usuarioService.bloquearUsuario(999L))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessageContaining("No existe usuario con id 999");
    }

    @Test
    public void desbloquearUsuarioInexistenteLanzaExcepcion() {
        assertThatThrownBy(() -> usuarioService.desbloquearUsuario(999L))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessageContaining("No existe usuario con id 999");
    }
}