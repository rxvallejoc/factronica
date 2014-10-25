/**
 *
 */
package com.obiectumclaro.factronica.core.importing.customers;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.ejb.Stateful;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

import com.obiectumclaro.factronica.core.enumeration.IdType;
import com.obiectumclaro.factronica.core.enumeration.Person;
import com.obiectumclaro.factronica.core.importing.ValueConversionException;
import com.obiectumclaro.factronica.core.importing.ValueValidationException;
import com.obiectumclaro.factronica.core.importing.products.HeaderLabel;
import com.obiectumclaro.factronica.core.model.Customer;
import com.obiectumclaro.factronica.core.service.CustomersBean;
import com.obiectumclaro.factronica.core.service.exception.CustomerAdditionException;

/**
 * Products importation from spreadsheet files.
 *
 * @author iapazmino
 *
 */
@Stateful
public class SpreadSheetCustomerImporter implements CustomerImporter {
	
	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	
	@EJB
    CustomersBean customerBean;
    private Workbook spreadSheet;
    private Sheet sheet;
    private InputStream report;
    private List<HeaderLabel> headers;

    @Override
    public void readSource(final InputStream source) {
        try {
            spreadSheet = WorkbookFactory.create(source);
            sheet = spreadSheet.getSheetAt(0);
            updateHeaders();
        } catch (InvalidFormatException ex) {
//            ex.printStackTrace();
//            Logger.getLogger(SpreadSheetImporter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException e) {
            // TODO exception should be translated and caught on view
            throw new RuntimeException(e);
        }
    }

    @Override
    public void importCustomers(final ColumnsCustomerMapping mapping) {
        Row row = null;
        Cell result = null;
        Customer customer= null;

        final Row headersRow = sheet.getRow(0);
        final Cell resultHeader = headersRow.createCell(headersRow.getLastCellNum());
        addSuccessMessage(resultHeader, "Resultado");
        
        for (int i = 1; i < sheet.getLastRowNum() + 1; i++) {
            row = sheet.getRow(i);
            if (isUndefined(row)) {
                continue;
            }
            result = row.createCell(row.getLastCellNum());

            try {
                customer = convert(row, mapping);
                validate(customer, mapping);
                save(customer);
            } catch (ValueConversionException vce) {
                addFailureMessage(result, vce.getMessage());
                continue;
            } catch (ValueValidationException vve) {
                addFailureMessage(result, vve.getMessage());
                continue;
            } catch (CustomerAdditionException e) {
                addFailureMessage(result, e.getMessage());
                continue;
            }

            addSuccessMessage(result, "IMPORTADO");
        }

        writeReport();
    }

    private boolean isUndefined(final Row row) {
        return null == row;
    }

    private void updateHeaders() {
        headers = new ArrayList<>();
        final Row headersRow = sheet.getRow(0);
        Cell header = null;
        for (Iterator<Cell> it = headersRow.cellIterator(); it.hasNext();) {
            header = it.next();
            headers.add(new HeaderLabel(header.getStringCellValue(), header
                    .getColumnIndex()));
        }
    }

    private Customer convert(final Row row, final ColumnsCustomerMapping mapping) throws ValueConversionException {
        final Customer customer =new Customer();
        customer.setId(convertToString(row,mapping.getId()));
        customer.setIdType(convertToIdType(row, mapping.getIdType()));
        customer.setEmail(convertToString(row, mapping.getEmail()));
        customer.setPerson(convertToPerson(row, mapping.getPerson()));
        customer.setSocialReason(convertToString(row, mapping.getSocialReason()));
        customer.setName(convertToString(row, mapping.getName()));
        customer.setLastName(convertToString(row, mapping.getLastName()));
        customer.setAddress(convertToString(row, mapping.getAddress()));
        customer.setPhone(convertToString(row, mapping.getPhone()));
        
        return customer;
    }

    private String convertToString(final Row row, final HeaderLabel header) throws ValueConversionException {
        final Cell cell = row.getCell(header.getIndex());
        if (isUndefined(cell)) {
            return "";
        }

        final Integer cellType = cell.getCellType();
        switch (cellType) {
            case Cell.CELL_TYPE_STRING:
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_NUMERIC:
            	cell.setCellType(Cell.CELL_TYPE_STRING);
                return cell.getStringCellValue();
            case Cell.CELL_TYPE_FORMULA:
                final String errorFormula = "El valor de %s en la columna %s es una formula. Por favor copie y pegue los valores";
                throwValueConversionException(errorFormula, header);
            default:
                final String error = "El valor de %s en la columna %s puede ser un texto o un n\u00famero";
                throwValueConversionException(error, header);
        }

        return null;
    }

