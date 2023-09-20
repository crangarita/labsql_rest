package link.softbond.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import link.softbond.entities.Practica;

public interface PracticaRepository extends JpaRepository<Practica, Integer> {
	
	public List<Practica> findByUsuarioAndConsultaId(Integer usuario, Integer idconsulta);

}
