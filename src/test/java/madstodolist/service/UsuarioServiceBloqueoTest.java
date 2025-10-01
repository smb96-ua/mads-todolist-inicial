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
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        // WHEN
        usuarioService.bloquearUsuario(usuario.getId());
        UsuarioService.LoginStatus status = usuarioService.login("user@ua.es", "123");

        // THEN
        assertThat(status).isEqualTo(UsuarioService.LoginStatus.USER_BLOCKED);
    }

    @Test
    public void usuarioDesbloqueadoPuedeLoguear() {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        // Bloqueamos y luego desbloqueamos
        usuarioService.bloquearUsuario(usuario.getId());
        usuarioService.desbloquearUsuario(usuario.getId());

        // WHEN
        UsuarioService.LoginStatus status = usuarioService.login("user@ua.es", "123");

        // THEN
        assertThat(status).isEqualTo(UsuarioService.LoginStatus.LOGIN_OK);
    }

    @Test
    public void bloquearUsuarioActualizaEstado() {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        // WHEN
        UsuarioData usuarioBloqueado = usuarioService.bloquearUsuario(usuario.getId());

        // THEN
        assertThat(usuarioBloqueado.getBloqueado()).isTrue();
        
        // Verificar que se actualiza en BD
        UsuarioData usuarioEnBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioEnBD.getBloqueado()).isTrue();
    }

    @Test
    public void desbloquearUsuarioActualizaEstado() {
        // GIVEN
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setNombre("Usuario Test");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        usuarioService.bloquearUsuario(usuario.getId());

        // WHEN
        UsuarioData usuarioDesbloqueado = usuarioService.desbloquearUsuario(usuario.getId());

        // THEN
        assertThat(usuarioDesbloqueado.getBloqueado()).isFalse();
        
        // Verificar que se actualiza en BD
        UsuarioData usuarioEnBD = usuarioService.findById(usuario.getId());
        assertThat(usuarioEnBD.getBloqueado()).isFalse();
    }

    @Test
    public void noSePuedeBloquearAdministrador() {
        // GIVEN
        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@ua.es");
        admin.setNombre("Admin Test");
        admin.setPassword("123");
        admin.setIsAdmin(true);
        UsuarioData adminRegistrado = usuarioService.registrarConAdmin(admin);

        // WHEN & THEN
        assertThatThrownBy(() -> usuarioService.bloquearUsuario(adminRegistrado.getId()))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessageContaining("No se puede bloquear a un administrador");
    }

    @Test
    public void bloquearUsuarioInexistenteLanzaExcepcion() {
        // WHEN & THEN
        assertThatThrownBy(() -> usuarioService.bloquearUsuario(999L))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessageContaining("No existe usuario con id 999");
    }

    @Test
    public void desbloquearUsuarioInexistenteLanzaExcepcion() {
        // WHEN & THEN
        assertThatThrownBy(() -> usuarioService.desbloquearUsuario(999L))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessageContaining("No existe usuario con id 999");
    }
}