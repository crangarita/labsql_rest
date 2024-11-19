package link.softbond.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import link.softbond.entities.Problema;

public interface ProblemaRepository extends JpaRepository<Problema, Integer> {

	List<Problema> findByEstado(Integer estado);
	boolean existsByNombrebase(String nombrebase);

}
