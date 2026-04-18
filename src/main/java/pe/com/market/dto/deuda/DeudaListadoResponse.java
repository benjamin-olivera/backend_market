package pe.com.market.dto.deuda;

import pe.com.market.enums.EstadoDeuda;

import java.math.BigDecimal;
import java.sql.Date;

public record DeudaListadoResponse(
        Integer idDeuda,
        String codigoPuesto,
        Integer idPuesto,
        Integer idMotivo,
        String motivoNombre,
        BigDecimal monto,
        Date fecha,
        EstadoDeuda estado,
        Integer idSocio,
        String socioNombre,
        String socioDni,
        String socioEmail
) {}
