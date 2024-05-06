package link.softbond.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity 
@Data
public class                                  Consulta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String descripcion;
	
	private String explicacion;
	
	@JsonIgnore
	private String solucion;
	
	@JsonIgnore
	private String explicsolucion;
	
	@JsonIgnore
	private String solucionalternativa;
	
	private Integer numpracticas;
	
	private Integer estado;

	private Boolean ultimaOpcion;
	
	@ManyToOne
	@JoinColumn(name="idproblema")
	private Problema problema;
	
	@OneToMany(mappedBy="consulta")
	@JsonIgnore
	private List<Practica> practicas;
	
	@OneToMany(mappedBy="consulta")
	@JsonIgnore
	private List<Opcion> opciones;

}
