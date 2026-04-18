package pe.com.market.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import pe.com.market.dto.socio_puesto.SocioPuestoCountDTO;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.socio_puesto.SocioPuesto;
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

    // Asignación activa de un puesto (fecha_fin IS NULL)
    Optional<SocioPuesto> findByPuesto_IdPuestoAndFechaFinIsNull(Integer idPuesto);

    // Puestos activos de un socio
    List<SocioPuesto> findBySocio_IdSocioAndFechaFinIsNull(Integer idSocio);

    // Historial completo de un puesto
    List<SocioPuesto> findByPuesto_IdPuestoOrderByFechaAsignacionDesc(Integer idPuesto);

    // Verificar si un puesto ya tiene asignación activa
    boolean existsByPuesto_IdPuestoAndFechaFinIsNull(Integer idPuesto);

    // IDs de puestos que tienen asignación activa (para filtrar en frontend)
    @Query("SELECT sp.puesto.idPuesto FROM SocioPuesto sp WHERE sp.fechaFin IS NULL")
    List<Integer> findIdPuestosOcupados();

    @Query("""
        select new pe.com.market.dto.socio_puesto.SocioPuestoCountDTO(sp.socio.idSocio, count(sp))
        from SocioPuesto sp
        where sp.fechaFin is null
        group by sp.socio.idSocio
    """)
    List<SocioPuestoCountDTO> countPuestosActivosGroupBySocio();

    @Query("""
        select sp
        from SocioPuesto sp
        where sp.fechaFin is null
        order by sp.puesto.codigo asc
    """)
    List<SocioPuesto> findActivos();


}
