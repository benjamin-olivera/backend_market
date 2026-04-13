package pe.com.market.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import pe.com.market.security.JwtService;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails user = (UserDetails) authentication.getPrincipal();

        List<String> roles = user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)  // "ROLE_ADMIN"
                .map(r -> r.replace("ROLE_", ""))     // "ADMIN"
                .toList();

        String token = jwtService.generateToken(user, roles);

        return ResponseEntity.ok(new LoginResponse(
                token,
                user.getUsername(),
                roles
        ));
    }

    // DTOs simples como records (puedes ponerlos en otro paquete si prefieres)

    public record LoginRequest(String username, String password) {}

    public record LoginResponse(String token, String username, List<String> roles) {}
}