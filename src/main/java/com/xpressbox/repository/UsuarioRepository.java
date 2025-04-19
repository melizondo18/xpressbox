package com.xpressbox.repository;

import com.xpressbox.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    //Se usa para el proceso de login.
    Usuario findByUsername(String username);

    //Se usa para encontrar un registro de usuario en el proceso de activacion de un nuevo usuario.
    Usuario findByUsernameAndPassword(String username, String Password);

    //Se utiliza para vaidad si dentro de la tabla usuario ya existe un registro con el username o el correo indicados.
    Usuario findByUsernameOrCorreo(String username, String correo);

    //Se utiliza para vaidad si dentro de la tabla usuario ya existe un registro con el username o el correo indicados.
    boolean existsByUsernameOrCorreo(String username, String correo);
}
