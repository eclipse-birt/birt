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

package org.eclipse.birt.report.engine.emitter;

import java.util.HashMap;

import org.eclipse.birt.report.engine.api.HTMLActionHandler;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.IAction;
import org.eclipse.birt.report.engine.api.impl.Action;
import org.eclipse.birt.report.engine.content.IHyperlinkAction;
import org.eclipse.birt.report.engine.content.impl.ActionContent;

import junit.framework.TestCase;

/**
 * Test case
 *
 *
 */
public class EmbeddedHyperlinkProcessorTest extends TestCase {

	HTMLActionHandler processor;
	HTMLRenderContext context;

	@Override
	protected void setUp() {
		processor = new HTMLActionHandler();
		context = new HTMLRenderContext();
		context.setBaseURL("http://localhost/birt/servlet"); //$NON-NLS-1$
		context.setImageDirectory("image"); //$NON-NLS-1$
		context.setBaseImageURL("http://localhost/birt/image"); //$NON-NLS-1$
	}

	public void testBookmark() {
		String bookmark = "bookmark";//$NON-NLS-1$
		IHyperlinkAction action = new ActionContent();
		action.setBookmark(bookmark);
		IAction act = new Action(action);
		String url = processor.getURL(act, context);

		assertEquals(url, "#" + bookmark);
	}

	public void testHyperlink() {
		String hyperlink = "hyperlink";//$NON-NLS-1$
		String target = "target";//$NON-NLS-1$
		IHyperlinkAction action = new ActionContent();
		action.setHyperlink(hyperlink, target);
		IAction act = new Action(action);
		String url = processor.getURL(act, context);

		assertEquals(url, hyperlink);
	}

	public void testDrillThrough() {
		String bookmark = "bookmark";//$NON-NLS-1$
		String reportName = "report";//$NON-NLS-1$
		HashMap params = new HashMap();
		params.put("param1", "string");
		String goldenUrl = "http://localhost/birt/servlet?__report=report&__format=html&param1=string&__overwrite=true&__bookmark=bookmark";

		IHyperlinkAction action = new ActionContent();
		action.setDrillThrough(bookmark, true, reportName, params, null, "_blank", "html", null);//$NON-NLS-1$
		IAction act = new Action(action);
		String url = processor.getURL(act, context);

		assertEquals(goldenUrl, url);// $NON-NLS-1$
	}
}
