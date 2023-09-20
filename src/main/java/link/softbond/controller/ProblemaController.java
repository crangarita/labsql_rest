package link.softbond.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.util.Response;
import link.softbond.entities.Problema;
import link.softbond.repositorios.ProblemaRepository;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({"/problemas"})
public class ProblemaController {
	
	@Autowired
	private ProblemaRepository problemaRepository;

	@GetMapping
	public Response listar(){
		
		List<Problema> problemas = problemaRepository.findAll();
		
		return Response.crear(true, null, problemas.toArray());

	}
	
	@GetMapping({"/activos"})
	public Response activos(){
		
		List<Problema> problemas = problemaRepository.findByEstado(1);
		
		return Response.crear(true, null, problemas.toArray());

	}
	
	@GetMapping({"/{id}/consultas"})
	public Response consultasProblema(@PathVariable Integer id){
		
		Optional<Problema> problemaOpt = problemaRepository.findById(id);
		
		if (problemaOpt.isPresent()) {
			return Response.crear(true, null, problemaOpt.get().getConsultas().toArray());
		}
		
		return Response.crear(true, null, null);
		
	}
	
	@GetMapping({"/{id}/tablas"})
	public Response tablasProblema(@PathVariable Integer id){
		
		Optional<Problema> problemaOpt = problemaRepository.findById(id);
		
		if (problemaOpt.isPresent()) {
			return Response.crear(true, null, problemaOpt.get().getTablas().toArray());
		}
		
		return Response.crear(true, null, null);	

	}
	
	@GetMapping({"/{id}/examenes"})
	public Response examenProblema(@PathVariable Integer id){
		
		Optional<Problema> problemaOpt = problemaRepository.findById(id);
		
		if (problemaOpt.isPresent()) {
			return Response.crear(true, null, problemaOpt.get().getExamenes().toArray());
		}
		
		return Response.crear(true, null, null);

	}
	
	
	@GetMapping({"/{id}"})
	public Response getProblema(@PathVariable Integer id){
		
		Optional<Problema> problemaOpt = problemaRepository.findById(id);
		
		if (problemaOpt.isPresent()) {
			return Response.crear(true, null, problemaOpt.get());
		}
		
		return Response.crear(true, null, null);

	}
	
	
	@GetMapping({"/status"})
	public String status(){
		return "ok";
	}
	
}