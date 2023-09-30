package link.softbond.service;

import link.softbond.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	public Usuario getUsuarioCurrent() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String) authentication.getPrincipal();
		
		Usuario usuario = usuarioRepository.findByEmail(username);
		
		return usuario;
	}

	public Response registerUsuario(Usuario usuario) {

		Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

		if (usuarioExistente != null) {
			return new Response(false, "El usuario ya existe", null, 0);
		}

		usuario.setEstado("A");
		usuario.setClave(new BCryptPasswordEncoder().encode(usuario.getClave()));

		usuarioRepository.save(usuario);

		return new Response(true, "Usuario registrado", null, 0);
	}
	
}
