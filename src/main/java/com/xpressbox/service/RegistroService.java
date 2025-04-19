package com.xpressbox.service;

import com.xpressbox.domain.Usuario;

import jakarta.mail.MessagingException;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

@Service
public class RegistroService {

    @Autowired
    private CorreoService correoService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MessageSource messageSource;

    
    //Este metodo se usa cuando se va a "activar" un usuario via el correo enviado, por medio del link.
    public Model activar(Model model, String username, String clave) {
        Usuario usuario = usuarioService.getUsuarioPorUsernameYPassword(username,clave);
        if (usuario != null) {
            model.addAttribute("usuario", usuario);
        } else {
            model.addAttribute(
                    "titulo",
                    messageSource.getMessage(
                            "registro.activar",
                            null, Locale.getDefault()));
            model.addAttribute(
                    "mensaje",
                    messageSource.getMessage(
                            "registro.activar.error",
                            null, Locale.getDefault()));
        }
        return model;
    }

    //Este metodo se utiliza cuando finalmente esi se termina de activar el usuario, ya con foto.
    public void activar(Usuario usuario, MultipartFile imagenFile) {
        var codigo = new BCryptPasswordEncoder();
        usuario.setPassword(codigo.encode(usuario.getPassword()));

        if (!imagenFile.isEmpty()) {
            usuarioService.save(usuario, false);
        }
        usuarioService.save(usuario, true);
    }

    //Es el primer metodo que se usa para crear "Parcialmente" el usuario y enviar un link por correo.
    public Model crearUsuario(Model model, Usuario usuario)
            throws MessagingException {
        String mensaje;
        if (!usuarioService.existeUsuarioPorUsernameOCorreo(usuario.getUsername(),usuario.getCorreo())) {
            String clave = demeClave();
            usuario.setPassword(clave);
            usuario.setActivo(false);
            usuarioService.save(usuario, true);
            enviaCorreoActivar(usuario);
            mensaje = String.format(
                    messageSource.getMessage(
                            "registro.mensaje.activacion.ok",
                            null,
                            Locale.getDefault()),
                    usuario.getCorreo());
        } else {
            mensaje = String.format(
                    messageSource.getMessage(
                            "registro.mensaje.usuario.o.correo",
                            null,
                            Locale.getDefault()),
                    usuario.getUsername(), usuario.getCorreo());
        }
        model.addAttribute(
                "titulo",
                messageSource.getMessage(
                        "registro.activar",
                        null,
                        Locale.getDefault()));
        model.addAttribute(
                "mensaje",
                mensaje);
        return model;
    }

    public Model recordarUsuario(Model model, Usuario usuario)
            throws MessagingException {
        String mensaje;
        Usuario usuario2 = usuarioService.getUsuarioPorUsernameOCorreo(
                usuario.getUsername(),
                usuario.getCorreo());
        if (usuario2 != null) {
            String clave = demeClave();
            usuario2.setPassword(clave);
            usuario2.setActivo(false);
            usuarioService.save(usuario2, false);
            enviaCorreoRecordar(usuario2);
            mensaje = String.format(
                    messageSource.getMessage(
                            "registro.mensaje.recordar.ok",
                            null,
                            Locale.getDefault()),
                    usuario.getCorreo());
        } else {
            mensaje = String.format(
                    messageSource.getMessage(
                            "registro.mensaje.usuario.o.correo",
                            null,
                            Locale.getDefault()),
                    usuario.getUsername(), usuario.getCorreo());
        }
        model.addAttribute(
                "titulo",
                messageSource.getMessage(
                        "registro.activar",
                        null,
                        Locale.getDefault()));
        model.addAttribute(
                "mensaje",
                mensaje);
        return model;
    }

    private String demeClave() {
        String tira = "ABCDEFGHIJKLMNOPQRSTUXYZabcdefghijklmnopqrstuvwxyz0123456789";
        String clave = "";
        for (int i = 0; i < 40; i++) {
            clave += tira.charAt((int) (Math.random() * tira.length()));
        }
        return clave;
    }

    //Ojo cómo le lee una informacion del application.properties
    @Value("${servidor.http}")
    private String servidor;

    private void enviaCorreoActivar(Usuario usuario) throws MessagingException {
        String mensaje = messageSource.getMessage(
                "registro.correo.activar",
                null, Locale.getDefault());
        mensaje = String.format(
                mensaje, usuario.getNombre(),
                usuario.getApellidos(), servidor,
                usuario.getUsername(), usuario.getPassword());
        String asunto = messageSource.getMessage(
                "registro.mensaje.activacion",
                null, Locale.getDefault());
        correoService.enviarCorreoHtml(usuario.getCorreo(), asunto, mensaje);
    }

    private void enviaCorreoRecordar(Usuario usuario) throws MessagingException {
        String mensaje = messageSource.getMessage(""
                + "registro.correo.recordar",
                null,
                Locale.getDefault());
        mensaje = String.format(
                mensaje, usuario.getNombre(),
                usuario.getApellidos(), servidor,
                usuario.getUsername(), usuario.getPassword());
        String asunto = messageSource.getMessage(
                "registro.mensaje.recordar",
                null, Locale.getDefault());
        correoService.enviarCorreoHtml(
                usuario.getCorreo(),
                asunto, mensaje);
    }
}
