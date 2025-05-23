/*******************************************************************************
 * Copyright (c) 2004, 2025 Actuate Corporation and others
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

	/** property: separator pipe */
	String SEPARATOR_PIPE = "|"; //$NON-NLS-1$

	/** property: separator comma */
	String SEPARATOR_COMMA = ","; //$NON-NLS-1$

	/** property: separator double dot */
	String SEPARATOR_COLON = ":"; //$NON-NLS-1$

	/** property: separator semicolon */
	String SEPARATOR_SEMICOLON = ";"; //$NON-NLS-1$

	/** property: separator tabulator */
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
	 * the option checks if using header line with column display name.
	 */
	String ADD_COLUMN_DISPLAY_NAME = "AddColumnDisplayName";

	/**
	 * the option checks if using header line with column display name.
	 */
	String ADD_COLUMN_NAME = "AddColumnName";

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

	/**
	 * Get flag to add additional carriage return
	 *
	 * @return additional carriage return is to be set
	 */
	boolean getAddCR();

	/**
	 * Set additional carriage return
	 *
	 * @param addCR carriage return is to be set
	 */
	void setAddCR(boolean addCR);

	/**
	 * Get flag to use header line with column display name
	 *
	 * @return header line with column display name is to be set
	 */
	boolean getAddColumnDisplayName();

	/**
	 * Set header line column display name
	 *
	 * @param addColumnDisplayName header line with column display name
	 */
	void setAddColumnDisplayName(boolean addColumnDisplayName);

	/**
	 * Get flag to use header line with column name
	 *
	 * @return header line with column name is to be set
	 */
	boolean getAddColumnName();

	/**
	 * Set header line column name
	 *
	 * @param addColumnDisplayName header line with column name
	 */
	void setAddColumnName(boolean addColumnDisplayName);
}
