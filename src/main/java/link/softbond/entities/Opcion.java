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

import lombok.Data;

@Entity 
@Data
public class Opcion implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Timestamp fecha;
	
	@ManyToOne
	@JoinColumn(name="consulta")
	private Problema consulta;
	
	@ManyToOne
	@JoinColumn(name="examen")
	private Problema examen;
	
	private Integer usuario;

}
