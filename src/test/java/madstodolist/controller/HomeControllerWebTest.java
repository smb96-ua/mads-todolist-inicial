package madstodolist.controller;

import madstodolist.authentication.ManagerUserSession;
import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
public class HomeControllerWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;
    
    @MockBean
    private ManagerUserSession managerUserSession;

    @Test
    public void getAboutPage() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setEmail("test@ua.es");
        usuario.setNombre("Usuario Test");
        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        when(usuarioService.findById(1L)).thenReturn(usuario);

        this.mockMvc.perform(get("/about"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("ToDoList")))
                .andExpect(content().string(containsString("Desarrollada por Sergio Martínez Borrell")))
                .andExpect(view().name("about"));
    }

    @Test
    public void getListadoUsuarios() throws Exception {
        UsuarioData usuario1 = new UsuarioData();
        usuario1.setId(1L);
        usuario1.setEmail("ana.garcia@ua.es");
        usuario1.setNombre("Ana García");
        
        UsuarioData usuario2 = new UsuarioData();
        usuario2.setId(2L);
        usuario2.setEmail("juan.lopez@ua.es");
        usuario2.setNombre("Juan López");

        UsuarioData usuarioLogueado = new UsuarioData();
        usuarioLogueado.setId(3L);
        usuarioLogueado.setEmail("admin@ua.es");
        usuarioLogueado.setNombre("Admin");
        usuarioLogueado.setIsAdmin(true); // Usuario administrador

        List<UsuarioData> usuarios = Arrays.asList(usuario1, usuario2);

        when(managerUserSession.usuarioLogeado()).thenReturn(3L);
        when(usuarioService.findById(3L)).thenReturn(usuarioLogueado);
        when(usuarioService.findAll()).thenReturn(usuarios);

        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(view().name("listaUsuarios"))
                .andExpect(model().attribute("usuarios", usuarios))
                .andExpect(model().attribute("usuarioLogueado", usuarioLogueado))
                .andExpect(content().string(containsString("Ana García")))
                .andExpect(content().string(containsString("Juan López")))
                .andExpect(content().string(containsString("ana.garcia@ua.es")))
                .andExpect(content().string(containsString("juan.lopez@ua.es")));
    }

    @Test
    public void getDescripcionUsuario() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setId(1L);
        usuario.setEmail("test@ua.es");
        usuario.setNombre("Usuario Test");
        
        UsuarioData usuarioLogueado = new UsuarioData();
        usuarioLogueado.setId(2L);
        usuarioLogueado.setEmail("admin@ua.es");
        usuarioLogueado.setNombre("Admin");
        usuarioLogueado.setIsAdmin(true); // Usuario administrador

        when(managerUserSession.usuarioLogeado()).thenReturn(2L);
        when(usuarioService.findById(2L)).thenReturn(usuarioLogueado);
        when(usuarioService.findById(1L)).thenReturn(usuario);

        this.mockMvc.perform(get("/registrados/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("descripcionUsuario"))
                .andExpect(model().attribute("usuario", usuario))
                .andExpect(model().attribute("usuarioLogueado", usuarioLogueado))
                .andExpect(content().string(containsString("Usuario Test")))
                .andExpect(content().string(containsString("test@ua.es")));
    }

    @Test
    public void getListadoUsuarios_SinLoguear_DevuelveUnauthorized() throws Exception {
        when(managerUserSession.usuarioLogeado()).thenReturn(null);

        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getListadoUsuarios_UsuarioNoAdmin_DevuelveUnauthorized() throws Exception {
        UsuarioData usuarioNoAdmin = new UsuarioData();
        usuarioNoAdmin.setId(1L);
        usuarioNoAdmin.setEmail("user@ua.es");
        usuarioNoAdmin.setNombre("Usuario Normal");
        usuarioNoAdmin.setIsAdmin(false); // Usuario no administrador

        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        when(usuarioService.findById(1L)).thenReturn(usuarioNoAdmin);

        this.mockMvc.perform(get("/registrados"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getDescripcionUsuario_SinLoguear_DevuelveUnauthorized() throws Exception {
        when(managerUserSession.usuarioLogeado()).thenReturn(null);

        this.mockMvc.perform(get("/registrados/1"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void getDescripcionUsuario_UsuarioNoAdmin_DevuelveUnauthorized() throws Exception {
        UsuarioData usuarioNoAdmin = new UsuarioData();
        usuarioNoAdmin.setId(1L);
        usuarioNoAdmin.setEmail("user@ua.es");
        usuarioNoAdmin.setNombre("Usuario Normal");
        usuarioNoAdmin.setIsAdmin(false); // Usuario no administrador

        when(managerUserSession.usuarioLogeado()).thenReturn(1L);
        when(usuarioService.findById(1L)).thenReturn(usuarioNoAdmin);

        this.mockMvc.perform(get("/registrados/2"))
                .andExpect(status().isUnauthorized());
    }
}