    private boolean isUndefined(final Cell cell) {
        return null == cell;
    }
    
    private IdType convertToIdType(final Row row,
            final HeaderLabel header) throws ValueConversionException{
    	 final String value = convertToString(row, header).toUpperCase();
         try {
             validateNotNull(value, header);
             return IdType.valueOf(value);
         } catch (IllegalArgumentException iae) {
             final String error = "No es un tipo de Identificacion v\u00e1lido (RUC o CEDULA o PASAPORTE).";
             throwValueConversionException(error, header);
             return null;
         } catch (ValueValidationException vve) {
             throwValueConversionException(vve.getRawMessage(), header);
             return null;
         }
    	
    }
    
    private Person convertToPerson(final Row row,
            final HeaderLabel header) throws ValueConversionException{
    	 final String value = convertToString(row, header).toUpperCase();
         try {
             validateNotNull(value, header);
             return Person.valueOf(value);
         } catch (IllegalArgumentException iae) {
             final String error = "No es un tipo de persona v\u00e1lido (JURIDICA o NATURAL).";
             throwValueConversionException(error, header);
             return null;
         } catch (ValueValidationException vve) {
             throwValueConversionException(vve.getRawMessage(), header);
             return null;
         }
    	
    }
   
 
    private void throwValueConversionException(final String error,
            final HeaderLabel header) throws ValueConversionException {
        throw new ValueConversionException(error, header);
    }

    private void validate(final Customer customer, final ColumnsCustomerMapping mapping) throws ValueValidationException {
        validateNotNull(customer.getId(), mapping.getId());
        validateEmail(customer.getEmail(), mapping.getEmail());
        if(customer.getPerson().equals(Person.JURIDICA)){
        	validateNotNull(customer.getSocialReason(), mapping.getSocialReason());
        }else{
        	validateNotNull(customer.getName(), mapping.getName());
        	validateNotNull(customer.getLastName(), mapping.getLastName());
        }
    }
    
    private void validateEmail(final String email, final HeaderLabel header) throws ValueValidationException {
    	validateNotNull(email, header);
    	validateMaxLength(email, header, 255);
    	validatePattern(email, header);
    }

    private void validateNotNull(final String string, final HeaderLabel header)
            throws ValueValidationException {
        if (null == string || string.isEmpty()) {
            final String error = "El valor de la celda %s en la posicion %s no puede ser nulo o vacu00edo.";
            throw new ValueValidationException(error, header);
        }
    }
    
    private void validateMaxLength(final String string, final HeaderLabel header, final int maxLength) 
    		throws ValueValidationException {
    	if (string.length() > maxLength) {
    		final StringBuffer error = new StringBuffer("El texto de la celda %s en la posicion %s no puede ser mayor a ")
    		.append(maxLength)
    		.append(" caracteres.");
            throw new ValueValidationException(error.toString(), header);
    	}
    }
    
    private void validatePattern(final String string, final HeaderLabel header) throws ValueValidationException {
    	final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    	final Matcher matcher = pattern.matcher(string);
    	if (!matcher.matches()) {
    		final String error = "El valor de la celda %s en la posicion %s no es una direccion de correo valida.";
            throw new ValueValidationException(error, header);
    	}
    }
    
    private void save(final Customer customer) throws CustomerAdditionException {
    	customerBean.add(customer);
    }

    private void writeReport() {
        try {
            report = new PipedInputStream();
            final PipedOutputStream output = new PipedOutputStream((PipedInputStream) report);
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        spreadSheet.write(output);
                        output.close();
                    } catch (IOException e) {
                        // TODO exception should be translated and caught on view
                        throw new RuntimeException(e);
                    }
                }
            }).start();
        } catch (IOException e) {
            // TODO exception should be translated and caught on view
            throw new RuntimeException(e);
        }
    }

    private void addSuccessMessage(final Cell cell, final String text) {
        cell.setCellValue(text);
        cell.setCellStyle(getGreenFilledCell());
    }

    private void addFailureMessage(final Cell cell, final String text) {
        cell.setCellValue(text);
        cell.setCellStyle(getRedFilledCell());
    }

    private CellStyle getRedFilledCell() {
        final CellStyle style = spreadSheet.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.RED.getIndex());
        style.setFillPattern(CellStyle.BIG_SPOTS);
        return style;
    }

    private CellStyle getGreenFilledCell() {
        final CellStyle style = spreadSheet.createCellStyle();
        style.setFillBackgroundColor(IndexedColors.GREEN.getIndex());
        style.setFillPattern(CellStyle.BIG_SPOTS);
        return style;
    }

    @Override
    public List<HeaderLabel> getHeaders() {
        return headers;
    }

    @Override
    public InputStream getReport() {
        return report;
    }
}
