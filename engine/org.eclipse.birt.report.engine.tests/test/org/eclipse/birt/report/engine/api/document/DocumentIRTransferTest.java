/*******************************************************************************
 * Copyright (c) 2004,2009 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.engine.api.document;

import java.io.ByteArrayOutputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.EngineCase;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IResultMetaData;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.TableHandle;

/**
 * in the report document, we have:
 *
 * table id="6" bookmark is reportlet_table list id="38" bookmark is
 * reportlet_group_[groupid]
 *
 *
 *
 */
public class DocumentIRTransferTest extends EngineCase {

	final String REPORT_DOCUMENT = "./utest/report.rptdocument";
	final String REPORT_DESIGN = "./utest/report.design";
	final String REPORT_DESIGN_SOURCE = "org/eclipse/birt/report/engine/api/document/report.rptdesign";
	final String REPORT_DOCUMENT_V2_1_3 = "org/eclipse/birt/report/engine/api/document/v2_1_3.rptdocument";
	final String REPORT_DOCUMENT_V2_2_1 = "org/eclipse/birt/report/engine/api/document/v2_2_1.rptdocument";

	public void setUp() throws Exception {
		super.setUp();
		copyResource(REPORT_DESIGN_SOURCE, REPORT_DESIGN);
	}

	public void tearDown() throws Exception {
		removeFile(REPORT_DOCUMENT);
		removeFile(REPORT_DESIGN);
		super.tearDown();
	}

	public void testV2_1_3() throws Exception {
		copyResource(REPORT_DOCUMENT_V2_1_3, REPORT_DOCUMENT);
		doTestDocument();
		super.removeFile(REPORT_DOCUMENT);
	}

	public void testV2_2_1() throws Exception {
		copyResource(REPORT_DOCUMENT_V2_2_1, REPORT_DOCUMENT);
		doTestDocument();
		super.removeFile(REPORT_DOCUMENT);
	}

	ReportDesignHandle reportHandle = null;

	void doTestDocument() throws Exception {
		IReportRunnable report = engine.openReportDesign(REPORT_DESIGN);
		reportHandle = (ReportDesignHandle) report.getDesignHandle();
		doRenderPages();
		doRenderAll();
		doRenderReportletWithInstanceID();
		doRenderReportletWithBookmark();
		doDataExtractionWithInstanceID();
	}

	void doRenderPages() throws Exception {
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		long totalPage = document.getPageCount();
		assertEquals(2, totalPage);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		for (long pageNumber = 1; pageNumber <= totalPage; pageNumber++) {
			IRenderTask renderTask = engine.createRenderTask(document);
			renderTask.getReportRunnable().setDesignHandle(reportHandle);
			IRenderOption option = new HTMLRenderOption();
			option.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
			option.setOutputStream(out);
			renderTask.setRenderOption(option);
			renderTask.setPageNumber(pageNumber);
			renderTask.render();
			assertTrue(renderTask.getErrors().isEmpty());
			renderTask.close();
			String pageContent = out.toString("UTF-8");
			if (pageNumber == 1) {
				assertTrue(pageContent.indexOf("reportlet_table") != -1);
				assertTrue(pageContent.indexOf("SECOND-PAGE") == -1);
			} else {
				assertTrue(pageContent.indexOf("reportlet_table") == -1);
				assertTrue(pageContent.indexOf("SECOND-PAGE") != -1);
			}
			out.reset();
		}
		document.close();
	}

	void doRenderAll() throws Exception {
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IRenderTask renderTask = engine.createRenderTask(document);
		renderTask.getReportRunnable().setDesignHandle(reportHandle);
		IRenderOption option = new HTMLRenderOption();
		option.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		option.setOutputStream(out);
		renderTask.setRenderOption(option);
		renderTask.render();
		assertTrue(renderTask.getErrors().isEmpty());
		renderTask.close();
		String pageContent = out.toString("UTF-8");
		assertTrue(pageContent.indexOf("reportlet_table") != -1);
		assertTrue(pageContent.indexOf("SECOND-PAGE") != -1);
		document.close();
	}

