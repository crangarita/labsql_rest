package link.softbond.controller;

import java.util.List;
import java.util.Optional;

import link.softbond.service.ExamenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
	private ExamenService examenService;

	@Autowired
	private UserService userService;
	
	@GetMapping
	public Response listar(){
		
		List<Examen> examenes = examenRepository.findAll();
		
		return Response.crear(true, null, examenes.toArray());

	}

	@PostMapping
	public Response crear(@RequestBody Examen examen){

		examenRepository.save(examen);

		return Response.crear(true, null, examen);

	}
	
	@GetMapping({"/{token}/generar"})
	public Response generarExamen(@PathVariable String token){
		
		Usuario usuario = userService.getUsuarioCurrent();
		
		List<Opcion> opciones = examenService.generarExamen(usuario.getId(), token);
		
		return Response.crear(true, null, opciones.toArray());
		
	}
	
	@GetMapping({"/{id}/opciones"})
	public Response opcionesExamen(@PathVariable Integer id){
		
		Usuario usuario = userService.getUsuarioCurrent();
		
		List<Opcion> opciones = opcionRepository.findByUsuarioAndExamenId(usuario.getId(), id);
		
		if (opciones.isEmpty()) {
			examenService.generarExamen(usuario.getId(), id);
			opciones = opcionRepository.findByUsuarioAndExamenId(usuario.getId(), id);
		}
		
		return Response.crear(true, null, opciones.toArray());
		
	}
	
}