package pe.com.market.service.deuda;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.deuda.DeudaListadoResponse;
import pe.com.market.dto.deuda.DeudaResponse;
import pe.com.market.dto.deuda.DistribuirDeudaRequest;
import pe.com.market.dto.deuda.MismaDeudaRequest;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.service.deuda.mapper.DeudaMapper;
import pe.com.market.model.deuda.Deuda;
import pe.com.market.model.concepto.Concepto;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.socio_puesto.SocioPuesto;
import pe.com.market.model.socio.Socio;
import pe.com.market.repository.deuda.DeudaRepository;
import pe.com.market.repository.concepto.ConceptoRepository;
import pe.com.market.repository.puesto.PuestoRepository;
import pe.com.market.repository.SocioPuestoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private final PuestoRepository puestoRepository;
    private final ConceptoRepository conceptoRepository;
    private final SocioPuestoRepository socioPuestoRepository;
    private final DeudaMapper deudaMapper;

    // --------- GENERACIÓN DE DEUDAS ---------
    @Transactional
    public void generarDeudaDistribuida(DistribuirDeudaRequest request) {

        Concepto motivo = conceptoRepository.findById(request.getIdMotivo())
                .orElseThrow(() -> new IllegalArgumentException("Motivo de cobro no encontrado"));

        BigDecimal montoTotal = request.getMontoTotal();
        LocalDate fecha = request.getFecha() != null ? request.getFecha() : LocalDate.now();

        List<Puesto> puestosObjetivo = obtenerPuestosObjetivo(request.getCodigosPuestos());
        if (puestosObjetivo.isEmpty()) {
            return;
        }

        int cantidadPuestos = puestosObjetivo.size();
        BigDecimal montoPorPuesto = montoTotal
                .divide(BigDecimal.valueOf(cantidadPuestos), 2, RoundingMode.HALF_UP);

        for (Puesto puesto : puestosObjetivo) {
            crearDeudaParaPuesto(puesto, motivo, montoPorPuesto, fecha);
        }
    }

    @Transactional
    public void generarMismaDeuda(MismaDeudaRequest request) {

        Concepto motivo = conceptoRepository.findById(request.getIdMotivo())
                .orElseThrow(() -> new IllegalArgumentException("Motivo de cobro no encontrado"));

        BigDecimal monto = request.getMonto();
        LocalDate fecha = request.getFecha() != null ? request.getFecha() : LocalDate.now();

        List<Puesto> puestosObjetivo = obtenerPuestosObjetivo(request.getCodigosPuestos());
        if (puestosObjetivo.isEmpty()) {
            return;
        }

        for (Puesto puesto : puestosObjetivo) {
            crearDeudaParaPuesto(puesto, motivo, monto, fecha);
        }
    }

    // --------- CONSULTA DE DEUDAS ---------


    public List<DeudaListadoResponse> listarFiltrado(
            String q,
            EstadoDeuda estado,
            Integer idMotivo,
            LocalDate fechaDesde,
            LocalDate fechaHasta
    ) {
        Date desde = (fechaDesde != null) ? Date.valueOf(fechaDesde) : null;
        Date hasta = (fechaHasta != null) ? Date.valueOf(fechaHasta) : null;

        String qq = (q == null || q.trim().isEmpty()) ? null : q.trim();

        return deudaRepository.listarFiltrado(qq, estado, idMotivo, desde, hasta);
    }

    // Para la pantalla de caja
    public List<DeudaResponse> listarPendientesPorPuesto(String codigoPuesto) {
        List<Deuda> deudas = deudaRepository
                .findByPuesto_CodigoAndEstado(codigoPuesto, EstadoDeuda.PENDIENTE);

        return deudas.stream()
                .map(deudaMapper::toResponse)
                .toList();
    }

    // Opcional: histórico (todas las deudas de un puesto)
    public List<DeudaResponse> listarTodasPorPuesto(String codigoPuesto) {
        List<Deuda> deudas = deudaRepository.findByPuesto_Codigo(codigoPuesto);
        return deudas.stream()
                .map(deudaMapper::toResponse)
                .toList();
    }

    // --------- Helpers internos ---------

    private Socio obtenerSocioActualDePuesto(Puesto puesto) {
        Optional<SocioPuesto> socioPuestoOpt =
                socioPuestoRepository.findByPuestoAndFechaFinIsNull(puesto);

        return socioPuestoOpt
                .map(SocioPuesto::getSocio)
                .orElseThrow(() ->
                        new IllegalStateException("El puesto " + puesto.getCodigo()
                                + " no tiene socio asignado actualmente"));
    }

    private void crearDeudaParaPuesto(Puesto puesto,
                                      Concepto motivo,
                                      BigDecimal monto,
                                      LocalDate fecha) {

        Deuda deuda = new Deuda();
        deuda.setPuesto(puesto);
        deuda.setMotivo(motivo);
        deuda.setMonto(monto);
        deuda.setFecha(java.sql.Date.valueOf(fecha));
        deuda.setEstado(EstadoDeuda.PENDIENTE);

        if (Boolean.TRUE.equals(puesto.getEstado())) {
            deuda.setSocio(null);
        } else {
            Socio socio = obtenerSocioActualDePuesto(puesto);
            deuda.setSocio(socio);
        }

        deudaRepository.save(deuda);
    }

    private List<Puesto> obtenerPuestosObjetivo(List<String> codigosPuestos) {

        // Si no especifican códigos, aplicamos a TODOS los puestos
        if (codigosPuestos == null || codigosPuestos.isEmpty()) {
            return puestoRepository.findAll();
        }

        // Si especifican, aplicamos a esos puestos (activos o inactivos según exista)
        return codigosPuestos.stream()
                .map(codigo -> puestoRepository.findByCodigo(codigo)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Puesto " + codigo + " no existe")))
                .toList();
    }
}