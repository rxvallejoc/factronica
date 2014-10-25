package com.obiectumclaro.factronica.core.importing.invoices.tab;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.obiectumclaro.file.reader.exception.ObjectExecutionException;
import com.obiectumclaro.file.reader.executor.ObjectExecutor;

public class InvoiceFileExecutor implements ObjectExecutor<InvoiceLine> {

    public final static Logger LOG = Logger.getLogger(InvoiceFileExecutor.class);
    public int validExecutions = 0;

    public boolean errorConvertion = false;
    public int rowConvertionError = -1;

    public boolean errorValidation = false;
    public int rowValidationError = -1;

    public boolean errorExecution = false;
    public int rowExecutionError = -1;

    private List<InvoiceLine> invoiceFiles = new ArrayList<InvoiceLine>();

    @Override
    public void execute(InvoiceLine invoice) throws ObjectExecutionException {
	invoiceFiles.add(invoice);
    }

    @Override
    public void onConversionError(Throwable t, Map<String, Object> aditionalInfo) throws ObjectExecutionException {
	errorConvertion = true;
	rowConvertionError = (Integer) aditionalInfo.get("rowNumber");
	if (rowConvertionError == 15) {
	    LOG.error(String.format("Error de conversion la fila es %s SI se detiene la lectura del archivo, %s",
		    rowConvertionError, t.getMessage()));
	    throw new ObjectExecutionException(String.format(
		    "Error de conversion la fila es %s se detiene la lectura del archivo", rowConvertionError));
	}
	LOG.error(String.format("Error de conversion la fila es %s NO se detiene la lectura del archivo, %s",
		rowConvertionError, t.getMessage()));

    }

    @Override
    public void onExecutionError(Throwable t, Map<String, Object> aditionalInfo) throws ObjectExecutionException {
	errorValidation = true;
	rowValidationError = (Integer) aditionalInfo.get("rowNumber");
	LOG.error("onValidationError linea " + rowValidationError + " " + t.getMessage());
	if (t.getMessage().compareTo("validErrorFatal") == 0) {
	    throw new ObjectExecutionException(String.format(
		    "Se detiene la lectura del archivo en la linea %s por error: %s", rowValidationError,
		    t.getMessage()));
	}
    }

    @Override
    public void onValidationError(Throwable t, Map<String, Object> aditionalInfo) throws ObjectExecutionException {
	errorExecution = true;
	rowExecutionError = (Integer) aditionalInfo.get("rowNumber");
	LOG.error("onExecutionError " + t.getMessage());
	if (t.getMessage().compareTo("execErrorFatal") == 0) {
	    throw new ObjectExecutionException("Se detiene la lectura del archivo por: " + t.getMessage());
	}

    }

    public List<InvoiceLine> getInvoiceFiles() {
	return invoiceFiles;
    }

}
