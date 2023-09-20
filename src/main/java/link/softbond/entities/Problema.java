package link.softbond.entities;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Entity 
@Data
public class Problema implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String nombre;

	private String descripcion;

	private String docente;

	private String nombrebase;
	
	private Integer estado;
	
	@OneToMany(mappedBy="problema")
	@JsonIgnore
	private List<Consulta> consultas;
	
	@OneToMany(mappedBy="problema")
	@JsonIgnore
	private List<Tabla> tablas;
	
	@OneToMany(mappedBy="problema")
	@JsonIgnore
	private List<Examen> examenes;
}
