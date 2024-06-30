/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
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

import java.util.Map;

/**
 * Default export filename generator. Generates an export file name based on the
 * report design.
 */
public class DefaultFilenameGenerator implements IFilenameGenerator {

	public static final String DEFAULT_FILENAME = "BIRTReport"; //$NON-NLS-1$

	public DefaultFilenameGenerator() {

	}

	/**
	 * @see org.eclipse.birt.report.utility.filename.IFilenameGenerator#getExportFilename(String,String,Map)
	 */
	@Override
	public String getFilename(String baseName, String extension, String outputType, Map options) {
		return makeFileName(baseName, extension);
	}

	/**
	 * Makes a filename using the target extension from the options.
	 *
	 * @param fileName file name which extension must be replaced
	 * @return file name with replaced extension
	 */
	public static String makeFileName(String fileName, String extensionName) {
		String baseName = fileName;
		if (baseName == null || baseName.trim().length() <= 0) {
			baseName = DEFAULT_FILENAME;
		}

		// check whether the file name contains non US-ASCII characters
		for (int i = 0; i < baseName.length(); i++) {
			char c = baseName.charAt(i);

			// char is from 0-127
			if (c < 0x00 || c >= 0x80) {
				baseName = DEFAULT_FILENAME;
				break;
			}
		}

		// append extension name
		if (extensionName != null && extensionName.length() > 0) {
			baseName += "." + extensionName; //$NON-NLS-1$
		}
		return baseName;
	}

}
