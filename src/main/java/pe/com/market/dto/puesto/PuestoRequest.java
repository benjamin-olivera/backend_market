package pe.com.market.dto.puesto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PuestoRequest {

    @NotBlank(message = "El código es obligatorio")
    private String codigo;

    private String descripcion;

    private Boolean esPropiedadAsociacion = false;
}
