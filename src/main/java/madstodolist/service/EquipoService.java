package madstodolist.service;

import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import madstodolist.model.Equipo;
import madstodolist.model.Usuario;
import madstodolist.repository.EquipoRepository;
import madstodolist.repository.UsuarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Transactional(readOnly = true)
    public EquipoData findById(Long id) {
        Equipo equipo = equipoRepository.findById(id).orElse(null);
        if (equipo == null) return null;
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional
    public EquipoData crearEquipo(String nombre) {
        Equipo equipo = new Equipo(nombre);
        equipoRepository.save(equipo);
        return modelMapper.map(equipo, EquipoData.class);
    }

    @Transactional(readOnly = true)
    public List<EquipoData> findAllOrderedByName() {
        List<Equipo> equipos = new ArrayList<>();
        equipoRepository.findAll().forEach(equipos::add);
        Collections.sort(equipos, (a, b) -> a.getNombre().compareTo(b.getNombre()));
        return equipos.stream()
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .toList();
    }

    @Transactional
    public void añadirUsuarioAEquipo(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (equipo == null || usuario == null) return;
        equipo.addUsuario(usuario);
    }

    @Transactional(readOnly = true)
    public List<UsuarioData> usuariosEquipo(Long equipoId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        if (equipo == null) return new ArrayList<>();
        return equipo.getUsuarios().stream()
                .map(usuario -> modelMapper.map(usuario, UsuarioData.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void eliminarUsuarioDeEquipo(Long equipoId, Long usuarioId) {
        Equipo equipo = equipoRepository.findById(equipoId).orElse(null);
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (equipo == null || usuario == null) return;
        equipo.getUsuarios().remove(usuario);
        usuario.getEquipos().remove(equipo);
    }

    @Transactional(readOnly = true)
    public List<EquipoData> equiposUsuario(Long usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);
        if (usuario == null) return new ArrayList<>();
        return usuario.getEquipos().stream()
                .map(equipo -> modelMapper.map(equipo, EquipoData.class))
                .collect(Collectors.toList());
    }
}
