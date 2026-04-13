package pe.com.market.repository.pago;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.pago.Comprobante;
import pe.com.market.model.pago.MetodoPagoComprobante;

import java.util.List;

public interface MetodoPagoComprobanteRepository extends JpaRepository<MetodoPagoComprobante, Integer> {

    List<MetodoPagoComprobante> findByComprobante(Comprobante comprobante);
}
