package link.softbond.controller;

import link.softbond.dto.UsuarioDTO;
import link.softbond.model.request.UserRegisterRequest;
import link.softbond.service.UserService;
import link.softbond.util.Response;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({"/users"})
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping({"/register"})
    public Response registerUsuario(@RequestBody @Valid UserRegisterRequest usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, usuarioDTO);
        return userService.registrarUsuario(usuarioDTO);

    }

    @GetMapping("/confirmar/{token}")
    public String confirmarCuenta(@PathVariable String token){

        return userService.confirmarCuenta(token);

    }

    @PostMapping("/reestablecer/email")
    public Response emailContrasena(@RequestParam String email) {

        userService.emailContrasena(email);

        return new Response(true, "Se ha enviado un correo electrónico para restablecer la contraseña.", null, 0);
    }

    @PostMapping("/reestablecer/codigo")
    public Response codigoTemporal(@RequestParam String email,@RequestParam String codigoTemporal) {

        return userService.confirmarCodigo(email, codigoTemporal);
    }

    @PostMapping({"/reestablecer"})
    public Response reestablecerClave(@RequestParam String nuevaContraseña) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getPrincipal().toString();
        userService.reestablecerContrasena(email, nuevaContraseña);

        return new Response(true, "Se ha reestablecido la contraseña con exito.", null, 0);
    }
}
