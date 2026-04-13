package pe.com.market.model.puesto;

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
    private Integer idPuesto;

    @Column(nullable = false, length = 20, unique = true)
    private String codigo;

    @Column(length = 100)
    private String descripcion;

    @Column
    private Boolean estado;

    @Column(name = "es_propiedad_asociacion", nullable = false)
    private Boolean esPropiedadAsociacion = false;
}
