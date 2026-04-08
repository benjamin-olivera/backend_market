package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "metodo_pago_comprobante")
@Getter
@Setter
public class MetodoPagoComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_comprobante", nullable = false)
    private Comprobante comprobante;

    @Column(name = "metodo_pago", nullable = false, length = 20)
    private String metodoPago; // EFECTIVO, VISA, YAPE, etc.

    @Column(nullable = false)
    private BigDecimal monto;
}
