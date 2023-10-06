package link.softbond.service;

import link.softbond.auth.service.JWTService;
import link.softbond.util.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
@Slf4j
public class UsuarioService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	@Autowired
	private EmailService emailService;
	@Autowired
	private String appBaseUrl;
	@Autowired
	private JWTService jwtService;

	@Override
	@Transactional(readOnly = true)
	public UserDetails loadUserByUsername(String usuarioEmail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(usuarioEmail);

		if (usuario == null) {
			log.error("Error Login: no existe el usuario " + usuarioEmail);
			throw new UsernameNotFoundException("Usuario no existe en el sistema");
		}

		if (!usuario.getEstado().contentEquals("A")) {
			log.error("Error Login: el usuario " + usuarioEmail + " aun no se ha validado");
			throw new UsernameNotFoundException("Usuario no se ha validado");
		}

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		authorities.add(new SimpleGrantedAuthority("USUARIO"));

		return new User(usuario.getEmail(), usuario.getClave(), true, true, true, true, authorities);
	}
	
	public Usuario getUsuarioCurrent() {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = (String) authentication.getPrincipal();
		
		Usuario usuario = usuarioRepository.findByEmail(username);
		
		return usuario;
	}

	public Response registrarUsuario(Usuario usuario) {

		Usuario usuarioExistente = usuarioRepository.findByEmail(usuario.getEmail());

		if (usuarioExistente != null) {
			return new Response(false, "El usuario ya existe", null, 0);
		}

		usuario.setEstado("B");
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
				if(usuario.getEstado().contentEquals("A")){
					//html = emailService.getHtml("Correo ya confirmado","");
					html = "Correo ya confirmado";
					return html;
				}
				usuario.setEstado("A");
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

	public void reestablecerContrasena(String email, String actualContraseña, String nuevaContraseña) {

		Usuario user = usuarioRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("No existe el usuario con el correo electrónico proporcionado");
		}
		if (nuevaContraseña.equals(user.getEmail())){
			throw new IllegalArgumentException("La nueva contraseña no puede ser igual al correo electrónico");
		}

		if (!new BCryptPasswordEncoder().matches(actualContraseña, user.getClave())) {
			throw new IllegalArgumentException("Contraseña actual incorrecta");
		}

		user.setClave(new BCryptPasswordEncoder().encode(nuevaContraseña));

		usuarioRepository.save(user);

	}

	public void emailContrasena(String email) {

		Usuario user = usuarioRepository.findByEmail(email);

		if (user == null) {
			throw new UsernameNotFoundException("No existe el usuario con el correo electrónico proporcionado");
		}

		String tempcontrasena = generarContrasena(10);
		user.setClave(new BCryptPasswordEncoder().encode(tempcontrasena));
		try {
			usuarioRepository.save(user);
		} catch (Exception e) {
			throw new RuntimeException("Error al guardar el usuario");
		}
		emailService.sendListEmail(email, "Contraseña de Recuperación: \n" +tempcontrasena);
	}

	public String generarEnlaceConfirmacion(String token) {
		String tokenCodificado = UriUtils.encode(token, "UTF-8");
		return UriComponentsBuilder.fromHttpUrl(appBaseUrl.concat("/users/confirmar"))
				.pathSegment(tokenCodificado)
				.toUriString();
	}

	public static String generarContrasena(int longitud) {
		String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()_+";
		Random rand = new Random();
		StringBuilder sb = new StringBuilder(longitud);
		for (int i = 0; i < longitud; i++) {
			sb.append(caracteres.charAt(rand.nextInt(caracteres.length())));
		}
		return sb.toString();
	}
}
