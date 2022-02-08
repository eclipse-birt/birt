/*******************************************************************************
 * Copyright (c) 2009 Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import junit.framework.TestCase;

/**
 * Unit test for Class HTMLWriter.
 * 
 */
public class HTMLWriterTest extends TestCase {

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
	}

	/**
	 * Test HTMLWriter getEscapedStr()
	 * <p>
	 * Test Case:
	 * <ul>
	 * <li>getEscapedStr</li>
	 * </ul>
	 * Excepted:
	 * <ul>
	 * <li>all the corresponding characters are transformed</li>
	 * </ul>
	 * 
	 * @throws IOException
	 */
	public void testGetEscapeStr() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		HTMLWriter writer = new HTMLWriter();

		writer.open(stream);

		writer.text("&<>\"1 2  3   4    "); //$NON-NLS-1$
		// flush the buffer
		writer.endWriter();
		writer.close();
		assertEquals("&amp;&lt;>\"1 2&#xa0; 3&#xa0;&#xa0; 4&#xa0;&#xa0;&#xa0;&#xa0;", //$NON-NLS-1$
				stream.toString().replaceAll("[\\r|\\t|\\n]*", "")); //$NON-NLS-1$ //$NON-NLS-2$

		stream.close();
	}

	public void testWhiteSpace() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		HTMLWriter writer = new HTMLWriter();

		writer.open(stream);

		writer.text(" a  b \n  abc  \r\n   abc cde"); //$NON-NLS-1$
		// flush the buffer
		writer.endWriter();
		writer.close();
		assertEquals("&#xa0;a&#xa0; b&#xa0;<br/>&#xa0; abc&#xa0;&#xa0;<br/>&#xa0;&#xa0; abc cde", //$NON-NLS-1$
				stream.toString().replaceAll("[\\r|\\t|\\n]*", "")); //$NON-NLS-1$ //$NON-NLS-2$

		stream.close();
	}

	public void testStyleEscape() throws IOException {
		ByteArrayOutputStream stream = new ByteArrayOutputStream();
		HTMLWriter writer = new HTMLWriter();
		writer.open(stream);

		writer.attribute("style", " font-family: Arial,\"Courier New\",\"Franklin Gothic Book\",'ABC{!}\"DEF'");
		// flush the buffer
		writer.endWriter();
		writer.close();
		assertEquals(
				" style=\" font-family: Arial,&#34;Courier New&#34;,&#34;Franklin Gothic Book&#34;,'ABC{!}&#34;DEF'\"", //$NON-NLS-1$
				stream.toString().replaceAll("[\\r|\\t|\\n]*", "")); //$NON-NLS-1$ //$NON-NLS-2$

		stream.close();
	}
}
