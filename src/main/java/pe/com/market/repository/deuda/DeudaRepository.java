package pe.com.market.repository.deuda;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pe.com.market.dto.deuda.DeudaListadoResponse;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.model.deuda.Deuda;

import java.sql.Date;
import java.util.List;

public interface DeudaRepository extends JpaRepository<Deuda, Integer> {

    // Para caja
    List<Deuda> findByPuesto_CodigoAndEstado(String codigoPuesto, EstadoDeuda estado);

    // Para reportes/histórico si lo necesitas
    List<Deuda> findByPuesto_Codigo(String codigoPuesto);

    @Query("""
      select new pe.com.market.dto.deuda.DeudaListadoResponse(
        d.idDeuda,
        p.codigo,
        p.idPuesto,
        m.idMotivo,
        m.nombre,
        d.monto,
        d.fecha,
        d.estado,
        s.idSocio,
        s.nombre,
        s.dni,
        s.email
      )
      from Deuda d
      join d.puesto p
      join d.concepto m
      left join d.socio s
      where (:estado is null or d.estado = :estado)
        and (:idMotivo is null or m.idMotivo = :idMotivo)
        and (:desde is null or d.fecha >= :desde)
        and (:hasta is null or d.fecha <= :hasta)
        and (
          :q is null
          or lower(p.codigo) like lower(concat('%', :q, '%'))
          or cast(d.idDeuda as string) like concat('%', :q, '%')
          or lower(m.nombre) like lower(concat('%', :q, '%'))
          or (
            s is not null and (
              lower(s.nombre) like lower(concat('%', :q, '%'))
              or s.dni like concat('%', :q, '%')
              or lower(coalesce(s.email,'')) like lower(concat('%', :q, '%'))
            )
          )
        )
      order by d.fecha desc, d.idDeuda desc
    """)
    List<DeudaListadoResponse> listarFiltrado(
            @Param("q") String q,
            @Param("estado") EstadoDeuda estado,
            @Param("idMotivo") Integer idMotivo,
            @Param("desde") Date desde,
            @Param("hasta") Date hasta
    );
}
