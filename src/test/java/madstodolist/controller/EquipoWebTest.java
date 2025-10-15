package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.EquipoService;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Sql(scripts = "/clean-db.sql")
public class EquipoWebTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private EquipoService equipoService;

    @Test
    public void listarEquiposSinLogearse() throws Exception {
        this.mockMvc.perform(get("/equipos"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    public void listarEquiposLogeado() throws Exception {
        UsuarioData usuario = new UsuarioData();
        usuario.setEmail("user@ua.es");
        usuario.setPassword("123");
        usuario = usuarioService.registrar(usuario);

        equipoService.crearEquipo("Proyecto A");
        equipoService.crearEquipo("Proyecto B");

        this.mockMvc.perform(get("/equipos")
                        .sessionAttr("idUsuarioLogeado", usuario.getId()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Proyecto A")))
                .andExpect(content().string(containsString("Proyecto B")));
    }
}
