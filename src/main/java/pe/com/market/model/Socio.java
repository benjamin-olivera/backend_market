package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "socio",
        uniqueConstraints = {
                @UniqueConstraint(name = "uq_socio_telefono", columnNames = "telefono")
        }
)
@Getter
@Setter
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private Integer id;

    @Column(nullable = false)
    private String nombre;

    private String dni;

    @Column(nullable = false)
    private String telefono;

    private Boolean estado;
}
