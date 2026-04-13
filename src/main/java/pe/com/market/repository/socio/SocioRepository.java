package pe.com.market.repository.socio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.socio.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioRepository extends JpaRepository<Socio, Integer> {

    // para buscar un socio por DNI exacto
    Optional<Socio> findByDni(String dni);

    Optional<Socio> findByDniAndEstadoTrue(String dni);

    List<Socio> findTop10ByDniStartingWithOrderByDniAsc(String dniPrefix);
}
