package pe.com.market.model.deuda;

import jakarta.persistence.*;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.model.concepto.MotivoCobro;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.socio.Socio;

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
    private Integer idDeuda;

    @ManyToOne
    @JoinColumn(name = "id_puesto", nullable = false)
    private Puesto puesto;

    @ManyToOne
    @JoinColumn(name = "id_motivo", nullable = false)
    private MotivoCobro motivo;

    @ManyToOne
    @JoinColumn(name = "id_socio")
    private Socio socio;

    @Column(name = "monto", nullable = false, precision = 10, scale = 2)
    @Positive
    private BigDecimal monto;

    @Column(name = "fecha", nullable = false)
    private Date fecha;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 10)
    private EstadoDeuda estado;

    @Column(name = "fecha_pago")
    private Date fechaPago;
}