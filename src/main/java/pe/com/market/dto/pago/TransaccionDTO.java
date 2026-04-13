package pe.com.market.dto.pago;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransaccionDTO {

    private String fecha;      // "25 oct. 2023"
    private String hora;       // "10:05 AM"
    private String puestoId;   // código del puesto
    private String inquilino;  // nombre del socio o "Asociación"
    private String concepto;   // Renta, Luz, etc.
    private BigDecimal monto;  // total del comprobante o del ítem
    private String metodoPago; // EFECTIVO / TARJETA / MIXTO
    private String idRecibo;   // número del comprobante
}