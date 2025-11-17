package proyectoFinal.proyectoFinal.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import proyectoFinal.proyectoFinal.modelo.cita;

@Repository
public interface citaRepositorio extends JpaRepository<cita, Long> {
}
