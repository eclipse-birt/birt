/*******************************************************************************
 * Copyright (c) 2010 Actuate Corporation.
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
package org.eclipse.birt.report.engine.odf.writer;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.util.Map;

import org.eclipse.birt.report.engine.emitter.XMLWriter;

/**
 * Extended XML Writer with additional features.
 *
 */
public class ExtendedXMLWriter extends XMLWriter {
	public void flush() {
		super.flush();
	}

	public Writer getWriter() {
		return writer;
	}

	public void write(InputStream input) throws IOException {
		flush();
		Reader reader = new InputStreamReader(input, "UTF-8");
		char[] buffer = new char[1024];

		int count;
		while ((count = reader.read(buffer)) > 0) {
			writer.write(buffer, 0, count);
		}
		writer.flush();
	}

	public void attributes(Map<String, String> attributes) {
		for (Map.Entry<String, String> entry : attributes.entrySet()) {
			attribute(entry.getKey(), entry.getValue());
		}
	}
}
