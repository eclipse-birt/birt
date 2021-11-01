/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.util.ArrayList;

import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IGetParameterDefinitionTask;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>HTMLRenderOption test</b>
 * <p>
 * This case tests methods in HTMLRenderOption API.
 */
public class HTMLRenderOptionTest extends EngineCase {

	private String name = "case1";

	final static String INPUT = "case1.rptdesign";
	private String rptdesign = this.genInputFile(INPUT);
	private IGetParameterDefinitionTask task = null;

	/**
	 * @param name
	 */
	public HTMLRenderOptionTest(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}

	/**
	 * Test suite()
	 *
	 * @return
	 */
	public static Test suite() {
		return new TestSuite(HTMLRenderOptionTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
		IReportRunnable reportRunnable = engine.openReportDesign(rptdesign);
		task = engine.createGetParameterDefinitionTask(reportRunnable);
		assertTrue(task.getErrors().size() == 0);

	}

	protected void tearDown() throws Exception {
		task.close();
		super.tearDown();
		removeResource();
	}

	/**
	 * Test setEmbeddable(boolean embeddable) method Test getEmbeddable() method
	 */
	public void testGetEmbeddable() throws Exception {
		HTMLRenderOption option = new HTMLRenderOption();
		boolean bEmbed = true, bEmbedGet;
		option.setEmbeddable(bEmbed);
		bEmbedGet = option.getEmbeddable();
		assertEquals("set/getEmbeddable() fail", bEmbed, bEmbedGet);

	}

	/**
	 * Test setUserAgent(java.lang.String userAgent) method Test getUserAgent()
	 * method
	 */
	public void testGetUserAgent() {
		String agent = "agent", agentGet;
		HTMLRenderOption option = new HTMLRenderOption();
		option.setUserAgent(agent);
		agentGet = option.getUserAgent();
		assertEquals("set/getUserAgent() fail", agent, agentGet);
	}

	/**
	 * Test setActionHandle(IHTMLActionHandler) method Test getActionHandle() method
	 */
	public void testGetActionHandle() {
		HTMLActionHandler htmlAction = new HTMLActionHandler();
		HTMLRenderOption option = new HTMLRenderOption();
		option.setActionHandle(htmlAction);
		assertNotNull(option.getActionHandle());

	}

	/**
	 * Test setDisplayFilterIcon(boolean displayFilterIcon) method Test
	 * getDisplayFilterIcon() method
	 */
	public void testGetDisplayFilterIcon() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setDisplayFilterIcon(true);
		assertTrue(option.getDisplayFilterIcon());
	}

	/**
	 * Test setDisplayGroupIcon(boolean displayGroupIcon) method
	 * getDisplayGroupIcon()
	 */
	public void testGetDisplayGroupIcon() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setDisplayGroupIcon(false);
		assertFalse(option.getDisplayGroupIcon());
	}

	/**
	 * Test setEnableMetadata(boolean enableMetadata) getEnableMetadata()
	 */
	public void testGetEnableMetadata() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setEnableMetadata(false);
		assertFalse(option.getEnableMetadata());
	}

	/**
	 * Test setHtmlPagination(boolean pagination) getHtmlPagination()
	 */
	public void testGetHtmlPagination() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setHtmlPagination(true);
		assertTrue(option.getHtmlPagination());
	}

	/**
	 * Test setHtmlRtLFlag(boolean option) getHtmlRtLFlag()
	 */
	public void testGetHtmlRtLFlag() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setHtmlRtLFlag(true);
		assertTrue(option.getHtmlRtLFlag());
	}

	/**
	 * Test setHtmlTitle(java.lang.String htmlTitle) getHtmlTitle()
	 */
	public void testGetHtmlTitle() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setHtmlTitle("HTMLTITLE");
		assertEquals("HTMLTITLE", option.getHtmlTitle());
	}

	/**
	 * Test setInstanceIDs (java.util.List instanceIDs) getInstanceIDs()
	 */
	public void testGetInstanceIDs() {
		ArrayList ins = new ArrayList();

		HTMLRenderOption option = new HTMLRenderOption();
		option.setInstanceIDs(ins);
		assertNotNull(option.getInstanceIDs());

	}

	/**
	 * Test setMasterPageContent(boolean show) getMasterPageContent()
	 */
	public void testGetMasterPageContent() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setMasterPageContent(true);
		assertTrue(option.getMasterPageContent());
	}

	/**
	 * Test setPageFooterFloatFlag(boolean option) getPageFooterFloatFlag()
	 */
	public void testGetPageFooterFloatFlag() {
		HTMLRenderOption option = new HTMLRenderOption();
		option.setPageFooterFloatFlag(true);
		assertTrue(option.getPageFooterFloatFlag());
	}

}
