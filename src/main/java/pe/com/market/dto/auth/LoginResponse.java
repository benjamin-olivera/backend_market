package pe.com.market.dto.auth;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {

    private String mensaje;
    private String username;
    private String rol;
}
