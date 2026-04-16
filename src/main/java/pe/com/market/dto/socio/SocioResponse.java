package pe.com.market.dto.socio;

import pe.com.market.dto.socio_puesto.PuestoSocioResponse;

import java.util.List;

public record SocioResponse(
        Integer idSocio,
        String nombre,
        String dni,
        String telefono,
        Boolean estado,
        List<PuestoSocioResponse> puestos
) {}
