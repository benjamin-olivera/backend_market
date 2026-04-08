package pe.com.market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class MismaDeudaRequest {

    private BigDecimal montoPorPuesto;
    private Integer idMotivo;

    // Opcional: si es null o vacío => todos los puestos
    private List<String> codigosPuestos;
}
