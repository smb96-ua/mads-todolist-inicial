package madstodolist.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String loginForm(Map<String, Object> model) {
        model.put("loginData", new LoginData());
        return "loginForm";
    }
}
