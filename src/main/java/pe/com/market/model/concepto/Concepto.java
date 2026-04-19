package pe.com.market.model.concepto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.enums.EstadoConcepto;

import java.time.LocalDateTime;

@Entity
@Table(name = "motivo_cobro")
@Getter
@Setter
public class Concepto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_motivo")
    private Integer idMotivo;

    @Column(name = "nombre", unique = true, nullable = false, length = 100)
    private String nombre;

    @Column(name = "descripcion", length = 255)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "estado", nullable = false, length = 15)
    private EstadoConcepto estado = EstadoConcepto.ACTIVO;

    @Column(name = "fecha_creacion")
    private LocalDateTime fechaCreacion;
}
