package pe.com.market.dto.socio;

import java.util.List;

public record SocioResponse(
        Integer idSocio,
        String nombre,
        String dni,
        String telefono,
        Boolean estado,
        List<PuestoSocioResponse> puestos
) {}
