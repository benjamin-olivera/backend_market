package pe.com.market.service.pago;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.PagoListadoRow;
import pe.com.market.dto.pago.*;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.model.auth.Usuario;
import pe.com.market.model.deuda.Deuda;
import pe.com.market.model.pago.Comprobante;
import pe.com.market.model.pago.DetalleComprobante;
import pe.com.market.model.pago.MetodoPagoComprobante;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.repository.auth.UsuarioRepository;
import pe.com.market.repository.deuda.DeudaRepository;
import pe.com.market.repository.pago.ComprobanteRepository;
import pe.com.market.repository.pago.DetalleComprobanteRepository;
import pe.com.market.repository.pago.MetodoPagoComprobanteRepository;
import pe.com.market.repository.pago.PagoRepository;
import pe.com.market.repository.puesto.PuestoRepository;
import pe.com.market.service.pago.helper.PagoValidador;
import pe.com.market.service.pago.helper.PagoValidador.DeudaValidationResult;
import pe.com.market.service.pago.helper.TotalesPagoHelper;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagoService {

    private final PuestoRepository puestoRepository;
    private final UsuarioRepository usuarioRepository;
    private final DeudaRepository deudaRepository;
    private final ComprobanteRepository comprobanteRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;
    private final MetodoPagoComprobanteRepository metodoPagoComprobanteRepository;

    private final PagoValidador pagoValidador;
    private final TotalesPagoHelper totalesPagoHelper;

    private final PagoRepository pagoRepository;


    public Page<PagoListadoDto> listar(String q, LocalDate from, LocalDate to, int page, int size) {
        Page<PagoListadoRow> base = pagoRepository.listarBase(
                q, from, to,
                PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "fechaPago")) // si falla por alias, quítalo
        );

        List<Integer> ids = base.getContent().stream()
                .map(PagoListadoRow::getIdComprobante)
                .toList();

        Map<Integer, List<String>> conceptosPorId = new HashMap<>();

        if (!ids.isEmpty()) {
            pagoRepository.listarConceptosPorComprobantes(ids).forEach(r -> {
                conceptosPorId
                        .computeIfAbsent(r.getIdComprobante(), k -> new ArrayList<>())
                        .add(r.getConcepto());
            });
        }

        List<PagoListadoDto> dtos = base.getContent().stream().map(row -> {
            PagoListadoDto dto = new PagoListadoDto();
            dto.setIdComprobante(row.getIdComprobante());
            dto.setIdRecibo(row.getIdRecibo());
            dto.setFechaPago(row.getFechaPago());
            dto.setPuesto(row.getPuesto());
            dto.setSocio(row.getSocio());
            dto.setMonto(row.getMonto());
            dto.setEstado(row.getEstado());
            dto.setConceptos(conceptosPorId.getOrDefault(row.getIdComprobante(), List.of()));
            return dto;
        }).toList();

        return new PageImpl<>(dtos, base.getPageable(), base.getTotalElements());
    }

    @Transactional
    public ComprobanteResponse registrarPago(PagoRequest request) {

        // ========= LOG INICIAL: qué llega desde el front =========
        System.out.println("===== PagoRequest recibido =====");
        System.out.println("idPuesto (request): " + request.getIdPuesto());
        System.out.println("idUsuario (request): " + request.getIdUsuario());
        System.out.println("--- Deudas en request ---");
        if (request.getDeudas() != null && !request.getDeudas().isEmpty()) {
            for (DeudaPagoItem d : request.getDeudas()) {
                System.out.println("  * idDeuda=" + d.getIdDeuda()
                        + ", montoPagado=" + d.getMontoPagado());
            }
        } else {
            System.out.println("  (sin deudas en request)");
        }
        System.out.println("--- Métodos de pago en request ---");
        if (request.getMetodosPago() != null && !request.getMetodosPago().isEmpty()) {
            for (MetodoPagoRequest mp : request.getMetodosPago()) {
                System.out.println("  * metodo=" + mp.getMetodo()
                        + ", monto=" + mp.getMonto());
            }
        } else {
            System.out.println("  (sin métodos de pago en request)");
        }
        System.out.println("==================================");

        // -------- 1. Validaciones básicas del request --------
        if (request.getDeudas() == null || request.getDeudas().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar al menos una deuda para pagar.");
        }
        if (request.getMetodosPago() == null || request.getMetodosPago().isEmpty()) {
            throw new IllegalArgumentException("Debe indicar al menos un método de pago.");
        }

        // -------- 2. Obtener puesto y usuario --------
        Puesto puesto = puestoRepository.findById(request.getIdPuesto())
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));

        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado."));

        LocalDateTime fechaOperacion = request.getFechaOperacion() != null
                ? request.getFechaOperacion()
                : LocalDateTime.now();

        // -------- 3. Validar deudas y calcular total --------
        // Aquí es donde PagoValidador comprobará que cada deuda pertenece al puesto
        DeudaValidationResult result = pagoValidador.validarDeudas(
                request.getDeudas(),
                puesto
        );
        BigDecimal totalDeudas = result.totalDeudas();
        List<Deuda> deudas = result.deudas();

        // -------- 4. Validar métodos de pago y calcular vuelto --------
        BigDecimal totalMetodos = totalesPagoHelper.calcularTotalMetodos(request.getMetodosPago());

        if (totalMetodos.compareTo(totalDeudas) < 0) {
            throw new IllegalArgumentException(
                    "No se permiten pagos parciales. " +
                            "El monto total de los métodos de pago es menor al total de las deudas.");
        }

        BigDecimal totalEfectivo = totalesPagoHelper.calcularTotalEfectivo(request.getMetodosPago());
        BigDecimal vuelto = totalesPagoHelper.calcularVuelto(totalDeudas, totalEfectivo);

        // -------- 5. Crear comprobante --------
        Comprobante comprobante = new Comprobante();
        comprobante.setPuesto(puesto);
        comprobante.setUsuario(usuario);
        comprobante.setFecha(fechaOperacion);
        comprobante.setTotal(totalDeudas);
        comprobante.setNumero(generarNumeroComprobante());
        comprobante.setTipo("RECIBO");
        comprobante.setVuelto(vuelto);

        comprobanteRepository.save(comprobante);

        // -------- 6. Crear detalle por cada deuda y marcar como PAGADA --------
        for (int i = 0; i < deudas.size(); i++) {
            Deuda deuda = deudas.get(i);
            DeudaPagoItem item = request.getDeudas().get(i);

            DetalleComprobante detalle = new DetalleComprobante();
            detalle.setComprobante(comprobante);
            detalle.setDeuda(deuda);
            detalle.setMontoPagado(item.getMontoPagado());
            detalleComprobanteRepository.save(detalle);

            deuda.setEstado(EstadoDeuda.PAGADA);
            deudaRepository.save(deuda);
        }

        // -------- 7. Guardar métodos de pago --------
        for (MetodoPagoRequest mp : request.getMetodosPago()) {
            MetodoPagoComprobante mpc = new MetodoPagoComprobante();
            mpc.setComprobante(comprobante);
            mpc.setMetodo(mp.getMetodo());
            mpc.setMonto(mp.getMonto());
            metodoPagoComprobanteRepository.save(mpc);
        }

        // -------- 8. Construir respuesta --------
        return ComprobanteResponse.fromEntity(comprobante, deudas, request.getMetodosPago());
    }

    private String generarNumeroComprobante() {
        Long count = comprobanteRepository.count() + 1;
        return "REC-" + LocalDateTime.now().getYear() + "-" + String.format("%05d", count);
    }
}