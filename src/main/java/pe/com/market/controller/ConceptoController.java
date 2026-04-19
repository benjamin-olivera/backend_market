package pe.com.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.concepto.ConceptoResponse;
import pe.com.market.model.concepto.Concepto;
import pe.com.market.service.concepto.ConceptoService;

import java.util.List;

@RestController
@RequestMapping("/conceptos")
@RequiredArgsConstructor
public class ConceptoController {

    private final ConceptoService service;

    @GetMapping("/activos")
    public ResponseEntity<List<ConceptoResponse>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping
    public ResponseEntity<Page<Concepto>> listar(
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(service.listarPaginado(q, pageable));
    }

    @PostMapping
    public ResponseEntity<Concepto> registrar(@RequestBody Concepto motivo) {
        return ResponseEntity.ok(service.guardar(motivo));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Concepto> editar(@PathVariable Integer id, @RequestBody Concepto motivo) {
        return ResponseEntity.ok(service.actualizar(id, motivo));
    }

    @PatchMapping("/{id}/estado")
    public ResponseEntity<Concepto> cambiarEstado(@PathVariable Integer id) {
        return ResponseEntity.ok(service.cambiarEstado(id));
    }

    @GetMapping("/count-activos")
    public ResponseEntity<Long> obtenerConteoActivos() {
        return ResponseEntity.ok(service.contarActivos());
    }

}