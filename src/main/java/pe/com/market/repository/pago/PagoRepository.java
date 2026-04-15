package pe.com.market.repository.pago;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import pe.com.market.dto.PagoListadoRow;
import pe.com.market.dto.pago.PagoConceptoRow;
import pe.com.market.model.pago.Comprobante;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PagoRepository extends JpaRepository<Comprobante, Integer> {

    @Query(value = """
      SELECT
        c.id_comprobante AS idComprobante,
        c.numero AS idRecibo,
        c.fecha AS fechaPago,
        p.codigo AS puesto,
        COALESCE(
          s.nombre,
          (
            SELECT s2.nombre
            FROM socio_puesto sp2
            JOIN socio s2 ON s2.id_socio = sp2.id_socio
            WHERE sp2.id_puesto = p.id_puesto
              AND (sp2.fecha_fin IS NULL OR sp2.fecha_fin >= DATE(c.fecha))
            ORDER BY sp2.fecha_asignacion DESC
            LIMIT 1
          ),
          'Asociación'
        ) AS socio,
        c.total AS monto,
        'Completado' AS estado
      FROM comprobante c
      JOIN puesto p ON p.id_puesto = c.id_puesto
      LEFT JOIN detalle_comprobante dc ON dc.id_comprobante = c.id_comprobante
      LEFT JOIN deuda d ON d.id_deuda = dc.id_deuda
      LEFT JOIN socio s ON s.id_socio = d.id_socio
      WHERE c.tipo = 'RECIBO'
        AND (:from IS NULL OR DATE(c.fecha) >= :from)
        AND (:to IS NULL OR DATE(c.fecha) <= :to)
        AND (
          :q IS NULL OR :q = '' OR
          c.numero LIKE CONCAT('%', :q, '%') OR
          p.codigo LIKE CONCAT('%', :q, '%') OR
          s.nombre LIKE CONCAT('%', :q, '%')
        )
      GROUP BY c.id_comprobante, c.numero, c.fecha, p.codigo, s.nombre, c.total
      ORDER BY c.fecha DESC
      """,
            countQuery = """
      SELECT COUNT(DISTINCT c.id_comprobante)
      FROM comprobante c
      JOIN puesto p ON p.id_puesto = c.id_puesto
      LEFT JOIN detalle_comprobante dc ON dc.id_comprobante = c.id_comprobante
      LEFT JOIN deuda d ON d.id_deuda = dc.id_deuda
      LEFT JOIN socio s ON s.id_socio = d.id_socio
      WHERE c.tipo = 'RECIBO'
        AND (:from IS NULL OR DATE(c.fecha) >= :from)
        AND (:to IS NULL OR DATE(c.fecha) <= :to)
        AND (
          :q IS NULL OR :q = '' OR
          c.numero LIKE CONCAT('%', :q, '%') OR
          p.codigo LIKE CONCAT('%', :q, '%') OR
          s.nombre LIKE CONCAT('%', :q, '%')
        )
      """,
            nativeQuery = true)
    Page<PagoListadoRow> listarBase(
            @Param("q") String q,
            @Param("from") LocalDate from,
            @Param("to") LocalDate to,
            Pageable pageable
    );

    @Query(value = """
      SELECT
        dc.id_comprobante AS idComprobante,
        mc.descripcion AS concepto
      FROM detalle_comprobante dc
      JOIN deuda d ON d.id_deuda = dc.id_deuda
      JOIN motivo_cobro mc ON mc.id_motivo = d.id_motivo
      WHERE dc.id_comprobante IN (:ids)
      ORDER BY dc.id_comprobante, mc.descripcion
      """, nativeQuery = true)
    List<PagoConceptoRow> listarConceptosPorComprobantes(@Param("ids") List<Integer> ids);
}
