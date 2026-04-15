package pe.com.market.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface PagoListadoRow {
    Integer getIdComprobante();
    String getIdRecibo();
    LocalDateTime getFechaPago();
    String getPuesto();
    String getSocio();
    BigDecimal getMonto();
    String getEstado();
}
