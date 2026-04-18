package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.puesto.PuestoDTO;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.service.puesto.PuestoService;

import java.util.List;

@RestController
@RequestMapping("/puestos")
@RequiredArgsConstructor
public class PuestoController {

    private final PuestoService puestoService;

    //@GetMapping
    //public ResponseEntity<List<PuestoResponse>> listar() {
    //    List<PuestoResponse> lista = puestoRepository.findAll().stream()
    //            .map(this::mapearAPuestoResponse)
    //            .collect(Collectors.toList());
    //    return ResponseEntity.ok(lista);
    //}

    @GetMapping
    public ResponseEntity<List<Puesto>> listarTodos() {
        return ResponseEntity.ok(puestoService.listarTodos());
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Puesto>> listar() {
        return ResponseEntity.ok(puestoService.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Puesto> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(puestoService.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Puesto> crear(@Valid @RequestBody PuestoDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(puestoService.guardar(dto));
    }

    //@PostMapping
    //public ResponseEntity<PuestoResponse> crear(@Valid @RequestBody PuestoRequest request) {
    //    Puesto p = new Puesto();
    //    p.setCodigo(request.getCodigo());
    //    p.setDescripcion(request.getDescripcion());
    //    p.setEstado(request.getEsPropiedadAsociacion());
    //    p.setEstado(true);

    //    Puesto guardado = puestoRepository.save(p);
    //    return ResponseEntity.ok(mapearAPuestoResponse(guardado));
    //}

    @PutMapping("/{id}")
    public ResponseEntity<Puesto> actualizar(@PathVariable Integer id,
                                             @Valid @RequestBody PuestoDTO dto) {
        return ResponseEntity.ok(puestoService.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        puestoService.eliminarLogico(id);
        return ResponseEntity.noContent().build(); // 204
    }

    // Asignar un puesto a un socio
    //@PostMapping("/asignar")
    //public ResponseEntity<?> asignarPuesto(@Valid @RequestBody AsignarPuestoRequest request) {

    //    socioPuestoService.asignarPuesto(
    //            request.getIdSocio(),
    //            request.getIdPuesto(),
    //            request.getFechaAsignacion()
    //    );

    //    return ResponseEntity.ok().build();
    //}

    // ================== helpers ==================

    //private PuestoResponse mapearAPuestoResponse(Puesto p) {
    //    PuestoResponse resp = new PuestoResponse();
    //    resp.setId(p.getIdPuesto());
    //    resp.setCodigo(p.getCodigo());
    //    resp.setDescripcion(p.getDescripcion());
    //    resp.setEstado(p.getEstado());
    //    resp.setEsPropiedadAsociacion(p.getEstado());
    //    return resp;
    //}
}