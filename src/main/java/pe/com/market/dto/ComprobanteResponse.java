package pe.com.market.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ComprobanteResponse {

    private Integer idComprobante;
    private String numero;
    private String tipo;
    private String codigoPuesto;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS")
    private LocalDateTime fecha;

    private BigDecimal total;
    private BigDecimal vuelto;

    private List<LineaDeuda> deudasPagadas;
    private List<LineaMetodoPago> metodosPago;

    @Data
    public static class LineaDeuda {
        private Integer idDeuda;
        private String motivo;
        private BigDecimal monto;
    }

    @Data
    public static class LineaMetodoPago {
        private String metodo;
        private BigDecimal monto;
    }
}
