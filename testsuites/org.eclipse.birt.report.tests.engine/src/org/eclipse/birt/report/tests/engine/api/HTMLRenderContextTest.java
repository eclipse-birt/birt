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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.RenderOptionBase;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>HTMLRenderContext test</b>
 * <p>
 * This case tests methods in HTMLRenderContext API.
 */
public class HTMLRenderContextTest extends EngineCase {

	/**
	 * @param name
	 */
	public HTMLRenderContextTest(String name) {
		super(name);
	}

	/**
	 * Test suite()
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(HTMLRenderContextTest.class);
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
	 * Test setBaseImageURL(java.lang.String baseImageURL) method Test
	 * getBaseImageURL() method
	 */
	public void testGetBaseImageURL() {
		HTMLRenderContext context = new HTMLRenderContext();
		String baseURL = "image", baseURLGet;
		context.setBaseImageURL(baseURL);
		baseURLGet = context.getBaseImageURL();
		assertEquals("getBaseImageURL() fail", baseURL, baseURLGet);
	}

	/**
	 * Test setBaseURL(java.lang.String baseURL) method Test getBaseURL() method
	 */
	public void testGetBaseURL() {
		HTMLRenderContext context = new HTMLRenderContext();
		String baseURL = "image", baseURLGet;
		context.setBaseURL(baseURL);
		baseURLGet = context.getBaseURL();
		assertEquals("getBaseURL() fail", baseURL, baseURLGet);
	}

	/**
	 * Test setImageDirectory(java.lang.String imageDirectory) method Test
	 * getImageDirectory() method
	 */
	public void testGetImageDirectory() {
		HTMLRenderContext context = new HTMLRenderContext();
		String dir = "image", dirGet;
		context.setImageDirectory(dir);
		dirGet = context.getImageDirectory();
		assertEquals("getBaseURL() fail", dir, dirGet);

		String separator = "/";
		dir = "image" + separator + "doc";
		context.setImageDirectory(dir);
		assertEquals("image/doc", context.getImageDirectory());

	}

	/**
	 * Test setRenderOption(IRenderOption) method Test getRenderOption() method
	 */

	public void testGetRenderOption() {
		RenderOptionBase rendop = new RenderOptionBase();
		rendop.setOutputFormat("fo");
		rendop.setOutputFileName("outputfile");

		HTMLRenderContext context = new HTMLRenderContext();
		context.setRenderOption(rendop);

		RenderOptionBase ropb = (RenderOptionBase) (context.getRenderOption());

		assertEquals("fo", ropb.getOutputFormat());
		Map outsetting = new HashMap();
		outsetting = ropb.getOutputSetting();

		assertFalse(outsetting.isEmpty());
		assertEquals(2, outsetting.size());

		ropb.getOutputSetting().put(HTMLRenderOption.URL_ENCODING, "UTF-8");
		assertEquals(3, outsetting.size());

	}

	/**
	 * Test setSupportedImageFormats(java.lang.String formats) method Test
	 * getSupportedImageFormats() method
	 */

	public void testGetSupportedImageFormats() {
		HTMLRenderContext context = new HTMLRenderContext();
		String baseURL = "Format", baseURLGet;
		context.setBaseImageURL(baseURL);
		baseURLGet = context.getBaseImageURL();
		assertEquals("getBaseImageURL() fail", baseURL, baseURLGet);
	}

}
