package pe.com.market.service.reportes;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.market.dto.pago.PagoConceptoRow;
import pe.com.market.dto.reportes.caja.*;
import pe.com.market.enums.Metodo;
import pe.com.market.repository.pago.PagoRepository;
import pe.com.market.repository.reportes.ReporteCajaRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReporteCajaService {

    private final ReporteCajaRepository reporteCajaRepository;
    private final PagoRepository pagoRepository;

    private static final DateTimeFormatter HORA_FMT = DateTimeFormatter.ofPattern("HH:mm");

    public CajaDiariaResponse obtenerCajaDiaria(LocalDate fecha) {
        LocalDate f = (fecha != null) ? fecha : LocalDate.now();

        List<CajaComprobanteRow> comprobantes = reporteCajaRepository.listarComprobantesDelDia(f);
        List<Integer> ids = comprobantes.stream().map(CajaComprobanteRow::getIdComprobante).toList();

        // conceptos por comprobante
        Map<Integer, List<String>> conceptosPorId = new HashMap<>();
        if (!ids.isEmpty()) {
            List<PagoConceptoRow> rows = pagoRepository.listarConceptosPorComprobantes(ids);
            for (PagoConceptoRow r : rows) {
                conceptosPorId.computeIfAbsent(r.getIdComprobante(), k -> new ArrayList<>())
                        .add(r.getConcepto());
            }
        }

        // métodos por comprobante
        Map<Integer, List<Metodo>> metodosPorId = new HashMap<>();
        if (!ids.isEmpty()) {
            List<CajaMetodoRow> metodoRows = reporteCajaRepository.listarMetodosPorComprobanteDelDia(f);
            for (CajaMetodoRow r : metodoRows) {
                metodosPorId.computeIfAbsent(r.getIdComprobante(), k -> new ArrayList<>())
                        .add(r.getMetodo());
            }
        }

        // cards
        CajaDiariaCards cards = new CajaDiariaCards();
        BigDecimal total = safe(reporteCajaRepository.totalRecaudadoDelDia(f));
        cards.setTotalRecaudado(total);

        EnumMap<Metodo, BigDecimal> porMetodo = new EnumMap<>(Metodo.class);
        for (Metodo m : Metodo.values()) porMetodo.put(m, BigDecimal.ZERO);

        for (CajaMetodoTotalRow r : reporteCajaRepository.sumarPorMetodoDelDia(f)) {
            porMetodo.put(r.getMetodo(), safe(r.getMonto()));
        }

        cards.setPorMetodo(porMetodo);

        BigDecimal efectivo = porMetodo.getOrDefault(Metodo.EFECTIVO, BigDecimal.ZERO);
        BigDecimal digitales = total.subtract(efectivo);
        if (digitales.compareTo(BigDecimal.ZERO) < 0) digitales = BigDecimal.ZERO;

        cards.setIngresosEfectivo(efectivo);
        cards.setIngresosDigitales(digitales);

        // items (tabla)
        List<CajaDiariaItem> items = new ArrayList<>();
        for (CajaComprobanteRow c : comprobantes) {
            CajaDiariaItem it = new CajaDiariaItem();
            it.setIdRecibo(c.getNumero());
            it.setPuesto(c.getPuestoCodigo());
            it.setMonto(safe(c.getTotal()));
            it.setHora(c.getFecha() != null ? c.getFecha().format(HORA_FMT) : "");

            List<Metodo> metodos = metodosPorId.getOrDefault(c.getIdComprobante(), List.of());
            it.setMetodoPago(resumirMetodoPago(metodos));

            it.setConceptos(conceptosPorId.getOrDefault(c.getIdComprobante(), List.of()));
            items.add(it);
        }

        CajaDiariaResponse resp = new CajaDiariaResponse();
        resp.setFecha(f.toString());
        resp.setCards(cards);
        resp.setItems(items);
        return resp;
    }

    private String resumirMetodoPago(List<Metodo> metodos) {
        if (metodos == null || metodos.isEmpty()) return "N/A";
        Set<Metodo> unique = new LinkedHashSet<>(metodos);
        if (unique.size() == 1) return unique.iterator().next().name();
        return "MIXTO";
    }

    private BigDecimal safe(BigDecimal v) {
        return v != null ? v : BigDecimal.ZERO;
    }
}