package pe.com.market.service.deuda;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.deuda.DeudaResponse;
import pe.com.market.dto.deuda.DistribuirDeudaRequest;
import pe.com.market.dto.deuda.MismaDeudaRequest;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.mapper.DeudaMapper;
import pe.com.market.model.deuda.Deuda;
import pe.com.market.model.deuda.MotivoCobro;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.puesto.SocioPuesto;
import pe.com.market.model.socio.Socio;
import pe.com.market.repository.deuda.DeudaRepository;
import pe.com.market.repository.deuda.MotivoCobroRepository;
import pe.com.market.repository.puesto.PuestoRepository;
import pe.com.market.repository.socio.SocioPuestoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeudaService {

    private final DeudaRepository deudaRepository;
    private final PuestoRepository puestoRepository;
    private final MotivoCobroRepository motivoCobroRepository;
    private final SocioPuestoRepository socioPuestoRepository;
    private final DeudaMapper deudaMapper;

    // --------- GENERACIÓN DE DEUDAS ---------
    @Transactional
    public void generarDeudaDistribuida(DistribuirDeudaRequest request) {

        MotivoCobro motivo = motivoCobroRepository.findById(request.getIdMotivo())
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

        MotivoCobro motivo = motivoCobroRepository.findById(request.getIdMotivo())
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
                                      MotivoCobro motivo,
                                      BigDecimal monto,
                                      LocalDate fecha) {

        Deuda deuda = new Deuda();
        deuda.setPuesto(puesto);
        deuda.setMotivo(motivo);
        deuda.setMonto(monto);
        deuda.setFecha(java.sql.Date.valueOf(fecha));
        deuda.setEstado(EstadoDeuda.PENDIENTE);

        if (Boolean.TRUE.equals(puesto.getEsPropiedadAsociacion())) {
            deuda.setSocio(null);
        } else {
            Socio socio = obtenerSocioActualDePuesto(puesto);
            deuda.setSocio(socio);
        }

        deudaRepository.save(deuda);
    }

    private List<Puesto> obtenerPuestosObjetivo(List<String> codigosPuestos) {

        if (codigosPuestos == null || codigosPuestos.isEmpty()) {
            return puestoRepository.findByEstadoTrue();
        }

        return codigosPuestos.stream()
                .map(codigo -> puestoRepository.findByCodigoAndEstadoTrue(codigo)
                        .orElseThrow(() -> new IllegalArgumentException(
                                "Puesto " + codigo + " no existe o está inactivo")))
                .toList();
    }
}