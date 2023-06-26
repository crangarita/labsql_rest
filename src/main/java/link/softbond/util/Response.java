package link.softbond.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import javax.annotation.PostConstruct;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;

public class Response {
	/*
	private static MessageSource mensajeStatic;
	
	@Autowired
	private MessageSource mensajes;
	*/
	 private Boolean success;
	 private String msg;
	 private Object data;
	 private long numrows;
	 	 	 
	 public Response() {
		 // TODO Auto-generated constructor stub
	 }
	
	 public Response(Boolean success, String msg, Object data, long total) {
		super();
		this.success = success;
		this.msg = msg;
		this.data = data;
		this.numrows = total;
	 }
	
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public Object getData() {
		return data;
	}
	public void setData(Object data) {
		this.data = data;
	}
	public long getTotal() {
		return numrows;
	}
	public void setTotal(long total) {
		this.numrows = total;
	}
	
	/**
	* Genera la respuesta junto a los datos consultados
    *
    * @param success guarda true si todo fue correcto, false si se presenta algun inconveniente
    * @param mensaje texto informativo del resultado que se devolvera
    * @param data objeto complejo con información que se retornara
    * @return Response 
    */
	public static Response crear(Boolean success, String msg, Object data) {
        return new Response(success, msg, data, 0);
    }
	
/* Este metodo recibe dos instancias de un objeto y utilizando las propiedades Reflection de Java
 * permite transferir la información de los atributos entre las clases utilizada especialmente
 * para persistencia en JPA	
 */
public static void cloneData (Object origen, Object destino) {
		
		/* Obtiene el tipo de clase a la que pertenecen los objetos
		 */
		Class clase = origen.getClass();
		
		/* Obtiene la relación de atributos de la clase y los almacena en un arreglo de Field
		 */
		Field[] todasLasVariables = clase.getDeclaredFields();
		
		for(int i = 0; i<todasLasVariables.length; i++) {			
			try {
				/* Obtenemos el atributo a trabajar 
				 */
				Field field = todasLasVariables[i];
				
				/* Obtenemos el nombre del atributo
				 */
				String str = field.getName();
				/* Esta condición valida si el atributo tiene un conjunto de anotaciones asociadas
				 * al campo, por ejemplo @Id, para evitar el traslado del valor de estos atributos,
				 * se entiende como una excepción.  Adicional se omiten las constantes verificando
				 * si es final
				 */
				if(!field.isAnnotationPresent(Id.class) && !field.isAnnotationPresent(OneToMany.class) 
					&& !field.isAnnotationPresent(ManyToMany.class) 
					&& !Modifier.isFinal(field.getModifiers())) {
					/* Obtenemos el metodo get del campo, concatenando la palabra get y aplicando camelCase
					 * al nombre del atributo
					 */
					Method metodoGet = origen.getClass().getDeclaredMethod("get"+Character.toUpperCase(str.charAt(0)) + 
							str.substring(1));
					/* Obtenemos el metodo set del campo, concatenando la palabra get y aplicando camelCase
					 * al nombre del atributo, se pasa un atributo del tipo de atributo definido
					 */
					Method metodoSet = origen.getClass().getDeclaredMethod("set"+Character.toUpperCase(str.charAt(0)) + 
							str.substring(1), field.getType());
					/* Se realiza la invocación de los metodos utilizando el valor del get del atributo del origen
					 * como parametro del metodo set del atributo del destino
					 */
					if(metodoGet.invoke(origen)!=null)
						metodoSet.invoke(destino,metodoGet.invoke(origen));
				}
				
			} catch (ReflectiveOperationException e1) {
				e1.printStackTrace();
			} 		
			
		}
	}

/*
	@PostConstruct
	public void registerInstance() {
		Response.mensajeStatic = this.mensajes;
	}
	@Autowired
	public void setMensajeStatic(MessageSource mensajes) {
	    Response.mensajeStatic = this.mensajes;
	}
	public static String mensaje(String mensaje, String atributo) {
		return mensajeStatic.getMessage("list", new String[] {"Usuario"}, LocaleContextHolder.getLocale());
	}
*/
	public static String mensaje(MessageSource mensajes, String mensaje, String atributo) {
		return mensajes.getMessage(mensaje, new String[] {atributo}, LocaleContextHolder.getLocale());
	}
	
	public static String mensaje(MessageSource mensajes, String mensaje) {
		return mensajes.getMessage(mensaje, null, LocaleContextHolder.getLocale());
	}
}
