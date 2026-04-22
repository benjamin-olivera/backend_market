package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.pago.ComprobanteResponse;
import pe.com.market.dto.pago.PagoListadoDto;
import pe.com.market.dto.pago.PagoRequest;
import pe.com.market.service.pago.ComprobantePdfService;
import pe.com.market.service.pago.PagoService;

import java.time.LocalDate;

@RestController
@RequestMapping("/pagos")
@RequiredArgsConstructor
public class PagoController {

    private final PagoService pagoService;
    private final ComprobantePdfService comprobantePdfService;

    @GetMapping
    public ResponseEntity<Page<PagoListadoDto>> listarPagos(
            @RequestParam(required = false) String q,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(pagoService.listar(q, from, to, page, size));
    }

    @GetMapping("/{id}/comprobante.pdf")
    public ResponseEntity<byte[]> descargarComprobantePdf(
            @PathVariable Integer id,
            @RequestParam(defaultValue = "false") boolean download
    ) {
        byte[] pdf = comprobantePdfService.generarPdf(id);

        String dispositionType = download ? "attachment" : "inline";
        String filename = "comprobante-" + id + ".pdf";

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, dispositionType + "; filename=\"" + filename + "\"")
                .body(pdf);
    }

    @PostMapping
    public ResponseEntity<ComprobanteResponse> registrarPago(
            @Valid @RequestBody PagoRequest request) {

        ComprobanteResponse comprobante = pagoService.registrarPago(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(comprobante);
    }
}
