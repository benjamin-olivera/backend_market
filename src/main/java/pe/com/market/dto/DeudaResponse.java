package pe.com.market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class DeudaResponse {
    private Integer idDeuda;
    private String motivo;
    private BigDecimal monto;
    private String estado;
    private LocalDate fecha;
}
