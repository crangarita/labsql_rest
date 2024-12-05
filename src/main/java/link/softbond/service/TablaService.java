package link.softbond.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import link.softbond.entities.Problema;
import link.softbond.entities.Tabla;
import link.softbond.repositorios.ProblemaRepository;
import link.softbond.repositorios.TablaRespository;
import link.softbond.util.Response;

@Service
public class TablaService {
	
	@Autowired
	TablaRespository tablaRespository;
	@Autowired
	ProblemaRepository problemaRepository;
	
	public Response registrarTablaProblema(Tabla tabla) {
		Optional<Problema>problema=problemaRepository.findById(tabla.getProblema().getId());
		if(!problema.isPresent()) {
			return Response.crear(false, "Problema no esta registrado", null);
		}
		
		return Response.crear(true, "Problema registrado", tablaRespository.save(tabla));
		
	}
	public Response actualizarTablaProblema(Tabla tabla) {
		Optional<Tabla>tablaCurrent=tablaRespository.findById(tabla.getId());
		if(!tablaCurrent.isPresent()) {
			return Response.crear(false, "Tabla no esta registrada", null);
		}
		Tabla tablarReturn=tablaCurrent.get();
		tablarReturn.setNombre(tabla.getNombre());
		tablarReturn.setDescripcion(tabla.getDescripcion());
		return Response.crear(true, "Tabla actualizada", tablaRespository.save(tablarReturn));
	}
	public Response deleteTablaProblema(Integer idTabla) {
		Optional<Tabla>tablaCurrent=tablaRespository.findById(idTabla);
		if(!tablaCurrent.isPresent()) {
			return Response.crear(false, "Tabla no esta registrada", null);
		}
		tablaRespository.deleteById(idTabla);
		return Response.crear(true, "Tabla eliminada",true );
		
	}

}
