package pe.com.market.dto.socio_puesto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
public class SocioPuestoDTO {

    @NotNull(message = "El id del socio es obligatorio")
    private Integer idSocio;

    @NotNull(message = "El id del puesto es obligatorio")
    private Integer idPuesto;

    private LocalDate fechaAsignacion;
}