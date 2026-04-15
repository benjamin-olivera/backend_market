package pe.com.market.dto.pago;

import java.util.List;

public class PaginaRespuesta<T> {
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;
    private List<T> data;
}
