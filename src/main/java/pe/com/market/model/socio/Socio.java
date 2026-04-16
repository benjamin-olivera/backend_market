package pe.com.market.model.socio;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 100)
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;

    @NotBlank(message = "El DNI es obligatorio")
    @Size(min = 8, max = 15)
    @Column(name = "dni", nullable = false, unique = true, length = 15)
    private String dni;

    @NotBlank(message = "El teléfono es obligatorio")
    @Size(max = 20)
    @Column(name = "telefono", nullable = false, unique = true, length = 20)
    private String telefono;

    @Size(max = 200)
    @Column(name = "direccion", length = 200)
    private String direccion;

    @Size(max = 100)
    @Column(name = "email", length = 100)
    private String email;

    @Column(name = "estado", nullable = false)
    private Boolean estado = true;

    @Column(name = "fecha_creacion", updatable = false)
    private LocalDateTime fechaCreacion;

    @PrePersist
    public void prePersist() {
        if (estado == null) estado = true;
        if (fechaCreacion == null) fechaCreacion = LocalDateTime.now();
    }
}
