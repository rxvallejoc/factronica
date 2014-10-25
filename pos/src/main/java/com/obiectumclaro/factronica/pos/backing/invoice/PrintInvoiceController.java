/**
 * 
 */
package com.obiectumclaro.factronica.pos.backing.invoice;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.obiectumclaro.factronica.core.service.InvoiceBean;
import com.obiectumclaro.factronica.core.service.exception.InvoicePrintException;
import com.obiectumclaro.factronica.pos.backing.BaseBackingBean;
import com.obiectumclaro.factronica.pos.jsf.FacesMessageHelper;

/**
 * @author fausto
 * 
 */
@Named
public class PrintInvoiceController extends BaseBackingBean {

	private static final Logger LOG=Logger.getLogger(PrintInvoiceController.class);
	@Inject
	private InvoiceBean invoiceBean;
	private Long invoiceId;

	@PostConstruct
	public void init() {
		LOG.info("PrintInvoiceController post contruct");
		String invoice = getRequestParameter("id");
		System.out.println("Invoice id: " + invoice);
		try {
			invoiceId = Long.valueOf(invoice);
			printInvoice();
		} catch (NumberFormatException e) {
			FacesMessageHelper.addError("Se esperaba que el paramtero id sea un numero", "");
		}
	}

	private void printInvoice() {
		try {
			byte[] agreement = invoiceBean.printInvoice(invoiceId,null);
			downloadFile(agreement, "application/pdf", String.format("factura_%s", invoiceId));
		} catch (InvoicePrintException e) {
			FacesMessageHelper
					.addError(String.format("Error al imprimir la factura, Error [%s]", e.getMessage()), e.getMessage());
		} catch (IOException e) {
			FacesMessageHelper.addError(String.format("Error al descargar el archivo de la factura, Error [%s]", e.getMessage()),
					e.getMessage());
		}

	}

	public Long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Long invoiceId) {
		this.invoiceId = invoiceId;
	}

}
