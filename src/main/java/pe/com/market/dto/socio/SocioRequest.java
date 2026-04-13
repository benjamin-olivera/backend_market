package pe.com.market.dto.socio;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SocioRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    private String dni;

    @NotBlank(message = "El teléfono es obligatorio")
    private String telefono;
}
