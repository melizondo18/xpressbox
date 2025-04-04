package com.xpressbox.service;

import com.xpressbox.domain.Rol;
import com.xpressbox.domain.Usuario;
import com.xpressbox.repository.UsuarioRepository;
import jakarta.servlet.http.HttpSession;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userDetailsService")
public class UsuarioDetailsService implements UserDetailsService{

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private HttpSession session;
    
    @Override
    @Transactional(readOnly=true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //Se busca el usuario de la tabla de usuarios.
        Usuario usuario = usuarioRepository.findByUsername(username);
        
        //Se valida si se encuentra el usuario.
        if (usuario==null){
            //El usuario con ese username, no se encontró
            throw new UsernameNotFoundException(username);
        }
        
        //Si estamos acá.... si se ecnontró el usuario
        session.removeAttribute("usuarioImagen");
        session.setAttribute("usuarioImagen", usuario.getRutaImagen());
        
        //Se cargan los roles del usuario.
        var roles=new ArrayList<GrantedAuthority>();
        for (Rol rol : usuario.getRoles()){
            roles.add(new SimpleGrantedAuthority("ROLE_"+rol.getNombre()));
        }
                
        //Se retorna el usuario con username,password y roles.
        return new User(usuario.getUsername(),usuario.getPassword(),roles);
    }    
}
