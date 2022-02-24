/*******************************************************************************
 * Copyright (c) 2021 Contributors to the Eclipse Foundation
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *   See git history
 *******************************************************************************/

package org.eclipse.birt.report.tests.engine.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.content.IDataContent;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.IImageContent;
import org.eclipse.birt.report.engine.content.ILabelContent;
import org.eclipse.birt.report.engine.content.impl.ActionContent;
import org.eclipse.birt.report.tests.engine.BaseEmitter;

/**
 * Test IAction API methods
 */
public class IActionTest extends BaseEmitter {

	final static String INPUT = "IActionTest.rptdesign";
	private String reportName = INPUT;

	public void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT(INPUT, INPUT);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	protected String getReportName() {
		return reportName;
	}

	public void testActionFromReport() throws EngineException {
		runandrender_emitter(EMITTER_HTML, false);
	}

	/**
	 * Test bookmark action
	 */
	public void testBookmarkAction() {
		IHyperlinkAction hyperAction = new ActionContent();
		hyperAction.setBookmark("bk");
		IAction action = new Action(hyperAction);
		assertEquals("bk", action.getBookmark());
		assertEquals("bk", action.getActionString());
		assertEquals(IHyperlinkAction.ACTION_BOOKMARK, action.getType());
		assertFalse(action.isBookmark());
		assertNull(action.getFormat());
		assertNull(action.getParameterBindings());
		assertNull(action.getSearchCriteria());
		assertNull(action.getReportName());
		assertNull(action.getSystemId());
		assertNull(action.getTargetWindow());

		hyperAction.setBookmark("");
		assertEquals("", action.getBookmark());

		String bookmark = "longbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmarklongbookmark";
		hyperAction.setBookmark(bookmark);
		assertEquals(276, action.getBookmark().length());
	}

	/**
	 * Test hyperlink action
	 */
	public void testHyperlinkAction() {
		IHyperlinkAction hyperAction = new ActionContent();
		hyperAction.setHyperlink("http://test", "_blank");
		IAction action = new Action(hyperAction);
		assertEquals("http://test", action.getActionString());
		assertEquals("_blank", action.getTargetWindow());
		assertEquals(IHyperlinkAction.ACTION_HYPERLINK, action.getType());
		assertNull(action.getBookmark());
		assertFalse(action.isBookmark());
		assertNull(action.getFormat());
		assertNull(action.getParameterBindings());
		assertNull(action.getSearchCriteria());
		assertNull(action.getReportName());
		assertNull(action.getSystemId());

		hyperAction.setHyperlink("", "target");
		assertEquals("", action.getActionString());

		hyperAction.setHyperlink(null, "_blank");
		assertNull(action.getActionString());

		hyperAction.setHyperlink("http://test", null);
		assertNull(action.getTargetWindow());

		hyperAction.setHyperlink(null, null);
		assertNull(action.getActionString());
		assertNull(action.getTargetWindow());
	}

	/**
	 * Test Drillthrough action
	 */
	public void testDrillthroughAction() {
		IHyperlinkAction hyperAction = new ActionContent();
		String bk = "bookmark", name = "report1", target = "_blank", format = "html";
		boolean isBk = true;
		Map paramBindings = new HashMap(), searchCriteria = new HashMap();
		hyperAction.setDrillThrough(bk, isBk, name, paramBindings, searchCriteria, target, format);
		IAction action = new Action(hyperAction);
		assertEquals(IHyperlinkAction.ACTION_DRILLTHROUGH, action.getType());
		assertEquals(isBk, action.isBookmark());
		assertEquals(paramBindings, action.getParameterBindings());
		assertEquals(searchCriteria, action.getSearchCriteria());
		assertEquals(target, action.getTargetWindow());
		assertEquals(format, action.getFormat());

		hyperAction.setDrillThrough(null, true, null, null, null, null, null);
		assertEquals(IHyperlinkAction.ACTION_DRILLTHROUGH, action.getType());
		assertTrue(action.isBookmark());
		assertNull(action.getParameterBindings());
		assertNull(action.getSearchCriteria());
		assertNull(action.getTargetWindow());
		assertNull(action.getFormat());
	}

	/**
	 * Test getSystemId() method
	 */
	public void testSystemID() {
		IAction action = new Action("id", null);
		assertEquals("id", action.getSystemId());

		action = new Action(null, null);
		assertNull(action.getSystemId());
	}

	@SuppressWarnings("unchecked")
	public void startImage(IImageContent image) {
		IAction action = new Action(image.getHyperlinkAction());
		assertEquals(IHyperlinkAction.ACTION_DRILLTHROUGH, action.getType());
		assertEquals(reportName, action.getReportName());
		assertTrue(action.getParameterBindings().size() > 0);
		Object value = action.getParameterBindings().get("p1");
		assertTrue(value instanceof List);
		List<String> valueList = (List<String>) value;
		assertTrue(valueList.size() > 0);
		assertEquals("target value", valueList.get(0));
		assertEquals("html", action.getFormat());
		assertEquals("_self", action.getTargetWindow());
		assertEquals("labelbk", action.getBookmark());
		assertTrue(action.isBookmark());
	}

	public void startData(IDataContent data) {
		IAction action = new Action(data.getHyperlinkAction());
		assertEquals(IHyperlinkAction.ACTION_BOOKMARK, action.getType());
		assertEquals("labelbk", action.getBookmark());
		assertEquals("labelbk", action.getActionString());
	}

	public void startLabel(ILabelContent label) {
		if (label.getLabelText().equals("label1")) {
			IAction action = new Action(label.getHyperlinkAction());
			assertEquals(IHyperlinkAction.ACTION_HYPERLINK, action.getType());
			assertEquals("http://label.com", action.getActionString());
			assertEquals("_blank", action.getTargetWindow());
		}
		if (label.getLabelText().equals("label2")) {
			IAction action = new Action(label.getHyperlinkAction());
			assertEquals(IHyperlinkAction.ACTION_DRILLTHROUGH, action.getType());
			assertEquals("IActionTest.rptdocument", action.getReportName());
			assertEquals("_blank", action.getTargetWindow());
			assertEquals(0, action.getParameterBindings().size());
			assertNull(action.getSearchCriteria());
			assertEquals("pdf", action.getFormat());
			assertTrue(action.isBookmark());
			assertNull(action.getBookmark());
		}
	}
}
