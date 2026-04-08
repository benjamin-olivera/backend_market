package pe.com.market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class DistribuirDeudaSeleccionadosRequest {

    private BigDecimal montoTotal;
    private Integer idMotivo;
    private List<String> codigosPuestos; // P-101, P-102, ...
}
