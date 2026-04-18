package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.deuda.DeudaListadoResponse;
import pe.com.market.dto.deuda.DeudaResponse;
import pe.com.market.dto.deuda.DistribuirDeudaRequest;
import pe.com.market.dto.deuda.MismaDeudaRequest;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.service.deuda.DeudaService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/deudas")
@RequiredArgsConstructor
public class DeudaController {

    private final DeudaService deudaService;

    // ================== GENERACIÓN ==================
    @PostMapping("/distribuir")
    public ResponseEntity<Void> generarDeudaDistribuida(@Valid @RequestBody DistribuirDeudaRequest request) {
        deudaService.generarDeudaDistribuida(request);
        return ResponseEntity.noContent().build(); // 204
    }

    @PostMapping("/misma")
    public ResponseEntity<Void> generarMismaDeuda(@Valid @RequestBody MismaDeudaRequest request) {
        deudaService.generarMismaDeuda(request);
        return ResponseEntity.noContent().build(); // 204
    }

    // ================== CONSULTA ==================
    @GetMapping
    public ResponseEntity<List<DeudaListadoResponse>> listar(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) EstadoDeuda estado,
            @RequestParam(required = false) Integer idMotivo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaDesde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaHasta
    ) {
        return ResponseEntity.ok(deudaService.listarFiltrado(q, estado, idMotivo, fechaDesde, fechaHasta));
    }

    @GetMapping("/puesto/{codigo}")
    public ResponseEntity<List<DeudaResponse>> listarTodasPorPuesto(@PathVariable String codigo) {
        return ResponseEntity.ok(deudaService.listarTodasPorPuesto(codigo));
    }

    @GetMapping("/puesto/{codigo}/pendientes")
    public ResponseEntity<List<DeudaResponse>> listarPendientesPorPuesto(@PathVariable String codigo) {
        return ResponseEntity.ok(deudaService.listarPendientesPorPuesto(codigo));
    }
}