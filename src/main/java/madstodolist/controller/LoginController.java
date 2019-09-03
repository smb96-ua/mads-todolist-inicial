package madstodolist.controller;

import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import madstodolist.model.Usuario.LoginStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class LoginController {

    @Autowired
    UsuarioService usuarioService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "loginForm";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model, RedirectAttributes flash) {

        // Llamada al servicio para comprobar si el login es correcto
        LoginStatus loginStatus = usuarioService.login(loginData.geteMail(), loginData.getPassword());

        if (loginStatus == LoginStatus.LOGIN_OK) {
            model.addAttribute("mensaje", "Hola " + loginData.geteMail() + "!!!");
            return "saludo";
        } else if (loginStatus == LoginStatus.USER_NOT_FOUND) {
            flash.addFlashAttribute("error", "No existe usuario");
            return "redirect:/login";
        } else if (loginStatus == LoginStatus.ERROR_PASSWORD) {
            flash.addFlashAttribute("error", "Contraseña incorrecta");
            return "redirect:/login";
        }
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String registroForm(Model model) {
        model.addAttribute("registroData", new RegistroData());
        return "registroForm";
    }

   @PostMapping("/registro")
   public String registroSubmit(@ModelAttribute RegistroData registroData, Model model, RedirectAttributes flash) {

        if (usuarioService.findByEmail(registroData.geteMail()) != null) {
            flash.addFlashAttribute("error", "El usuario " + registroData.geteMail() + " ya existe");
            return "redirect:/registro";
        }

        Usuario usuario = new Usuario(registroData.geteMail());
        usuario.setPassword(registroData.getPassword());
        usuario.setFechaNacimiento(registroData.getFechaNacimiento());
        usuario.setNombre(registroData.getNombre());

        usuarioService.registrar(usuario);
        return "redirect:/login";
   }

}
