package proyectoFinal.proyectoFinal.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import proyectoFinal.proyectoFinal.modelo.cita;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface citaRepositorio extends JpaRepository<cita, Long> {
    
    @Query("SELECT c FROM cita c " +
           "WHERE c.fechaHora BETWEEN :inicio AND :fin " +
           "ORDER BY c.fechaHora ASC")
    List<cita> findCitasDeHoy(@Param("inicio") LocalDateTime inicio,
                              @Param("fin") LocalDateTime fin);
    
    List<cita> findByClienteIdOrderByFechaHoraAsc(Long clienteId);
    
     List<cita> findByFechaHoraBetween(LocalDateTime inicio, LocalDateTime fin);
     
      // citas del profesional
    List<cita> findByTerapeutaIdOrderByFechaHoraAsc(Long terapeutaId);

    // próximas citas (si quieres filtrar solo futuras)
    List<cita> findByTerapeutaIdAndFechaHoraAfterOrderByFechaHoraAsc(Long terapeutaId,
                                                                      LocalDateTime fechaDesde);
}
