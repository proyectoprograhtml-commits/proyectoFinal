package proyectoFinal.proyectoFinal.repositorio;

import java.time.LocalDateTime;
import org.springframework.data.jpa.repository.JpaRepository;
import proyectoFinal.proyectoFinal.modelo.usuario;

public interface usuarioRepositorio extends JpaRepository<usuario, Long> {

    boolean existsByEmail(String email);

    usuario findByEmail(String email);

    long countByFechaRegistroBetween(LocalDateTime inicio, LocalDateTime fin);

    long countByTipoUsuarioAndFechaRegistroBetween(usuario.TipoUsuario tipo,
                                                   LocalDateTime inicio,
                                                   LocalDateTime fin);

    public long countByTipoUsuario(usuario.TipoUsuario tipoUsuario);
}
