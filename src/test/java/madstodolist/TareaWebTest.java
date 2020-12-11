package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.model.Tarea;
import madstodolist.model.Usuario;
import madstodolist.service.TareaService;
import madstodolist.service.UsuarioService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class TareaWebTest {

    @Autowired
    private MockMvc mockMvc;

    // Al mocker el manegerUserSession, no lanza la excepción cuando
    // se intenta comprobar si un usuario está logeado
    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void getNuevaTareaDevuelveForm() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(get("/usuarios/1/tareas/nueva"))
                //.andDo(print())
                .andExpect(content().string(containsString("action=\"/usuarios/1/tareas/nueva\"")));
    }

    @Test
    public void postNuevaTareaDevuelveListaConTarea() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(post("/usuarios/1/tareas/nueva")
                    .param("titulo", "Estudiar examen MADS"))
                    .andExpect(status().is3xxRedirection())
                    .andExpect(redirectedUrl("/usuarios/1/tareas"));
    }

    @Test
    public void editarTareaDevuelveForm() throws Exception {
        // GIVEN
        // En el application.properties se cargan los datos de prueba del fichero datos-test.sql

        this.mockMvc.perform(get("/tareas/1/editar"))
                .andExpect(content().string(allOf(
                    // Contiene la acción para enviar el post a la URL correcta
                    containsString("action=\"/tareas/1/editar\""),
                    // Contiene el texto de la tarea a editar
                    containsString("Lavar coche"),
                    // Contiene enlace a listar tareas del usuario si se cancela la edición
                    containsString("href=\"/usuarios/1/tareas\""))));
    }
}
