/*******************************************************************************
 * Copyright (c) 2004, 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.executor.template;

import java.util.HashMap;

import junit.framework.TestCase;

import org.eclipse.birt.core.template.TemplateParser;
import org.eclipse.birt.core.template.TextTemplate;
import org.eclipse.birt.report.engine.executor.ExecutionContext;

public class TemplateExecutorTest extends TestCase {

	public void testExecutor() {
		String input = "<value-of>textData</value-of> DEF <image type=''>imageData</image>";
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("textData", "RESULT");
		values.put("imageData", new byte[] {});
		String output = execute(input, values);
		boolean matched = output.matches("RESULT DEF <img src=.*>");
		assertTrue(matched);
	}

	public void testFormat() {
		String input = "<value-of format=\"0.00\">textData</value-of>";
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("textData", 78.9711);
		String output = execute(input, values);
		assertEquals("78.97", output);
	}

	public void testFormatExpression() {
		String input = "<value-of format-expr=format>textData</value-of>";
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("textData", 78.9711);
		values.put("format", "0.00");
		String output = execute(input, values);
		assertEquals("78.97", output);
	}

	/**
	 * https://bugs.eclipse.org/278728 <VALUE-OF > returns nothing in this case.
	 */
	public void testExpressionWithWhitespace() {
		String input = "<value-of format-expr=\" format \"> textData </value-of>";
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("textData", 78.9711);
		values.put("format", "0.00");
		String output = execute(input, values);
		assertEquals("78.97", output);
	}

	private String execute(String text, HashMap<String, Object> values) {
		TextTemplate template = new TemplateParser().parse(text);
		ExecutionContext context = new ExecutionContext();
		TemplateExecutor executor = new TemplateExecutor(context);
		return executor.execute(template, values);
	}
}
