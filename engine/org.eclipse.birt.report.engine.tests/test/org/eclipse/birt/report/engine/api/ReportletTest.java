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

package org.eclipse.birt.report.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

public class ReportletTest extends EngineCase {

	static final String REPORT_DESIGN_RESOURCE = "org/eclipse/birt/report/engine/api/reportlet.rptdesign";
	static final String REPORT_DESIGN_RESOURCE2 = "org/eclipse/birt/report/engine/api/reportlet1.rptdesign";

	public void setUp() {
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE, REPORT_DESIGN);
		// create the report engine using default config
		engine = createReportEngine();
	}

	public void tearDown() {
		// shut down the engine.
		engine.shutdown();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
	}

	public void testReportlet() throws Exception {
		ArrayList iidList = new ArrayList();

		// first execute the report to get the reportlet
		IReportRunnable runnable = engine.openReportDesign(REPORT_DESIGN);
		IRunTask task = engine.createRunTask(runnable);
		task.run(REPORT_DOCUMENT);
		task.close();

		// render the whole text to html.
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		IRenderTask render = engine.createRenderTask(document);
		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		HTMLRenderOption option = new HTMLRenderOption();
		option.setOutputFormat("html");
		option.setOutputStream(ostream);
		option.setEnableMetadata(true);
		render.setRenderOption(option);
		render.render();
		render.close();

		// for all the reportlets
		String content = ostream.toString("utf-8");
		Pattern iidPattern = Pattern.compile("iid=\"([^\"]*)\"");
		Matcher matcher = iidPattern.matcher(content);
		while (matcher.find()) {
			render = engine.createRenderTask(document);
			String strIid = matcher.group(1);
			InstanceID iid = InstanceID.parse(strIid);
			long designId = iid.getComponentID();
			runnable = render.getReportRunnable();
			ReportDesignHandle report = (ReportDesignHandle) runnable.getDesignHandle();
			DesignElementHandle element = report.getElementByID(designId);
			if (element instanceof TableHandle) {
				// we get the report let
				iidList.add(iid);
				render = engine.createRenderTask(document);
				option = new HTMLRenderOption();
				option.setOutputFormat("html");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				option.setOutputStream(out);
				render.setRenderOption(option);
				render.setInstanceID(iid.toUniqueString());

				render.render();

				assertTrue(render.getErrors().isEmpty());
				render.close();
				assertTrue(out.toString("utf-8").length() > 1500);
			}
		}

		/*
		 * API test on IReportDocument.getPageNumber( InstanceID ) And here only test on
		 * the first *offset*
		 */
		int[] goldenPageNumbers = new int[] { 1 };/* is the first page */
		InstanceID iidTemp = (InstanceID) iidList.get(0);
		assertTrue(goldenPageNumbers[0] == document.getPageNumber(iidTemp));
		assertTrue(document.getInstanceOffset(iidTemp) != -1);
		render.close();
		document.close();
	}

	public void testRenderReportlet() throws Exception {
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		copyResource(REPORT_DESIGN_RESOURCE2, REPORT_DESIGN);
		// create the report engine using default config
		engine = createReportEngine();

		createReportDocument();
		doRenderReportletTest();
		engine.shutdown();
		removeFile(REPORT_DESIGN);
		removeFile(REPORT_DOCUMENT);
	}

	protected void doRenderReportletTest() throws Exception {
		String BOOKMARK_1 = "bookmark1";
		String BOOKMARK_2 = "bookmark2";
		String CONTENT_1 = "test_reportlet_table1";
		String CONTENT_2 = "test_reportlet_table2";
		IReportDocument reportDoc = engine.openReportDocument(REPORT_DOCUMENT);
		try {
			// get the page number
			List bookmarks = reportDoc.getBookmarks();
			assertEquals(2, bookmarks.size());
			assertTrue(bookmarks.contains(BOOKMARK_1));
			assertTrue(bookmarks.contains(BOOKMARK_2));

			// test reportlet through bookmark
			testReportletWithBookmark(reportDoc, BOOKMARK_1, CONTENT_1, CONTENT_2);
			testReportletWithBookmark(reportDoc, BOOKMARK_2, CONTENT_2, CONTENT_1);

			// test reportlet through reportlet instanceId
			List<InstanceID> instanceIds = getTableInstanceIds();
			assertEquals(2, instanceIds.size());

			testReportletWithInstanceId(reportDoc, instanceIds.get(0), CONTENT_1, CONTENT_2);
			testReportletWithInstanceId(reportDoc, instanceIds.get(1), CONTENT_2, CONTENT_1);
		} finally {
			reportDoc.close();
		}
	}

	private void testReportletWithBookmark(IReportDocument reportDoc, String bookmark, String contain,
			String notContain) throws EngineException {
		IRenderTask task = engine.createRenderTask(reportDoc);
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			IRenderOption option = new HTMLRenderOption();
			option.setOutputFormat("html"); //$NON-NLS-1$
			option.setOutputStream(outputStream);
			task.setRenderOption(option);

			task.setReportlet(bookmark);
			task.render();

			String content = new String(outputStream.toByteArray());
			assertTrue(contains(content, contain));
			assertFalse(contains(content, notContain));
		} finally {
			task.close();
		}
	}

	private void testReportletWithInstanceId(IReportDocument reportDoc, InstanceID iid, String contain,
			String notContain) throws EngineException {
		IRenderTask task = engine.createRenderTask(reportDoc);
		try {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			IRenderOption option = new HTMLRenderOption();
			option.setOutputFormat("html"); //$NON-NLS-1$
			option.setOutputStream(outputStream);
			task.setRenderOption(option);

			task.setInstanceID(iid);
			task.render();

			String content = new String(outputStream.toByteArray());
			assertTrue(contains(content, contain));
			assertFalse(contains(content, notContain));
		} finally {
			task.close();
		}
	}

	private boolean contains(String content, String searchString) {
		return content.indexOf(searchString) >= 0;
	}

	private List<InstanceID> getTableInstanceIds() throws EngineException, UnsupportedEncodingException {
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		try {
			ByteArrayOutputStream ostream = new ByteArrayOutputStream();
			IRenderTask render = engine.createRenderTask(document);
			try {
				HTMLRenderOption option = new HTMLRenderOption();
				option.setOutputFormat("html");
				option.setOutputStream(ostream);
				option.setEnableMetadata(true);
				render.setRenderOption(option);
				render.render();
			} finally {
				render.close();
			}

			IReportRunnable runnable = document.getReportRunnable();
			ReportDesignHandle report = (ReportDesignHandle) runnable.getDesignHandle();
			List<InstanceID> result = new ArrayList<InstanceID>();

			// for all the reportlets
			String content = ostream.toString("utf-8");
			Pattern iidPattern = Pattern.compile("iid=\"([^\"]*)\"");
			Matcher matcher = iidPattern.matcher(content);
			while (matcher.find()) {
				String strIid = matcher.group(1);
				InstanceID iid = InstanceID.parse(strIid);
				long designId = iid.getComponentID();
				DesignElementHandle element = report.getElementByID(designId);
				if (element instanceof TableHandle) {
					result.add(iid);
				}
			}
			return result;
		} finally {
			document.close();
		}
	}
}
