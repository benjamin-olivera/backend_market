package pe.com.market.service.concepto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.market.dto.concepto.MotivoCobroResponse;
import pe.com.market.enums.EstadoMotivoCobro;
import pe.com.market.repository.concepto.MotivoCobroRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MotivoCobroService {

    private final MotivoCobroRepository repository;

    public List<MotivoCobroResponse> listarActivos() {
        return repository.findByEstadoOrderByNombreAsc(EstadoMotivoCobro.ACTIVO)
                .stream()
                .map(m -> new MotivoCobroResponse(
                        m.getIdMotivo(),
                        m.getNombre(),
                        m.getDescripcion(),
                        m.getEstado(),
                        m.getFechaCreacion()
                ))
                .toList();
    }
}