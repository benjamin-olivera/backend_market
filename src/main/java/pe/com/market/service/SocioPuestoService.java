package pe.com.market.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.socio_puesto.SocioPuestoDTO;
import pe.com.market.exception.BusinessException;
import pe.com.market.exception.ResourceNotFoundException;
import pe.com.market.model.socio_puesto.SocioPuesto;
import pe.com.market.repository.SocioPuestoRepository;
import pe.com.market.service.puesto.PuestoService;
import pe.com.market.service.socio.SocioService;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SocioPuestoService {

    private final SocioPuestoRepository repository;
    private final SocioService socioService;
    private final PuestoService puestoService;

    @Transactional
    public SocioPuesto asignar(SocioPuestoDTO dto) {
        // Verificar que el puesto no tenga asignación activa
        if (repository.existsByPuesto_IdPuestoAndFechaFinIsNull(dto.getIdPuesto())) {
            // Cerrar asignación anterior
            repository.findByPuesto_IdPuestoAndFechaFinIsNull(dto.getIdPuesto())
                    .ifPresent(anterior -> {
                        anterior.setFechaFin(LocalDate.now());
                        repository.save(anterior);
                    });
        }

        SocioPuesto nueva = new SocioPuesto();
        nueva.setSocio(socioService.buscarPorId(dto.getIdSocio()));
        nueva.setPuesto(puestoService.buscarPorId(dto.getIdPuesto()));
        if (dto.getFechaAsignacion() != null) {
            nueva.setFechaAsignacion(dto.getFechaAsignacion());
        }
        return repository.save(nueva);
    }

    public List<SocioPuesto> listarActivos() {
        return repository.findActivos();
    }

    public SocioPuesto obtenerAsignacionActivaPorPuesto(Integer idPuesto) {
        return repository.findByPuesto_IdPuestoAndFechaFinIsNull(idPuesto)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No hay socio asignado al puesto con id: " + idPuesto));
    }

    public List<SocioPuesto> historialPorPuesto(Integer idPuesto) {
        puestoService.buscarPorId(idPuesto);
        return repository.findByPuesto_IdPuestoOrderByFechaAsignacionDesc(idPuesto);
    }

    public List<SocioPuesto> puestosActivosPorSocio(Integer idSocio) {
        socioService.buscarPorId(idSocio);
        return repository.findBySocio_IdSocioAndFechaFinIsNull(idSocio);
    }

    public List<Integer> obtenerIdsPuestosOcupados() {
        return repository.findIdPuestosOcupados();
    }

    @Transactional
    public void desasignar(Integer idSocioPuesto) {
        SocioPuesto sp = repository.findById(idSocioPuesto)
                .orElseThrow(() -> new ResourceNotFoundException("Asignación no encontrada"));
        if (sp.getFechaFin() != null) {
            throw new BusinessException("Esta asignación ya está inactiva");
        }
        sp.setFechaFin(LocalDate.now());
        repository.save(sp);
    }
}