package madstodolist.model;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoTest {

    @Test
    public void crearEquipo() {
        Equipo equipo = new Equipo("Proyecto A");
        
        assertThat(equipo.getNombre()).isEqualTo("Proyecto A");
    }

    @Test
    public void comprobarIgualdadEquipos() {
        Equipo equipo1 = new Equipo("Proyecto A");
        Equipo equipo2 = new Equipo("Proyecto B");
        Equipo equipo3 = new Equipo("Proyecto A");
        
        assertThat(equipo1).isEqualTo(equipo3);
        assertThat(equipo1).isNotEqualTo(equipo2);
    }

    @Test
    public void añadirUsuariosAEquipo() {
        Usuario usuario1 = new Usuario("user1@ua.es");
        Usuario usuario2 = new Usuario("user2@ua.es");
        Equipo equipo = new Equipo("Proyecto A");
        
        equipo.addUsuario(usuario1);
        equipo.addUsuario(usuario2);
        
        assertThat(equipo.getUsuarios()).hasSize(2);
        assertThat(equipo.getUsuarios()).contains(usuario1, usuario2);
    }
}
