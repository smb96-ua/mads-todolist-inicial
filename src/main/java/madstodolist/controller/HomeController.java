package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ManagerUserSession managerUserSession;

    @GetMapping("/about")
    public String about(Model model) {
        return "about";
    }

    private void verificarAccesoAdministrador() {
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogueado == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no logueado");
        }
        
        UsuarioData usuarioLogueado = usuarioService.findById(idUsuarioLogueado);
        if (usuarioLogueado == null || !Boolean.TRUE.equals(usuarioLogueado.getIsAdmin())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Acceso solo para administradores");
        }
    }

    @GetMapping("/registrados")
    public String listadoUsuarios(Model model) {
        verificarAccesoAdministrador();
        
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();
        UsuarioData usuarioLogueado = usuarioService.findById(idUsuarioLogueado);
        model.addAttribute("usuarioLogueado", usuarioLogueado);
        
        List<UsuarioData> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }

    @GetMapping("/registrados/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario, Model model) {
        verificarAccesoAdministrador();
        
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();
        UsuarioData usuarioLogueado = usuarioService.findById(idUsuarioLogueado);
        model.addAttribute("usuarioLogueado", usuarioLogueado);
        
        UsuarioData usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            return "redirect:/registrados";
        }
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }

    @PostMapping("/registrados/{id}/bloquear")
    public String bloquearUsuario(@PathVariable(value="id") Long idUsuario) {
        verificarAccesoAdministrador();
        usuarioService.bloquearUsuario(idUsuario);
        return "redirect:/registrados/" + idUsuario;
    }

    @PostMapping("/registrados/{id}/desbloquear")
    public String desbloquearUsuario(@PathVariable(value="id") Long idUsuario) {
        verificarAccesoAdministrador();
        usuarioService.desbloquearUsuario(idUsuario);
        return "redirect:/registrados/" + idUsuario;
    }

}