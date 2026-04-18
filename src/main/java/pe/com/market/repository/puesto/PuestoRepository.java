package pe.com.market.repository.puesto;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.puesto.Puesto;

import java.util.List;
import java.util.Optional;

public interface PuestoRepository extends JpaRepository<Puesto, Integer> {

    Optional<Puesto> findByCodigo(String codigo);

    // Ya lo tenías:
    List<Puesto> findByEstadoTrue();

    // Nuevo: buscar un puesto por código y estado = true
    Optional<Puesto> findByCodigoAndEstadoTrue(String codigo);

    List<Puesto> findAllByOrderByCodigoAsc();

    List<Puesto> findByEstadoTrueOrderByCodigoAsc();

    boolean existsByCodigo(String codigo);

    boolean existsByCodigoAndIdPuestoNot(String codigo, Integer idPuesto);
}
