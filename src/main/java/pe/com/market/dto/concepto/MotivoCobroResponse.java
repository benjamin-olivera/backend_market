package pe.com.market.dto.concepto;

import pe.com.market.enums.EstadoMotivoCobro;

import java.time.LocalDateTime;

public record MotivoCobroResponse(
        Integer idMotivo,
        String nombre,
        String descripcion,
        EstadoMotivoCobro estado,
        LocalDateTime fechaCreacion
) {}