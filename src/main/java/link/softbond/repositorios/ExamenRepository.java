package link.softbond.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;


import link.softbond.entities.Examen;
import link.softbond.entities.Opcion;

public interface ExamenRepository extends JpaRepository<Examen, Integer> {

}
