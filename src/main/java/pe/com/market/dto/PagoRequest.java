package pe.com.market.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class PagoRequest {

    private List<MetodoPagoDTO> metodosPago;

    @Data
    public static class MetodoPagoDTO {
        private String metodo;
        private BigDecimal monto;
    }
}
