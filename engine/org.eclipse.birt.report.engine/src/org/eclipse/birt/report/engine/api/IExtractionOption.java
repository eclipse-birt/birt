/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.engine.api;

import java.io.OutputStream;
import java.util.Map;

public interface IExtractionOption extends ITaskOption {

	public static final String OUTPUT_FORMAT = "Format"; //$NON-NLS-1$

	public static final String OUTPUT_FILE_NAME = "outputFile"; //$NON-NLS-1$

	public static final String OUTPUT_STREAM = "outputStream"; //$NON-NLS-1$

	public static final String OPTION_FORMATTER = "extract.formatter";//$NON-NLS-1$

	/**
	 * Set output format.
	 * 
	 * @param format output format.
	 */
	void setOutputFormat(String format);

	/**
	 * Get output format.
	 */
	String getOutputFormat();

	/**
	 * Set output stream.
	 * 
	 * @param out output stream.
	 */
	void setOutputStream(OutputStream out);

	/**
	 * Get output stream.
	 */
	OutputStream getOutputStream();

	/**
	 * Set output file.
	 * 
	 * @param filename name of the output file.
	 */
	void setOutputFile(String filename);

	/**
	 * Get output file name.
	 */
	String getOutputFile();

	/**
	 * Set the formatters used to output the value. The format option is a hash map,
	 * the key can be the column name or column index (start from 1), the value
	 * should be a format pattern. If no format is defined, the value should be
	 * outputted as current implementation.
	 * 
	 * @param formatters
	 */
	void setFormatter(Map formatters);

	/**
	 * Get the format option.
	 * 
	 * @return
	 */
	Map getFormatter();
}
