package pe.com.market.service.pago.helper;

import org.springframework.stereotype.Component;
import pe.com.market.dto.pago.MetodoPagoRequest;
import pe.com.market.enums.Metodo;

import java.math.BigDecimal;
import java.util.List;

@Component
public class TotalesPagoHelper {

    public BigDecimal calcularTotalMetodos(List<MetodoPagoRequest> metodos) {
        return metodos.stream()
                .map(MetodoPagoRequest::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularTotalEfectivo(List<MetodoPagoRequest> metodos) {
        return metodos.stream()
                .filter(mp -> mp.getMetodo() == Metodo.EFECTIVO)
                .map(MetodoPagoRequest::getMonto)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calcularVuelto(BigDecimal totalDeudas, BigDecimal totalEfectivo) {
        if (totalEfectivo.compareTo(totalDeudas) > 0) {
            return totalEfectivo.subtract(totalDeudas);
        }
        return BigDecimal.ZERO;
    }
}