package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "puesto")
@Getter
@Setter
public class Puesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_puesto")
    private Integer id;

    @Column(nullable = false, unique = true)
    private String codigo;

    private String descripcion;

    private Boolean estado;

    @Column(name = "es_propiedad_asociacion", nullable = false)
    private Boolean esPropiedadAsociacion = Boolean.FALSE;
}
