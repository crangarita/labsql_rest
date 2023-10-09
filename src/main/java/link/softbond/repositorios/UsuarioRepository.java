package link.softbond.repositorios;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import link.softbond.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

	public Usuario findByEmail(String email) ;

	public Usuario findByUsuario(String usuario) ;

}
