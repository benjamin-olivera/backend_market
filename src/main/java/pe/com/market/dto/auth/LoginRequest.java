package pe.com.market.dto.auth;


public record LoginRequest(
        String username,
        String password) {

}