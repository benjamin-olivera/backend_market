package pe.com.market.repository.deuda;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.model.deuda.Deuda;

import java.util.List;

public interface DeudaRepository extends JpaRepository<Deuda, Integer> {

    // Para caja
    List<Deuda> findByPuesto_CodigoAndEstado(String codigoPuesto, EstadoDeuda estado);

    // Para reportes/histórico si lo necesitas
    List<Deuda> findByPuesto_Codigo(String codigoPuesto);
}