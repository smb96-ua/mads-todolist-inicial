package madstodolist.service;

import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Sql(scripts = "/clean-db.sql")
public class EquipoServiceTest {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    public void crearNuevoEquipo() {
        EquipoData equipoData = equipoService.crearEquipo("Proyecto A");
        
        assertThat(equipoData.getId()).isNotNull();
        assertThat(equipoData.getNombre()).isEqualTo("Proyecto A");
    }

    @Test
    public void buscarEquipoPorId() {
        EquipoData nuevoEquipo = equipoService.crearEquipo("Proyecto A");
        
        EquipoData equipoRecuperado = equipoService.findById(nuevoEquipo.getId());
        
        assertThat(equipoRecuperado).isNotNull();
        assertThat(equipoRecuperado.getNombre()).isEqualTo("Proyecto A");
    }

    @Test
    public void listarEquipos() {
        equipoService.crearEquipo("Proyecto A");
        equipoService.crearEquipo("Proyecto B");
        
        List<EquipoData> equipos = equipoService.findAllOrderedByName();
        
        assertThat(equipos).hasSize(2);
        assertThat(equipos.get(0).getNombre()).isEqualTo("Proyecto A");
        assertThat(equipos.get(1).getNombre()).isEqualTo("Proyecto B");
    }

    @Test
    @Transactional
    public void añadirUsuarioAEquipo() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        EquipoData equipo = equipoService.crearEquipo("Proyecto A");
        
        equipoService.añadirUsuarioAEquipo(equipo.getId(), usuario.getId());
        
        EquipoData equipoRecuperado = equipoService.findById(equipo.getId());
        assertThat(equipoRecuperado).isNotNull();
    }
}
