package pe.com.market.dto;

import lombok.Data;

@Data
public class PuestoResponse {
    private Integer id;
    private String codigo;
    private String descripcion;
    private Boolean estado;
    private Integer idSocio;
    private String nombreSocio;
}
