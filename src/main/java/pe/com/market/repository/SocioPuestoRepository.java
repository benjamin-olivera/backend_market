package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.Puesto;
import pe.com.market.model.Socio;
import pe.com.market.model.SocioPuesto;

import java.util.List;
import java.util.Optional;

public interface SocioPuestoRepository extends JpaRepository<SocioPuesto, Integer> {


    Optional<SocioPuesto> findByPuesto(Puesto puesto);

    List<SocioPuesto> findAllBySocio(Socio socio);
}
