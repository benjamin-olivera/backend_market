package pe.com.market.repository.deuda;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.deuda.MotivoCobro;

public interface MotivoCobroRepository extends JpaRepository<MotivoCobro, Integer> {
}
