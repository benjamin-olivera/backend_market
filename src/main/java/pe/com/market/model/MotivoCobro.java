package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "motivo_cobro")
@Getter
@Setter
public class MotivoCobro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motivo")
    private Integer id;

    @Column(nullable = false)
    private String descripcion;
}
