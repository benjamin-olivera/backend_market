package pe.com.market.dto.puesto;

import lombok.Data;

@Data
public class PuestoResponse {

    private Integer id;
    private String codigo;
    private String descripcion;
    private Boolean estado;
    private Boolean esPropiedadAsociacion;
}
