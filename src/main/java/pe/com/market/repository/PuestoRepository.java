package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Puesto;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    Optional<Puesto> findByCodigo(String codigo);

    List<Puesto> findAllByCodigoIn(List<String> codigos);
}
