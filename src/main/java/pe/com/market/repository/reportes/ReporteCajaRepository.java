package pe.com.market.repository.reportes;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import pe.com.market.dto.reportes.caja.CajaComprobanteRow;
import pe.com.market.dto.reportes.caja.CajaMetodoRow;
import pe.com.market.dto.reportes.caja.CajaMetodoTotalRow;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface ReporteCajaRepository extends Repository<Object, Integer> {

    @Query(value = """
      SELECT
        c.id_comprobante AS idComprobante,
        c.numero AS numero,
        c.fecha AS fecha,
        p.codigo AS puestoCodigo,
        c.total AS total
      FROM comprobante c
      JOIN puesto p ON p.id_puesto = c.id_puesto
      WHERE c.tipo = 'RECIBO'
        AND DATE(c.fecha) = :fecha
      ORDER BY c.fecha ASC
      """, nativeQuery = true)
    List<CajaComprobanteRow> listarComprobantesDelDia(@Param("fecha") LocalDate fecha);

    @Query(value = """
      SELECT
        mpc.id_comprobante AS idComprobante,
        mpc.metodo_pago AS metodo,
        mpc.monto AS monto
      FROM metodo_pago_comprobante mpc
      JOIN comprobante c ON c.id_comprobante = mpc.id_comprobante
      WHERE c.tipo = 'RECIBO'
        AND DATE(c.fecha) = :fecha
      ORDER BY mpc.id_comprobante
      """, nativeQuery = true)
    List<CajaMetodoRow> listarMetodosPorComprobanteDelDia(@Param("fecha") LocalDate fecha);

    @Query(value = """
      SELECT
        mpc.metodo_pago AS metodo,
        SUM(mpc.monto) AS monto
      FROM metodo_pago_comprobante mpc
      JOIN comprobante c ON c.id_comprobante = mpc.id_comprobante
      WHERE c.tipo = 'RECIBO'
        AND DATE(c.fecha) = :fecha
      GROUP BY mpc.metodo_pago
      """, nativeQuery = true)
    List<CajaMetodoTotalRow> sumarPorMetodoDelDia(@Param("fecha") LocalDate fecha);

    @Query(value = """
      SELECT COALESCE(SUM(c.total), 0)
      FROM comprobante c
      WHERE c.tipo = 'RECIBO'
        AND DATE(c.fecha) = :fecha
      """, nativeQuery = true)
    BigDecimal totalRecaudadoDelDia(@Param("fecha") LocalDate fecha);
}
