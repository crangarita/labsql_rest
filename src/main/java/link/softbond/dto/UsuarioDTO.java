package link.softbond.dto;

import java.io.Serializable;
import java.sql.Timestamp;

import lombok.Data;

@Data
public class UsuarioDTO implements Serializable{
	
	private Integer id;
	private String usuario;
	private String clave;
	private String nombre;
	private String email;
	private String estado;
		
}