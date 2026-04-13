package pe.com.market.dto.pago;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DeudaPagoItem {
    private Long idDeuda;
    private BigDecimal montoPagado;
}