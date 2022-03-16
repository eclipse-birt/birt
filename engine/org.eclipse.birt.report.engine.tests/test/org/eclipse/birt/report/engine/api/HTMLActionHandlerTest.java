/*******************************************************************************
 * Copyright (c) 2004, 2005 Actuate Corporation.
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

package org.eclipse.birt.report.engine.api;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.ActionContent;

/**
 *
 */

public class HTMLActionHandlerTest extends EngineCase {

	/**
	 * API test on HTMLActionHandle.getURL( ) method
	 */
	public void testGetURL() {
		HTMLRenderContext context = new HTMLRenderContext();
		context.setBaseURL("http://localhost/birt/servlet"); //$NON-NLS-1$
		context.setImageDirectory("image"); //$NON-NLS-1$
		context.setBaseImageURL("http://localhost/birt/image"); //$NON-NLS-1$

		/*
		 * test on set{normal_bookmark , blank_bookmark , special_char_bookmark}
		 */
		HTMLActionHandler handler = null;
		IHyperlinkAction action = null;
		IAction act = null;
		String url = null;
		String[] bookmarks = { "bookmark", // normal bookmark
				"", // blank bookmark
				"/()=?`!\"?$?:;_?????", // special char bookmark
				null // null
		};
		for (int size = bookmarks.length, index = 0; index < size; index++) {
			handler = new HTMLActionHandler();
			action = new ActionContent();
			action.setBookmark(bookmarks[index]);
			act = new Action(action);
			url = handler.getURL(act, context);
			if (bookmarks[index] != null) {
				assertEquals("#" + bookmarks[index], url);
			} else {
				assertNull(url);
			}
		}
	}

	/**
	 * API test on HTMLActionHandle.getURLHyperlink( ) method
	 */
	public void testGetURLHyperlink() {
		HTMLRenderContext context = new HTMLRenderContext();
		context.setBaseURL("http://localhost/birt/servlet"); //$NON-NLS-1$
		context.setImageDirectory("image"); //$NON-NLS-1$
		context.setBaseImageURL("http://localhost/birt/image"); //$NON-NLS-1$

		HTMLActionHandler handler = null;
		String target = "_blank";
		IHyperlinkAction action = null;
		IAction act = null;
		String url = null;

		String[] hyperlinks = { "hyperlink", "", "/()=?`!\"?$?:;_?????", null };
		for (int size = hyperlinks.length, index = 0; index < size; index++) {
			handler = new HTMLActionHandler();
			action = new ActionContent();
			action.setHyperlink(hyperlinks[index], target);
			act = new Action(action);
			url = handler.getURL(act, context);
			if (hyperlinks[index] != null) {
				assertEquals(hyperlinks[index], url);
			} else {
				assertNull(url);
			}
		}
	}

	/**
	 * API test on HTMLActionHandler.appendReportDesignName( ) method
	 */
	public void testAppendReportDesignName() {
		HTMLActionHandler handler = new HTMLActionHandler();
		StringBuffer buffer = new StringBuffer();
		String reportName = "testReportName";
		String goldenDesignName = "?__report=testReportName";
		HTMLActionHandlerUtil.appendReportDesignName(handler, buffer, reportName);
		assertTrue(goldenDesignName.equals(buffer.toString()));
	}

	/**
	 * API test on HTMLActionHandler.appendFormat( ) method
	 */
	public void testAppendFormat() {
		HTMLActionHandler handler = new HTMLActionHandler();
		StringBuffer buffer = null;
		String[] formats = { "html", "pdf" };
		String[] goldenFormats = { "&__format=html", "&__format=pdf" };
		assertTrue(formats.length == goldenFormats.length);
		for (int length = formats.length, index = 0; index < length; index++) {
			buffer = new StringBuffer();
			HTMLActionHandlerUtil.appendFormat(handler, buffer, formats[index]);
			assertTrue(goldenFormats[index].equals(buffer.toString()));
		}
	}
}
