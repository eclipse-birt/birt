
/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
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
package org.eclipse.birt.data.engine.impl;

import java.io.File;

import org.eclipse.birt.data.engine.core.security.FileSecurity;

/**
 * The util class which is used to provide utilities for Result Set Cache
 * feature.
 *
 */

class ResultSetCacheUtil {
	private static final String CACHED_FILE_PREFIX = "cachedResultIterator";

	/**
	 * Return the Meta file
	 *
	 * @return
	 */
	static File getMetaFile(String tempDir, String id) {
		File tmpDir = new File(tempDir);
		if (!FileSecurity.fileExist(tmpDir) || !FileSecurity.fileIsDirectory(tmpDir)) {
			FileSecurity.fileExist(tmpDir);
		}
		File file = new File(tempDir + CACHED_FILE_PREFIX + id + "meta");
		return file;
	}

	/**
	 * Return the data file
	 *
	 * @return
	 */
	static File getDataFile(String tempDir, String id) {
		File file = new File(tempDir + CACHED_FILE_PREFIX + id + "data");
		return file;
	}
}
