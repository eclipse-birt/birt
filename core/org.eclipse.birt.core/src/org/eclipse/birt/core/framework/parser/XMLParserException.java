/*******************************************************************************
* Copyright (c) 2004 Actuate Corporation.
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v2.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-2.0.html
*
* Contributors:
*  Actuate Corporation  - initial API and implementation
*******************************************************************************/

package org.eclipse.birt.core.framework.parser;

import java.util.ArrayList;
import java.util.List;

import org.xml.sax.SAXException;

/**
 * Reports a parse error. Describes errors as codes so that the error text can
 * be localized.
 *
 */

public class XMLParserException extends SAXException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4843979661543157480L;

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

	protected ArrayList errorList = null;

	/**
	 * The XML file contains an unsupported element.
	 */

	public static final String UNKNOWN_TAG = "Error.XMLParserException.UNKNOWN_TAG";

	/**
	 * SAX detected an error with the basic XML syntax of the file.
	 */

	public static final String SAX_ERROR = "Error.XMLParserException.SAX_ERROR";

	/**
	 * A Boolean attribute does not contain a valid value.
	 */

	public static final String INVALID_BOOLEAN = "Error.XMLParserException.INVALID_BOOLEAN";

	/**
	 * The parse completed, but recoverable errors occurred.
	 */

	public static final String WARNINGS_FOUND = "Error.XMLParserException.WARNINGS_FOUND";

	/**
	 * A generic exception occurred.
	 */

	public static final String EXCEPTION = "Error.XMLParserException.EXCEPTION";

	/**
	 * An integer attribute contains an invalid value.
	 */

	public static final String INVALID_INTEGER = "Error.XMLParserException.INVALID_INTEGER";

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
		errorCode = SAX_ERROR;
	}

	/**
	 * Constructor.
	 * 
	 * @param e a generic exception
	 */

	public XMLParserException(Exception e) {
		super(e);
		errorCode = EXCEPTION;
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

	public XMLParserException(ArrayList errors) {
		super((String) null);
		errorCode = WARNINGS_FOUND;
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
	 * @see java.lang.Throwable#getMessage()
	 */

	public String getMessage() {
		StringBuffer sb = new StringBuffer();

		String SEPARATOR = " "; //$NON-NLS-1$

		sb.append("Line Number:").append(getLineNumber()).append(SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$
		sb.append("Error Code:").append(errorCode).append(SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$

		if (getException() != null)
			sb.append("Exception:").append(getException()).append(SEPARATOR); //$NON-NLS-1$ //$NON-NLS-2$

		sb.append("Message:").append(errorCode).append(SEPARATOR); //$NON-NLS-1$

		return sb.toString();
	}

	/**
	 * Returns the error list.
	 * 
	 * @return the error list
	 */

	public List getErrorList() {
		return errorList;
	}
}
