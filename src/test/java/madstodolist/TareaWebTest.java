package madstodolist;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.service.TareaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql("/datos-test.sql")
@Sql(scripts = "/clean-db.sql", executionPhase = AFTER_TEST_METHOD)
public class TareaWebTest {

    @Autowired
    private MockMvc mockMvc;

    // Declaramos los servicios como Autowired y usamos los datos
    // de prueba de la base de datos
    @Autowired
    private TareaService tareaService;

    // Moqueamos el managerUserSession y la sesión HTTP para poder moquear el usuario logeado
    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void listaTareas() throws Exception {
        // Moqueamos el método usuarioLogeado para que devuelva el usuario 1L,
        // el mismo que se está usando en la petición. De esta forma evitamos
        // que salte la excepción de que el usuario que está haciendo la
        // petición no está logeado.
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN, THEN
        // se realiza la petición GET al listado de tareas de un usuario,
        // el HTML devuelto contiene las descripciones de sus tareas.

        this.mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect((content().string(allOf(
                        containsString("Lavar coche"),
                        containsString("Renovar DNI")
                ))));
    }

    @Test
    public void getNuevaTareaDevuelveForm() throws Exception {
        // Ver el comentario en el primer test
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN, THEN
        // si ejecutamos una petición GET para crear una nueva tarea de un usuario,
        // el HTML resultante contiene un formulario y la ruta con
        // la acción para crear la nueva tarea.

        this.mockMvc.perform(get("/usuarios/1/tareas/nueva"))
                .andExpect((content().string(allOf(
                        containsString("form method=\"post\""),
                        containsString("action=\"/usuarios/1/tareas/nueva\"")
                ))));
    }

    @Test
    public void postNuevaTareaDevuelveRedirectYAñadeTarea() throws Exception {
        // Ver el comentario en el primer test
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN, THEN
        // realizamos la petición POST para añadir una nueva tarea,
        // el estado HTTP que se devuelve es un REDIRECT al listado
        // de tareas.

        this.mockMvc.perform(post("/usuarios/1/tareas/nueva")
                        .param("titulo", "Estudiar examen MADS"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios/1/tareas"));

        // y si después consultamos el listado de tareas con una petición
        // GET el HTML contiene la tarea añadida.

        this.mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect((content().string(containsString("Estudiar examen MADS"))));
    }

    @Test
    public void deleteTareaDevuelveOKyBorraTarea() throws Exception {
        // Ver el comentario en el primer test
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN, THEN
        // realizamos la petición DELETE para borrar una tarea,
        // se devuelve el estado HTTP que se devuelve es OK,

        this.mockMvc.perform(delete("/tareas/1"))
                .andExpect(status().isOk());

        // y cuando se pide un listado de tareas del usuario, la tarea borrada ya no aparece.

        this.mockMvc.perform(get("/usuarios/1/tareas"))
                .andExpect(content().string(
                        allOf(not(containsString("Lavar coche")),
                                containsString("Renovar DNI"))));
    }

    @Test
    public void editarTareaDevuelveForm() throws Exception {
        // Ver el comentario en el primer test
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);

        // GIVEN
        // Cargados datos de prueba del fichero datos-test.sql,

        // WHEN, THEN
        // realizamos una petición GET al endpoint para editar una tarea
        // el HTML devuelto

        this.mockMvc.perform(get("/tareas/1/editar"))
                .andExpect(content().string(allOf(
                    // contiene la acción para enviar el post a la URL correcta,
                    containsString("action=\"/tareas/1/editar\""),
                    // contiene la descripción de la tarea a editar,
                    containsString("Lavar coche"),
                    // y contiene enlace a listar tareas del usuario si se cancela la edición.
                    containsString("href=\"/usuarios/1/tareas\""))));
    }
}
