package pe.com.market.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Comprobante;
import pe.com.market.model.MetodoPagoComprobante;

import java.util.List;

public interface MetodoPagoComprobanteRepository extends JpaRepository<MetodoPagoComprobante, Integer> {

    List<MetodoPagoComprobante> findByComprobante(Comprobante comprobante);
}
