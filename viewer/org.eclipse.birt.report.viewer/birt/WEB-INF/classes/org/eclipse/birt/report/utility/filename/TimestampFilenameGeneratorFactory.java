/*************************************************************************************
 * Copyright (c) 2008 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/
package org.eclipse.birt.report.utility.filename;

import javax.servlet.ServletContext;

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
	 * @see org.eclipse.birt.report.utility.filename.IFilenameGeneratorFactory#createFilenameGenerator(javax.servlet.ServletContext)
	 */
	public IFilenameGenerator createFilenameGenerator(ServletContext context) {
		String datePattern = context.getInitParameter(INIT_PARAMETER_FILENAME_DATE_PATTERN);
		return new TimestampFilenameGenerator(datePattern);
	}
}
