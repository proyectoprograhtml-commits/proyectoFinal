/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyectoFinal.proyectoFinal.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import proyectoFinal.proyectoFinal.modelo.Comentario;
import proyectoFinal.proyectoFinal.servicio.ComentarioServicio;
import proyectoFinal.proyectoFinal.repositorio.usuarioRepositorio;

import java.util.List;

@Controller
public class BlogControlador {

    private final ComentarioServicio comentarioServicio;
    private final usuarioRepositorio usuarioRepo; // ← NUEVO

    // ← CONSTRUCTOR ACTUALIZADO
    public BlogControlador(ComentarioServicio comentarioServicio, usuarioRepositorio usuarioRepo) {
        this.comentarioServicio = comentarioServicio;
        this.usuarioRepo = usuarioRepo;
    }

    @GetMapping("/blog")
    public String blogIndex(Model model) {
        model.addAttribute("guias", new String[]{
                "guia-ansiedad.pdf",
                "guia-duelo.pdf",
                "Material Escolar - Guia Estudiantes.pdf",
                "Estrategias Aula.pdf"
        });

        model.addAttribute("videos", new String[]{
                "dQw4w9WgXcQ"
        });

        return "blog";
    }

    @GetMapping("/foro")
    public String foro(Model model) {
        List<Comentario> comentarios = comentarioServicio.listarRecientes();
        model.addAttribute("comentarios", comentarios);
        model.addAttribute("comentarioForm", new Comentario());
        return "foro";
    }

    @PostMapping("/foro/comentar")
    public String comentar(@ModelAttribute("comentarioForm") Comentario comentario) {

        // Si no dan nombre, se pone Anónimo
        if (comentario.getAutor() == null || comentario.getAutor().trim().isEmpty()) {
            comentario.setAutor("Anónimo");
        }

        comentarioServicio.guardar(comentario);

        return "redirect:/foro";
    }

    @GetMapping("/recursos")
    public String recursos(Model model) {
        model.addAttribute("recursos", new String[]{
                "Material Escolar - Guia Estudiantes.pdf",
                "Estrategias Aula.pdf"
        });

        return "recursos";
    }
}