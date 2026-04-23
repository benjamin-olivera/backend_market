package pe.com.market.dto.reportes.caja;

import pe.com.market.enums.Metodo;

import java.math.BigDecimal;

public interface CajaMetodoRow {
    Integer getIdComprobante();
    Metodo getMetodo();
    BigDecimal getMonto();
}