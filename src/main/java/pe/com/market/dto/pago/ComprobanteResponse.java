package pe.com.market.dto.pago;

import lombok.AllArgsConstructor;
import lombok.Data;
import pe.com.market.model.deuda.Deuda;
import pe.com.market.model.pago.Comprobante;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ComprobanteResponse {

    private Integer idComprobante;
    private String numero;
    private String tipo;
    private String codigoPuesto;
    private String nombreUsuario;
    private LocalDateTime fecha;
    private BigDecimal total;
    private BigDecimal vuelto;
    private List<DeudaPagadaItemResponse> deudas;
    private List<MetodoPagoItemResponse> metodosPago;

    public static ComprobanteResponse fromEntity(
            Comprobante comprobante,
            List<Deuda> deudas,
            List<MetodoPagoRequest> metodosPagoRequest
    ) {
        List<DeudaPagadaItemResponse> deudasResp = deudas.stream()
                .map(d -> new DeudaPagadaItemResponse(
                        d.getIdDeuda(),
                        d.getMotivo().getDescripcion(),
                        d.getMonto(),
                        d.getFecha()
                ))
                .toList();

        List<MetodoPagoItemResponse> metodosResp = metodosPagoRequest.stream()
                .map(m -> new MetodoPagoItemResponse(
                        m.getMetodo(),
                        m.getMonto()
                ))
                .toList();

        return new ComprobanteResponse(
                comprobante.getIdComprobante(),
                comprobante.getNumero(),
                comprobante.getTipo(),
                comprobante.getPuesto().getCodigo(),
                comprobante.getUsuario().getUsername(),
                comprobante.getFecha(),
                comprobante.getTotal(),
                comprobante.getVuelto(),
                deudasResp,
                metodosResp
        );
    }
}