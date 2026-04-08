package pe.com.market.dto;

import lombok.Data;
import lombok.Getter;

import java.math.BigDecimal;

@Data
@Getter
public class TransaccionDTO {

    public String fecha;      // "2026-04-07"
    public String hora;       // "10:23 AM"
    public String puestoId;   // "P-101"
    public String inquilino;  // nombre del socio
    public String concepto;   // "Luz", "Renta Mensual"
    public BigDecimal monto;  // total de la deuda pagada
    public String metodoPago; // primer métod. o "MIXTO"
    public String idRecibo;   // numero de comprobante

}
