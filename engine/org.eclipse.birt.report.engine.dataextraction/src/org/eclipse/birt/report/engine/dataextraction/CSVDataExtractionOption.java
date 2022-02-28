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

import java.util.Map;

/**
 * Extends Data Extraction options for CSV format
 *
 */
public class CSVDataExtractionOption extends CommonDataExtractionOption implements ICSVDataExtractionOption {

	public CSVDataExtractionOption() {
		super();
	}

	public CSVDataExtractionOption(Map options) {
		super(options);
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption#getSeparator()
	 */
	@Override
	public String getSeparator() {
		return getStringOption(OUTPUT_SEPARATOR);
	}

	/**
	 * @see org.eclipse.birt.report.engine.dataextraction.ICSVDataExtractionOption#setSeparator(java.lang.String)
	 */
	@Override
	public void setSeparator(String sep) {
		setOption(OUTPUT_SEPARATOR, sep);
	}

	@Override
	public boolean getAddCR() {
		return getBooleanOption(ADD_CR_LINE_BREAK, false);
	}

	@Override
	public void setAddCR(boolean addCR) {
		setOption(ADD_CR_LINE_BREAK, addCR);
	}

}
