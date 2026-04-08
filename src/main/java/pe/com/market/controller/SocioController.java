package pe.com.market.controller;

import org.springframework.web.bind.annotation.*;
import pe.com.market.model.Socio;
import pe.com.market.repository.SocioRepository;

import java.util.List;

@RestController
@RequestMapping("/api/mercado/socios")
public class SocioController {

    private final SocioRepository socioRepository;

    public SocioController(SocioRepository socioRepository) {
        this.socioRepository = socioRepository;
    }

    @GetMapping
    public List<Socio> listar() {
        return socioRepository.findAll();
    }

    @PostMapping
    public Socio crear(@RequestBody Socio socio) {
        socio.setEstado(true);
        return socioRepository.save(socio);
    }
}
