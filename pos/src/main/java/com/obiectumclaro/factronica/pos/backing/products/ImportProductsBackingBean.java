/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.products;

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

import com.obiectumclaro.factronica.core.importing.products.ColumnsMapping;
import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;
import com.obiectumclaro.factronica.core.importing.products.ProductsImporter;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.service.Products;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * @author iapazmino
 * 
 */
@ManagedBean
@ViewScoped
public class ImportProductsBackingBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private static final String XLS_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	@Inject
	ProductsImporter prodsImporter;
	@Inject
	private Products prodsService;

	private boolean disableImport;
	private boolean disableDownloadReport;
	String xlsName;
	private StreamedContent report;

	private HeaderLabel[] indexes;
	private TaxValue tax;

	@PostConstruct
	public void init() {
		disableImport = true;
		disableDownloadReport = true;
		indexes = new HeaderLabel[8];
	}

	public void handleFileUpload(final FileUploadEvent event) {
		final String message = "El archivo %s fue cargado";
		try {
			final UploadedFile file = event.getFile();
			final InputStream xls = file.getInputstream();
			xlsName = file.getFileName();
			prodsImporter.readSource(xls);
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
		final ColumnsMapping mapping = new ColumnsMapping(indexes);
		prodsImporter.importProducts(mapping, tax);
		FacesMessageHelper.addInfo("Los productos han sido importados", null);
		disableDownloadReport = false;
	}

	public void downloadReportListener(final ActionEvent evt) {
		if (null == report) {
			final InputStream stream = prodsImporter.getReport();
			final String reportName = "report-" + xlsName;
			report = new DefaultStreamedContent(stream, XLS_MIME_TYPE,
					reportName);
		}
	}

	public StreamedContent getReport() {
		return report;
	}

	public List<HeaderLabel> getHeaders() {
		return prodsImporter.getHeaders();
	}

	public List<TaxValue> getTaxes() {
		return prodsService.listTaxValues();
	}

	public HeaderLabel[] getIndexes() {
		return indexes;
	}

	public void setIndexes(HeaderLabel[] indexes) {
		this.indexes = indexes;
	}

	public TaxValue getTax() {
		return tax;
	}

	public void setTax(TaxValue tax) {
		this.tax = tax;
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
