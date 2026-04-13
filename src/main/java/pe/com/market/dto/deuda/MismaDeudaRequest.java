package pe.com.market.dto.deuda;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@Getter
@Setter
public class MismaDeudaRequest {

    @NotNull(message = "El id del motivo es obligatorio")
    private Integer idMotivo;

    @NotNull(message = "El monto es obligatorio")
    @Positive
    private BigDecimal monto;

    @NotNull(message = "Debe seleccionar al menos un puesto")
    private List<String> codigosPuestos;

    // opcional
    private LocalDate fecha;
}
