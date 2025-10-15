package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.EquipoData;
import madstodolist.dto.UsuarioData;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class EquipoController {

    @Autowired
    private EquipoService equipoService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @GetMapping("/equipos")
    public String listarEquipos(Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        List<EquipoData> equipos = equipoService.findAllOrderedByName();
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipos", equipos);
        return "listaEquipos";
    }

    @GetMapping("/equipos/{id}")
    public String mostrarEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        EquipoData equipo = equipoService.findById(idEquipo);
        List<UsuarioData> usuarios = equipoService.usuariosEquipo(idEquipo);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        model.addAttribute("usuarios", usuarios);
        return "detalleEquipo";
    }

    @GetMapping("/equipos/nuevo")
    public String formNuevoEquipo(Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        model.addAttribute("usuario", usuario);
        return "formNuevoEquipo";
    }

    @PostMapping("/equipos")
    public String crearEquipo(@RequestParam("nombre") String nombre, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        equipoService.crearEquipo(nombre);
        return "redirect:/equipos";
    }

    @PostMapping("/equipos/{id}/usuarios/{usuarioId}/agregar")
    public String agregarUsuarioAEquipo(@PathVariable(value="id") Long idEquipo,
                                        @PathVariable(value="usuarioId") Long idUsuario,
                                        HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        equipoService.añadirUsuarioAEquipo(idEquipo, idUsuario);
        return "redirect:/equipos/" + idEquipo;
    }

    @DeleteMapping("/equipos/{id}/usuarios/{usuarioId}")
    @ResponseBody
    public String eliminarUsuarioDeEquipo(@PathVariable(value="id") Long idEquipo,
                                          @PathVariable(value="usuarioId") Long idUsuario,
                                          HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        equipoService.eliminarUsuarioDeEquipo(idEquipo, idUsuario);
        return "";
    }

    @GetMapping("/equipos/{id}/editar")
    public String formEditarEquipo(@PathVariable(value="id") Long idEquipo, Model model, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        UsuarioData usuario = usuarioService.findById(idUsuarioLogeado);
        EquipoData equipo = equipoService.findById(idEquipo);
        
        model.addAttribute("usuario", usuario);
        model.addAttribute("equipo", equipo);
        return "formEditarEquipo";
    }

    @PostMapping("/equipos/{id}/editar")
    public String editarEquipo(@PathVariable(value="id") Long idEquipo,
                               @RequestParam("nombre") String nombre,
                               HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        equipoService.renombrarEquipo(idEquipo, nombre);
        return "redirect:/equipos/" + idEquipo;
    }

    @DeleteMapping("/equipos/{id}")
    @ResponseBody
    public String eliminarEquipo(@PathVariable(value="id") Long idEquipo, HttpSession session) {
        Long idUsuarioLogeado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogeado == null) {
            return "redirect:/login";
        }

        equipoService.eliminarEquipo(idEquipo);
        return "";
    }
}
