package proyectoFinal.proyectoFinal.controlador;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import proyectoFinal.proyectoFinal.modelo.cita;
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.servicio.citaServicio;
import proyectoFinal.proyectoFinal.servicio.psicologoServicio;
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
    public String buscarPsicologos(@RequestParam(required = false, defaultValue = "") String especialidad,
            Model model) {

        List<usuario> terapeutas = psicologoServicio.buscarTerapeutas(especialidad);
        model.addAttribute("listaTerapeutas", terapeutas);
        model.addAttribute("filtroActual", especialidad);

        return "psicologos";
    }

    @GetMapping("/paciente/dashboard")
    public String mostrarDashboard(Model model, Authentication authentication) {

        String emailUsuario = authentication.getName();
        usuario usuarioActual = usuarioServicio.buscarPorEmail(emailUsuario);
        Long usuarioId = usuarioActual.getId();

        
        java.util.List<cita> citasPaciente = java.util.Collections.emptyList();
        if (usuarioActual.getTipoUsuario() == usuario.TipoUsuario.PACIENTE) {
            cita proximaCita = psicologoServicio.getProximaSesion(usuarioId);
            if (proximaCita != null) {
                model.addAttribute("notificacionCita", proximaCita);
            }
            citasPaciente = citaServicio.obtenerCitasDePaciente(usuarioId);
            model.addAttribute("citasPaciente", citasPaciente);
        }

       
        java.util.List<cita> citasProfesional = java.util.Collections.emptyList();
        if (usuarioActual.getTipoUsuario() == usuario.TipoUsuario.PROFESIONAL
                || usuarioActual.getTipoUsuario() == usuario.TipoUsuario.ADMINISTRADOR) {

            citasProfesional = citaServicio.obtenerCitasDeProfesional(usuarioId);
            model.addAttribute("citasProfesional", citasProfesional);
            model.addAttribute("esProfesionalOAdmin", true);
        }

        
        java.util.Map<Long, String> nombresUsuarios = new java.util.HashMap<>();

        for (cita c : citasPaciente) {
            Long idTerapeuta = c.getTerapeutaId();
            if (idTerapeuta != null && !nombresUsuarios.containsKey(idTerapeuta)) {
                usuario u = usuarioServicio.buscarPorId(idTerapeuta);
                if (u != null) {
                    nombresUsuarios.put(idTerapeuta, u.getNombre());
                }
            }
        }

        
        for (cita c : citasProfesional) {
            Long idPaciente = c.getClienteId();
            if (idPaciente != null && !nombresUsuarios.containsKey(idPaciente)) {
                usuario u = usuarioServicio.buscarPorId(idPaciente);
                if (u != null) {
                    nombresUsuarios.put(idPaciente, u.getNombre());
                }
            }
        }

        model.addAttribute("nombresUsuarios", nombresUsuarios);

        return "dashboard";
    }

    @GetMapping("/agendar-cita/{terapeutaId}")
    public String mostrarFormularioCita(@PathVariable Long terapeutaId, Model model) {
        model.addAttribute("terapeutaId", terapeutaId);
        return "agendar-cita";
    }

    @PostMapping("/paciente/citas/{id}/cancelar")
    public String cancelarCita(@PathVariable("id") Long citaId,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String emailUsuario = authentication.getName();
        Long pacienteId = usuarioServicio.getIdPorEmail(emailUsuario);

        try {
            citaServicio.cancelarCita(citaId, pacienteId);
            redirectAttributes.addFlashAttribute("exito",
                    "La cita ha sido cancelada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo cancelar la cita: " + e.getMessage());
        }

        return "redirect:/paciente/dashboard";
    }

    @PostMapping("/paciente/citas/{id}/reprogramar")
    public String reprogramarCita(@PathVariable("id") Long citaId,
            @RequestParam("nuevaFecha") String nuevaFecha,
            @RequestParam("nuevaHora") String nuevaHora,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        String emailUsuario = authentication.getName();
        Long pacienteId = usuarioServicio.getIdPorEmail(emailUsuario);

        try {
            citaServicio.reprogramarCita(citaId, pacienteId, nuevaFecha, nuevaHora);
            redirectAttributes.addFlashAttribute("exito",
                    "La cita ha sido reprogramada correctamente.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error",
                    "No se pudo reprogramar la cita: " + e.getMessage());
        }

        return "redirect:/paciente/dashboard";
    }

    @PostMapping("/citas/{id}/completar")
    public String completarCita(@PathVariable("id") Long citaId, Authentication authentication) {

        String emailUsuario = authentication.getName();
        usuario terapeuta = usuarioServicio.buscarPorEmail(emailUsuario);

        citaServicio.marcarComoCompletada(citaId, terapeuta.getId());

        return "redirect:/paciente/dashboard";
    }

}
