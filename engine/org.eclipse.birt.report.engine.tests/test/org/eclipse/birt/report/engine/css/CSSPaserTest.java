/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
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

package org.eclipse.birt.report.engine.css;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.birt.report.engine.css.engine.BIRTCSSEngine;
import org.eclipse.birt.report.engine.css.engine.CSSEngine;
import org.w3c.dom.css.CSSStyleDeclaration;
import org.w3c.dom.css.CSSValue;

import junit.framework.TestCase;

public class CSSPaserTest extends TestCase {

	CSSEngine engine = new BIRTCSSEngine();

	private String parseStyle(String cssText) {
		try {
			CSSStyleDeclaration style = engine.parseStyleDeclaration(cssText);
			return style.getCssText();
		} catch (Exception ex) {
		}
		return "";
	}

	private String parseProperty(String text) {
		int at = text.indexOf(':');
		if (at != -1) {
			String name = text.substring(0, at);
			String valueText = text.substring(at + 1).trim();
			int idx = engine.getPropertyIndex(name);
			if (idx != -1) {
				CSSValue value = engine.parsePropertyValue(idx, valueText);
				return name + ": " + value.getCssText();
			}
		}
		return "";
	}

	public void testPropertyParser() throws Exception {
		InputStream stream = getClass().getClassLoader()
				.getResourceAsStream("org/eclipse/birt/report/engine/css/css_property_test.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		String input = readLine(reader);
		while (input != null) {
			String golden = readLine(reader);
			String output = parseProperty(input);
			assertEquals(golden, output);
			input = readLine(reader);
		}
	}

	public void testStyleParser() throws Exception {
		InputStream stream = getClass().getClassLoader()
				.getResourceAsStream("org/eclipse/birt/report/engine/css/css_style_test.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

		String input = readLine(reader);
		while (input != null) {
			String golden = readLine(reader);
			String output = parseStyle(input);
			assertEquals(golden, output);
			input = readLine(reader);
		}
	}

	protected String readLine(BufferedReader reader) throws IOException {
		String line = reader.readLine();
		while (line != null) {
			if (line.trim().length() != 0 && !line.startsWith("#")) {
				return line.trim();
			}
			line = reader.readLine();
		}
		return null;
	}
}
