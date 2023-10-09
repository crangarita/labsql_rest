package link.softbond.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.util.Response;
import link.softbond.entities.Examen;
import link.softbond.entities.Opcion;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.ExamenRepository;
import link.softbond.repositorios.OpcionRepository;
import link.softbond.repositorios.UsuarioRepository;
import link.softbond.service.UserService;

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
	private UserService userService;
	
	@GetMapping
	public Response listar(){
		
		List<Examen> examenes = examenRepository.findAll();
		
		return Response.crear(true, null, examenes.toArray());

	}
	
	@GetMapping({"/{id}/opciones"})
	public Response opcionesExamen(@PathVariable Integer id){
		
		Usuario usuario = userService.getUsuarioCurrent();
		
		List<Opcion> opciones = opcionRepository.findByUsuarioAndExamenId(usuario.getId(), id);
		
		return Response.crear(true, null, opciones.toArray());
		
	}
	
	@GetMapping({"/{id}/generar"})
	public Response generarExamen(@PathVariable Integer id){
		
		Usuario usuario = userService.getUsuarioCurrent();
		
		List<Opcion> opciones = opcionRepository.findByUsuarioAndExamenId(usuario.getId(), id);
		
		return Response.crear(true, null, opciones.toArray());
		
	}
	
}