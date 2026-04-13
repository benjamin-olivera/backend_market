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
public class DistribuirDeudaRequest {

    @NotNull
    private Integer idMotivo;

    @NotNull
    @Positive
    private BigDecimal montoTotal;

    private LocalDate fecha;

    // opcional: si viene vacío o null → se usan todos los puestos activos
    private List<String> codigosPuestos;
}