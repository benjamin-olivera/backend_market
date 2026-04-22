package pe.com.market.service.pago;

import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import pe.com.market.dto.pago.detalle.ItemDetalle;
import pe.com.market.model.pago.Comprobante;
import pe.com.market.model.pago.DetalleComprobante;
import pe.com.market.repository.pago.ComprobanteRepository;
import pe.com.market.repository.pago.DetalleComprobanteRepository;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ComprobantePdfService {

    private final ComprobanteRepository comprobanteRepository;
    private final DetalleComprobanteRepository detalleComprobanteRepository;

    private static final DateTimeFormatter FECHA_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    @Value("${empresa.nombre}")
    private String empresaNombre;

    @Value("${empresa.ruc}")
    private String empresaRuc;

    @Value("${empresa.direccion}")
    private String empresaDireccion;

    @Value("${empresa.telefono}")
    private String empresaTelefono;

    @Value("${empresa.logo.path:reports/assets/logo.png}")
    private String logoPath;

    public byte[] generarPdf(Integer idComprobante) {
        Comprobante c = comprobanteRepository.findById(idComprobante)
                .orElseThrow(() -> new IllegalArgumentException("Comprobante no encontrado: " + idComprobante));

        List<DetalleComprobante> detalles = detalleComprobanteRepository.findByComprobante(c);

        Map<String, Object> params = new HashMap<>();
        params.put("EMPRESA_NOMBRE", empresaNombre);
        params.put("EMPRESA_RUC", empresaRuc);
        params.put("EMPRESA_DIRECCION", empresaDireccion);
        params.put("EMPRESA_TELEFONO", empresaTelefono);

        // Header
        params.put("RECIBO_NUMERO", c.getNumero());
        params.put("FECHA_PAGO", c.getFecha().format(FECHA_FMT));
        params.put("PUESTO", c.getPuesto().getCodigo());

        // Socio (si existe en alguna deuda)
        String socio = detalles.stream()
                .map(d -> d.getDeuda().getSocio() != null ? d.getDeuda().getSocio().getNombre() : null)
                .filter(s -> s != null && !s.isBlank())
                .findFirst()
                .orElse("Asociación");
        params.put("SOCIO", socio);

        // Totales
        params.put("TOTAL", c.getTotal());
        params.put("VUELTO", c.getVuelto() != null ? c.getVuelto() : BigDecimal.ZERO);

        // Logo: pasa ruta como string (JRXML la usa como classpath)
        params.put("LOGO_PATH", logoPath);

        // Items (tabla)
        List<ItemDetalle> items = detalles.stream()
                .map(d -> new ItemDetalle(
                        d.getDeuda().getConcepto().getNombre(),
                        d.getMontoPagado()
                ))
                .toList();

        JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(items);

        try (InputStream jrxml = new ClassPathResource("reports/comprobante_pago.jrxml").getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(jrxml);
            JasperPrint print = JasperFillManager.fillReport(report, params, ds);
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception e) {
            // deja el mensaje claro
            throw new RuntimeException("Error generando PDF Jasper: " + e.getMessage(), e);
        }
    }
}
