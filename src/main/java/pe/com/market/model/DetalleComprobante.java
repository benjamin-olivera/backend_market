package pe.com.market.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "detalle_comprobante")
@Getter
@Setter
public class DetalleComprobante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_comprobante", nullable = false)
    private Comprobante comprobante;

    @ManyToOne
    @JoinColumn(name = "id_deuda", nullable = false)
    private Deuda deuda;

    @Column(name = "monto_pagado", nullable = false)
    private BigDecimal montoPagado;
}
