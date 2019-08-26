package madstodolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("loginData", new LoginData());
        return "loginForm";
    }

    @PostMapping("/login")
    public String loginSubmit(@ModelAttribute LoginData loginData, Model model, RedirectAttributes flash) {

        // Llamada al servicio para comprobar si el login es correcto
        Boolean loginCorrecto = loginData.geteMail().equals("domingo@ua") && loginData.getPassword().equals("123");

        if (loginCorrecto) {
            model.addAttribute("mensaje", "Hola " + loginData.geteMail() + "!!!");
            return "saludo";
        }
        else
            flash.addFlashAttribute("error", "login incorrecto");
            return "redirect:/login";
    }
}
