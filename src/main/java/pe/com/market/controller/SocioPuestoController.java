package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.socio_puesto.SocioPuestoCountDTO;
import pe.com.market.dto.socio_puesto.SocioPuestoDTO;
import pe.com.market.model.socio_puesto.SocioPuesto;
import pe.com.market.repository.SocioPuestoRepository;
import pe.com.market.service.SocioPuestoService;

import java.util.List;

@RestController
@RequestMapping("/socio-puesto")
@RequiredArgsConstructor
public class SocioPuestoController {

    private final SocioPuestoRepository socioPuestoRepository;
    private final SocioPuestoService service;

    @GetMapping("/activos")
    public ResponseEntity<List<SocioPuesto>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @PostMapping
    public ResponseEntity<SocioPuesto> asignar(@Valid @RequestBody SocioPuestoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.asignar(dto));
    }

    @GetMapping("/puesto/{idPuesto}/activo")
    public ResponseEntity<SocioPuesto> asignacionActivaPorPuesto(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(service.obtenerAsignacionActivaPorPuesto(idPuesto));
    }

    @GetMapping("/puesto/{idPuesto}/historial")
    public ResponseEntity<List<SocioPuesto>> historialPorPuesto(@PathVariable Integer idPuesto) {
        return ResponseEntity.ok(service.historialPorPuesto(idPuesto));
    }

    @GetMapping("/activos/contador-por-socio")
    public List<SocioPuestoCountDTO> contadorPorSocio() {
        return socioPuestoRepository.countPuestosActivosGroupBySocio();
    }

    @GetMapping("/socio/{idSocio}/puestos")
    public ResponseEntity<List<SocioPuesto>> puestosActivosPorSocio(@PathVariable Integer idSocio) {
        return ResponseEntity.ok(service.puestosActivosPorSocio(idSocio));
    }

    // Nuevo endpoint: lista IDs de puestos ocupados
    @GetMapping("/ocupados")
    public ResponseEntity<List<Integer>> puestosOcupados() {
        return ResponseEntity.ok(service.obtenerIdsPuestosOcupados());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> desasignar(@PathVariable Integer id) {
        service.desasignar(id);
        return ResponseEntity.noContent().build();
    }

}
