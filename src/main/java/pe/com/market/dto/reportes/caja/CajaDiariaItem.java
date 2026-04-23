package pe.com.market.dto.reportes.caja;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class CajaDiariaItem {
    private String idRecibo;
    private String puesto;
    private String metodoPago;
    private BigDecimal monto;
    private String hora;
    private List<String> conceptos;
}