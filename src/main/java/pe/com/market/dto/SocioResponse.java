package pe.com.market.dto;

import lombok.Data;

@Data
public class SocioResponse {
    private Integer id;
    private String nombre;
    private String dni;
    private String telefono;
    private Boolean estado;
}
