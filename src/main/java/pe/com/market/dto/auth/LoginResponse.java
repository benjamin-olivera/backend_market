package pe.com.market.dto.auth;

import java.util.List;

public record LoginResponse(
        String token,
        String username,
        List<String> roles) {

}
