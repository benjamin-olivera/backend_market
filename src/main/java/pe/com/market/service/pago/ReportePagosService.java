package pe.com.market.service.pago;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.com.market.dto.pago.TransaccionDTO;
import pe.com.market.model.deuda.Deuda;
import pe.com.market.model.pago.Comprobante;
import pe.com.market.model.pago.DetalleComprobante;
import pe.com.market.model.pago.MetodoPagoComprobante;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.repository.pago.ComprobanteRepository;
import pe.com.market.repository.pago.DetalleComprobanteRepository;
import pe.com.market.repository.pago.MetodoPagoComprobanteRepository;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportePagosService {

    private final ComprobanteRepository comprobanteRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;
    private final MetodoPagoComprobanteRepository metodoPagoComprobanteRepository;

    private final DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd MMM. yyyy");
    private final DateTimeFormatter formatterHora  = DateTimeFormatter.ofPattern("hh:mm a");

    public ReportePagosService(ComprobanteRepository comprobanteRepository,
                               DetalleComprobanteRepository detalleComprobanteRepository,
                               MetodoPagoComprobanteRepository metodoPagoComprobanteRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.detalleComprobanteRepository = detalleComprobanteRepository;
        this.metodoPagoComprobanteRepository = metodoPagoComprobanteRepository;
    }

    public List<TransaccionDTO> obtenerHistorialPagos() {
        List<Comprobante> comprobantes =
                comprobanteRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));

        List<TransaccionDTO> resultado = new ArrayList<>();

        for (Comprobante c : comprobantes) {
            resultado.add(mapearComprobanteATransaccion(c));
        }

        return resultado;
    }

    private TransaccionDTO mapearComprobanteATransaccion(Comprobante c) {

        String fecha = c.getFecha().format(formatterFecha);
        String hora  = c.getFecha().format(formatterHora);

        Puesto puesto = c.getPuesto();
        String codigoPuesto = puesto.getCodigo();

        String nombreSocio = "Asociación";
        String concepto = "";
        BigDecimal monto = c.getTotal();

        List<DetalleComprobante> detalles = detalleComprobanteRepository.findByComprobante(c);

        if (!detalles.isEmpty()) {
            DetalleComprobante det = detalles.get(0);
            Deuda deuda = det.getDeuda();
            concepto = deuda.getMotivo().getDescripcion();

            if (deuda.getSocio() != null) {
                nombreSocio = deuda.getSocio().getNombre();
            }
        }

        List<MetodoPagoComprobante> metodos = metodoPagoComprobanteRepository.findByComprobante(c);
        String metodoPago;

        if (metodos.isEmpty()) {
            metodoPago = "N/A";
        } else if (metodos.size() == 1) {
            metodoPago = metodos.get(0).getMetodo().name(); // String "EFECTIVO"
        } else {
            metodoPago = "MIXTO";
        }

        TransaccionDTO dto = new TransaccionDTO();
        dto.setFecha(fecha);
        dto.setHora(hora);
        dto.setPuestoId(codigoPuesto);
        dto.setInquilino(nombreSocio);
        dto.setConcepto(concepto);
        dto.setMonto(monto);
        dto.setMetodoPago(metodoPago);
        dto.setIdRecibo(c.getNumero());

        return dto;
    }
}