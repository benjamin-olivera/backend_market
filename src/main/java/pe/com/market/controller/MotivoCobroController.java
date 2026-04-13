package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.model.deuda.MotivoCobro;
import pe.com.market.repository.deuda.MotivoCobroRepository;

import java.util.List;

@RestController
@RequestMapping("/api/mercado/motivos")
@RequiredArgsConstructor
public class MotivoCobroController {

    private final MotivoCobroRepository motivoCobroRepository;

    @GetMapping
    public ResponseEntity<List<MotivoCobro>> listar() {
        return ResponseEntity.ok(motivoCobroRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<MotivoCobro> crear(@Valid @RequestBody MotivoCobro motivo) {
        MotivoCobro guardado = motivoCobroRepository.save(motivo);
        return ResponseEntity.ok(guardado);
    }
}
