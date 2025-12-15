/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoFinal.proyectoFinal.servicio;


import org.springframework.stereotype.Service;
import proyectoFinal.proyectoFinal.modelo.Comentario;
import proyectoFinal.proyectoFinal.repositorio.ComentarioRepositorio;
import java.util.List;

@Service
public class ComentarioServicio {

    private final ComentarioRepositorio comentarioRepositorio;

    public ComentarioServicio(ComentarioRepositorio comentarioRepositorio) {
        this.comentarioRepositorio = comentarioRepositorio;
    }

    public Comentario guardar(Comentario c) {
        return comentarioRepositorio.save(c);
    }

    public List<Comentario> listarRecientes() {
        return comentarioRepositorio.findTop100ByOrderByFechaDesc();
    }

    public void eliminar(Long id) {
        comentarioRepositorio.deleteById(id);
    }
}