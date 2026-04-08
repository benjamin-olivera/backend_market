package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Date;

@Entity
@Table(name = "deuda")
@Getter
@Setter
public class Deuda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_deuda")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_puesto", nullable = false)
    private Puesto puesto;

    @ManyToOne
    @JoinColumn(name = "id_motivo", nullable = false)
    private MotivoCobro motivo;

    @Column(nullable = false)
    private BigDecimal monto;

    @Column(nullable = false)
    private Date fecha;

    @Column(nullable = false, length = 20)
    private String estado;

    @Column(name = "fecha_pago")
    private Date fechaPago;
}
