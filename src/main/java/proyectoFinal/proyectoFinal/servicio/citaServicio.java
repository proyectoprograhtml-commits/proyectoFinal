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

    @Autowired
    private EmailServicio emailServicio;

    public cita agendarCita(String emailCliente, Long terapeutaId,
            String fechaStr, String horaStr) {

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
        nuevaCita.setEstado("programada");

        cita guardada = citaRepositorio.save(nuevaCita);

        // Buscar terapeuta para el nombre (opcional)
        usuario terapeuta = usuarioRepositorio.findById(terapeutaId).orElse(null);

        // Enviar correo de confirmación (no lanza excepción si falla)
        try {
            emailServicio.enviarConfirmacionCita(cliente, guardada, terapeuta);
        } catch (Exception e) {
            // Puedes loguear el error si quieres
        }

        return guardada;
    }

    public java.util.List<cita> obtenerCitasDePaciente(Long pacienteId) {
        return citaRepositorio.findByClienteIdOrderByFechaHoraAsc(pacienteId);
    }

    public void cancelarCita(Long citaId, Long usuarioQueCancelaId) {
        cita c = citaRepositorio.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        // permite que cancele el paciente o el profesional
        if (!c.getClienteId().equals(usuarioQueCancelaId)
                && !c.getTerapeutaId().equals(usuarioQueCancelaId)) {
            throw new RuntimeException("No puede cancelar una cita de otro usuario");
        }

        c.setEstado("cancelada");
        cita citaGuardada = citaRepositorio.save(c);

        usuario cliente = usuarioRepositorio.findById(c.getClienteId()).orElse(null);
        usuario terapeuta = usuarioRepositorio.findById(c.getTerapeutaId()).orElse(null);

        try {
            emailServicio.enviarAvisoCitaCancelada(cliente, citaGuardada, terapeuta);
        } catch (Exception e) {
        }
    }

    public void reprogramarCita(Long citaId, Long usuarioQueCambiaId,
            String nuevaFechaStr, String nuevaHoraStr) {

        cita c = citaRepositorio.findById(citaId)
                .orElseThrow(() -> new RuntimeException("Cita no encontrada"));

        if (!c.getClienteId().equals(usuarioQueCambiaId)
                && !c.getTerapeutaId().equals(usuarioQueCambiaId)) {
            throw new RuntimeException("No puede reprogramar una cita de otro usuario");
        }

        String dateTimeStr = nuevaFechaStr + " " + nuevaHoraStr;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime nuevaFechaHora = LocalDateTime.parse(dateTimeStr, formatter);

        c.setFechaHora(nuevaFechaHora);
        c.setEstado("programada");
        cita citaGuardada = citaRepositorio.save(c);

        usuario cliente = usuarioRepositorio.findById(c.getClienteId()).orElse(null);
        usuario terapeuta = usuarioRepositorio.findById(c.getTerapeutaId()).orElse(null);

        try {
            emailServicio.enviarAvisoCitaReprogramada(cliente, citaGuardada, terapeuta);
        } catch (Exception e) {
        }
    }

    public java.util.List<cita> obtenerCitasDeProfesional(Long profesionalId) {
        return citaRepositorio.findByTerapeutaIdOrderByFechaHoraAsc(profesionalId);
    }

    public java.util.List<cita> obtenerProximasCitasProfesional(Long profesionalId) {
        LocalDateTime ahora = LocalDateTime.now();
        return citaRepositorio.findByTerapeutaIdAndFechaHoraAfterOrderByFechaHoraAsc(profesionalId, ahora);
    }

    public cita obtenerPorId(Long citaId) {
        return citaRepositorio.findById(citaId).orElse(null);
    }

    public void marcarComoCompletada(Long citaId, Long terapeutaId) {
        cita c = obtenerPorId(citaId);
        if (c == null) {
            throw new RuntimeException("Cita no encontrada.");
        }

        if (c.getTerapeutaId() == null || !c.getTerapeutaId().equals(terapeutaId)) {
            throw new RuntimeException("No autorizado para completar esta cita.");
        }

        c.setEstado("Completada");
        citaRepositorio.save(c);
    }

}
