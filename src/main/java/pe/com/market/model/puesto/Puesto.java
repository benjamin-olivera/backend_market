package pe.com.market.model.puesto;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El código es obligatorio")
    @Size(max = 20)
    @Column(name = "codigo", nullable = false, unique = true, length = 20)
    private String codigo;

    @Size(max = 50)
    @Column(name = "sector", length = 50)
    private String sector;

    @Size(max = 10)
    @Column(name = "numero", length = 10)
    private String numero;

    @Size(max = 100)
    @Column(name = "descripcion", length = 100)
    private String descripcion;

    @Column
    private Boolean estado;

    @PrePersist
    public void prePersist() {
        if (estado == null) estado = true;
    }

}
