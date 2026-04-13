package pe.com.market.dto.deuda;

import lombok.AllArgsConstructor;
import lombok.Data;
import pe.com.market.enums.EstadoDeuda;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

@Data
@AllArgsConstructor
public class DeudaResponse {

    private Integer idDeuda;
    private String codigoPuesto;
    private String motivo;
    private BigDecimal monto;
    private Date fecha;
    private EstadoDeuda estado;

    public void setFecha(LocalDate localDate) {
    }
}