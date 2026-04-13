package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.pago.ComprobanteResponse;
import pe.com.market.dto.pago.PagoRequest;
import pe.com.market.service.pago.PagoService;

@RestController
@RequestMapping("/api/mercado/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;

    @PostMapping
    public ResponseEntity<ComprobanteResponse> registrarPago(
            @Valid @RequestBody PagoRequest request) {

        ComprobanteResponse comprobante = pagoService.registrarPago(request);
        return ResponseEntity.ok(comprobante);
    }
}
