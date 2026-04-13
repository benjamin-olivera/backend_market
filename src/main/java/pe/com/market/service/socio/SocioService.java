// pe/com/market/service/socio/SocioService.java
package pe.com.market.service.socio;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.market.dto.socio.PuestoSocioResponse;
import pe.com.market.dto.socio.SocioBusquedaResponse;
import pe.com.market.dto.socio.SocioResponse;
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