package org.futurepages.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;

import org.futurepages.core.persistence.Dao;

/**
 *
 * @author angelo
 */
public class PDFReportsUtil {
		

	public static OutputStream createPDFReport(String fileName, InputStream inputStream, Map<String, Object> parametros, Connection conexao, HttpServletResponse response) throws JRException, IOException {
		JasperPrint report = JasperFillManager.fillReport(inputStream,parametros,Dao.getInstance().session().connection());
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition","inline; filename="+fileName);
		OutputStream out = response.getOutputStream();				
        JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
		return out;
    }

	public static OutputStream createPDFReport(String fileName, InputStream inputStream, Map<String, Object> parametros, List lista, HttpServletResponse response) throws JRException, IOException {
		JRDataSource bancoLimpo = new JRBeanCollectionDataSource(lista); 
		JasperPrint report = JasperFillManager.fillReport(inputStream,parametros,bancoLimpo);	
		//inline serve para abrir no target. attachment serve para abir janela de download
		response.setHeader("Content-Disposition","inline; filename="+fileName);
		response.setContentType("application/pdf");
		OutputStream out = response.getOutputStream();				
        JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
		return out;
    }
	public static OutputStream createPDFReport(String fileName,	JasperPrint report, HttpServletResponse response) throws JRException, IOException {
		response.setHeader("Content-Disposition","inline; filename="+fileName);
		response.setContentType("application/pdf");
		OutputStream out = response.getOutputStream();				
        JRExporter exporter = new JRPdfExporter();
		exporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, fileName);
        exporter.setParameter(JRExporterParameter.JASPER_PRINT, report);
        exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
        exporter.exportReport();
		return out;
    }
	public static JasperPrint getJasperPrint( InputStream inputStream, Map<String, Object> parametros, List lista) throws JRException {
			JRDataSource bancoLimpo = new JRBeanCollectionDataSource(lista); 
			JasperPrint jp = JasperFillManager.fillReport(inputStream, parametros, bancoLimpo);
			return jp;
	}
}
