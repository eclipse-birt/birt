/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.utility.filename;

import jakarta.servlet.ServletContext;

/**
 * Factory class for the time stamp file name generator.
 *
 * @see TimestampFilenameGenerator
 */
public class TimestampFilenameGeneratorFactory implements IFilenameGeneratorFactory {
	public static final String INIT_PARAMETER_FILENAME_DATE_PATTERN = "TIMESTAMP_FILENAME_GENERATOR_DATE_PATTERN"; //$NON-NLS-1$

	/**
	 * Returns an instance of TimestampFilenameGenerator. Reads the date format from
	 * the INIT_PARAMETER_FILENAME_DATE_PATTERN from the servlet context.
	 *
	 * @see org.eclipse.birt.report.utility.filename.IFilenameGeneratorFactory#createFilenameGenerator(jakarta.servlet.ServletContext)
	 */
	@Override
	public IFilenameGenerator createFilenameGenerator(ServletContext context) {
		String datePattern = context.getInitParameter(INIT_PARAMETER_FILENAME_DATE_PATTERN);
		return new TimestampFilenameGenerator(datePattern);
	}
}
