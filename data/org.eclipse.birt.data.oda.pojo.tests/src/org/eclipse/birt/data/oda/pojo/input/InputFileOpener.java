/*******************************************************************************
 * Copyright (c) 2013 Actuate Corporation.
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
package org.eclipse.birt.data.oda.pojo.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 *
 */

public class InputFileOpener {
	public static final String SIMPLE_QUERY_FILE = "pojoQuery.simple.xml"; //$NON-NLS-1$

	private InputFileOpener() {

	}

	public static InputStream openFile(String fileName) {
		return InputFileOpener.class.getResourceAsStream(fileName);
	}

	/**
	 * Read all data from a InputStream and save it in a String
	 *
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String fromInputStream(InputStream in) throws IOException {
		assert in != null;
		StringBuilder sb = new StringBuilder();
		char[] buf = new char[1024];
		Reader reader = new BufferedReader(new InputStreamReader(in));
		int len = reader.read(buf);
		while (len != -1) {
			sb.append(buf, 0, len);
			len = reader.read(buf);
		}
		reader.close();

		return sb.toString();
	}
}
