package link.softbond.service;

import java.util.Optional;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import link.softbond.dto.ConsultaDTO;
import link.softbond.entities.Consulta;
import link.softbond.entities.Problema;
import link.softbond.repositorios.ConsultaRepository;
import link.softbond.repositorios.ProblemaRepository;
import link.softbond.util.Response;

@Service
public class ConsultaService {

	@Autowired
	ConsultaRepository consultaRepository;
	@Autowired
	ProblemaRepository problemaRepository;
	
	
	public Response saveConsulta(ConsultaDTO consultaDto) {
		
		Optional<Problema>problema=problemaRepository.findById(consultaDto.getProblema().getId());
		if(!problema.isPresent()) {
			return Response.crear(true, "Problema no esta registrado", null);
		}
		Consulta consulta=new Consulta();
		BeanUtils.copyProperties(consultaDto,consulta);
		return Response.crear(true, "Consulta registrada", consultaRepository.save(consulta));
		
		
	}
	public Response updateConsulta(ConsultaDTO consulta) {
		Optional<Consulta>consultaCurrent=consultaRepository.findById(consulta.getId());
		if(!consultaCurrent.isPresent()) {
			return Response.crear(true, "Consulta no esta registrado", null);
		}
		Consulta consultaReturn=consultaCurrent.get();
		consultaReturn.setDescripcion(consulta.getDescripcion());
		consultaReturn.setEstado(consulta.getEstado());
		consultaReturn.setExplicacion(consulta.getExplicacion());
		consultaReturn.setExplicsolucion(consulta.getExplicsolucion());
		consultaReturn.setNumpracticas(consulta.getNumpracticas());
		consultaReturn.setSolucion(consulta.getSolucion());
		consultaReturn.setSolucionalternativa(consulta.getSolucionalternativa());
		return Response.crear(true, "Consulta actualizada", consultaRepository.save(consultaReturn));
	}
}
