package pe.com.market.repository.deuda;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.deuda.LoteDeuda;

public interface LoteDeudaRepository extends JpaRepository<LoteDeuda, Integer> {
}
