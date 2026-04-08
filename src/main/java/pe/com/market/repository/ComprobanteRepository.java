package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Comprobante;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {
}
