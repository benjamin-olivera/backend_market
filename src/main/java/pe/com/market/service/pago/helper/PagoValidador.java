package pe.com.market.service.pago.helper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pe.com.market.dto.deuda.DeudaPagoItem;
import pe.com.market.enums.EstadoDeuda;
import pe.com.market.model.deuda.Deuda;
import pe.com.market.model.puesto.Puesto;
import pe.com.market.repository.deuda.DeudaRepository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class PagoValidador {

    private final DeudaRepository deudaRepository;

    public DeudaValidationResult validarDeudas(
            List<DeudaPagoItem> items,
            Puesto puesto
    ) {
        BigDecimal totalDeudas = BigDecimal.ZERO;
        List<Deuda> deudas = new ArrayList<>();

        System.out.println("===== Iniciando validación de deudas =====");
        System.out.println("Puesto seleccionado -> idPuesto=" + puesto.getIdPuesto()
                + ", codigo=" + puesto.getCodigo());
        System.out.println("Items recibidos (ids de deuda y montos):");
        for (DeudaPagoItem it : items) {
            System.out.println("  * item.idDeuda=" + it.getIdDeuda()
                    + ", item.montoPagado=" + it.getMontoPagado());
        }
        System.out.println("===========================================");

        for (DeudaPagoItem item : items) {

            // 1) Cargar deuda desde BD
            Deuda deuda = deudaRepository.findById(item.getIdDeuda())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "La deuda " + item.getIdDeuda() + " no existe."));

            Integer idDeuda = deuda.getIdDeuda();
            Integer idPuestoDeuda = deuda.getPuesto().getIdPuesto();

            System.out.println("Validando deuda " + idDeuda
                    + " -> id_puesto BD=" + idPuestoDeuda
                    + ", estado=" + deuda.getEstado()
                    + ", monto BD=" + deuda.getMonto());

            // 2) Estado pendiente
            if (deuda.getEstado() != EstadoDeuda.PENDIENTE) {
                System.out.println(">> ERROR: deuda " + idDeuda
                        + " no está en estado PENDIENTE (estado actual: "
                        + deuda.getEstado() + ")");
                throw new IllegalStateException(
                        "La deuda " + deuda.getIdDeuda() + " no está pendiente.");
            }

            // 3) Pertenencia al puesto
            if (!idPuestoDeuda.equals(puesto.getIdPuesto())) {
                System.out.println(">> ERROR: deuda " + idDeuda
                        + " pertenece al puesto " + idPuestoDeuda
                        + " pero puesto seleccionado es " + puesto.getIdPuesto()
                        + " (codigo seleccionado: " + puesto.getCodigo() + ")");
                throw new IllegalArgumentException(
                        "La deuda " + deuda.getIdDeuda()
                                + " no pertenece al puesto seleccionado.");
            }

            // 4) Monto exacto (no parciales)
            if (item.getMontoPagado() == null
                    || item.getMontoPagado().compareTo(deuda.getMonto()) != 0) {
                System.out.println(">> ERROR: monto pagado para deuda " + idDeuda
                        + " es " + item.getMontoPagado()
                        + " pero monto BD es " + deuda.getMonto());
                throw new IllegalArgumentException(
                        "El monto pagado de la deuda " + deuda.getIdDeuda()
                                + " debe ser igual al monto de la deuda (no se permiten pagos parciales).");
            }

            totalDeudas = totalDeudas.add(item.getMontoPagado());
            deudas.add(deuda);
        }

        System.out.println("=== Validación de deudas OK ===");
        System.out.println("Total de deudas a pagar: " + totalDeudas);
        System.out.println("================================\n");

        return new DeudaValidationResult(totalDeudas, deudas);
    }

    public record DeudaValidationResult(BigDecimal totalDeudas, List<Deuda> deudas) {}
}