package pe.com.market.repository.pago;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.pago.Comprobante;
import pe.com.market.model.pago.DetalleComprobante;

import java.util.List;

public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobante, Integer> {

    List<DetalleComprobante> findByComprobante(Comprobante comprobante);
}
