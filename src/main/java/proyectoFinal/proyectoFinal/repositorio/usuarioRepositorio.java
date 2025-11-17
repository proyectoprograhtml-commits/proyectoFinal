package proyectoFinal.proyectoFinal.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import proyectoFinal.proyectoFinal.modelo.usuario;

public interface usuarioRepositorio extends JpaRepository<usuario, Long> {
    
    boolean existsByEmail(String email);
    
    usuario findByEmail(String email); 
}

