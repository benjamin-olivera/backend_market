package pe.com.market.dto.reportes.caja;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface CajaComprobanteRow {
    Integer getIdComprobante();
    String getNumero();
    LocalDateTime getFecha();
    String getPuestoCodigo();
    BigDecimal getTotal();
}