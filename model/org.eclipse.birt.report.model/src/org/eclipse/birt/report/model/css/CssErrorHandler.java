/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.css;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.model.api.util.StringUtil;
import org.w3c.css.sac.CSSException;
import org.w3c.css.sac.CSSParseException;
import org.w3c.css.sac.ErrorHandler;

/**
 * Implements ErrorHandler to deal with the errors, warnings and fatal errors
 * during the parse of the CSS file.
 */

public class CssErrorHandler implements ErrorHandler {

	/**
	 * The message list for parser errors.
	 */

	List<String> errors = new ArrayList<String>();

	/**
	 * The message list for parser fatal errors.
	 */

	List<String> fatalErrors = new ArrayList<String>();

	/**
	 * The message list for parser warnings.
	 */

	List<String> warnings = new ArrayList<String>();

	/**
	 * Default constructor.
	 * 
	 */

	public CssErrorHandler() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.css.sac.ErrorHandler#error(org.w3c.css.sac.CSSParseException)
	 */

	public void error(CSSParseException exception) throws CSSException {
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isBlank(exception.getURI()))
			sb.append(exception.getURI()).append(" "); //$NON-NLS-1$
		;
		sb.append("[").append( //$NON-NLS-1$
				exception.getLineNumber()).append(":").append( //$NON-NLS-1$
						exception.getColumnNumber())
				.append("] ").append( //$NON-NLS-1$
						exception.getMessage());
		System.err.println(sb.toString());
		errors.add(sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.w3c.css.sac.ErrorHandler#fatalError(org.w3c.css.sac.CSSParseException )
	 */

	public void fatalError(CSSParseException exception) throws CSSException {
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isBlank(exception.getURI()))
			sb.append(exception.getURI()).append(" "); //$NON-NLS-1$
		;
		sb.append("[").append( //$NON-NLS-1$
				exception.getLineNumber()).append(":").append( //$NON-NLS-1$
						exception.getColumnNumber())
				.append("] ").append( //$NON-NLS-1$
						exception.getMessage());
		System.err.println(sb.toString());
		fatalErrors.add(sb.toString());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.w3c.css.sac.ErrorHandler#warning(org.w3c.css.sac.CSSParseException)
	 */

	public void warning(CSSParseException exception) throws CSSException {
		StringBuffer sb = new StringBuffer();
		if (!StringUtil.isBlank(exception.getURI()))
			sb.append(exception.getURI()).append(" "); //$NON-NLS-1$
		;
		sb.append("[").append( //$NON-NLS-1$
				exception.getLineNumber()).append(":").append( //$NON-NLS-1$
						exception.getColumnNumber())
				.append("] ").append( //$NON-NLS-1$
						exception.getMessage());
		System.err.println(sb.toString());
		warnings.add(sb.toString());
	}

	/**
	 * Gets the message list for the parser errors.
	 * 
	 * @return the message list for the parser errors
	 */

	public List<String> getParserErrors() {
		return errors;
	}

	/**
	 * Gets the message list for the parser fatal errors.
	 * 
	 * @return the message list for the parser fatal errors
	 */

	public List<String> getParserFatalErrors() {
		return fatalErrors;
	}

	/**
	 * Gets the message list for the parser warnings.
	 * 
	 * @return the message list for the parser warnings
	 */

	public List<String> getParserWarnings() {
		return warnings;
	}
}