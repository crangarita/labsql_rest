package link.softbond.controller;

import link.softbond.entities.Usuario;
import link.softbond.service.UsuarioService;
import link.softbond.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({"/users"})
public class UserController {

    @Autowired
    UsuarioService usuarioService;

    @PostMapping({"/register"})
    public Response registerUsuario(@RequestBody Usuario usuario) {

        return usuarioService.registerUsuario(usuario);

    }

    @GetMapping("/confirmar/{token}")
    public String confirmarCuenta(@PathVariable String token){

        return usuarioService.confirmarCuenta(token);

    }
}
