package pe.com.market.repository.concepto;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.enums.EstadoMotivoCobro;
import pe.com.market.model.concepto.MotivoCobro;

import java.util.List;

public interface MotivoCobroRepository extends JpaRepository<MotivoCobro, Integer> {
    List<MotivoCobro> findByEstadoOrderByNombreAsc(EstadoMotivoCobro estado);
    List<MotivoCobro> findAllByOrderByNombreAsc();
}