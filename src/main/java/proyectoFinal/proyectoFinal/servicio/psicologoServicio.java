package proyectoFinal.proyectoFinal.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.repositorio.psicologoRepositorio;
import java.util.List;
import java.time.LocalDateTime;
import proyectoFinal.proyectoFinal.modelo.cita;

@Service
public class psicologoServicio {

    @Autowired
    private psicologoRepositorio psicologoRepositorio;

    public List<usuario> buscarTerapeutas(String especialidad) {
        return psicologoRepositorio.findByTipoUsuarioAndEspecialidadContainingIgnoreCase(
            usuario.TipoUsuario.PROFESIONAL, especialidad
        );
    }
    
    public cita getProximaSesion(Long pacienteId) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime next24Hours = now.plusDays(1);
        
        List<cita> proximasCitas = psicologoRepositorio.findNextCitas(pacienteId, now, next24Hours);
        return proximasCitas.isEmpty() ? null : proximasCitas.get(0);
    }
}