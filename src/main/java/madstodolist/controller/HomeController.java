package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/registrados")
    public String listadoUsuarios(Model model) {
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogueado != null) {
            UsuarioData usuarioLogueado = usuarioService.findById(idUsuarioLogueado);
            model.addAttribute("usuarioLogueado", usuarioLogueado);
        }
        List<UsuarioData> usuarios = usuarioService.findAll();
        model.addAttribute("usuarios", usuarios);
        return "listaUsuarios";
    }

    @GetMapping("/registrados/{id}")
    public String descripcionUsuario(@PathVariable(value="id") Long idUsuario, Model model) {
        Long idUsuarioLogueado = managerUserSession.usuarioLogeado();
        if (idUsuarioLogueado != null) {
            UsuarioData usuarioLogueado = usuarioService.findById(idUsuarioLogueado);
            model.addAttribute("usuarioLogueado", usuarioLogueado);
        }
        UsuarioData usuario = usuarioService.findById(idUsuario);
        if (usuario == null) {
            // Podríamos lanzar una excepción personalizada o redireccionar
            return "redirect:/registrados";
        }
        model.addAttribute("usuario", usuario);
        return "descripcionUsuario";
    }

}