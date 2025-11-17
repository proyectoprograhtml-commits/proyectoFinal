package proyectoFinal.proyectoFinal.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectoFinal.proyectoFinal.modelo.cuestionarioRespuesta;

public interface cuestionarioRepositorio extends JpaRepository<cuestionarioRespuesta, Long> {

}
