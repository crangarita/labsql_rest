package link.softbond.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import link.softbond.util.Response;
import link.softbond.entities.Consulta;
import link.softbond.entities.Problema;
import link.softbond.repositorios.ProblemaRepository;
import link.softbond.service.ExamenService;
import link.softbond.service.FileService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({"/problemas"})
public class ProblemaController {
	
	@Autowired
	private ProblemaRepository problemaRepository;

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ExamenService examenService;
	
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
			if (problemaOpt.get().getEstado()==1) {
				return Response.crear(true, null, problemaOpt.get().getConsultas().toArray());
			}else {
				List<Consulta> consultas = examenService.getConsultas(id);
				return Response.crear(true, null, consultas.toArray());
			}
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
	
	@GetMapping("/files/view/{filename}")
	  @ResponseBody
	  public ResponseEntity<Resource> viewFile(@PathVariable String filename) {
	    Resource file = fileService.loadFile(filename );
	    
	    HttpHeaders headers = new HttpHeaders();

	    headers.add("content-disposition", "inline; filename="+file.getFilename());
	    headers.setContentType(MediaType.parseMediaType("image/jpeg"));
	    
	    ResponseEntity<Resource> response = new ResponseEntity<Resource>(
	            file, headers, HttpStatus.OK);

	    return response;
	    

	  }
	
}