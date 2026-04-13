package pe.com.market.service.puesto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.socio.Socio;
import pe.com.market.model.puesto.SocioPuesto;
import pe.com.market.repository.puesto.PuestoRepository;
import pe.com.market.repository.socio.SocioPuestoRepository;
import pe.com.market.repository.socio.SocioRepository;

import java.sql.Date;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class SocioPuestoService {

    private final SocioPuestoRepository socioPuestoRepository;
    private final SocioRepository socioRepository;
    private final PuestoRepository puestoRepository;

    @Transactional
    public void asignarPuesto(Integer idSocio, Integer idPuesto, LocalDate fechaAsignacion) {

        Socio socio = socioRepository.findById(idSocio)
                .orElseThrow(() -> new IllegalArgumentException("Socio no encontrado"));

        Puesto puesto = puestoRepository.findById(idPuesto)
                .orElseThrow(() -> new IllegalArgumentException("Puesto no encontrado"));

        // 1. Validar la regla: un puesto solo puede tener un socio activo (fecha_fin NULL)
        boolean yaAsignado = socioPuestoRepository.existsByPuestoAndFechaFinIsNull(puesto);

        if (yaAsignado) {
            throw new IllegalStateException("El puesto ya tiene un socio asignado actualmente");
        }

        // 2. Crear la nueva asignación
        SocioPuesto sp = new SocioPuesto();
        sp.setSocio(socio);
        sp.setPuesto(puesto);
        sp.setFechaAsignacion(
                Date.valueOf(fechaAsignacion != null ? fechaAsignacion : LocalDate.now()).toLocalDate()
        );
        // sp.setFechaFin(null);  // por defecto ya es null

        socioPuestoRepository.save(sp);
    }
}