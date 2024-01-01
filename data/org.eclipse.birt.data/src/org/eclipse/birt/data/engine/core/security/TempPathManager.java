/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.data.engine.core.security;

import java.io.File;

/**
 *
 */

public class TempPathManager {
	private String tmpPath = System.getProperty("java.io.tmpdir");

	public void setTempPath(String tmpPath) {
		if (tmpPath.endsWith("" + File.separatorChar)) {
			this.tmpPath = tmpPath.substring(0, tmpPath.length() - 1);
		} else {
			this.tmpPath = tmpPath;
		}
	}

	public String getTempFileName(String fileNamePrefix, int objectID, String extName) {
		checkTempDir();
		if (extName == null || extName.equals("")) {
			return tmpPath + File.separatorChar + fileNamePrefix + objectID;
		}
		return tmpPath + File.separatorChar + fileNamePrefix + objectID + "." + extName;
	}

	private void checkTempDir() {
		File tmpDir = new File(tmpPath);
		if (!tmpDir.exists() || !tmpDir.isDirectory()) {
			tmpDir.mkdirs();
		}
		if (!tmpDir.exists() || !tmpDir.isDirectory()) {
			tmpPath = System.getProperty("java.io.tmpdir");
		}
	}
}
