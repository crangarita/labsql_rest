package link.softbond.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import link.softbond.entities.Consulta;
import link.softbond.entities.Practica;
import link.softbond.entities.Usuario;
import link.softbond.repositorios.ConsultaRepository;
import link.softbond.repositorios.DBRepository;
import link.softbond.repositorios.PracticaRepository;
import link.softbond.util.Response;

@Service
public class PracticaService {

	@Autowired
	private PracticaRepository practicaRepository;

	@Autowired
	private DBRepository dBRepository;

	@Autowired
	private ConsultaRepository consultaRepository;

	@Autowired
	private UserService userService;

	public Response listaPracticasUsuario(Usuario usuario) {
		List<Practica> practicas = practicaRepository.findByUsuario(usuario);
		return Response.crear(true, "Listado de practicas del usuario", practicas);
	}

	public Response registrarPractica(Integer consultaId, String sql) {

		Optional<Consulta> consulta = consultaRepository.findById(consultaId);

		Practica practica = new Practica();

		practica.setSsql(sql);

		practica.setConsulta(consulta.get());

		practica.setFecha(LocalDateTime.now());

		String resultado = this.ejecutarPractica(practica);

		return null;
	}

	public String ejecutarPractica(Practica practica) {
		return "";
	}

	public Response ejecutar(Integer consultaId, String sql) {

		Optional<Consulta> consulta = consultaRepository.findById(consultaId);
		Usuario usuario = userService.getUsuarioCurrent();

		if (!consulta.isPresent()) {
			return Response.crear(false, "Error: consulta no existe", null);
		}

		Practica practica = new Practica();
		practica.setConsulta(consulta.get());
		practica.setFecha(LocalDateTime.now());
		practica.setSsql(sql);
		practica.setUsuario(usuario);
		practica.setEstado(1);

		String database = consulta.get().getProblema().getNombrebase();
		String sqlEjemplo = consulta.get().getSolucion();

		List<Map<String, Object>> resultPractica = null;

		try {
			resultPractica = this.ejecutarQuery(database, sql);
		} catch (Exception e) {
			practica.setResultado("Error: " + e.getMessage());
			practica.setEstado(0);
			this.guardar(practica);
			return Response.crear(false, "Error: " + e.getMessage(), null);
		}

		List<Map<String, Object>> resultEjemplo = this.ejecutarQuery(database, sqlEjemplo);

		if (areQueryResultsIdenticalHash(resultPractica, resultEjemplo)) {
			practica.setResultado("Consulta correcta");
			this.guardar(practica);
			return Response.crear(true, "Consulta correcta", resultPractica.toArray());
		} else {
			practica.setResultado("Consulta incorrecta");
			this.guardar(practica);
			return Response.crear(true, "Consulta incorrecta", resultPractica.toArray());
		}

	}

	private void guardar(Practica practica) {

		practicaRepository.save(practica);

	}

	public Response ejecutarEjemplo(Integer consultaId) {

		Optional<Consulta> consulta = consultaRepository.findById(consultaId);

		if (!consulta.isPresent()) {
			return Response.crear(false, "Error: consulta no existe", null);
		}

		try {
			List<Map<String, Object>> result = this.ejecutarQuery(consulta.get().getProblema().getNombrebase(),
					consulta.get().getSolucion());

			int maxRegistros = 10;
			List<Map<String, Object>> resultadoLimitado = result.subList(0, Math.min(result.size(), maxRegistros));

			return Response.crear(true, null, resultadoLimitado.toArray());
		} catch (Exception e) {
			return Response.crear(false, "Error: se ha generado error al intentar ejecutar el ejemplo", null);
		}
	}

	public Response actualizarUltimaOpcion(Integer idOld, Integer idNew) {
		// Busco la practica old
		Optional<Practica> practicaCurrentOld = practicaRepository.findById(idOld);
		// Busco la practica new
		Optional<Practica> practicaCurrentNew = practicaRepository.findById(idNew);
		if (!practicaCurrentOld.isPresent() || !practicaCurrentNew.isPresent()) {
			return Response.crear(false, "Error para actualizar las consultas", null);
		}
		// Actualizo la practica old
		Practica practicaReturnOld = practicaCurrentOld.get();
		practicaReturnOld.setUltimaOpcion(false);
		// Actualizo la practica new
		Practica practicaReturnNew = practicaCurrentNew.get();
		practicaReturnNew.setUltimaOpcion(true);
		// Guardo las practica
		practicaRepository.save(practicaReturnOld);
		practicaRepository.save(practicaReturnNew);

		return Response.crear(true, "Consultas actualizada", practicaReturnNew);

	}

	 public Response obtenerPracticasPorExamen(Integer examenId) {
	        List<Practica> practicas = practicaRepository.findPracticasByExamenId(examenId);
	        return Response.crear(true, "Prácticas del examen", practicas);
	    }

	private List<Map<String, Object>> ejecutarQuery(String database, String sql) {

		List<Map<String, Object>> result = dBRepository.ejecutarConsulta(database, sql);
		return result;

	}

	/*
	 * 
	 * private Result obtenerEjemplo(Consulta consulta) { try { this.errorExample =
	 * false; ResConsulta x =
	 * ConexionBD.ejecutarConsulta(consulta.getProblema().getNombrebase(),
	 * consulta.getSolucion(), 5);
	 * 
	 * if(x.getRes()==null) { this.errorExample = true; this.mensaje =
	 * "La consulta de ejemplo ha presentado un error y este ejercicio no se encuentra disponible en este momento"
	 * ; this.tipo = "danger"; return null; } else { if(x.getRes().getRowCount()<=0)
	 * { this.errorExample = true; this.mensaje =
	 * "La consulta de ejemplo no ha retornado resultados y este ejercicio no se encuentra disponible en este momento"
	 * ; this.tipo = "danger"; return null; } }
	 * 
	 * return x.getRes();
	 * 
	 * } catch (SQLException e) { this.errorExample = true; this.mensaje =
	 * "La consulta de ejemplo ha presentado un error y este ejercicio no se encuentra disponible en este momento"
	 * ; this.tipo = "danger"; return null; } }
	 */

	public boolean areQueryResultsIdentical(List<Map<String, Object>> result1, List<Map<String, Object>> result2) {
		if (result1.size() != result2.size()) {
			return false;
		}

		for (int i = 0; i < result1.size(); i++) {
			Map<String, Object> row1 = result1.get(i);
			Map<String, Object> row2 = result2.get(i);

			if (!areRowsIdentical(row1, row2)) {
				return false;
			}
		}

		return true;
	}

	public boolean areRowsIdentical(Map<String, Object> row1, Map<String, Object> row2) {
		if (row1.size() != row2.size()) {
			return false;
		}

		for (String key : row1.keySet()) {
			if (!row2.containsKey(key) || !row1.get(key).equals(row2.get(key))) {
				return false;
			}
		}

		return true;
	}

	public boolean areQueryResultsIdenticalHash(List<Map<String, Object>> result1, List<Map<String, Object>> result2) {
		if (result1.size() != result2.size()) {
			return false;
		}

		int hashCode1 = generateHashCode(result1);
		int hashCode2 = generateHashCode(result2);

		return hashCode1 == hashCode2;
	}

	public int generateHashCode(List<Map<String, Object>> result) {
		int hashCode = 1;

		for (Map<String, Object> row : result) {
			hashCode = 31 * hashCode + row.hashCode();
		}

		return hashCode;
	}

}
