package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Socio;

import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {
    Optional<Socio> findByTelefono(String telefono);
}
