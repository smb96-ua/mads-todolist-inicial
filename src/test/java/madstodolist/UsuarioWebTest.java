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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(LoginController.class)
public class UsuarioWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    public void servicioLoginUsuario() throws Exception {

        when(usuarioService.login("ana.garcia@gmail.com", "12345678")).thenReturn(LoginStatus.LOGIN_OK);

        this.mockMvc.perform(post("/login")
                    .param("eMail","ana.garcia@gmail.com")
                    .param("password","12345678"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Hola ana.garcia@gmail.com!!!")));
    }
}
