package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findByUsernameAndEstado(String username, Boolean estado);

}
