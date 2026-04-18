package pe.com.market.model.socio_puesto;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.model.socio.Socio;

import java.time.LocalDate;

@Entity
@Table(name = "socio_puesto")
@Getter
@Setter
public class SocioPuesto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio_puesto")
    private Integer idSocioPuesto;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_socio", nullable = false)
    private Socio socio;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_puesto", nullable = false, unique = true)
    private Puesto puesto;

    @Column(name = "fecha_asignacion")
    private LocalDate fechaAsignacion;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin; // NULL = activo, con fecha = inactivo

    @PrePersist
    public void prePersist() {
        if (fechaAsignacion == null) fechaAsignacion = LocalDate.now();
    }

    public boolean isActivo() {
        return fechaFin == null;
    }
}
