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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * File name generator which inserts a time stamp in the name.
 * 
 * @see TimestampFilenameGeneratorFactory
 */
public class TimestampFilenameGenerator implements IFilenameGenerator {
	private static final String DEFAULT_DATE_PATTERN = "yyyyMMdd-HHmmss"; //$NON-NLS-1$

	public String datePattern;

	/**
	 * 
	 */
	public TimestampFilenameGenerator() {
		this(null);
	}

	/**
	 * Constructor.
	 * 
	 * @param datePattern date pattern to use
	 */
	public TimestampFilenameGenerator(String datePattern) {
		if (datePattern == null) {
			datePattern = DEFAULT_DATE_PATTERN;
		}
		this.datePattern = datePattern;
	}

	/**
	 * Returns a file name containing a formatted time stamp.
	 * 
	 * @see IFilenameGenerator#getFilename(String, String, String, Map)
	 */
	public String getFilename(String baseName, String fileExtension, String outputType, Map options) {
		DateFormat dateFormatter = new SimpleDateFormat(datePattern);
		if (fileExtension == null) {
			fileExtension = ""; //$NON-NLS-1$
		}
		return baseName + "_" + dateFormatter.format(new Date()) + "." + fileExtension; //$NON-NLS-1$
	}

}
