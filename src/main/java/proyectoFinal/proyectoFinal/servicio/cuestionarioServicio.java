package proyectoFinal.proyectoFinal.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import proyectoFinal.proyectoFinal.modelo.cuestionarioRespuesta;
import proyectoFinal.proyectoFinal.repositorio.cuestionarioRepositorio;

@Service
public class cuestionarioServicio {

    @Autowired
    private cuestionarioRepositorio cuestionarioRepositorio;

    public String guardarRespuestas(Long userId, int puntuacionTotal) {
        
        // **Lógica de la H.U. 2:** Calcular el resultado básico
        String resultado;
        if (puntuacionTotal > 5) {
            resultado = "Alto nivel de riesgo. Se recomienda cita inmediata.";
        } else {
            resultado = "Nivel bajo de riesgo. Se sugieren recursos de autoayuda.";
        }

        // Crear y guardar el objeto CuestionarioRespuesta (Requiere el Repositorio)
        // Ejemplo simplificado:
        /*
        CuestionarioRespuesta respuesta = new CuestionarioRespuesta();
        respuesta.setUsuarioId(userId);
        respuesta.setPuntuacionTotal(puntuacionTotal);
        respuesta.setResumenResultado(resultado);
        cuestionarioRepositorio.save(respuesta);
        */
        
        return resultado;
    }
}
