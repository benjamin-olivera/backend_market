package pe.com.market.dto.reportes.caja;

import lombok.Data;
import pe.com.market.enums.Metodo;

import java.math.BigDecimal;
import java.util.EnumMap;

@Data
public class CajaDiariaCards {
    private BigDecimal totalRecaudado = BigDecimal.ZERO;
    private BigDecimal ingresosEfectivo = BigDecimal.ZERO;
    private BigDecimal ingresosDigitales = BigDecimal.ZERO;

    private EnumMap<Metodo, BigDecimal> porMetodo = new EnumMap<>(Metodo.class);
}