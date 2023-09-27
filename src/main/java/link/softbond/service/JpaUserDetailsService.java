package link.softbond.service;

import java.util.ArrayList;
import java.util.List; 

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import link.softbond.entities.Usuario;
import link.softbond.repositorios.UsuarioRepository;
//import com.encrypta.backendapp.model.entities.Rol;

@Service ("jpaUserDetailsService")
public class
JpaUserDetailsService implements UserDetailsService {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	
	private Logger logger = LoggerFactory.getLogger(JpaUserDetailsService.class);
	
	@Override
	@Transactional (readOnly = true)
	public UserDetails loadUserByUsername(String usuarioEmail) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByEmail(usuarioEmail);

		if (usuario == null) {
			logger.error("Error Login: no existe el usuario " + usuarioEmail);
			throw new UsernameNotFoundException("Usuario no existe en el sistema");
		}
		
		if (!usuario.getEstado().contentEquals("A")) {
			logger.error("Error Login: el usuario " + usuarioEmail + " aun no se ha validado");
			throw new UsernameNotFoundException("Usuario no se ha validado");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>(); 
		
		authorities.add(new SimpleGrantedAuthority("USUARIO"));

		return new User(usuario.getEmail(), usuario.getClave(), true, true, true, true, authorities);
	}

}
