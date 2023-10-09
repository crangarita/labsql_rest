package link.softbond.auth.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import link.softbond.auth.service.JWTService;
import link.softbond.auth.service.JWTServiceImpl;
import link.softbond.dto.UsuarioDTO;
import link.softbond.util.Response;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private AuthenticationManager authenticationManager;
	private JWTService jwtService;
	private static final Logger logger = LoggerFactory.getLogger(JWTAuthenticationFilter.class);

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTService jwtService) {

		this.authenticationManager = authenticationManager;
		setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher("/login", "POST"));
		
		this.jwtService = jwtService;
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {
		
		String username = obtainUsername(request);
		String password = obtainPassword(request);

		if (username != null && password != null) {
			logger.info("Usuario: " + username);
		} else {
			UsuarioDTO user = null;
			try {
				ServletInputStream x = request.getInputStream();
				
				user = new ObjectMapper().readValue(x, UsuarioDTO.class);

				username = user.getUsuario();
				password = user.getClave();
				
				username = username.toLowerCase();
				user.setUsuario(username);
				
				logger.info("Usuario: " + username);
				
				
			} catch (IOException e) {
				logger.error("Usuario: " + username + " Err " + e.getMessage());
				e.printStackTrace();
			}
		}

		username = username.trim();

		UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

		return authenticationManager.authenticate(authToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		
		String token = jwtService.create(authResult);
		response.addHeader(JWTServiceImpl.HEADER_STRING, JWTServiceImpl.TOKEN_PREFIX + token);

		Map<String, Object> body = new HashMap<String, Object>();
		body.put("token", token);
		User u = (User) authResult.getPrincipal();
		body.put("user", u);
		body.put("mensaje", String.format("Hola %s, has iniciado sesión con éxito!", authResult.getName())); // Tambien puede ser ((User)authResult.getPrincipal()).getUsername()
		
		logger.info("Usuario: " + u.getUsername());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(Response.crear(true, "El ingreso se ha realizado de forma correcta", body)));
		response.setStatus(200);
		response.setContentType("application/json"); 
	}

	@Override
	protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
		AuthenticationException failed) throws IOException, ServletException {
		Map<String, Object> body = new HashMap<String, Object>();
		body.put("mensaje", "Error de autenticación: Usuario o Password errones");
		body.put("error", failed.getMessage());
		
		String username = obtainUsername(request);
		
		logger.error("Usuario: " + username + " Err " + failed.getMessage());
		
		response.getWriter().write(new ObjectMapper().writeValueAsString(Response.crear(false, "Se ha generado un error al intentar ingresar", body)));
		response.setStatus(200);
		response.setContentType("application/json");
	}
	
	

}

