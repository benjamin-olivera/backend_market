package pe.com.market.dto.pago;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
public class PagoListadoDto {
    private Integer idComprobante;
    private String idRecibo;
    private LocalDateTime fechaPago;
    private String puesto;
    private String socio;
    private java.math.BigDecimal monto;
    private String estado;
    private java.util.List<String> conceptos;
}
