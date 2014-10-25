/**
 * 
 */
package com.obiectumclaro.factronica.core.util;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * @author iapazmino
 *
 */
public class JaxbMarshaller<T extends Serializable> {

	private Class<T> type;

	public JaxbMarshaller(final Class<T> type) {
		this.type = type;
	}
	
	public byte[] toByteArray(final T invoice) {
		final ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		final CDataXMLStreamWriter cdataWriter = createCDataXMLStreamWriter(outStream);
		final Marshaller marshaller = createFormattedOutputMarshaller();
		try {
			marshaller.marshal(invoice, cdataWriter);
			cdataWriter.flush();
			cdataWriter.close();
			return outStream.toByteArray();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	
	private CDataXMLStreamWriter createCDataXMLStreamWriter(final ByteArrayOutputStream outStream) {
		try {
			final XMLOutputFactory outFactory = XMLOutputFactory.newInstance();
			final XMLStreamWriter writer = outFactory.createXMLStreamWriter(outStream);
			return new CDataXMLStreamWriter(writer);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
	}
	
	private Marshaller createFormattedOutputMarshaller() {
		try {
			final JAXBContext ctx = JAXBContext.newInstance(type);
			final Marshaller marshaller = ctx.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			return marshaller;
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		}
	}
	
}
