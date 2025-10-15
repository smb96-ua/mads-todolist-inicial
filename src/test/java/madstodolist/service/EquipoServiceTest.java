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

    @Test
    @Transactional
    public void obtenerUsuariosDeEquipo() {
        UsuarioData usuario1 = new UsuarioData();
        usuario1.setEmail("user1@ua.es");
        usuario1.setPassword("123");
        usuario1 = usuarioService.registrar(usuario1);

        UsuarioData usuario2 = new UsuarioData();
        usuario2.setEmail("user2@ua.es");
        usuario2.setPassword("123");
        usuario2 = usuarioService.registrar(usuario2);
        
        EquipoData equipo = equipoService.crearEquipo("Proyecto A");
        
        equipoService.añadirUsuarioAEquipo(equipo.getId(), usuario1.getId());
        equipoService.añadirUsuarioAEquipo(equipo.getId(), usuario2.getId());
        
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(equipo.getId());
        
        assertThat(usuarios).hasSize(2);
    }

    @Test
    @Transactional
    public void eliminarUsuarioDeEquipo() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        EquipoData equipo = equipoService.crearEquipo("Proyecto A");
        
        equipoService.añadirUsuarioAEquipo(equipo.getId(), usuario.getId());
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(equipo.getId());
        assertThat(usuarios).hasSize(1);
        
        equipoService.eliminarUsuarioDeEquipo(equipo.getId(), usuario.getId());
        usuarios = equipoService.usuariosEquipo(equipo.getId());
        assertThat(usuarios).hasSize(0);
    }

    @Test
    @Transactional
    public void obtenerEquiposDeUsuario() {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);
        
        EquipoData equipo1 = equipoService.crearEquipo("Proyecto A");
        EquipoData equipo2 = equipoService.crearEquipo("Proyecto B");
        
        equipoService.añadirUsuarioAEquipo(equipo1.getId(), usuario.getId());
        equipoService.añadirUsuarioAEquipo(equipo2.getId(), usuario.getId());
        
        List<EquipoData> equipos = equipoService.equiposUsuario(usuario.getId());
        
        assertThat(equipos).hasSize(2);
    }
}
