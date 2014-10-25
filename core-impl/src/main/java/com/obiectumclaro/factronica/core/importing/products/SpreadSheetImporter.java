/**
 *
 */
package com.obiectumclaro.factronica.core.importing.products;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.ejb.Stateful;
import javax.inject.Inject;
//import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.obiectumclaro.factronica.core.enumeration.ProductType;
import com.obiectumclaro.factronica.core.importing.ValueConversionException;
import com.obiectumclaro.factronica.core.importing.ValueValidationException;
import com.obiectumclaro.factronica.core.model.Product;
import com.obiectumclaro.factronica.core.model.TaxValue;
import com.obiectumclaro.factronica.core.service.Products;
import com.obiectumclaro.factronica.core.service.exception.ProductAdditionException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.WorkbookFactory;

/**
 * Products importation from spreadsheet files.
 *
 * @author iapazmino
 *
 */
@Stateful
public class SpreadSheetImporter implements ProductsImporter {

    @Inject
    Products prods;
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
    public void importProducts(final ColumnsMapping mapping, final TaxValue tax) {
        Row row = null;
        Cell result = null;
        Product product = null;

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
                product = convert(row, mapping);
                validate(product, mapping);
                save(product, tax);
            } catch (ValueConversionException vce) {
                addFailureMessage(result, vce.getMessage());
                continue;
            } catch (ValueValidationException vve) {
                addFailureMessage(result, vve.getMessage());
                continue;
            } catch (ProductAdditionException e) {
                addFailureMessage(result, e.getMessage());
                continue;
            }

            addSuccessMessage(result, "ENVIADA");
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

    private Product convert(final Row row, final ColumnsMapping mapping) throws ValueConversionException {
        String code = null, altCode = null;
        String name = null, description = null;
        ProductType type = null;
        BigDecimal price = BigDecimal.ZERO;

        code = convertToString(row, mapping.getCode());
        altCode = convertToString(row, mapping.getAltCode());
        name = convertToString(row, mapping.getName());
        description = convertToString(row, mapping.getDescription());
        type = convertToProductType(row, mapping.getType());
        price = convertToNumber(row, mapping.getPrice());

        final Product product = new Product();
        product.setCode(code);
        product.setAlternateCode(altCode);
        product.setName(name);
        product.setDescription(description);
        product.setProductType(type);
        product.setUnitPrice(price);

        return product;
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
                return Double.toString(cell.getNumericCellValue());
            default:
                final String error = "El valor de %s en la columna %s puede ser un texto o un n\u00famero " + cellType;
                throwValueConversionException(error, header);
        }

        return null;
    }

    private boolean isUndefined(final Cell cell) {
        return null == cell;
    }

    private ProductType convertToProductType(final Row row,
            final HeaderLabel header) throws ValueConversionException {
        final String value = convertToString(row, header).toUpperCase();
        try {
            validateNotNull(value, header);
            return ProductType.valueOf(value);
        } catch (IllegalArgumentException iae) {
            final String error = "No es un tipo de producto v\u00e1lido (BIEN o SERVICIO).";
            throwValueConversionException(error, header);
            return null;
        } catch (ValueValidationException vve) {
            throwValueConversionException(vve.getRawMessage(), header);
            return null;
        }
    }

    private BigDecimal convertToNumber(final Row row, final HeaderLabel header)
            throws ValueConversionException {
        final Cell cell = row.getCell(header.getIndex());
        if (isUndefined(cell)) {
            return BigDecimal.ZERO;
        }
        if (cell.getCellType() == Cell.CELL_TYPE_STRING) {
        	final String value = cell.getStringCellValue();
        	try {
        		return new BigDecimal(value);
        	} catch (NumberFormatException ne) {
        		final String error = "El valor de %s en la columna %s es una cadena de texto y no un n\u00famero";
        		throwValueConversionException(error, header);
        	}
        }
        try {
            final double value = cell.getNumericCellValue();
            return new BigDecimal(value);
        } catch (NumberFormatException nfe) {
            final String error = "El valor de %s en la columna %s no es un n\u00famero.";
            throwValueConversionException(error, header);
            return BigDecimal.ZERO;
        }
    }

    private void throwValueConversionException(final String error,
            final HeaderLabel header) throws ValueConversionException {
        throw new ValueConversionException(error, header);
    }

    private void validate(final Product product, final ColumnsMapping mapping) throws ValueValidationException {
        validateNotNull(product.getCode(), mapping.getCode());
//        validateNotNull(product.getAlternateCode(), mapping.getAltCode());
        validateNotNull(product.getName(), mapping.getName());
        validateGreaterThanZero(product.getUnitPrice(), mapping.getPrice());
    }

    private void validateNotNull(final String string, final HeaderLabel header)
            throws ValueValidationException {
        if (null == string || string.isEmpty()) {
            final String error = "El valor de la celda %s en la posicion %s no puede ser nulo o vacu00edo.";
            throw new ValueValidationException(error, header);
        }
    }

    private void validateGreaterThanZero(final BigDecimal number,
            final HeaderLabel header) throws ValueValidationException {
        if (BigDecimal.ZERO.compareTo(number) > 0) {
            final String error = "El valor de la celda %s en la posicion %s no puede ser menor a cero.";
            throw new ValueValidationException(error, header);
        }
    }

    private void save(final Product product, final TaxValue tax) throws ProductAdditionException {
        final TaxValue managedTaxValue = prods
                .findTaxValueById(tax.getPk());
        final List<TaxValue> taxes = new ArrayList<>();
        taxes.add(managedTaxValue);
        prods.addProduct(product, taxes);
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
