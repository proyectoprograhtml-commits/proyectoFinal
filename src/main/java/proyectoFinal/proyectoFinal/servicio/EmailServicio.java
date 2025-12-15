package proyectoFinal.proyectoFinal.servicio;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import proyectoFinal.proyectoFinal.modelo.cita;
import proyectoFinal.proyectoFinal.modelo.usuario;

@Service
public class EmailServicio {

    private final JavaMailSender mailSender;

    public EmailServicio(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void enviarConfirmacionCita(usuario cliente, cita citaEntidad, usuario terapeuta) {
        if (cliente == null || citaEntidad == null) return;

        SimpleMailMessage mensaje = new SimpleMailMessage();
        mensaje.setTo(cliente.getEmail());
        mensaje.setSubject("Confirmación de cita - Terapia y Salud Mental");

        String nombreTerapeuta = (terapeuta != null) ? terapeuta.getNombre() : "su terapeuta";
        String texto = """
                Hola %s,

                Tu cita ha sido agendada correctamente.

                Detalles de la cita:
                - Fecha y hora: %s
                - Terapeuta: %s
                - Estado: %s

                Si necesitas reprogramar o cancelar, ingresa a tu Dashboard.

                Atentamente,
                Equipo de Terapia y Salud Mental
                """.formatted(
                cliente.getNombre(),
                citaEntidad.getFechaHora(),
                nombreTerapeuta,
                citaEntidad.getEstado()
        );

        mensaje.setText(texto);
        mailSender.send(mensaje);
    }
    
    public void enviarAvisoCitaCancelada(usuario cliente, cita citaEntidad, usuario terapeuta) {
    if (cliente == null || citaEntidad == null) return;

    SimpleMailMessage mensaje = new SimpleMailMessage();
    mensaje.setTo(cliente.getEmail());
    mensaje.setSubject("Cita cancelada - Terapia y Salud Mental");

    String nombreTerapeuta = (terapeuta != null) ? terapeuta.getNombre() : "tu terapeuta";
    String texto = """
            Hola %s,

            Tu cita ha sido CANCELADA.

            Detalles de la cita:
            - Fecha y hora original: %s
            - Terapeuta: %s

            Si fue un error o deseas agendar una nueva cita, ingresa al sistema y selecciona un nuevo horario.

            Atentamente,
            Equipo de Terapia y Salud Mental
            """.formatted(
            cliente.getNombre(),
            citaEntidad.getFechaHora(),
            nombreTerapeuta
    );

    mensaje.setText(texto);
    mailSender.send(mensaje);
}

public void enviarAvisoCitaReprogramada(usuario cliente, cita citaEntidad, usuario terapeuta) {
    if (cliente == null || citaEntidad == null) return;

    SimpleMailMessage mensaje = new SimpleMailMessage();
    mensaje.setTo(cliente.getEmail());
    mensaje.setSubject("Cita reprogramada - Terapia y Salud Mental");

    String nombreTerapeuta = (terapeuta != null) ? terapeuta.getNombre() : "tu terapeuta";
    String texto = """
            Hola %s,

            Tu cita ha sido REPROGRAMADA.

            Nuevos detalles de la cita:
            - Nueva fecha y hora: %s
            - Terapeuta: %s
            - Estado: %s

            Si necesitas hacer más cambios, puedes volver a tu Dashboard.

            Atentamente,
            Equipo de Terapia y Salud Mental
            """.formatted(
            cliente.getNombre(),
            citaEntidad.getFechaHora(),
            nombreTerapeuta,
            citaEntidad.getEstado()
    );

    mensaje.setText(texto);
    mailSender.send(mensaje);
}

}
