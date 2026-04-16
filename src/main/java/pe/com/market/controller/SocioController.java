// pe/com/market/controller/SocioController.java
package pe.com.market.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.socio.SocioBusquedaResponse;
import pe.com.market.dto.socio.SocioDTO;
import pe.com.market.dto.socio.SocioResponse;
import pe.com.market.model.socio.Socio;
import pe.com.market.service.socio.SocioService;

import java.util.List;

@RestController
@RequestMapping("/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService service;

    @GetMapping
    public ResponseEntity<List<Socio>> listar() {
        return ResponseEntity.ok(service.listarActivos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Socio> obtener(@PathVariable Integer id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Socio> crear(@Valid @RequestBody SocioDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Socio> actualizar(@PathVariable Integer id,
                                            @Valid @RequestBody SocioDTO dto) {
        return ResponseEntity.ok(service.actualizar(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        service.eliminarLogico(id);
        return ResponseEntity.noContent().build();
    }

    // /api/socios/buscar-por-dni?dni=70 (autocomplete)
    @GetMapping("/buscar-por-dni")
    public ResponseEntity<List<SocioBusquedaResponse>> buscarPorDniPrefix(
            @RequestParam("dni") String dniPrefix
    ) {
        return ResponseEntity.ok(service.buscarPorDniPrefix(dniPrefix));
    }

    // /api/socios/{dni} (detalle completo)
    @GetMapping("/dni/{dni}")
    public ResponseEntity<SocioResponse> obtenerPorDni(@PathVariable String dni) {
        return ResponseEntity.ok(service.obtenerSocioPorDni(dni));
    }
}