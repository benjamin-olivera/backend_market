package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.puesto.AsignarPuestoRequest;
import pe.com.market.dto.puesto.PuestoRequest;
import pe.com.market.dto.puesto.PuestoResponse;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.repository.puesto.PuestoRepository;
import pe.com.market.service.puesto.SocioPuestoService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/puestos")
@RequiredArgsConstructor
public class PuestoController {

    private final PuestoRepository puestoRepository;
    private final SocioPuestoService socioPuestoService;

    @GetMapping
    public ResponseEntity<List<PuestoResponse>> listar() {
        List<PuestoResponse> lista = puestoRepository.findAll().stream()
                .map(this::mapearAPuestoResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @PostMapping
    public ResponseEntity<PuestoResponse> crear(@Valid @RequestBody PuestoRequest request) {
        Puesto p = new Puesto();
        p.setCodigo(request.getCodigo());
        p.setDescripcion(request.getDescripcion());
        p.setEsPropiedadAsociacion(request.getEsPropiedadAsociacion());
        p.setEstado(true);

        Puesto guardado = puestoRepository.save(p);
        return ResponseEntity.ok(mapearAPuestoResponse(guardado));
    }

    // Asignar un puesto a un socio
    @PostMapping("/asignar")
    public ResponseEntity<?> asignarPuesto(@Valid @RequestBody AsignarPuestoRequest request) {

        socioPuestoService.asignarPuesto(
                request.getIdSocio(),
                request.getIdPuesto(),
                request.getFechaAsignacion()
        );

        return ResponseEntity.ok().build();
    }



    // ================== helpers ==================

    private PuestoResponse mapearAPuestoResponse(Puesto p) {
        PuestoResponse resp = new PuestoResponse();
        resp.setId(p.getIdPuesto());
        resp.setCodigo(p.getCodigo());
        resp.setDescripcion(p.getDescripcion());
        resp.setEstado(p.getEstado());
        resp.setEsPropiedadAsociacion(p.getEsPropiedadAsociacion());
        return resp;
    }
}