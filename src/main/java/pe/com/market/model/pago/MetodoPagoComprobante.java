package pe.com.market.model.pago;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.enums.Metodo;

import java.math.BigDecimal;

@Entity
@Table(name = "metodo_pago_comprobante")
@Getter
@Setter
public class MetodoPagoComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metodo_pago")
    private Integer idMetodoPagoComprobante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comprobante", nullable = false)
    private Comprobante comprobante;

    @Enumerated(EnumType.STRING)
    @Column(name = "metodo_pago", nullable = false)
    private Metodo metodo;

    @Column(name = "monto", nullable = false)
    private BigDecimal monto;
}