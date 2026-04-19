package pe.com.market.dto.concepto;

import pe.com.market.enums.EstadoConcepto;

import java.time.LocalDateTime;

public record ConceptoResponse(
        Integer idMotivo,
        String nombre,
        String descripcion,
        EstadoConcepto estado,
        LocalDateTime fechaCreacion
) {}