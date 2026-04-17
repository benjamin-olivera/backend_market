package pe.com.market.service.puesto;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.puesto.PuestoDTO;
import pe.com.market.exception.BusinessException;
import pe.com.market.exception.ResourceNotFoundException;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.repository.puesto.PuestoRepository;

import java.util.List;

@Service
public class PuestoService {

    private final PuestoRepository repository;

    public PuestoService(PuestoRepository repository) {
        this.repository = repository;
    }

    public List<Puesto> listarActivos() {
        return repository.findByEstadoTrueOrderByCodigoAsc();
    }

    public Puesto buscarPorId(Integer id) {
        return repository.findById(id)
                //.filter(Puesto::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Puesto no encontrado con id: " + id));
    }

    @Transactional
    public Puesto guardar(PuestoDTO dto) {
        if (repository.existsByCodigo(dto.codigo())) {
            throw new BusinessException("El código de puesto '" + dto.codigo() + "' ya existe");
        }
        Puesto puesto = new Puesto();
        mapearDesdeDTO(puesto, dto);
        return repository.save(puesto);
    }

    @Transactional
    public Puesto actualizar(Integer id, PuestoDTO dto) {
        Puesto actual = buscarPorId(id);
        if (repository.existsByCodigoAndIdPuestoNot(dto.codigo(), id)) {
            throw new BusinessException("El código '" + dto.codigo() + "' ya está en uso por otro puesto");
        }
        mapearDesdeDTO(actual, dto);
        return repository.save(actual);
    }

    @Transactional
    public void eliminarLogico(Integer id) {
        Puesto puesto = buscarPorId(id);
        puesto.setEstado(false);
        repository.save(puesto);
    }

    private void mapearDesdeDTO(Puesto puesto, PuestoDTO dto) {
        puesto.setCodigo(dto.codigo());
        puesto.setSector(dto.sector());
        puesto.setNumero(dto.numero());
        puesto.setDescripcion(dto.descripcion());
    }
}