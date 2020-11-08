package madstodolist;

import madstodolist.model.Usuario;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UsuarioWebTestSinMocks {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void servicioLoginUsuarioOK() throws Exception {
        // GIVEN
        // Datos cargados de datos-test.sql

        this.mockMvc.perform(post("/login")
                .param("eMail", "ana.garcia@gmail.com")
                .param("password", "12345678"))
                //.andDo(print())
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/1/tareas"));
    }

    @Test
    public void servicioLoginUsuarioNotFound() throws Exception {
        // GIVEN
        // Datos cargados de datos-test.sql

        this.mockMvc.perform(post("/login")
                    .param("eMail","pepito.perez@gmail.com")
                    .param("password","12345678"))
                .andExpect(content().string(containsString("No existe usuario")));
    }

    @Test
    public void servicioLoginUsuarioErrorPassword() throws Exception {
        // GIVEN
        // Datos cargados de datos-test.sql

        this.mockMvc.perform(post("/login")
                    .param("eMail","ana.garcia@gmail.com")
                    .param("password","000"))
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }

    @Test
    public void servicioLoginRedirectContraseñaIncorrecta() throws Exception {
        // GIVEN
        // Datos cargados de datos-test.sql

        this.mockMvc.perform(get("/login")
                .flashAttr("error", "Contraseña incorrecta"))
                .andExpect(content().string(containsString("Contraseña incorrecta")));
    }

    @Test
    public void servicioLoginRedirectUsuarioNotFound() throws Exception {
        // GIVEN
        // Datos cargados de datos-test.sql

        this.mockMvc.perform(get("/login")
                .flashAttr("error", "No existe usuario"))
                .andExpect(content().string(containsString("No existe usuario")));
    }

}
