package madstodolist.repository;

import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoTest {

    @Autowired
    private EquipoRepository equipoRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    public void crearEquipoEnBD() {
        Equipo equipo = new Equipo("Proyecto A");
        equipoRepository.save(equipo);
        
        assertThat(equipo.getId()).isNotNull();
    }

    @Test
    @Transactional
    public void buscarEquipoPorId() {
        Equipo equipo = new Equipo("Proyecto A");
        equipoRepository.save(equipo);
        
        Equipo equipoRecuperado = equipoRepository.findById(equipo.getId()).orElse(null);
        
        assertThat(equipoRecuperado).isNotNull();
        assertThat(equipoRecuperado.getNombre()).isEqualTo("Proyecto A");
    }

    @Test
    @Transactional
    public void añadirUsuariosEquipoEnBD() {
        Usuario usuario = new Usuario("user@ua.es");
        usuarioRepository.save(usuario);
        
        Equipo equipo = new Equipo("Proyecto A");
        equipo.addUsuario(usuario);
        equipoRepository.save(equipo);
        
        Equipo equipoRecuperado = equipoRepository.findById(equipo.getId()).orElse(null);
        assertThat(equipoRecuperado.getUsuarios()).hasSize(1);
        assertThat(equipoRecuperado.getUsuarios()).contains(usuario);
    }

    @Test
    @Transactional
    public void listarTodosLosEquipos() {
        Equipo equipo1 = new Equipo("Proyecto A");
        Equipo equipo2 = new Equipo("Proyecto B");
        equipoRepository.save(equipo1);
        equipoRepository.save(equipo2);
        
        Iterable<Equipo> equipos = equipoRepository.findAll();
        
        assertThat(equipos).hasSize(2);
        assertThat(equipos).contains(equipo1, equipo2);
    }
}
