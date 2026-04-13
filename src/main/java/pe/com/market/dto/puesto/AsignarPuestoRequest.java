package pe.com.market.dto.puesto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class AsignarPuestoRequest {

    @NotNull(message = "El id del socio es obligatorio")
    private Integer idSocio;

    @NotNull(message = "El id del puesto es obligatorio")
    private Integer idPuesto;

    private LocalDate fechaAsignacion; // si es null, se usa hoy
}