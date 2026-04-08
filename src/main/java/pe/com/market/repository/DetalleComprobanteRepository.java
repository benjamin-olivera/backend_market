package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Comprobante;
import pe.com.market.model.DetalleComprobante;

import java.util.List;

public interface DetalleComprobanteRepository extends JpaRepository<DetalleComprobante, Integer> {

    List<DetalleComprobante> findByComprobante(Comprobante comprobante);
}
