package pe.com.market.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.auth.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Optional<Usuario> findByUsername(String username);

    Usuario findByUsernameAndEstado(String username, Boolean estado);

}
