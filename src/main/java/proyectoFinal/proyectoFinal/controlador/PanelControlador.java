package proyectoFinal.proyectoFinal.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import proyectoFinal.proyectoFinal.servicio.EstadisticasService;

@Controller
@RequestMapping("/admin/estadisticas")
public class PanelControlador {

    private final EstadisticasService estadisticasService;

    public PanelControlador(EstadisticasService estadisticasService) {
        this.estadisticasService = estadisticasService;
    }

@GetMapping
public String verPanelAdmin(
        @RequestParam(required = false) Integer anio,
        @RequestParam(required = false) Integer mes,
        Model model) {

    // Totales generales (ya lo tenías)
    model.addAttribute("totalUsuarios", estadisticasService.totalUsuarios());
    model.addAttribute("totalPacientes", estadisticasService.totalPacientes());
    model.addAttribute("totalProfesionales", estadisticasService.totalProfesionales());
    model.addAttribute("totalAdministradores", estadisticasService.totalAdministradores());
    model.addAttribute("totalCitas", estadisticasService.totalCitas());
    model.addAttribute("citasDeHoy", estadisticasService.citasDeHoy());

    // Valores por defecto: mes/año actuales
    java.time.LocalDate hoy = java.time.LocalDate.now();
    int anioSel = (anio != null) ? anio : hoy.getYear();
    int mesSel = (mes != null) ? mes : hoy.getMonthValue();

    EstadisticasService.ResumenMensual resumen =
            estadisticasService.obtenerResumenMensual(anioSel, mesSel);

    model.addAttribute("anioSel", anioSel);
    model.addAttribute("mesSel", mesSel);
    model.addAttribute("resumenMensual", resumen);

    return "admin-estadisticas";
}

}
