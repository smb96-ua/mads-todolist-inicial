package madstodolist.controller;

import madstodolist.service.SaludoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;


@Controller
public class SaludoControllerPlantilla {

    private final SaludoService service;

    @Autowired
    public SaludoControllerPlantilla(SaludoService service) {
        this.service = service;
    }

    @RequestMapping("/saludoplantilla/{nombre}")
    public String saludo(@PathVariable(value="nombre") String nombre, Map<String, String> model) {
        model.put("mensaje", service.saluda(nombre));
        return "saludo";
    }
}
