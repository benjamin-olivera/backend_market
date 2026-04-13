package pe.com.market.dto.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@Data
@AllArgsConstructor
@Getter
@Setter
public class DeudaPagadaItemResponse {
    private Integer idDeuda;
    private String motivo;
    private BigDecimal monto;
    private Date fecha;
}