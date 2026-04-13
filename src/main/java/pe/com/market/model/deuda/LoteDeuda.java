package pe.com.market.model.deuda;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "lote_deuda")
@Getter
@Setter
public class LoteDeuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_lote")
    private Integer id;

    @Column(length = 100)
    private String descripcion;

    @Column(nullable = false)
    private LocalDate fecha;
}
