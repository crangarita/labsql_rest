package link.softbond.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import link.softbond.dto.ConsultaDTO;
import link.softbond.dto.ProblemaDTO;
import link.softbond.entities.Consulta;
import link.softbond.entities.Problema;
import link.softbond.entities.Tabla;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.ConsultaRepository;
import link.softbond.repositorios.ProblemaRepository;
import link.softbond.repositorios.TablaRespository;
import link.softbond.repositorios.UsuarioRepository;
import link.softbond.util.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ProblemaService {

	private static final Logger logger = LoggerFactory.getLogger(ProblemaService.class);

	@Autowired
	private ProblemaRepository problemaRepository;

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private TablaRespository tablaRespository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	@Autowired
	private FileService fileService;
	@Autowired
	private UserService userService;

	@Autowired
	DBService dbService;
	
	public Response updateProblema(Problema problema) {
		Optional<Problema>problemaCurrent=problemaRepository.findById(problema.getId());
		if(!problemaCurrent.isPresent()) {
			return Response.crear(false, "Problema esta registrado", null);
			
		}
		Problema problemaReturn=problemaCurrent.get();
		problemaReturn.setNombre(problema.getNombre());
		problemaReturn.setDescripcion(problema.getDescripcion());
		problemaReturn.setDocente(problema.getDocente());
		problemaReturn.setEstado(problema.getEstado());
		return Response.crear(true, "Problema Actualizado", problemaRepository.save(problemaReturn));
	}

	public Response saveProblema(ProblemaDTO problemaDTO, MultipartFile file, MultipartFile backup) {
		try {
			logger.info("Iniciando registro de problema: {}", problemaDTO.getNombrebase());

			// Buscar usuario asociado al problema
			Usuario usuario = usuarioRepository.findByEmail(problemaDTO.getDocente());
			if (usuario == null) {
				return Response.crear(false, "Docente no encontrado: " + problemaDTO.getDocente(), null);
			}

			// Valido si ya existe el nombre de la base de datos
			boolean existeNombreBase = problemaRepository.existsByNombrebase(problemaDTO.getNombrebase().toLowerCase());
			if (existeNombreBase) {
				return Response.crear(false, "Nombre de la BD ya existe", null);
			}

			// Crear el problema
			Problema problema = new Problema();
			BeanUtils.copyProperties(problemaDTO, problema);
			problema.setNombrebase(problemaDTO.getNombrebase().toLowerCase());
			problema.setDocente(usuario.getNombre());
			

			// Realizo el backup en el servidor
			dbService.restoreDatabaseFromBackup(backup, problema.getNombrebase());
			// Guardar archivo asociado
			fileService.storeFile(file, problema.getNombrebase());
			// Guardar archivo backup
			fileService.storeBackup(backup, problema.getNombrebase());
			

			//Guardo el problema 
			Problema problemaSaved = problemaRepository.save(problema);
			
			// Procesar tablas asociadas
			problemaDTO.getTablas().forEach(tabla -> tabla.setProblema(problemaSaved));
			tablaRespository.saveAll(problemaDTO.getTablas());

			// Procesar consultas asociadas
			List<Consulta> consultas = problemaDTO.getConsultas().stream().map(consultaDTO -> {
				Consulta consulta = new Consulta();
				BeanUtils.copyProperties(consultaDTO, consulta);
				consulta.setProblema(problemaSaved);
				consulta.setEstado(0);
				return consulta;
			}).collect(Collectors.toList());

			consultaRepository.saveAll(consultas);

			logger.info("Problema registrado exitosamente: {}", problemaSaved.getNombrebase());
			return Response.crear(true, "Problema registrado", problemaSaved);

		} catch (IOException e) {
			logger.error("Error al almacenar archivo para el problema: {}", problemaDTO.getNombrebase(), e);
			return Response.crear(false, "Error al guardar archivo", e);
		} catch (Exception e) {
			logger.error("Error general al registrar problema: {}", problemaDTO, e);
			return Response.crear(false, "Error registrando problema", e);
		}
	}

	public Response listaConsultas(Integer id) {
		Usuario usuario = userService.getUsuarioCurrent();
		if (usuario.getRol() == null) {
			return Response.crear(false, "Usuario no tiene rol", null);
		}
		if (!usuario.getRol().getNombre().equals("PROFESOR")) {
			return Response.crear(false, "Rol no autorizado", null);
		}
		Optional<Problema> problema = problemaRepository.findById(id);
		if (!problema.isPresent()) {
			return Response.crear(false, "Problema no esa registrado", null);
		}
		List<Consulta> consultas = consultaRepository.findByProblema(problema.get());
		List<ConsultaDTO> consultasDTO = new ArrayList<>();
		for (Consulta consulta : consultas) {
			ConsultaDTO consultaDTO = new ConsultaDTO();
			BeanUtils.copyProperties(consulta, consultaDTO);
			consultasDTO.add(consultaDTO);
		}
		return Response.crear(true, "Lista de consultas", consultasDTO);

	}
}