	void doRenderReportletWithInstanceID() throws Exception {
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IRenderTask renderTask = engine.createRenderTask(document);
		renderTask.getReportRunnable().setDesignHandle(reportHandle);
		HTMLRenderOption option = new HTMLRenderOption();
		option.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		option.setOutputStream(out);
		option.setEnableMetadata(true);
		renderTask.setRenderOption(option);
		renderTask.render();
		renderTask.close();
		String content = out.toString("UTF-8");
		out.reset();

		Pattern iidPattern = Pattern.compile("iid=\"([^\"]*)\"");
		Matcher matcher = iidPattern.matcher(content);
		while (matcher.find()) {
			String strIid = matcher.group(1);
			InstanceID iid = InstanceID.parse(strIid);
			long designId = iid.getComponentID();
			renderTask = engine.createRenderTask(document);
			IReportRunnable runnable = renderTask.getReportRunnable();
			ReportDesignHandle report = (ReportDesignHandle) runnable.getDesignHandle();
			DesignElementHandle element = report.getElementByID(designId);
			if (element instanceof TableHandle || element instanceof ListHandle) {
				renderTask = engine.createRenderTask(document);
				renderTask.getReportRunnable().setDesignHandle(reportHandle);
				renderTask.setRenderOption(option);
				renderTask.setInstanceID(strIid);
				renderTask.render();
				assertTrue(renderTask.getErrors().isEmpty());
				renderTask.close();
				String pageContent = out.toString("UTF-8");
				if (element instanceof TableHandle) {
					// it cotains the table and all three lists
					assertTrue(pageContent.indexOf("reportlet_table") != -1);
					int indexOf = pageContent.indexOf("reportlet_group");
					int lastIndexOf = pageContent.lastIndexOf("reportlet_group");
					assertTrue(indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf);
				} else {
					// it won't contains the table, only the inner single list exists.
					assertTrue(pageContent.indexOf("reportlet_table") == -1);
					int indexOf = pageContent.indexOf("reportlet_group");
					int lastIndexOf = pageContent.lastIndexOf("reportlet_group");
					assertTrue(indexOf != -1 && lastIndexOf != -1 && indexOf == lastIndexOf);
				}
				out.reset();
			}
		}
		document.close();
	}

	void doRenderReportletWithBookmark() throws Exception {
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IRenderOption option = new HTMLRenderOption();
		option.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		option.setOutputStream(out);

		String[] bookmarks = new String[] { "reportlet_table", "reportlet_group_0", "reportlet_group_1",
				"reportlet_group_2" };

		for (int i = 0; i < bookmarks.length; i++) {
			IRenderTask renderTask = engine.createRenderTask(document);
			renderTask.getReportRunnable().setDesignHandle(reportHandle);
			renderTask.setRenderOption(option);
			renderTask.setReportlet(bookmarks[i]);
			renderTask.render();
			assertTrue(renderTask.getErrors().isEmpty());
			renderTask.close();
			String pageContent = out.toString("UTF-8");
			if (bookmarks[i].indexOf("group") == -1) {
				// it cotains the table and all three lists
				assertTrue(pageContent.indexOf("reportlet_table") != -1);
				int indexOf = pageContent.indexOf("reportlet_group");
				int lastIndexOf = pageContent.lastIndexOf("reportlet_group");
				assertTrue(indexOf != -1 && lastIndexOf != -1 && indexOf != lastIndexOf);
			} else {
				// it won't contains the table and only the inner single list exist.
				assertTrue(pageContent.indexOf("reportlet_table") == -1);
				int indexOf = pageContent.indexOf("reportlet_group");
				int lastIndexOf = pageContent.lastIndexOf("reportlet_group");
				assertTrue(indexOf != -1 && lastIndexOf != -1 && indexOf == lastIndexOf);
			}
			out.reset();
		}
		document.close();

	}

	void doDataExtractionWithInstanceID() throws Exception {
		IReportDocument document = engine.openReportDocument(REPORT_DOCUMENT);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		IRenderTask renderTask = engine.createRenderTask(document);
		renderTask.getReportRunnable().setDesignHandle(reportHandle);
		IRenderOption option = new HTMLRenderOption();
		option.setOutputFormat(HTMLRenderOption.OUTPUT_FORMAT_HTML);
		option.setOutputStream(out);
		renderTask.setRenderOption(option);
		renderTask.render();
		renderTask.close();
		String content = out.toString("UTF-8");

		Pattern iidPattern = Pattern.compile("iid=\"([^\"]*)\"");
		Matcher matcher = iidPattern.matcher(content);
		while (matcher.find()) {
			String strIid = matcher.group(1);
			InstanceID iid = InstanceID.parse(strIid);
			long designId = iid.getComponentID();
			IReportRunnable runnable = renderTask.getReportRunnable();
			ReportDesignHandle report = (ReportDesignHandle) runnable.getDesignHandle();
			DesignElementHandle element = report.getElementByID(designId);
			if (element instanceof TableHandle || element instanceof ListHandle) {
				IDataExtractionTask task = engine.createDataExtractionTask(document);
				task.setInstanceID(iid);
				IExtractionResults results = task.extract();
				if (element instanceof TableHandle) {
					assertEquals(27, getFieldCount(results));
				} else {
					assertEquals(6, getFieldCount(results));
				}
				task.close();

			}
		}
		document.close();
	}

	int getFieldCount(IExtractionResults results) throws BirtException {
		int fieldCount = 0;
		IDataIterator iter = results.nextResultIterator();
		IResultMetaData metaData = iter.getResultMetaData();
		int columnCount = metaData.getColumnCount();
		while (iter.next()) {
			fieldCount += columnCount;
		}
		return fieldCount;
	}
}
