package link.softbond.repositorios;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import link.softbond.entities.Practica;
import link.softbond.entities.Usuario;

@Repository
public interface PracticaRepository extends JpaRepository<Practica, Integer> {
	
	public List<Practica> findByUsuarioAndConsultaId(Usuario usuario, Integer idconsulta);
	
	public List<Practica> findByUsuario(Usuario usuario);
	
	@Query("SELECT p " +
		       "FROM Practica p " +
		       "JOIN p.consulta c " +
		       "JOIN c.opciones o " +
		       "JOIN o.examen e " +
		       "WHERE e.id = :examenId")
		List<Practica> findPracticasByExamenId(@Param("examenId") Integer examenId);

}
