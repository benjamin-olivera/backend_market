package pe.com.market.model.socio;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import pe.com.market.model.puesto.Puesto;

import java.time.LocalDateTime;

@Entity
@Table(name = "socio")
@Getter
@Setter
public class Socio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_socio")
    private Integer idSocio;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 8)
    private String dni;

    @Column(nullable = false, length = 20, unique = true)
    private String telefono;

    @Column
    private Boolean estado = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;
}
