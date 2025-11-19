/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoFinal.proyectoFinal.repositorio;

import proyectoFinal.proyectoFinal.modelo.Comentario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ComentarioRepositorio extends JpaRepository<Comentario, Long> {
    List<Comentario> findTop100ByOrderByFechaDesc();
}