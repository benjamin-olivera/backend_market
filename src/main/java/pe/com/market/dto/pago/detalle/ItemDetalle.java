package pe.com.market.dto.pago.detalle;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ItemDetalle {
    private final String concepto;
    private final BigDecimal monto;

    public ItemDetalle(String concepto, BigDecimal monto) {
        this.concepto = concepto;
        this.monto = monto;
    }

}