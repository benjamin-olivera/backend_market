package pe.com.market.model.pago;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.model.auth.Usuario;
import pe.com.market.model.puesto.Puesto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "comprobante")
@Getter
@Setter
public class Comprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_comprobante")
    private Long idComprobante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_puesto", nullable = false)
    private Puesto puesto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @Column(name = "fecha", nullable = false)
    private LocalDateTime fecha;

    @Column(name = "total", nullable = false)
    private BigDecimal total;

    @Column(name = "numero", nullable = false, unique = true)
    private String numero;

    @Column(name = "tipo", nullable = false)
    private String tipo; // "RECIBO", etc.

    @Column(name = "vuelto", nullable = false)
    private BigDecimal vuelto;

    @OneToMany(mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DetalleComprobante> detalles;

    @OneToMany(mappedBy = "comprobante", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MetodoPagoComprobante> metodosPago;
}