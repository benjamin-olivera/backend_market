package pe.com.market.dto.reportes.caja;

import pe.com.market.enums.Metodo;

import java.math.BigDecimal;

public interface CajaMetodoTotalRow {
    Metodo getMetodo();
    BigDecimal getMonto();
}