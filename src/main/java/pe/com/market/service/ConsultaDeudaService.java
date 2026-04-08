package pe.com.market.service;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;
import pe.com.market.dto.DeudaResponse;
import pe.com.market.model.Deuda;
import pe.com.market.model.Puesto;
import pe.com.market.repository.DeudaRepository;
import pe.com.market.repository.PuestoRepository;

import java.math.BigDecimal;
import java.util.List;

@Service
public class ConsultaDeudaService {

    private final PuestoRepository puestoRepository;
    private final DeudaRepository deudaRepository;

    public ConsultaDeudaService(PuestoRepository puestoRepository,
                                DeudaRepository deudaRepository) {
        this.puestoRepository = puestoRepository;
        this.deudaRepository = deudaRepository;
    }

    public ResultadoDeudasPuesto obtenerDeudasPendientes(String codigoPuesto) {
        Puesto puesto = puestoRepository.findByCodigo(codigoPuesto)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado: " + codigoPuesto));

        List<Deuda> deudas = deudaRepository.findByPuestoAndEstado(puesto, "PENDIENTE");

        BigDecimal total = BigDecimal.ZERO;

        List<DeudaResponse> lista = deudas.stream().map(d -> {
            DeudaResponse dto = new DeudaResponse();
            dto.setIdDeuda(d.getId());
            dto.setMotivo(d.getMotivo().getDescripcion());
            dto.setMonto(d.getMonto());
            dto.setEstado(d.getEstado());
            dto.setFecha(d.getFecha().toLocalDate());
            return dto;
        }).toList();

        for (Deuda d : deudas) {
            total = total.add(d.getMonto());
        }

        ResultadoDeudasPuesto r = new ResultadoDeudasPuesto();
        r.setCodigoPuesto(puesto.getCodigo());
        r.setDescripcionPuesto(puesto.getDescripcion());
        r.setDeudasPendientes(lista);
        r.setTotalPendiente(total);

        return r;
    }

    // Clase resultado
    @Setter
    @Getter
    public static class ResultadoDeudasPuesto {
        private String codigoPuesto;
        private String descripcionPuesto;
        private List<DeudaResponse> deudasPendientes;
        private BigDecimal totalPendiente;
    }
}