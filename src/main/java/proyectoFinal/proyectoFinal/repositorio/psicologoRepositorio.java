package proyectoFinal.proyectoFinal.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import proyectoFinal.proyectoFinal.modelo.usuario;
import java.util.List;
import java.time.LocalDateTime;
import proyectoFinal.proyectoFinal.modelo.cita;

public interface psicologoRepositorio extends JpaRepository<usuario, Long> {
    
    List<usuario> findByTipoUsuarioAndEspecialidadContainingIgnoreCase(usuario.TipoUsuario tipo, String especialidad);

    
    @Query("SELECT c FROM cita c WHERE c.clienteId = ?1 AND c.estado = 'programada' AND c.fechaHora > ?2 AND c.fechaHora < ?3 ORDER BY c.fechaHora ASC")
List<cita> findNextCitas(Long clienteId, LocalDateTime startDate, LocalDateTime endDate);
}
