package pe.com.market.repository.socio;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.puesto.SocioPuesto;
import pe.com.market.model.socio.Socio;

import java.util.List;
import java.util.Optional;

public interface SocioPuestoRepository extends JpaRepository<SocioPuesto, Integer> {

    // Relación actual de un puesto (sin filtrar por fecha_fin)
    Optional<SocioPuesto> findByPuesto(Puesto puesto);

    // Saber si un puesto tiene socio vigente
    boolean existsByPuestoAndFechaFinIsNull(Puesto puesto);

    // Relación vigente (fecha_fin IS NULL) para un puesto
    Optional<SocioPuesto> findByPuestoAndFechaFinIsNull(Puesto puesto);

    // Todos los puestos vigentes de un socio
    List<SocioPuesto> findBySocioAndFechaFinIsNull(Socio socio);

    // Puestos activos (sin fecha_fin) para un socio
    List<SocioPuesto> findBySocioIdSocioAndFechaFinIsNull(Integer idSocio);
}
