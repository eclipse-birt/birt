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

package org.eclipse.birt.report.engine.ooxml.writer;

import java.io.OutputStream;
import java.util.logging.Level;

import org.eclipse.birt.report.engine.emitter.XMLWriter;

public class OOXmlWriter extends XMLWriter {

	private OutputStream out;

	@Override
	public void open(OutputStream outputStream, String encoding) {
		super.open(outputStream, encoding);
		this.out = outputStream;
		this.bIndent = false;
	}

	@Override
	public void startWriter() {
		print("<?xml version=\"1.0\" encoding=\"" + encoding + "\" standalone=\"yes\"?>");
	}

	protected String escapeAttrValue(String s) {
		StringBuilder result = null;
		char[] s2char = s.toCharArray();

		for (int i = 0, max = s2char.length, delta = 0; i < max; i++) {
			char c = s2char[i];
			String replacement = null;
			// Filters the char not defined.
			if (!(c == 0x9 || c == 0xA || c == 0xD || (c >= 0x20 && c <= 0xD7FF) || (c >= 0xE000 && c <= 0xFFFD))) {
				// Ignores the illegal character.
				replacement = ""; //$NON-NLS-1$
				log.log(Level.WARNING, "Ignore the illegal XML character: 0x{0};", Integer //$NON-NLS-1$
						.toHexString(c));
			}
			if (c == '&') {
				replacement = "&amp;"; //$NON-NLS-1$
			} else if (c == '"') {
				replacement = "&quot;"; //$NON-NLS-1$
			} else if (c == '\r') {
				replacement = "&#13;"; //$NON-NLS-1$
			} else if (c == '<') {
				replacement = "&lt;"; //$NON-NLS-1$
			}
			if (replacement != null) {
				if (result == null) {
					result = new StringBuilder(s);
				}
				result.replace(i + delta, i + delta + 1, replacement);
				delta += (replacement.length() - 1);
			}
		}
		if (result == null) {
			return s;
		}
		return result.toString();
	}

	@Override
	public void attribute(String attrName, String attrValue) {
		if (attrValue != null) {
			print(' ');
			print(attrName);
			print("=\""); //$NON-NLS-1$
			print(escapeAttrValue(attrValue));
			print('\"');
		}
	}

	public void nameSpace(String name, String value) {
		attribute("xmlns:" + name, value);
	}

	public void attribute(String attrName, long value) {
		attribute(attrName, Long.toString(value));
	}

	public OutputStream getOutputStream() {
		return out;
	}
}
