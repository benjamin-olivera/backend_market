package pe.com.market.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.ComprobanteResponse;
import pe.com.market.dto.PagoRequest;
import pe.com.market.model.*;
import pe.com.market.repository.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class PagoService {

    private final PuestoRepository puestoRepository;
    private final DeudaRepository deudaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;
    private final MetodoPagoComprobanteRepository metodoPagoComprobanteRepository;

    public PagoService(PuestoRepository puestoRepository,
                       DeudaRepository deudaRepository,
                       ComprobanteRepository comprobanteRepository,
                       DetalleComprobanteRepository detalleComprobanteRepository,
                       MetodoPagoComprobanteRepository metodoPagoComprobanteRepository) {
        this.puestoRepository = puestoRepository;
        this.deudaRepository = deudaRepository;
        this.comprobanteRepository = comprobanteRepository;
        this.detalleComprobanteRepository = detalleComprobanteRepository;
        this.metodoPagoComprobanteRepository = metodoPagoComprobanteRepository;
    }

    @Transactional
    public ComprobanteResponse pagarTodoPorPuesto(String codigoPuesto, PagoRequest request) {

        Puesto puesto = puestoRepository.findByCodigo(codigoPuesto)
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado"));

        List<Deuda> deudasPendientes = deudaRepository.findByPuestoAndEstado(puesto, "PENDIENTE");
        if (deudasPendientes.isEmpty()) {
            throw new RuntimeException("El puesto no tiene deudas pendientes");
        }

        BigDecimal totalDeuda = deudasPendientes.stream()
                .map(Deuda::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPagado = request.getMetodosPago().stream()
                .map(PagoRequest.MetodoPagoDTO::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (totalPagado.compareTo(totalDeuda) < 0) {
            throw new RuntimeException("El monto pagado es menor que la deuda total");
        }

        BigDecimal vuelto = totalPagado.subtract(totalDeuda);

        // Crear comprobante
        Comprobante c = new Comprobante();
        c.setPuesto(puesto);
        c.setTipo("RECIBO");
        c.setFecha(LocalDateTime.now());
        c.setTotal(totalDeuda);
        c.setVuelto(vuelto);
        c.setNumero("REC-" + System.currentTimeMillis());
        Comprobante guardado = comprobanteRepository.save(c);

        // Detalles y actualizar deudas
        LocalDate hoy = LocalDate.now();
        List<ComprobanteResponse.LineaDeuda> lineasDeuda = new ArrayList<>();

        for (Deuda d : deudasPendientes) {
            DetalleComprobante det = new DetalleComprobante();
            det.setComprobante(guardado);
            det.setDeuda(d);
            det.setMontoPagado(d.getMonto());
            detalleComprobanteRepository.save(det);

            d.setEstado("PAGADA");
            d.setFechaPago(Date.valueOf(hoy));
            deudaRepository.save(d);

            ComprobanteResponse.LineaDeuda l = new ComprobanteResponse.LineaDeuda();
            l.setIdDeuda(d.getId());
            l.setMotivo(d.getMotivo().getDescripcion());
            l.setMonto(d.getMonto());
            lineasDeuda.add(l);
        }

        // Métodos de pago
        List<ComprobanteResponse.LineaMetodoPago> lineasPago = new ArrayList<>();
        for (PagoRequest.MetodoPagoDTO mp : request.getMetodosPago()) {
            MetodoPagoComprobante m = new MetodoPagoComprobante();
            m.setComprobante(guardado);
            m.setMetodoPago(mp.getMetodo());
            m.setMonto(mp.getMonto());
            metodoPagoComprobanteRepository.save(m);

            ComprobanteResponse.LineaMetodoPago lp = new ComprobanteResponse.LineaMetodoPago();
            lp.setMetodo(mp.getMetodo());
            lp.setMonto(mp.getMonto());
            lineasPago.add(lp);
        }

        // Armar respuesta
        ComprobanteResponse resp = new ComprobanteResponse();
        resp.setIdComprobante(guardado.getId());
        resp.setNumero(guardado.getNumero());
        resp.setTipo(guardado.getTipo());
        resp.setCodigoPuesto(puesto.getCodigo());
        resp.setFecha(guardado.getFecha());
        resp.setTotal(guardado.getTotal());
        resp.setVuelto(guardado.getVuelto());
        resp.setDeudasPagadas(lineasDeuda);
        resp.setMetodosPago(lineasPago);

        return resp;
    }
}
