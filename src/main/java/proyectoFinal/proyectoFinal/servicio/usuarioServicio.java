package proyectoFinal.proyectoFinal.servicio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import proyectoFinal.proyectoFinal.modelo.usuario;
import proyectoFinal.proyectoFinal.repositorio.usuarioRepositorio;
import java.util.Collections;

@Service
public class usuarioServicio implements UserDetailsService {

    @Autowired
    private usuarioRepositorio usuarioRepositorio;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public usuario registrarNuevoUsuario(String nombre, String email, String password) throws Exception {

        if (usuarioRepositorio.existsByEmail(email)) {
            throw new Exception("El email ya está registrado.");
        }

        usuario nuevoUsuario = new usuario();
        nuevoUsuario.setNombre(nombre);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setPasswordHash(passwordEncoder.encode(password));
        nuevoUsuario.setTipoUsuario(usuario.TipoUsuario.PACIENTE);

        return usuarioRepositorio.save(nuevoUsuario);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        usuario user = usuarioRepositorio.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
        }

        String roleName = "ROLE_" + user.getTipoUsuario().name();

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPasswordHash(),
                Collections.singletonList(() -> roleName)
        );
    }

    public Long getIdPorEmail(String email) {
        usuario user = usuarioRepositorio.findByEmail(email);
        if (user != null) {
            return user.getId();
        }
        throw new UsernameNotFoundException("Usuario no encontrado con email: " + email);
    }

    public usuario registrarUsuarioDesdeAdmin(String nombre,
            String email,
            String password,
            usuario.TipoUsuario tipoUsuario,
            String especialidad) throws Exception {

        if (usuarioRepositorio.existsByEmail(email)) {
            throw new Exception("El email ya está registrado.");
        }

        usuario nuevo = new usuario();
        nuevo.setNombre(nombre);
        nuevo.setEmail(email);
        nuevo.setPasswordHash(passwordEncoder.encode(password));
        nuevo.setTipoUsuario(tipoUsuario);
        nuevo.setEspecialidad(especialidad);

        return usuarioRepositorio.save(nuevo);
    }

    public usuario buscarPorEmail(String email) {
        return usuarioRepositorio.findByEmail(email);
    }

    public usuario buscarPorId(Long id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

}
