package link.softbond.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import link.softbond.entities.Opcion;

public interface OpcionRepository extends JpaRepository<Opcion, Integer> {

	List<Opcion> findByUsuarioAndExamenId(Integer idUsuario, Integer idExamen);


}
