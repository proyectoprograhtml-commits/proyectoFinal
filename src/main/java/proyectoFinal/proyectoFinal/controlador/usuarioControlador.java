package proyectoFinal.proyectoFinal.controlador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.servicio.citaServicio;
import proyectoFinal.proyectoFinal.servicio.usuarioServicio;
import proyectoFinal.proyectoFinal.servicio.cuestionarioServicio;

@Controller
public class usuarioControlador {

    @Autowired
    private usuarioServicio usuarioServicio;

    @Autowired
    private cuestionarioServicio cuestionarioServicio;
    
    @Autowired 
    private citaServicio citaServicio;

    @GetMapping("/registro")
    public String mostrarFormularioRegistro() {
        return "registro";
    }

    @PostMapping("/registro")
    public String registrarNuevoUsuario(@RequestParam String nombre, @RequestParam String email, @RequestParam String password, Model model) {
        try {
            usuario nuevoUsuario = usuarioServicio.registrarNuevoUsuario(nombre, email, password);
            model.addAttribute("userId", nuevoUsuario.getId());
            return "redirect:/cuestionario-inicial"; 
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "registro";
        }
    }
    
    @PostMapping("/cuestionario-inicial")
    public String completarCuestionario(@RequestParam Long userId, @RequestParam int pregunta1, Model model) {
        int puntuacion = pregunta1 * 3;
        String resultado = cuestionarioServicio.guardarRespuestas(userId, puntuacion);
        
        model.addAttribute("resultado", resultado);
        return "resultados-evaluacion";
    }
    
    @GetMapping("/cita-exitosa")
    public String mostrarCitaExitosa(Model model, Authentication authentication) {
        String emailUsuario = authentication.getName();
        model.addAttribute("emailUsuario", emailUsuario);
        return "cita-exitosa";
    }
    
    @PostMapping("/confirmar-cita")
    public String confirmarCita(
            Authentication authentication, 
            @RequestParam("fecha") String fecha,
            @RequestParam("hora") String hora,
            @RequestParam("terapeutaId") Long terapeutaId,
            Model model) {
        
        try {
            String emailCliente = authentication.getName(); 
            citaServicio.agendarCita(emailCliente, terapeutaId, fecha, hora); 
            
            return "redirect:/cita-exitosa";
            
        } catch (Exception e) {
            model.addAttribute("errorCita", "No se pudo agendar la cita: " + e.getMessage());
            return "agendar-cita"; 
        }
    }
    
    @GetMapping("/agendar-cita")
    public String mostrarAgendarCita(Model model) {
        return "agendar-cita";
    }
}