package link.softbond.dto;

import java.util.List;

import link.softbond.entities.Opcion;
import link.softbond.entities.Practica;
import link.softbond.entities.Problema;
import lombok.Data;

@Data
public class ConsultaDTO {
	private Integer id;

	private String descripcion;

	private String explicacion;

	private String solucion;

	private String explicsolucion;

	private String solucionalternativa;

	private Integer numpracticas;

	private Integer estado;

	private Problema problema;

	private List<Practica> practicas;

	
	private List<Opcion> opciones;

}
