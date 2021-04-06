/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.util;

/**
 * A pattern class serves for getting and setting pattern string for a custom
 * setted number.
 */
public class FormatCustomNumPattern extends FormatNumberPattern {

	private String fmtCode = ""; //$NON-NLS-1$

	/**
	 * Constructor.
	 * 
	 * @param category
	 */
	public FormatCustomNumPattern(String category) {
		super(category);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumFormatPattern#
	 * getPattern()
	 */
	public String getPattern() {
		return fmtCode;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.dialogs.NumFormatPattern#
	 * setPattern(java.lang.String)
	 */
	public void setPattern(String patternStr) {
		this.fmtCode = patternStr;
		return;
	}

	/**
	 * Get format code
	 * 
	 * @return Returns the fmtCode.
	 */
	public String getFmtCode() {
		return fmtCode;
	}
}