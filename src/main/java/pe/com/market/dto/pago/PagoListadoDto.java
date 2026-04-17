package pe.com.market.dto.pago;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PagoListadoDto {
    private Integer idComprobante;
    private String idRecibo;
    private LocalDateTime fechaPago;
    private String puesto;
    private String socio;
    private java.math.BigDecimal monto;
    private String estado;
    private List<String> conceptos;
}
