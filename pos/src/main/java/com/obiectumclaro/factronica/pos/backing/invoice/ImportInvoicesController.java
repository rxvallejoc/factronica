/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.pos.backing.invoice;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.obiectumclaro.factronica.core.importing.invoices.csv.CSVFileImporter;
import com.obiectumclaro.factronica.core.importing.invoices.csv.InvoiceLine;
import com.obiectumclaro.factronica.core.model.DocumentReport;
import com.obiectumclaro.factronica.core.service.DocumentReportBean;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 *
 * @author marco
 */
@ManagedBean
@ViewScoped
public class ImportInvoicesController implements Serializable {

	private static final long serialVersionUID = 1L;
    @Inject
    private CSVFileImporter csvImporter;
    @EJB
    private DocumentReportBean documentReportBean;
    private static final String XLS_MIME_TYPE = "text/csv";
    private boolean disableImport;
	private boolean disableDownloadReport;
	private String csvName;
	private StreamedContent report;
	
	@PostConstruct
	public void init() {
		disableImport = true;
		disableDownloadReport = true;
	}

    public void handleFileUpload(final FileUploadEvent event) {
        final String message = "%s lineas del archivo %s fueron cargadas";
        try {
            final UploadedFile file = event.getFile();
            final InputStream csv = file.getInputstream();
            csvName = file.getFileName();
            final List<InvoiceLine> lines = csvImporter.readCSVFile(csv);
            FacesMessageHelper.addInfo("Carga exitosa", String.format(message, lines.size(), csvName));
			disableImport = false;
        } catch (IOException e) {
            e.printStackTrace();
            FacesMessageHelper.addError("Error", "No se pudo leer el archivo cargado");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

	public void startImportationListener(final ActionEvent evt) {
		final String message = "%s facturas han sido importadas";
		int facturas = csvImporter.importInvoices(csvName);
		FacesMessageHelper.addInfo("Facturas importadas", String.format(message, facturas));
		disableDownloadReport = false;
	}

	public void downloadReportListener(final ActionEvent evt) {
		final String reportName = "report-" + csvName;
		System.out.println("BEGIN " + reportName);
		final List<InvoiceLine> stream = csvImporter.getReport();
		
		for (InvoiceLine invoiceLine : stream) {
			DocumentReport documentReport=new DocumentReport();
			documentReport.setArchive(reportName);
			documentReport.setDate(new Date());
			documentReport.setLine(invoiceLine.toString());
			documentReportBean.store(documentReport);
			System.out.println(invoiceLine);
		}
		System.out.println("END " + reportName);
	}

	public boolean isDisableImport() {
		return disableImport;
	}

	public void setDisableImport(boolean disableImport) {
		this.disableImport = disableImport;
	}

	public boolean isDisableDownloadReport() {
		return disableDownloadReport;
	}

	public void setDisableDownloadReport(boolean disableDownloadReport) {
		this.disableDownloadReport = disableDownloadReport;
	}

	public String getXlsName() {
		return csvName;
	}

	public void setXlsName(String xlsName) {
		this.csvName = xlsName;
	}

	public StreamedContent getReport() {
		return report;
	}
	
}
