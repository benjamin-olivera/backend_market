package pe.com.market.repository.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.pago.Comprobante;

public interface ComprobanteRepository extends JpaRepository<Comprobante, Integer> {
}
