package pe.com.market.dto.puesto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PuestoDTO(
        @NotBlank(message = "El código es obligatorio")
        @Size(max = 20)
        String codigo,

        @Size(max = 50)
        String sector,

        @Size(max = 10)
        String numero,

        @Size(max = 100)
        String descripcion
) {}
