package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "socio_puesto")
@Getter
@Setter
public class SocioPuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio_puesto")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    @OneToOne
    @JoinColumn(name = "id_puesto", nullable = false, unique = true)
    private Puesto puesto;

    @Column(name = "fecha_asignacion")
    private java.sql.Date fechaAsignacion;
}
