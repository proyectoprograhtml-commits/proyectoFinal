package proyectoFinal.proyectoFinal.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import proyectoFinal.proyectoFinal.modelo.cita; 
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.repositorio.citaRepositorio;
import proyectoFinal.proyectoFinal.repositorio.usuarioRepositorio;

@Service
public class citaServicio {

    @Autowired
    private citaRepositorio citaRepositorio;
    
    @Autowired
    private usuarioRepositorio usuarioRepositorio; 

    public cita agendarCita(String emailCliente, Long terapeutaId, String fechaStr, String horaStr) {

        usuario cliente = usuarioRepositorio.findByEmail(emailCliente);

        if (cliente == null) {
            throw new RuntimeException("Error de autenticación: Usuario cliente no encontrado.");
        }

        String dateTimeStr = fechaStr + " " + horaStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime fechaHoraCita = LocalDateTime.parse(dateTimeStr, formatter);
        cita nuevaCita = new cita();
        nuevaCita.setClienteId(cliente.getId());
        nuevaCita.setTerapeutaId(terapeutaId); 
        nuevaCita.setFechaHora(fechaHoraCita);
        nuevaCita.setEstado("Pendiente"); 

        return citaRepositorio.save(nuevaCita);
    }
}