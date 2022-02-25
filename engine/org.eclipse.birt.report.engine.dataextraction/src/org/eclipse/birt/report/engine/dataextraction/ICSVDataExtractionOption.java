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

package org.eclipse.birt.report.engine.dataextraction;

/**
 * Extends Data Extraction options for CSV format
 *
 */
public interface ICSVDataExtractionOption extends ICommonDataExtractionOption {
	String SEPARATOR_PIPE = "|"; //$NON-NLS-1$
	String SEPARATOR_COMMA = ","; //$NON-NLS-1$
	String SEPARATOR_COLON = ":"; //$NON-NLS-1$
	String SEPARATOR_SEMICOLON = ";"; //$NON-NLS-1$
	String SEPARATOR_TAB = "\t"; //$NON-NLS-1$

	/**
	 * the separator
	 */
	String OUTPUT_SEPARATOR = "Separator"; //$NON-NLS-1$

	/**
	 * the option checks if using CR + LF as the line separator.
	 */
	String ADD_CR_LINE_BREAK = "AddCR";

	/**
	 * Sets the output separator
	 *
	 * @param sep
	 */
	void setSeparator(String sep);

	/**
	 * Returns the output separator
	 *
	 * @return String
	 */
	String getSeparator();

	boolean getAddCR();

	void setAddCR(boolean addCR);
}
