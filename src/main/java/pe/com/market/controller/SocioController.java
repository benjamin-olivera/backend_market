// pe/com/market/controller/SocioController.java
package pe.com.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pe.com.market.dto.socio.SocioBusquedaResponse;
import pe.com.market.dto.socio.SocioResponse;
import pe.com.market.service.socio.SocioService;

import java.util.List;

@RestController
@RequestMapping("/api/socios")
@RequiredArgsConstructor
public class SocioController {

    private final SocioService socioService;

    // /api/socios/buscar-por-dni?dni=70 (autocomplete)
    @GetMapping("/buscar-por-dni")
    public List<SocioBusquedaResponse> buscarPorDniPrefix(
            @RequestParam("dni") String dniPrefix
    ) {
        return socioService.buscarPorDniPrefix(dniPrefix);
    }

    // /api/socios/{dni} (detalle completo)
    @GetMapping("/{dni}")
    public SocioResponse obtenerPorDni(@PathVariable String dni) {
        return socioService.obtenerSocioPorDni(dni);
    }
}