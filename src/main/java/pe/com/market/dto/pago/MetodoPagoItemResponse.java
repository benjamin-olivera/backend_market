package pe.com.market.dto.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import pe.com.market.enums.Metodo;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class MetodoPagoItemResponse {
    private Metodo metodo;
    private BigDecimal monto;
}