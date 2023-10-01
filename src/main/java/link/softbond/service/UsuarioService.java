package link.softbond.service;

import link.softbond.auth.service.JWTService;
import link.softbond.util.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

@Service
public class UsuarioService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private String appBaseUrl;
	@Autowired
	private JWTService jwtService;
	
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

		String tokenUsuario = jwtService.generarToken(usuario);

		emailService.sendListEmail(usuario.getEmail(), generarEnlaceConfirmacion(tokenUsuario));

		return new Response(true, "Usuario registrado", null, 0);
	}

	public String confirmarCuenta(String token){
		String message;
		String html;
		try {
			int usuarioId = jwtService.getId(token);
			Usuario usuario = usuarioRepository.findById(usuarioId).orElse(null);

			if (usuario != null) {
				if(!usuario.getEstado().contentEquals("A")){
					//html = emailService.getHtml("Correo ya confirmado","");
					html = "Correo ya confirmado";
					return html;
				}
				usuario.setEstado("B");
				usuarioRepository.save(usuario);
				//html = emailService.getHtml("Cuenta creada y correo confirmado exitosamente.","");
				html = "Cuenta creada y correo confirmado exitosamente.";
				return html;
			} else {
				//html = emailService.getHtml("Token de confirmación inválido.","");
				html = "Token de confirmación inválido.";
				return html;
			}
		} catch (Exception e) {
			//html = emailService.getHtml("Token de confirmación inválido.","");
			html = "Token de confirmación inválido.";
			return html;
		}
	}
	
	public String generarEnlaceConfirmacion(String token) {
		String tokenCodificado = UriUtils.encode(token, "UTF-8");
		return UriComponentsBuilder.fromHttpUrl(appBaseUrl.concat("/users/confirmar"))
				.pathSegment(tokenCodificado)
				.toUriString();
	}
}
