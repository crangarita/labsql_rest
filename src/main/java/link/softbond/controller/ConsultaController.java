package link.softbond.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.util.Response;
import link.softbond.dto.ConsultaDTO;
import link.softbond.entities.Consulta;
import link.softbond.entities.Practica;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.ConsultaRepository;
import link.softbond.repositorios.DBRepository;
import link.softbond.repositorios.PracticaRepository;
import link.softbond.service.ConsultaService;
import link.softbond.service.PracticaService;
import link.softbond.service.UserService;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping({ "/consultas" })
public class ConsultaController {

	@Autowired
	private PracticaRepository practicaRepository;

	@Autowired
	private UserService userService;



	@Autowired
	private PracticaService practicaService;
	@Autowired
	private ConsultaService consultaService;

	

	@GetMapping("/practicas/usuario")
	public Response consultasPracticasUsuario() {

		Usuario usuario = userService.getUsuarioCurrent();
		return practicaService.listaPracticasUsuario(usuario.getId());

	}

	@GetMapping("/practica/opcion/{idOld}/old/{nueva}/new")
	public Response practicaUltimaOpcion(@PathVariable Integer idOld, @PathVariable Integer nueva) {
		return practicaService.actualizarUltimaOpcion(idOld, nueva);

	}



	@GetMapping({ "/{id}/practicas" })
	public Response consultasProblema(@PathVariable Integer id) {

		Usuario usuario = userService.getUsuarioCurrent();

		List<Practica> practicas = practicaRepository.findByUsuarioAndConsultaId(usuario.getId(), id);

		return Response.crear(true, null, practicas.toArray());

	}

	@GetMapping({ "/{id}/ejemplo" })
	public Response ejemploConsulta(@PathVariable Integer id) {

		return practicaService.ejecutarEjemplo(id);

	}

	@PostMapping({ "/{id}/practicas" })
	public Response ejecutarConsulta(@PathVariable Integer id, @RequestBody Map<String, Object> json) {

		Usuario usuario = userService.getUsuarioCurrent();

		String sql = (String) json.get("sql");

		Response result = practicaService.ejecutar(id, sql);

		return result;

	}
	
	@PostMapping("/save")
	public Response saveConsulta(@RequestBody ConsultaDTO consulta) {
		return consultaService.saveConsulta(consulta);
	}
	@PutMapping("/update")
	public Response updateConsulta(@RequestBody ConsultaDTO consulta) {
		return consultaService.saveConsulta(consulta);
	}

}