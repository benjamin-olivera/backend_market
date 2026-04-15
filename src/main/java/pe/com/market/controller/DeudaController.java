package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.deuda.DeudaResponse;
import pe.com.market.dto.deuda.DistribuirDeudaRequest;
import pe.com.market.dto.deuda.MismaDeudaRequest;
import pe.com.market.service.deuda.DeudaService;

import java.util.List;

@RestController
@RequestMapping("/deudas")
@RequiredArgsConstructor
public class DeudaController {

    private final DeudaService deudaService;

    // ================== GENERACIÓN ==================
    @PostMapping("/distribuir")
    public ResponseEntity<Void> generarDeudaDistribuida(
            @Valid @RequestBody DistribuirDeudaRequest request) {

        deudaService.generarDeudaDistribuida(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/misma")
    public ResponseEntity<Void> generarMismaDeuda(
            @Valid @RequestBody MismaDeudaRequest request) {

        deudaService.generarMismaDeuda(request);
        return ResponseEntity.ok().build();
    }

    // ================== CONSULTA ==================
    // Opcional: TODAS las deudas de un puesto (pendientes + pagadas)
    @GetMapping("/puesto/{codigo}")
    public List<DeudaResponse> listarTodasPorPuesto(@PathVariable String codigo) {
        return deudaService.listarTodasPorPuesto(codigo);
    }

    @GetMapping("/puesto/{codigo}/pendientes")
    public List<DeudaResponse> listarPendientesPorPuesto(@PathVariable String codigo) {
        return deudaService.listarPendientesPorPuesto(codigo);
    }
}