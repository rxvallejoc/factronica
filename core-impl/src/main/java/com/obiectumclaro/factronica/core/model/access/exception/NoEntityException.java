/**
 * 
 */
package com.obiectumclaro.factronica.core.model.access.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

import javax.ejb.ApplicationException;
import javax.persistence.NoResultException;

/**
 * Wrapper used to rethrow a {@link NoResultException}.
 * 
 * @author iapazmino
 * 
 */
@ApplicationException
public class NoEntityException extends NoResultException {

	private static final long serialVersionUID = 1L;

	private final NoResultException source;

	/**
	 * Constructs an instance wrapping an empty {@link NoResultException}.
	 * Should not be used but for testing purposes.
	 */
	public NoEntityException() {
		this(new NoResultException());
	}

	public NoEntityException(final NoResultException source) {
		this.source = source;
	}

	public String getMessage() {
		return source.getMessage();
	}

	public String getLocalizedMessage() {
		return source.getLocalizedMessage();
	}

	public Throwable getCause() {
		return source.getCause();
	}

	public void printStackTrace() {
		source.printStackTrace();
	}

	public void printStackTrace(PrintStream s) {
		source.printStackTrace(s);
	}

	public void printStackTrace(PrintWriter s) {
		source.printStackTrace(s);
	}

	public StackTraceElement[] getStackTrace() {
		return source.getStackTrace();
	}

}
