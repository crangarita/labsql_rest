package link.softbond.entities;

import java.io.Serializable;
import java.sql.Timestamp;
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
public class Examen implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private String descripcion;
	
	private Timestamp fechainicio;
	
	private Timestamp fechafin;
	
	private Integer cantidad;
	
	@ManyToOne
	@JoinColumn(name="problema")
	private Problema problema;
	
	@OneToMany(mappedBy="examen")
	@JsonIgnore
	private List<Opcion> opciones;

}
