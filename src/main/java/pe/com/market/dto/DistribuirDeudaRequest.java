package pe.com.market.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DistribuirDeudaRequest {

    private BigDecimal montoTotal;   // monto total a distribuir
    private Integer idMotivo;        // motivo_cobro
}
