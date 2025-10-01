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
public class UsuarioServiceAdminTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void testExisteAdministradorVacio() {
        // GIVEN - Base de datos sin usuarios
        
        // WHEN
        boolean existe = usuarioService.existeAdministrador();
        
        // THEN
        assertThat(existe).isFalse();
    }

    @Test
    public void testRegistrarPrimerAdministrador() {
        // GIVEN
        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@ua.es");
        admin.setPassword("123");
        admin.setNombre("Administrador");
        admin.setIsAdmin(true);

        // WHEN
        UsuarioData adminCreado = usuarioService.registrarConAdmin(admin);

        // THEN
        assertThat(adminCreado.getIsAdmin()).isTrue();
        assertThat(usuarioService.existeAdministrador()).isTrue();
    }

    @Test
    public void testNoPermitirSegundoAdministrador() {
        // GIVEN - Crear primer administrador
        UsuarioData primerAdmin = new UsuarioData();
        primerAdmin.setEmail("admin1@ua.es");
        primerAdmin.setPassword("123");
        primerAdmin.setNombre("Primer Admin");
        primerAdmin.setIsAdmin(true);
        usuarioService.registrarConAdmin(primerAdmin);

        // WHEN - Intentar crear segundo administrador
        UsuarioData segundoAdmin = new UsuarioData();
        segundoAdmin.setEmail("admin2@ua.es");
        segundoAdmin.setPassword("456");
        segundoAdmin.setNombre("Segundo Admin");
        segundoAdmin.setIsAdmin(true);

        // THEN
        assertThatThrownBy(() -> usuarioService.registrarConAdmin(segundoAdmin))
                .isInstanceOf(UsuarioServiceException.class)
                .hasMessage("Ya existe un administrador en el sistema");
    }

    @Test
    public void testRegistrarUsuarioNormalCuandoExisteAdmin() {
        // GIVEN - Crear administrador
        UsuarioData admin = new UsuarioData();
        admin.setEmail("admin@ua.es");
        admin.setPassword("123");
        admin.setNombre("Administrador");
        admin.setIsAdmin(true);
        usuarioService.registrarConAdmin(admin);

        // WHEN - Registrar usuario normal
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("usuario@ua.es");
        usuario.setPassword("456");
        usuario.setNombre("Usuario Normal");
        usuario.setIsAdmin(false);

        UsuarioData usuarioCreado = usuarioService.registrarConAdmin(usuario);

        // THEN
        assertThat(usuarioCreado.getIsAdmin()).isFalse();
        assertThat(usuarioService.existeAdministrador()).isTrue();
    }
}