package pe.com.market.dto.deuda;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeudaPagoItem {
    private Integer idDeuda;
    private BigDecimal montoPagado;
}