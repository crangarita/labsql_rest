package link.softbond.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity 
@Data
public class Practica implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;
	
	private Integer usuario;
	
	private String ssql;
	
	private String resultado;
	
	private String ip;
	
	private LocalDateTime fecha;
	
	private Integer estado;
	
	@ManyToOne
	@JoinColumn(name="idconsulta")
	private Consulta consulta;
	
	

}
