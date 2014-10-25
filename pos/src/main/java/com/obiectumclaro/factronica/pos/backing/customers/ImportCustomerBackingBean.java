/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.customers;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;
import javax.inject.Inject;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import com.obiectumclaro.factronica.core.importing.customers.ColumnsCustomerMapping;
import com.obiectumclaro.factronica.core.importing.customers.CustomerImporter;
import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * @author iapazmino
 * 
 */
@ManagedBean
@ViewScoped
public class ImportCustomerBackingBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String XLS_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Inject
	CustomerImporter customerImporter;
	
	private boolean disableImport;
	private boolean disableDownloadReport;
	String xlsName;
	private StreamedContent report;

	private HeaderLabel[] indexes;

	@PostConstruct
	public void init() {
		disableImport = true;
		disableDownloadReport = true;
		indexes = new HeaderLabel[9];
	}

	public void handleFileUpload(final FileUploadEvent event) {
		final String message = "El archivo %s fue cargado";
		try {
			final UploadedFile file = event.getFile();
			final InputStream xls = file.getInputstream();
			xlsName = file.getFileName();
			customerImporter.readSource(xls);
			FacesMessageHelper.addInfo("Carga exitosa",
					String.format(message, xlsName));
			disableImport = false;
		} catch (IOException e) {
                        e.printStackTrace();
			FacesMessageHelper.addError("Error",
					"No se pudo leer el archivo cargado");
		}catch(Exception e){
                    e.printStackTrace();
                }
	}

	public void startImportationListener(final ActionEvent evt) {
		final ColumnsCustomerMapping mapping = new ColumnsCustomerMapping(indexes);
		customerImporter.importCustomers(mapping);
		FacesMessageHelper.addInfo("Los clientes han sido importados", null);
		disableDownloadReport = false;
	}

	public void downloadReportListener(final ActionEvent evt) {
		if (null == report) {
			final InputStream stream = customerImporter.getReport();
			final String reportName = "report-" + xlsName;
			report = new DefaultStreamedContent(stream, XLS_MIME_TYPE,
					reportName);
		}
	}

	public StreamedContent getReport() {
		return report;
	}

	public List<HeaderLabel> getHeaders() {
		return customerImporter.getHeaders();
	}

	

	public HeaderLabel[] getIndexes() {
		return indexes;
	}

	public void setIndexes(HeaderLabel[] indexes) {
		this.indexes = indexes;
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

}
