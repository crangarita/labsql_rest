package link.softbond.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import link.softbond.util.Response;
import link.softbond.entities.Consulta;
import link.softbond.repositorios.ConsultaRepository;
@Service
public class ConsultaService {

    @Autowired
    ConsultaRepository consultaRepository;

    public Response actualizarUltimaOpcion(Integer idOld, Integer idNew){
        //Busco la consulta old
        Optional<Consulta>consultaCurrentOld=consultaRepository.findById(idOld);
        //Busco la consulta new
        Optional<Consulta>consultaCurrentNew=consultaRepository.findById(idNew);
        if(consultaCurrentOld.isPresent() && consultaCurrentNew.isPresent()){
            //Actualizo la consulta old
            Consulta consultaReturnOld=consultaCurrentOld.get();
            consultaReturnOld.setUltimaOpcion(false);
             //Actualizo la consulta new
            Consulta consultaReturnNew=consultaCurrentOld.get();
            consultaReturnNew.setUltimaOpcion(true);
            return Response.crear(true, "Consultas actualizada", true);
        } 
        return Response.crear(false, "Error para actualizar las consultas", null);
    }
}
