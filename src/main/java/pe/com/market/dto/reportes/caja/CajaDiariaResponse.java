package pe.com.market.dto.reportes.caja;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CajaDiariaResponse {
    private String fecha;
    private CajaDiariaCards cards = new CajaDiariaCards();
    private List<CajaDiariaItem> items = new ArrayList<>();
}
