package pe.com.market.controller;

import org.springframework.web.bind.annotation.*;
import pe.com.market.model.MotivoCobro;
import pe.com.market.repository.MotivoCobroRepository;

import java.util.List;

@RestController
@RequestMapping("/api/mercado/motivos")
public class MotivoCobroController {

    private final MotivoCobroRepository repo;

    public MotivoCobroController(MotivoCobroRepository repo) {
        this.repo = repo;
    }

    @PostMapping
    public MotivoCobro crear(@RequestBody MotivoCobro m) {
        return repo.save(m);
    }

    @GetMapping
    public List<MotivoCobro> listar() {
        return repo.findAll();
    }
}
