/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.obiectumclaro.factronica.pos.backing.invoice;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.apache.log4j.Logger;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.obiectumclaro.factronica.core.importing.invoices.tab.InvoiceLine;
import com.obiectumclaro.factronica.core.importing.invoices.tab.TABFileImporter;
import com.obiectumclaro.factronica.core.service.DocumentReportBean;
import com.obiectumclaro.factronica.pos.backing.access.UserSession;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * 
 * @author marco
 */
@ManagedBean
@ViewScoped
public class ImportInvoicesFromTabFileController implements Serializable {

    private static final Logger LOG = Logger.getLogger(ImportInvoicesFromTabFileController.class);

    private static final long serialVersionUID = 1L;

    @Inject
    private TABFileImporter tabFileImporter;
    @EJB
    private DocumentReportBean documentReportBean;
    @Inject
    private UserSession userSession;
    private boolean disableImport;
    private boolean disableDownloadReport;
    private String tabFileName;
    private StreamedContent report;
    private List<InvoiceLine> lines;

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
	    tabFileName = file.getFileName();
	    lines = tabFileImporter.readTABFile(toByteArray(csv));
	    FacesMessageHelper.addInfo("Carga exitosa", String.format(message, lines.size(), tabFileName));
	    disableImport = false;
	} catch (IOException e) {
	    LOG.error(e);
	    FacesMessageHelper.addError("Error", "No se pudo leer el archivo cargado");
	} catch (Exception e) {
	    LOG.error(e);
	}
    }

    private byte[] toByteArray(InputStream inputStream) throws IOException {
	ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	int reads = inputStream.read();

	while (reads != -1) {
	    byteArrayOutputStream.write(reads);
	    reads = inputStream.read();
	}

	return byteArrayOutputStream.toByteArray();
    }

    public void startImportationListener(final ActionEvent evt) {
	final String message = "%s facturas han sido importadas. Por revisar reporte de Envios.";
	tabFileImporter.importInvoices(lines, userSession.getUser());
	FacesMessageHelper.addInfo("Facturas importadas", String.format(message, lines.size()));
	disableDownloadReport = false;
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
	return tabFileName;
    }

    public void setXlsName(String xlsName) {
	this.tabFileName = xlsName;
    }

    public StreamedContent getReport() {
	return report;
    }

}
