package pe.com.market.repository.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.auth.Rol;

public interface RolRepository extends JpaRepository<Rol, Integer> {
}