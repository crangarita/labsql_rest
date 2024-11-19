package link.softbond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import link.softbond.entities.Tabla;
import link.softbond.service.TablaService;
import link.softbond.util.Response;
@RestController
@RequestMapping("/tabla")
@CrossOrigin
public class TablaController {

	@Autowired
	TablaService tablaService;
	
	@PostMapping("/save")
	public Response registrarTablaProblema(@RequestBody Tabla tabla) {
		return tablaService.registrarTablaProblema(tabla);
	}
	
	@PutMapping("/update")
	public Response actualizarTabla(@RequestBody Tabla tabla) {
		return tablaService.actualizarTablaProblema(tabla);
	}
	@DeleteMapping("/delete/{idTabla}")
	public Response deleteTabla(@PathVariable Integer idTabla) {
		return tablaService.deleteTablaProblema(idTabla);
	}
}
