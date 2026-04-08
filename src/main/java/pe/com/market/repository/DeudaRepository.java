package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Deuda;
import pe.com.market.model.Puesto;

import java.util.List;

public interface DeudaRepository extends JpaRepository<Deuda, Integer> {

    List<Deuda> findByPuestoAndEstado(Puesto puesto, String estado);
}
