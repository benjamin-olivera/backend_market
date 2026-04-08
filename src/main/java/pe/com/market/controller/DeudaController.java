package pe.com.market.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.DistribuirDeudaRequest;
import pe.com.market.dto.DistribuirDeudaSeleccionadosRequest;
import pe.com.market.dto.MismaDeudaRequest;
import pe.com.market.service.ConsultaDeudaService;
import pe.com.market.service.DeudaService;

import java.util.Map;

@RestController
@RequestMapping("/api/mercado/deudas")
public class DeudaController {

    private final DeudaService deudaService;
    private final ConsultaDeudaService consultaDeudaService;

    public DeudaController(DeudaService deudaService,
                           ConsultaDeudaService consultaDeudaService) {
        this.deudaService = deudaService;
        this.consultaDeudaService = consultaDeudaService;
    }

    // MODO 1: distribuir entre TODOS
    @PostMapping("/distribuir")
    public ResponseEntity<?> distribuir(@RequestBody DistribuirDeudaRequest request) {
        var resultado = deudaService.distribuirDeudaTodos(request);

        return ResponseEntity.ok(
                Map.of(
                        "mensaje", "Deuda distribuida entre " + resultado.getCantidadPuestos() + " puestos",
                        "cantidadPuestos", resultado.getCantidadPuestos(),
                        "montoPorPuesto", resultado.getMontoPorPuesto()
                )
        );
    }

    // MODO 2: distribuir entre SELECCIONADOS
    @PostMapping("/distribuir/seleccionados")
    public ResponseEntity<?> distribuirSeleccionados(@RequestBody DistribuirDeudaSeleccionadosRequest request) {
        var resultado = deudaService.distribuirDeudaSeleccionados(request);

        return ResponseEntity.ok(
                Map.of(
                        "tipo", "DISTRIBUIR_SELECCIONADOS",
                        "cantidadPuestos", resultado.getCantidadPuestos(),
                        "montoPorPuesto", resultado.getMontoPorPuesto()
                )
        );
    }

    // MODO 3 y 4: MISMO monto (todos o seleccionados)
    @PostMapping("/misma")
    public ResponseEntity<?> mismaDeuda(@RequestBody MismaDeudaRequest request) {
        var resultado = deudaService.mismaDeuda(request);

        return ResponseEntity.ok(
                Map.of(
                        "tipo", (request.getCodigosPuestos() == null || request.getCodigosPuestos().isEmpty())
                                ? "MISMA_TODOS"
                                : "MISMA_SELECCIONADOS",
                        "cantidadPuestos", resultado.getCantidadPuestos(),
                        "montoPorPuesto", resultado.getMontoPorPuesto()
                )
        );
    }

    @GetMapping("/puesto/{codigo}")
    public ResponseEntity<?> deudasPorPuesto(@PathVariable String codigo) {
        var r = consultaDeudaService.obtenerDeudasPendientes(codigo);
        return ResponseEntity.ok(r);
    }
}