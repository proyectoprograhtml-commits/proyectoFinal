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
import proyectoFinal.proyectoFinal.servicio.citaServicio;
import proyectoFinal.proyectoFinal.servicio.usuarioServicio;

@Controller
public class psicologoControlador {

    @Autowired
    private psicologoServicio psicologoServicio;

    @Autowired
    private usuarioServicio usuarioServicio;

    @Autowired
    private citaServicio citaServicio;

    @GetMapping("/psicologos")
    public String buscarPsicologos(@RequestParam(required = false, defaultValue = "") String especialidad, Model model) {

        List<usuario> terapeutas = psicologoServicio.buscarTerapeutas(especialidad);
        model.addAttribute("listaTerapeutas", terapeutas);
        model.addAttribute("filtroActual", especialidad);

        return "psicologos";
    }

    @GetMapping("/paciente/dashboard")
    public String mostrarDashboard(Model model, Authentication authentication) {

        // Email del usuario autenticado
        String emailUsuario = authentication.getName();

        // Obtener id del paciente a partir del email
        Long pacienteId = usuarioServicio.getIdPorEmail(emailUsuario);

        // Notificación de próxima cita (ya lo tenías)
        cita proximaCita = psicologoServicio.getProximaSesion(pacienteId);
        if (proximaCita != null) {
            model.addAttribute("notificacionCita", proximaCita);
        }

        // NUEVO: lista completa de citas de este paciente
        java.util.List<cita> citasPaciente = citaServicio.obtenerCitasDePaciente(pacienteId);
        model.addAttribute("citasPaciente", citasPaciente);

        return "dashboard";
    }

    @GetMapping("/agendar-cita/{terapeutaId}")
    public String mostrarFormularioCita(@PathVariable Long terapeutaId, Model model) {
        model.addAttribute("terapeutaId", terapeutaId);
        return "agendar-cita";
    }
}
