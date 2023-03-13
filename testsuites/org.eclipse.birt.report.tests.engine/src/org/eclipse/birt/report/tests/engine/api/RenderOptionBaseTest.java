/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;

import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>RenderOptionBase test</b>
 * <p>
 * This case tests methods in RenderOptionBase API.
 */
public class RenderOptionBaseTest extends EngineCase {

	private TestRenderOptionBase optionBase = new TestRenderOptionBase();

	/**
	 * @param name
	 */
	public RenderOptionBaseTest(String name) {
		super(name);
	}

	/**
	 * Test suite()
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(RenderOptionBaseTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();

	}

	@Override
	protected void tearDown() throws Exception {

		super.tearDown();
	}

	/**
	 * Test setOption(java.lang.String name, java.lang.Object value) method Test
	 * getOption() method
	 */
	public void testgetOption() {

		String name = "newoption";
		Object value = "option1";
		optionBase.setOption(name, value);
		assertEquals("set/getOption() fail", optionBase.getOption(name), value);
	}

	/**
	 * Test setOutputFormat(java.lang.String format) method Test getOutputFormat()
	 * method
	 */
	public void testgetOutputFormat() {
		String format = "html", formatGet = "";
		optionBase.setOutputFormat(format);
		formatGet = optionBase.getOutputFormat();
		assertEquals("set/getOutputFormat() fail", format, formatGet);
	}

	/**
	 * Test setOutputFileName(java.lang.String outputFileName) method
	 */
	public void testsetOutputFileName() {
		String name = "ofName", nameGet = "";
		optionBase.setOutputFileName(name);
		nameGet = (String) optionBase.getOption(TestRenderOptionBase.OUTPUT_FILE_NAME);
		assertEquals("setOutputFileName() fail", name, nameGet);
	}

	/**
	 * Test setOutputStream(java.io.OutputStream ostream) method
	 */
	public void testsetOutputStream() {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		optionBase.setOutputStream(bos);
		ByteArrayOutputStream bosGet = (ByteArrayOutputStream) optionBase.getOutputSetting()
				.get(TestRenderOptionBase.OUTPUT_STREAM);
		assertEquals("setOutputStream(java.io.OutputStream ostream) fail", bos, bosGet);

	}

	/**
	 * Test getOutputSetting() method
	 */

	public void testgetOutputSetting() {
		assertEquals("locale", TestRenderOptionBase.LOCALE);
		assertEquals("Format", TestRenderOptionBase.OUTPUT_FORMAT);
		assertEquals("imageHandler", TestRenderOptionBase.IMAGE_HANDLER);
		assertEquals("fo", TestRenderOptionBase.OUTPUT_FORMAT_FO);
	}

	/**
	 * Test setOption() method
	 */

	public void testsetOption() {
		optionBase.setOption("LOCALE", "CHINA");
		assertEquals("CHINA", optionBase.getOption("LOCALE"));
	}

	/**
	 * Test setOutputFormat() method
	 */

	public void testsetOutputFormat() {

		optionBase.setOutputFormat("OutputFormat");
		assertEquals("OutputFormat", optionBase.getOutputFormat());
	}

}
