package pe.com.market.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.ComprobanteResponse;
import pe.com.market.dto.PagoRequest;
import pe.com.market.dto.TransaccionDTO;
import pe.com.market.service.PagoService;
import pe.com.market.service.ReportePagosService;

import java.util.List;

@RestController
@RequestMapping("/api/mercado/pagos")
public class PagoController {

    private final PagoService pagoService;
    private final ReportePagosService reportePagosService;

    public PagoController(PagoService pagoService, ReportePagosService reportePagosService) {
        this.pagoService = pagoService;
        this.reportePagosService = reportePagosService;
    }

    @GetMapping("/historial")
    public List<TransaccionDTO> historialPagos() {
        return reportePagosService.obtenerHistorialPagos();
    }

    @PostMapping("/puesto/{codigo}/cancelar-todo")
    public ResponseEntity<ComprobanteResponse> pagarTodo(@PathVariable String codigo,
                                                         @RequestBody PagoRequest request) {
        ComprobanteResponse resp = pagoService.pagarTodoPorPuesto(codigo, request);
        return ResponseEntity.ok(resp);
    }
}
