package proyectoFinal.proyectoFinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ProyectoFinalApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProyectoFinalApplication.class, args);
                
                // La contraseña que quieres usar para iniciar sesión
        String passwordPlano = "Admin1234"; 

        // Crea una instancia del codificador
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        // Genera el hash
        String hashGenerado = encoder.encode(passwordPlano);

        // Imprime el hash para que lo copiarlo
        System.out.println("------------------------------------");
        System.out.println("Password Plano: " + passwordPlano);
        System.out.println("HASH BCrypt (copia este valor):");
        System.out.println(hashGenerado);
        System.out.println("------------------------------------");
	}

}
