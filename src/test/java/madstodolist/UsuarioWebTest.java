package madstodolist;

import madstodolist.controller.LoginController;
import madstodolist.model.Usuario.LoginStatus;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class UsuarioWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void servicioLoginUsuarioOK() throws Exception {

        when(usuarioService.login("ana.garcia@gmail.com", "12345678")).thenReturn(LoginStatus.LOGIN_OK);

        this.mockMvc.perform(post("/login")
                    .param("eMail","ana.garcia@gmail.com")
                    .param("password","12345678"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hola ana.garcia@gmail.com!!!")));
    }

    @Test
    public void servicioLoginUsuarioNotFound() throws Exception {

        when(usuarioService.login("pepito.perez@gmail.com", "12345678")).thenReturn(LoginStatus.USER_NOT_FOUND);

        this.mockMvc.perform(post("/login")
                    .param("eMail","pepito.perez@gmail.com")
                    .param("password","12345678"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "No existe usuario"));
    }

    @Test
    public void servicioLoginUsuarioErrorPassword() throws Exception {

        when(usuarioService.login("ana.garcia@gmail.com", "000")).thenReturn(LoginStatus.ERROR_PASSWORD);

        this.mockMvc.perform(post("/login")
                    .param("eMail","ana.garcia@gmail.com")
                    .param("password","000"))
                .andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(flash().attribute("error", "Contraseña incorrecta"));
    }

    @Test
    public void servicioLoginRedirectContraseñaIncorrecta() throws Exception {
        this.mockMvc.perform(get("/login")
                .flashAttr("error", "Contraseña incorrecta"))
                .andDo(print())
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }

    @Test
    public void servicioLoginRedirectUsuarioNotFound() throws Exception {
        this.mockMvc.perform(get("/login")
                .flashAttr("error", "No existe usuario"))
                .andDo(print())
                .andExpect(content().string(containsString("No existe usuario")));
    }

}
