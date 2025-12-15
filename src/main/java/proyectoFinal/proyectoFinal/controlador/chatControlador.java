package proyectoFinal.proyectoFinal.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class chatControlador {

    // Página de chat accesible sólo para usuarios autenticados
    @GetMapping("/chat")
    public String verChat() {
        return "chat";
    }
}
