package proyectoFinal.proyectoFinal.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PathVariable;
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.servicio.psicologoServicio;

import java.util.List;
import org.springframework.security.core.Authentication;
import proyectoFinal.proyectoFinal.modelo.cita;
import proyectoFinal.proyectoFinal.servicio.usuarioServicio;

@Controller
public class psicologoControlador {

    @Autowired
    private psicologoServicio psicologoServicio;

    @Autowired
    private usuarioServicio usuarioServicio;

    @GetMapping("/psicologos")
    public String buscarPsicologos(@RequestParam(required = false, defaultValue = "") String especialidad, Model model) {

        List<usuario> terapeutas = psicologoServicio.buscarTerapeutas(especialidad);
        model.addAttribute("listaTerapeutas", terapeutas);
        model.addAttribute("filtroActual", especialidad);

        return "psicologos";
    }

    @GetMapping("/paciente/dashboard")
    public String mostrarDashboard(Model model, Authentication authentication) {
        String emailUsuario = authentication.getName();
        Long pacienteId = usuarioServicio.getIdPorEmail(emailUsuario);
        cita proximaCita = psicologoServicio.getProximaSesion(pacienteId);
        if (proximaCita != null) {
            model.addAttribute("notificacionCita", proximaCita);
        }

        return "dashboard";
    }

    @GetMapping("/agendar-cita/{terapeutaId}")
    public String mostrarFormularioCita(@PathVariable Long terapeutaId, Model model) {
        model.addAttribute("terapeutaId", terapeutaId);
        return "agendar-cita";
    }
}
