package pe.com.market.service;

import jakarta.transaction.Transactional;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pe.com.market.dto.DistribuirDeudaRequest;
import pe.com.market.dto.DistribuirDeudaSeleccionadosRequest;
import pe.com.market.dto.MismaDeudaRequest;
import pe.com.market.model.Deuda;
import pe.com.market.model.MotivoCobro;
import pe.com.market.model.Puesto;
import pe.com.market.repository.DeudaRepository;
import pe.com.market.repository.MotivoCobroRepository;
import pe.com.market.repository.PuestoRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@Service
public class DeudaService {

    private final PuestoRepository puestoRepository;
    private final MotivoCobroRepository motivoCobroRepository;
    private final DeudaRepository deudaRepository;

    public DeudaService(PuestoRepository puestoRepository,
                        MotivoCobroRepository motivoCobroRepository,
                        DeudaRepository deudaRepository) {
        this.puestoRepository = puestoRepository;
        this.motivoCobroRepository = motivoCobroRepository;
        this.deudaRepository = deudaRepository;
    }

    // === MODO 1: distribuir entre TODOS ===
    @Transactional
    public DistribucionResultado distribuirDeudaTodos(DistribuirDeudaRequest request) {
        List<Puesto> puestos = puestoRepository.findAll();
        if (puestos.isEmpty()) {
            throw new IllegalStateException("No hay puestos registrados");
        }

        MotivoCobro motivo = motivoCobroRepository.findById(request.getIdMotivo())
                .orElseThrow(() -> new IllegalArgumentException("Motivo no encontrado"));

        BigDecimal montoTotal = request.getMontoTotal();
        BigDecimal cantidad = new BigDecimal(puestos.size());
        BigDecimal montoPorPuesto = montoTotal.divide(cantidad, 2, RoundingMode.HALF_UP);

        LocalDate hoy = LocalDate.now();

        for (Puesto p : puestos) {
            Deuda d = new Deuda();
            d.setPuesto(p);
            d.setMotivo(motivo);
            d.setMonto(montoPorPuesto);
            d.setFecha(Date.valueOf(hoy));
            d.setEstado("PENDIENTE");
            deudaRepository.save(d);
        }

        DistribucionResultado r = new DistribucionResultado();
        r.setCantidadPuestos(puestos.size());
        r.setMontoPorPuesto(montoPorPuesto);
        return r;
    }

    // === MODO 2: distribuir entre SELECCIONADOS ===
    @Transactional
    public DistribucionResultado distribuirDeudaSeleccionados(DistribuirDeudaSeleccionadosRequest request) {

        if (request.getCodigosPuestos() == null || request.getCodigosPuestos().isEmpty()) {
            throw new IllegalArgumentException("Debe enviar al menos un código de puesto");
        }

        List<Puesto> puestos = puestoRepository.findAllByCodigoIn(request.getCodigosPuestos());
        if (puestos.isEmpty()) {
            throw new IllegalStateException("Los códigos de puesto no existen");
        }

        MotivoCobro motivo = motivoCobroRepository.findById(request.getIdMotivo())
                .orElseThrow(() -> new IllegalArgumentException("Motivo no encontrado"));

        BigDecimal montoTotal = request.getMontoTotal();
        BigDecimal cantidad = new BigDecimal(puestos.size());
        BigDecimal montoPorPuesto = montoTotal.divide(cantidad, 2, BigDecimal.ROUND_HALF_UP);

        LocalDate hoy = LocalDate.now();

        for (Puesto p : puestos) {
            Deuda d = new Deuda();
            d.setPuesto(p);
            d.setMotivo(motivo);
            d.setMonto(montoPorPuesto);
            d.setFecha(Date.valueOf(hoy));
            d.setEstado("PENDIENTE");
            deudaRepository.save(d);
        }

        DistribucionResultado r = new DistribucionResultado();
        r.setCantidadPuestos(puestos.size());
        r.setMontoPorPuesto(montoPorPuesto);
        return r;
    }

    // === MODO 3 y 4: MISMO monto (todos o seleccionados) ===
    @Transactional
    public DistribucionResultado mismaDeuda(MismaDeudaRequest request) {

        MotivoCobro motivo = motivoCobroRepository.findById(request.getIdMotivo())
                .orElseThrow(() -> new IllegalArgumentException("Motivo no encontrado"));

        List<Puesto> puestos;

        if (request.getCodigosPuestos() == null || request.getCodigosPuestos().isEmpty()) {
            // todos los puestos
            puestos = puestoRepository.findAll();
        } else {
            puestos = puestoRepository.findAllByCodigoIn(request.getCodigosPuestos());
        }

        if (puestos.isEmpty()) {
            throw new IllegalStateException("No hay puestos para generar la deuda");
        }

        BigDecimal montoPorPuesto = request.getMontoPorPuesto();
        LocalDate hoy = LocalDate.now();

        for (Puesto p : puestos) {
            Deuda d = new Deuda();
            d.setPuesto(p);
            d.setMotivo(motivo);
            d.setMonto(montoPorPuesto);
            d.setFecha(Date.valueOf(hoy));
            d.setEstado("PENDIENTE");
            deudaRepository.save(d);
        }

        DistribucionResultado r = new DistribucionResultado();
        r.setCantidadPuestos(puestos.size());
        r.setMontoPorPuesto(montoPorPuesto);
        return r;
    }


    @Setter
    @Getter
    public static class DistribucionResultado {
        private int cantidadPuestos;
        private BigDecimal montoPorPuesto;
    }
}
