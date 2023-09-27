package link.softbond.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.util.Response;
import link.softbond.entities.Examen;
import link.softbond.entities.Opcion;
import link.softbond.entities.Practica;
import link.softbond.entities.Problema;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.ConsultaRepository;
import link.softbond.repositorios.ExamenRepository;
import link.softbond.repositorios.OpcionRepository;
import link.softbond.repositorios.PracticaRepository;
import link.softbond.repositorios.ProblemaRepository;
import link.softbond.repositorios.UsuarioRepository;
import link.softbond.service.UsuarioService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({"/examenes"})
public class ExamenController {
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private ExamenRepository examenRepository;
	
	@Autowired
	private OpcionRepository opcionRepository;

	@Autowired
	private UsuarioService usuarioService;
	
	@GetMapping
	public Response listar(){
		
		List<Examen> examenes = examenRepository.findAll();
		
		return Response.crear(true, null, examenes.toArray());

	}
	
	@GetMapping({"/{id}/opciones"})
	public Response opcionesExamen(@PathVariable Integer id){
		
		Usuario usuario = usuarioService.getUsuarioCurrent();
		
		List<Opcion> opciones = opcionRepository.findByUsuarioAndExamenId(usuario.getId(), id);
		
		return Response.crear(true, null, opciones.toArray());
		
	}
	
	@GetMapping({"/{id}/generar"})
	public Response generarExamen(@PathVariable Integer id){
		
		Usuario usuario = usuarioService.getUsuarioCurrent();
		
		List<Opcion> opciones = opcionRepository.findByUsuarioAndExamenId(usuario.getId(), id);
		
		return Response.crear(true, null, opciones.toArray());
		
	}
	
}