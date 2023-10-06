package link.softbond.controller;

import link.softbond.entities.Usuario;
import link.softbond.model.ReestablecerRequest;
import link.softbond.service.UsuarioService;
import link.softbond.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({"/users"})
public class UserController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping({"/register"})
    public Response registerUsuario(@RequestBody Usuario usuario) {

        return usuarioService.registrarUsuario(usuario);

    }

    @GetMapping("/confirmar/{token}")
    public String confirmarCuenta(@PathVariable String token){

        return usuarioService.confirmarCuenta(token);

    }

    @PostMapping("/reestablecer/email")
    public Response emailContrasena(@RequestParam String email) {

        usuarioService.emailContrasena(email);;

        return new Response(true, "Se ha enviado un correo electrónico para restablecer la contraseña.", null, 0);
    }

    @PostMapping({"/reestablecer"})
    public Response reestablecerClave(@RequestBody ReestablecerRequest reestablecer) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        usuarioService.reestablecerContrasena(email, reestablecer.getActualContraseña(), reestablecer.getNuevaContraseña());

        return new Response(true, "Se ha reestablecido la contraseña con exito.", null, 0);
    }
}
