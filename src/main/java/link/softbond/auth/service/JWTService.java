package link.softbond.auth.service;

import java.io.IOException;
import java.util.Collection;

import link.softbond.entities.Usuario;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.jsonwebtoken.Claims;

public interface JWTService {
	
	public String create(Authentication auth) throws JsonProcessingException;
	public boolean validate(String token);
	public Claims getClaims(String token);
	public String getUsername(String token);
	public Collection<? extends GrantedAuthority> getRoles(String token) throws IOException;
	public String resolve(String token);
	public String generarToken(Usuario usuario);
	public Integer getId(String token);

}
