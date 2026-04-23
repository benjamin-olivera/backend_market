package pe.com.market.service.reportes;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import pe.com.market.dto.reportes.caja.CajaDiariaItem;
import pe.com.market.dto.reportes.caja.CajaDiariaResponse;

import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;

@Service
public class ReporteCajaExportService {

    public byte[] toCsv(CajaDiariaResponse data) {
        StringBuilder sb = new StringBuilder();
        sb.append("recibo,puesto,hora,metodo_pago,monto,conceptos\n");

        for (CajaDiariaItem it : data.getItems()) {
            String conceptos = String.join(" | ", it.getConceptos());
            sb.append(csv(it.getIdRecibo())).append(',')
                    .append(csv(it.getPuesto())).append(',')
                    .append(csv(it.getHora())).append(',')
                    .append(csv(it.getMetodoPago())).append(',')
                    .append(it.getMonto() != null ? it.getMonto() : "").append(',')
                    .append(csv(conceptos))
                    .append('\n');
        }

        return sb.toString().getBytes(StandardCharsets.UTF_8);
    }

    public byte[] toXlsx(CajaDiariaResponse data) {
        try (Workbook wb = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {

            // Hoja 1: Resumen
            Sheet resumen = wb.createSheet("Resumen");
            int r = 0;

            r = writeKV(resumen, r, "Fecha", data.getFecha());
            r = writeKV(resumen, r, "Total recaudado", String.valueOf(data.getCards().getTotalRecaudado()));
            r = writeKV(resumen, r, "Ingresos efectivo", String.valueOf(data.getCards().getIngresosEfectivo()));
            r = writeKV(resumen, r, "Ingresos digitales", String.valueOf(data.getCards().getIngresosDigitales()));

            r++; // línea en blanco

            // Totales por método
            Row header = resumen.createRow(r++);
            header.createCell(0).setCellValue("Método");
            header.createCell(1).setCellValue("Monto");

            // IMPORTANTE: usar for normal (no lambda) para poder hacer r++
            for (var entry : data.getCards().getPorMetodo().entrySet()) {
                var metodo = entry.getKey();
                var monto = entry.getValue();

                Row row = resumen.createRow(r++);
                row.createCell(0).setCellValue(String.valueOf(metodo));
                row.createCell(1).setCellValue(monto != null ? monto.doubleValue() : 0d);
            }

            resumen.autoSizeColumn(0);
            resumen.autoSizeColumn(1);

            // Hoja 2: Transacciones
            Sheet tx = wb.createSheet("Transacciones");
            Row h = tx.createRow(0);
            h.createCell(0).setCellValue("Recibo");
            h.createCell(1).setCellValue("Puesto");
            h.createCell(2).setCellValue("Hora");
            h.createCell(3).setCellValue("Método");
            h.createCell(4).setCellValue("Monto");
            h.createCell(5).setCellValue("Conceptos");

            int i = 1;
            for (CajaDiariaItem it : data.getItems()) {
                Row row = tx.createRow(i++);
                row.createCell(0).setCellValue(it.getIdRecibo());
                row.createCell(1).setCellValue(it.getPuesto());
                row.createCell(2).setCellValue(it.getHora());
                row.createCell(3).setCellValue(it.getMetodoPago());
                row.createCell(4).setCellValue(it.getMonto() != null ? it.getMonto().doubleValue() : 0d);
                row.createCell(5).setCellValue(String.join(" | ", it.getConceptos()));
            }

            for (int c = 0; c < 6; c++) tx.autoSizeColumn(c);

            wb.write(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error generando XLSX: " + e.getMessage(), e);
        }
    }

    private int writeKV(Sheet s, int r, String k, String v) {
        Row row = s.createRow(r);
        row.createCell(0).setCellValue(k);
        row.createCell(1).setCellValue(v != null ? v : "");
        return r + 1;
    }

    private String csv(String v) {
        if (v == null) return "";
        String escaped = v.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }
}