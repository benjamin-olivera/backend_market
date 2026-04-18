package pe.com.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.concepto.MotivoCobroResponse;
import pe.com.market.service.concepto.MotivoCobroService;

import java.util.List;

@RestController
@RequestMapping("/motivos-cobro")
@RequiredArgsConstructor
public class MotivoCobroController {

    private final MotivoCobroService service;

    @GetMapping
    public ResponseEntity<List<MotivoCobroResponse>> listarActivos() {
        return ResponseEntity.ok(service.listarActivos());
    }
}