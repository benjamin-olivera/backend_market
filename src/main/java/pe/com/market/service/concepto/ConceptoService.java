package pe.com.market.service.concepto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import pe.com.market.dto.concepto.ConceptoResponse;
import pe.com.market.enums.EstadoConcepto;
import pe.com.market.model.concepto.Concepto;
import pe.com.market.repository.concepto.ConceptoRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ConceptoService {

    private final ConceptoRepository repository;

    public List<ConceptoResponse> listarActivos() {
        return repository.findByEstadoOrderByNombreAsc(EstadoConcepto.ACTIVO)
                .stream()
                .map(m -> new ConceptoResponse(
                        m.getIdMotivo(),
                        m.getNombre(),
                        m.getDescripcion(),
                        m.getEstado(),
                        m.getFechaCreacion()
                ))
                .toList();
    }
    public Page<Concepto> listarPaginado(String q, Pageable pageable) {
        return repository.listarPaginado(q, pageable);
    }

//    public List<MotivoCobro> listarActivos() {
//        return repository.findByEstadoOrderByNombreAsc(EstadoMotivoCobro.ACTIVO);
//    }

    @Transactional
    public Concepto guardar(Concepto motivo) {

        if (motivo.getNombre() == null || motivo.getNombre().trim().isEmpty()) {
            throw new RuntimeException("El nombre del concepto es obligatorio");
        }

        String nombreLimpio = motivo.getNombre().trim();

        if (repository.existsByNombreIgnoreCase(nombreLimpio)) {
            throw new RuntimeException("Ya existe un concepto con el nombre: " + nombreLimpio);
        }

        motivo.setNombre(nombreLimpio);

        if (motivo.getEstado() == null) {
            motivo.setEstado(EstadoConcepto.ACTIVO);
        }
        motivo.setFechaCreacion(LocalDateTime.now());
        return repository.save(motivo);
    }

    @Transactional
    public Concepto actualizar(Integer id, Concepto datosActualizados) {
        Concepto motivoExistente = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("No se encontró el concepto con ID: " + id));

        String nuevoNombre = datosActualizados.getNombre().trim();

        if (!motivoExistente.getNombre().equalsIgnoreCase(nuevoNombre)) {
            if (repository.existsByNombreIgnoreCase(nuevoNombre)) {
                throw new RuntimeException("Ya existe otro concepto con el nombre: " + nuevoNombre);
            }
        }
        motivoExistente.setNombre(nuevoNombre);
        motivoExistente.setDescripcion(datosActualizados.getDescripcion());

        return repository.save(motivoExistente);
    }

    @Transactional
    public Concepto cambiarEstado(Integer id) {
        Concepto motivo = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("ID no encontrado"));

        if (motivo.getEstado() == EstadoConcepto.ACTIVO) {
            motivo.setEstado(EstadoConcepto.SUSPENDIDO);
        } else {
            motivo.setEstado(EstadoConcepto.ACTIVO);
        }

        return repository.save(motivo);
    }

    public long contarActivos() {
        return repository.countByEstado(EstadoConcepto.ACTIVO);
    }
}