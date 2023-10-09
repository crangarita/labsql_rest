package link.softbond.entities;

import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

import java.sql.Timestamp;
import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity 
@Data
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Integer id;

	private String usuario;
	private String nombre;
	@Column(nullable = false, length = 255)
	@NotEmpty
	private String clave;
	@Column(nullable = false, length = 255)
	@NotEmpty
	@Email
	private String email;
	private String estado;
	private String codigoTemporal;

}