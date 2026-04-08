package pe.com.market.service;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pe.com.market.dto.TransaccionDTO;
import pe.com.market.model.*;
import pe.com.market.repository.ComprobanteRepository;
import pe.com.market.repository.DetalleComprobanteRepository;
import pe.com.market.repository.MetodoPagoComprobanteRepository;
import pe.com.market.repository.SocioPuestoRepository;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReportePagosService {

    private final ComprobanteRepository comprobanteRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;
    private final MetodoPagoComprobanteRepository metodoPagoComprobanteRepository;
    private final SocioPuestoRepository socioPuestoRepository;

    private final DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("dd MMM. yyyy");
    private final DateTimeFormatter formatterHora  = DateTimeFormatter.ofPattern("hh:mm a");

    public ReportePagosService(ComprobanteRepository comprobanteRepository,
                               DetalleComprobanteRepository detalleComprobanteRepository,
                               MetodoPagoComprobanteRepository metodoPagoComprobanteRepository,
                               SocioPuestoRepository socioPuestoRepository) {
        this.comprobanteRepository = comprobanteRepository;
        this.detalleComprobanteRepository = detalleComprobanteRepository;
        this.metodoPagoComprobanteRepository = metodoPagoComprobanteRepository;
        this.socioPuestoRepository = socioPuestoRepository;
    }

    /**
     * Devuelve una lista de movimientos de pagos para el historial,
     * ordenada por fecha descendente.
     */
    public List<TransaccionDTO> obtenerHistorialPagos() {
        // Ordenamos por fecha descendente para que salgan los más recientes primero
        List<Comprobante> comprobantes =
                comprobanteRepository.findAll(Sort.by(Sort.Direction.DESC, "fecha"));

        List<TransaccionDTO> resultado = new ArrayList<>();

        for (Comprobante c : comprobantes) {
            resultado.add(mapearComprobanteATransaccion(c));
        }

        return resultado;
    }

    /** Mapea un comprobante a una fila de historial para el front. */
    private TransaccionDTO mapearComprobanteATransaccion(Comprobante c) {
        // FECHA y HORA
        String fecha = c.getFecha().format(formatterFecha);
        String hora  = c.getFecha().format(formatterHora);

        // PUESTO
        Puesto puesto = c.getPuesto();
        String codigoPuesto = puesto.getCodigo();

        // SOCIO (inquilino) - si existe
        String nombreSocio = socioPuestoRepository.findByPuesto(puesto)
                .map(SocioPuesto::getSocio)
                .map(Socio::getNombre)
                .orElse("Asociación");

        // DETALLE PRINCIPAL (por simplicidad, usamos el primer ítem)
        List<DetalleComprobante> detalles = detalleComprobanteRepository.findByComprobante(c);

        String concepto = "";
        BigDecimal monto = c.getTotal(); // si quieres mostrar el total del comprobante

        if (!detalles.isEmpty()) {
            DetalleComprobante det = detalles.get(0);
            Deuda deuda = det.getDeuda();
            concepto = deuda.getMotivo().getDescripcion();
            // si prefieres el monto de ese ítem, usa:
            // monto = det.getMontoPagado();
        }

        // MÉTOD0 DE PAGO
        List<MetodoPagoComprobante> metodos = metodoPagoComprobanteRepository.findByComprobante(c);
        String metodoPago;

        if (metodos.isEmpty()) {
            metodoPago = "N/A";
        } else if (metodos.size() == 1) {
            metodoPago = metodos.get(0).getMetodoPago();
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
