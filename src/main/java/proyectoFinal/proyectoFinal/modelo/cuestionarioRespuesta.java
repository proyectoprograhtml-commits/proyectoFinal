package proyectoFinal.proyectoFinal.modelo;

import jakarta.persistence.*;

@Entity
@Table(name = "cuestionario_respuestas")
public class cuestionarioRespuesta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long usuarioId; 
    
    private int puntuacionTotal;
    private String resumenResultado;
}
