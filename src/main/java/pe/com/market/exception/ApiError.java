package pe.com.market.exception;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ApiError {
    private String error;
    private List<String> detalles;
    private LocalDateTime fecha;
}
