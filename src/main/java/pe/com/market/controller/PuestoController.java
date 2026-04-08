package pe.com.market.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.model.Puesto;
import pe.com.market.model.Socio;
import pe.com.market.model.SocioPuesto;
import pe.com.market.repository.PuestoRepository;
import pe.com.market.repository.SocioPuestoRepository;
import pe.com.market.repository.SocioRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/mercado/puestos")
public class PuestoController {

    private final PuestoRepository puestoRepository;
    private final SocioRepository socioRepository;
    private final SocioPuestoRepository socioPuestoRepository;

    public PuestoController(PuestoRepository puestoRepository,
                            SocioRepository socioRepository,
                            SocioPuestoRepository socioPuestoRepository) {
        this.puestoRepository = puestoRepository;
        this.socioRepository = socioRepository;
        this.socioPuestoRepository = socioPuestoRepository;
    }

    @GetMapping
    public List<Puesto> listar() {
        return puestoRepository.findAll();
    }

    @PostMapping
    public Puesto crear(@RequestBody Puesto puesto) {
        puesto.setEstado(true);
        return puestoRepository.save(puesto);
    }

    @PostMapping("/{codigo}/asignar-socio/{idSocio}")
    public ResponseEntity<?> asignar(@PathVariable String codigo,
                                     @PathVariable Integer idSocio) {

        Puesto puesto = puestoRepository.findByCodigo(codigo)
                .orElseThrow(() -> new RuntimeException("Puesto no encontrado"));
        Socio socio = socioRepository.findById(idSocio)
                .orElseThrow(() -> new RuntimeException("Socio no encontrado"));

        socioPuestoRepository.findByPuesto(puesto)
                .ifPresent(socioPuestoRepository::delete);

        SocioPuesto sp = new SocioPuesto();
        sp.setPuesto(puesto);
        sp.setSocio(socio);
        sp.setFechaAsignacion(Date.valueOf(LocalDate.now()));
        socioPuestoRepository.save(sp);

        return ResponseEntity.ok(puesto);
    }

}
