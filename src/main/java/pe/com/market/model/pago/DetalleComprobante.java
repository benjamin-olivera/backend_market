package pe.com.market.model.pago;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.model.deuda.Deuda;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_comprobante")
@Getter
@Setter
public class DetalleComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer idDetalle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_comprobante", nullable = false)
    private Comprobante comprobante;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_deuda", nullable = false)
    private Deuda deuda;

    @Column(name = "monto_pagado", nullable = false)
    private BigDecimal montoPagado;
}