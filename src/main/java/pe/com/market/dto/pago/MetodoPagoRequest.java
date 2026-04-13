package pe.com.market.dto.pago;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pe.com.market.enums.Metodo;

import java.math.BigDecimal;

@Data
public class MetodoPagoRequest {

    @NotBlank(message = "El método de pago es obligatorio")
    private Metodo metodo;

    @NotNull(message = "El monto es obligatorio")
    private BigDecimal monto;
}