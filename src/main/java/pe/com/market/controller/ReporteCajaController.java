package pe.com.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.reportes.caja.CajaDiariaResponse;
import pe.com.market.service.reportes.ReporteCajaExportService;
import pe.com.market.service.reportes.ReporteCajaService;

import java.time.LocalDate;

@RestController
@RequestMapping("/reportes/caja")
@RequiredArgsConstructor
public class ReporteCajaController {

    private final ReporteCajaService reporteCajaService;
    private final ReporteCajaExportService exportService;

    @GetMapping("/diario")
    public ResponseEntity<CajaDiariaResponse> cajaDiaria(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        return ResponseEntity.ok(reporteCajaService.obtenerCajaDiaria(fecha));
    }

    @GetMapping("/diario.csv")
    public ResponseEntity<byte[]> cajaDiariaCsv(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        CajaDiariaResponse data = reporteCajaService.obtenerCajaDiaria(fecha);
        byte[] bytes = exportService.toCsv(data);

        String filename = "caja-diaria-" + data.getFecha() + ".csv";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("text/csv; charset=utf-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }

    @GetMapping("/diario.xlsx")
    public ResponseEntity<byte[]> cajaDiariaXlsx(
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fecha
    ) {
        CajaDiariaResponse data = reporteCajaService.obtenerCajaDiaria(fecha);
        byte[] bytes = exportService.toXlsx(data);

        String filename = "caja-diaria-" + data.getFecha() + ".xlsx";

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(bytes);
    }
}
