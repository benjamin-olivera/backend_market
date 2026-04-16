package pe.com.market.service.deuda.mapper;

import org.springframework.stereotype.Component;
import pe.com.market.dto.deuda.DeudaResponse;
import pe.com.market.model.deuda.Deuda;

@Component
public class DeudaMapper {

    public DeudaResponse toResponse(Deuda deuda) {
        return new DeudaResponse(
                deuda.getIdDeuda(),
                deuda.getPuesto().getCodigo(),
                deuda.getMotivo().getDescripcion(),
                deuda.getMonto(),
                deuda.getFecha(),
                deuda.getEstado()
        );
    }
}