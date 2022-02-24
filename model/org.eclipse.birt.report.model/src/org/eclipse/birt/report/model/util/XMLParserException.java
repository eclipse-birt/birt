/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.util;

import java.util.List;

import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.xml.sax.SAXException;

/**
 * Reports a parse error. Describes errors as codes so that the error text can
 * be localized.
 * 
 */

public class XMLParserException extends SAXException {

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = -6489371474593098521L;

	/**
	 * The line of the file on which the error occurred.
	 */

	protected int lineNo = 0;

	/**
	 * Detailed error description.
	 */

	protected String errorCode = null;

	/**
	 * The element that was in effect at the time of the error.
	 */

	protected String tag = null;

	/**
	 * The SAX exception, if any, associated with the error.
	 */

	protected SAXException saxException = null;

	/**
	 * Additional exceptions, if any, associated with the error.
	 */

	protected List<XMLParserException> errorList = null;

	/**
	 * The XML file contains an unsupported element.
	 */

	public static final String DESIGN_EXCEPTION_UNKNOWN_TAG = MessageConstants.XML_PARSER_EXCEPTION_UNKNOWN_TAG;

	/**
	 * SAX detected an error with the basic XML syntax of the file.
	 */

	public static final String DESIGN_EXCEPTION_SAX_ERROR = MessageConstants.XML_PARSER_EXCEPTION_SAX_ERROR;

	/**
	 * A Boolean attribute does not contain a valid value.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_BOOLEAN = MessageConstants.XML_PARSER_EXCEPTION_INVALID_BOOLEAN;

	/**
	 * The parse completed, but recoverable errors occurred.
	 */

	public static final String DESIGN_EXCEPTION_WARNINGS_FOUND = MessageConstants.XML_PARSER_EXCEPTION_WARNINGS_FOUND;

	/**
	 * A generic exception occurred.
	 */

	public static final String DESIGN_EXCEPTION_EXCEPTION = MessageConstants.XML_PARSER_EXCEPTION_EXCEPTION;

	/**
	 * An integer attribute contains an invalid value.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_INTEGER = MessageConstants.XML_PARSER_EXCEPTION_INVALID_INTEGER;

	/**
	 * Constructor.
	 * 
	 * @param errCode the error code
	 */

	public XMLParserException(String errCode) {
		super((String) null);
		errorCode = errCode;
	}

	/**
	 * Constructor.
	 * 
	 * @param e a SAX exception
	 */

	public XMLParserException(SAXException e) {
		super(e);
		saxException = e;
		errorCode = DESIGN_EXCEPTION_SAX_ERROR;
	}

	/**
	 * Constructor.
	 * 
	 * @param e a generic exception
	 */

	public XMLParserException(Exception e) {
		super(e);
		errorCode = DESIGN_EXCEPTION_EXCEPTION;
	}

	/**
	 * Constructor.
	 * 
	 * @param e       generic exception
	 * @param errCode error code that explains the exception
	 */

	public XMLParserException(Exception e, String errCode) {
		super(e);
		errorCode = errCode;
	}

	/**
	 * Constructor.
	 * 
	 * @param errors list of errors
	 */

	public XMLParserException(List<XMLParserException> errors) {
		super((String) null);
		errorCode = DESIGN_EXCEPTION_WARNINGS_FOUND;
		errorList = errors;
	}

	/**
	 * Sets the line number associated with the exception.
	 * 
	 * @param n The line number to set.
	 */

	public void setLineNumber(int n) {
		lineNo = n;
	}

	/**
	 * Sets the element associated with the exception.
	 * 
	 * @param theTag the element name to set.
	 */

	public void setTag(String theTag) {
		tag = theTag;
	}

	/**
	 * Gets the error code associated with the exception.
	 * 
	 * @return the error code
	 */
	public String getErrorCode() {
		return errorCode;
	}

	/**
	 * Gets the element associated with the exception.
	 * 
	 * @return the element name
	 */
	public String getTag() {
		return tag;
	}

	/**
	 * Gets the line number associated with the exception.
	 * 
	 * @return the line number
	 */
	public int getLineNumber() {
		return lineNo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage() {
		StringBuffer sb = new StringBuffer();

		String SEPARATOR = " "; //$NON-NLS-1$

		sb.append("Line Number:").append(getLineNumber()).append(SEPARATOR); //$NON-NLS-1$
		sb.append("Error Code:").append(errorCode).append(SEPARATOR); //$NON-NLS-1$

		if (getException() != null)
			sb.append("Exception:").append(getException()).append(SEPARATOR); //$NON-NLS-1$

		sb.append("Message:").append(ModelMessages.getMessage(errorCode)).append(SEPARATOR); //$NON-NLS-1$

		return sb.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getMessage()
	 */

	public String getMessage() {
		return getLocalizedMessage();
	}

	/**
	 * Returns the error list.
	 * 
	 * @return the error list
	 */

	public List<XMLParserException> getErrorList() {
		return errorList;
	}
}
