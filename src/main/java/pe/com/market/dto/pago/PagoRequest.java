package pe.com.market.dto.pago;

import lombok.Data;
import pe.com.market.dto.deuda.DeudaPagoItem;

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
