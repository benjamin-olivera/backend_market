// pe/com/market/service/socio/SocioService.java
package pe.com.market.service.socio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.dto.socio_puesto.PuestoSocioResponse;
import pe.com.market.dto.socio.SocioBusquedaResponse;
import pe.com.market.dto.socio.SocioDTO;
import pe.com.market.dto.socio.SocioResponse;
import pe.com.market.exception.BusinessException;
import pe.com.market.exception.ResourceNotFoundException;
import pe.com.market.model.puesto.SocioPuesto;
import pe.com.market.model.socio.Socio;
import pe.com.market.repository.socio.SocioPuestoRepository;
import pe.com.market.repository.socio.SocioRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SocioService {

    private final SocioRepository socioRepository;
    private final SocioPuestoRepository socioPuestoRepository;

    public List<Socio> listarActivos() {
        return socioRepository.findByEstadoTrueOrderByNombreAsc();
    }

    public Socio buscarPorId(Integer id) {
        return socioRepository.findById(id)
                .filter(Socio::getEstado)
                .orElseThrow(() -> new ResourceNotFoundException("Socio no encontrado con id: " + id));
    }

    @Transactional
    public Socio guardar(SocioDTO dto) {
        validarDniUnico(dto.getDni(), null);
        validarTelefonoUnico(dto.getTelefono(), null);
        Socio socio = new Socio();
        mapearDesdeDTO(socio, dto);
        return socioRepository.save(socio);
    }

    @Transactional
    public Socio actualizar(Integer id, SocioDTO dto) {
        Socio actual = buscarPorId(id);
        validarDniUnico(dto.getDni(), id);
        validarTelefonoUnico(dto.getTelefono(), id);
        mapearDesdeDTO(actual, dto);
        return socioRepository.save(actual);
    }

    @Transactional
    public void eliminarLogico(Integer id) {
        Socio socio = buscarPorId(id);
        socio.setEstado(false);
        socioRepository.save(socio);
    }


    private void validarDniUnico(String dni, Integer idExcluir) {
        boolean existe = (idExcluir == null)
                ? socioRepository.existsByDni(dni)
                : socioRepository.existsByDniAndIdSocioNot(dni, idExcluir);
        if (existe) throw new BusinessException("El DNI '" + dni + "' ya está registrado");
    }

    private void validarTelefonoUnico(String telefono, Integer idExcluir) {
        boolean existe = (idExcluir == null)
                ? socioRepository.existsByTelefono(telefono)
                : socioRepository.existsByTelefonoAndIdSocioNot(telefono, idExcluir);
        if (existe) throw new BusinessException("El teléfono '" + telefono + "' ya está registrado");
    }

    private void mapearDesdeDTO(Socio socio, SocioDTO dto) {
        socio.setNombre(dto.getNombre());
        socio.setDni(dto.getDni());
        socio.setTelefono(dto.getTelefono());
        socio.setEmail(dto.getEmail());
        socio.setDireccion(dto.getDireccion());
    }

    // ====== 1) Autocomplete por prefijo de DNI ======
    public List<SocioBusquedaResponse> buscarPorDniPrefix(String dniPrefix) {
        if (dniPrefix == null || dniPrefix.length() < 2) {
            return List.of();
        }

        List<Socio> socios = socioRepository
                .findTop10ByDniStartingWithOrderByDniAsc(dniPrefix);

        return socios.stream()
                .map(s -> new SocioBusquedaResponse(
                        s.getIdSocio(),
                        s.getDni(),
                        s.getNombre()
                ))
                .toList();
    }

    // ====== 2) Obtener socio completo por DNI (para Registrar Pago) ======
    public SocioResponse obtenerSocioPorDni(String dni) {
        Socio socio = socioRepository.findByDni(dni)
                .orElseThrow(() -> new RuntimeException("No existe socio"));

        List<SocioPuesto> relaciones = socioPuestoRepository
                .findBySocioIdSocioAndFechaFinIsNull(socio.getIdSocio());

        List<PuestoSocioResponse> puestosDto = relaciones.stream()
                .map(rel -> new PuestoSocioResponse(
                        rel.getPuesto().getIdPuesto(),
                        rel.getPuesto().getCodigo(),
                        rel.getPuesto().getDescripcion()
                ))
                .toList();

        return new SocioResponse(
                socio.getIdSocio(),
                socio.getNombre(),
                socio.getDni(),
                socio.getTelefono(),
                socio.getEstado(),
                puestosDto
        );
    }
}