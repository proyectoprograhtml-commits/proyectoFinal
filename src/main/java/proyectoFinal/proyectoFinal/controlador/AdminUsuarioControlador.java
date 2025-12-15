package proyectoFinal.proyectoFinal.controlador;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//Liberias para el CSV
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
//
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.repositorio.usuarioRepositorio;
import proyectoFinal.proyectoFinal.servicio.usuarioServicio;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/nuevosusuarios")
public class AdminUsuarioControlador {

    private final usuarioRepositorio usuarioRepositorio;
    private final usuarioServicio usuarioServicio;

    public AdminUsuarioControlador(usuarioRepositorio usuarioRepositorio,
            usuarioServicio usuarioServicio) {
        this.usuarioRepositorio = usuarioRepositorio;
        this.usuarioServicio = usuarioServicio;
    }

    // LISTAR + CARGAR USUARIO A EDITAR (opcional)
    @GetMapping
    public String listarUsuarios(@RequestParam(name = "editarId", required = false) Long editarId,
            Model model) {

        List<usuario> usuarios = usuarioRepositorio.findAll();
        model.addAttribute("usuarios", usuarios);

        if (editarId != null) {
            Optional<usuario> opt = usuarioRepositorio.findById(editarId);
            opt.ifPresent(u -> model.addAttribute("usuarioEditar", u));
        }

        return "admin-nuevosusuarios";
    }

    // CREAR NUEVO
    @PostMapping
    public String crearUsuario(@RequestParam String nombre,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam("tipoUsuario") usuario.TipoUsuario tipoUsuario,
            @RequestParam(required = false, defaultValue = "") String especialidad,
            RedirectAttributes redirectAttributes) {
        try {
            usuario nuevo = usuarioServicio.registrarUsuarioDesdeAdmin(
                    nombre, email, password, tipoUsuario, especialidad
            );
            redirectAttributes.addFlashAttribute(
                    "exito",
                    "Usuario '" + nuevo.getNombre() + "' creado correctamente."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo crear el usuario: " + e.getMessage()
            );
        }
        return "redirect:/admin/nuevosusuarios";
    }

    // EDITAR EXISTENTE
    @PostMapping("/editar")
    public String editarUsuario(@RequestParam Long id,
            @RequestParam String nombre,
            @RequestParam String email,
            @RequestParam("tipoUsuario") usuario.TipoUsuario tipoUsuario,
            @RequestParam(required = false, defaultValue = "") String especialidad,
            RedirectAttributes redirectAttributes) {

        try {
            usuario u = usuarioRepositorio.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            u.setNombre(nombre);
            u.setEmail(email);
            u.setTipoUsuario(tipoUsuario);
            u.setEspecialidad(especialidad);

            usuarioRepositorio.save(u);

            redirectAttributes.addFlashAttribute(
                    "exito",
                    "Usuario actualizado correctamente."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo actualizar el usuario: " + e.getMessage()
            );
        }

        return "redirect:/admin/nuevosusuarios";
    }

    // ELIMINAR
    @PostMapping("/eliminar")
    public String eliminarUsuario(@RequestParam Long id,
            RedirectAttributes redirectAttributes) {
        try {
            usuarioRepositorio.deleteById(id);
            redirectAttributes.addFlashAttribute(
                    "exito",
                    "Usuario eliminado correctamente."
            );
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute(
                    "error",
                    "No se pudo eliminar el usuario: " + e.getMessage()
            );
        }
        return "redirect:/admin/nuevosusuarios";
    }

    @GetMapping("/export/csv")
    public void exportarUsuariosCsv(HttpServletResponse response) throws IOException {

        // Configurar cabeceras de respuesta
        response.setContentType("text/csv; charset=UTF-8");
        String nombreArchivo = "usuarios-" + LocalDate.now() + ".csv";
        response.setHeader(
                HttpHeaders.CONTENT_DISPOSITION,
                "attachment; filename=\"" + nombreArchivo + "\""
        );

        // Obtener datos
        java.util.List<usuario> usuarios = usuarioRepositorio.findAll();

        // Escribir CSV
        try (PrintWriter writer = response.getWriter()) {

            // Encabezados
            writer.println("id;nombre;email;rol;especialidad;fecha_registro");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

            for (usuario u : usuarios) {
                String fecha = (u.getFechaRegistro() != null)
                        ? u.getFechaRegistro().format(formatter)
                        : "";
                writer.print(u.getId());
                writer.print(';');
                writer.print(u.getNombre());
                writer.print(';');
                writer.print(u.getEmail());
                writer.print(';');
                writer.print(u.getTipoUsuario());      // PACIENTE / PROFESIONAL / ADMINISTRADOR
                writer.print(';');
                writer.print(u.getEspecialidad() != null ? u.getEspecialidad() : "");
                writer.print(';');
                writer.print(fecha);
                writer.println();
            }

            writer.flush();
        }
    }

}
