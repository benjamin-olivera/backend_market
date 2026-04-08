package pe.com.market.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.market.dto.LoginRequest;
import pe.com.market.dto.LoginResponse;
import pe.com.market.exception.ApiError;
import pe.com.market.model.Rol;
import pe.com.market.model.Usuario;
import pe.com.market.service.UsuarioService;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private UsuarioService service;

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {

        Usuario user = service.login(request.getUsername(), request.getPassword());

        if (user != null) {

            String rol = user.getRoles()
                    .stream()
                    .findFirst()
                    .map(Rol::getNombre)
                    .orElse("SIN_ROL");

            return ResponseEntity.ok(
                    new LoginResponse("login exitoso", user.getUsername(), rol)
            );
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new ApiError("Error de autenticación", List.of("Credenciales incorrectas")));
        }
    }
}
