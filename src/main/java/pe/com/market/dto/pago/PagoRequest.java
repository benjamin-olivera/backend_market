package pe.com.market.dto.pago;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PagoRequest {

    private Integer idPuesto;
    private Integer idUsuario;
    private LocalDateTime fechaOperacion;
    private List<DeudaPagoItem> deudas;
    private List<MetodoPagoRequest> metodosPago;
}
