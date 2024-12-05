package link.softbond.dto;

import java.util.List;


import link.softbond.entities.Examen;
import link.softbond.entities.Tabla;
import lombok.Data;

@Data
public class ProblemaDTO {
	private Integer id;

	private String nombre;

	private String descripcion;

	private String docente;
	

	private String nombrebase;
	
	private Integer estado;
	
	
	private List<ConsultaDTO> consultas;
	
	
	private List<Tabla> tablas;
	
	

}
