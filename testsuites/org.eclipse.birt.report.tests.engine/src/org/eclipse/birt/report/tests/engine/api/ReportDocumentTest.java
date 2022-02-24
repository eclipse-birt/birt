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

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.birt.core.archive.FileArchiveReader;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveReader;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.ITOCTree;
import org.eclipse.birt.report.engine.api.TOCNode;
import org.eclipse.birt.report.tests.engine.EngineCase;

import com.ibm.icu.util.ULocale;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>Report Document creation test</b>
 * <p>
 * This case tests creating report document.
 */
public class ReportDocumentTest extends EngineCase {

	protected IReportRunnable reportRunnable;
	// private String path = this.getFullQualifiedClassName( )
	// + System.getProperty( "file.separator" );
	private String path = this.getInputResourceFolder() + File.separator + getFullQualifiedClassName() // $NON-NLS-1$
			+ File.separator;

	public ReportDocumentTest(String name) {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(ReportDocumentTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		copyResource_INPUT("report_document.rptdesign", "report_document.rptdesign");
	}

	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	/**
	 * test informations which should be contained by report document
	 */
	public void testReportDocument() {

		String report_design = path + INPUT_FOLDER + System.getProperty("file.separator") + "report_document.rptdesign";

		String report_document = this.genOutputFile("report_document");

		try {
			createReportDocument_param(report_design, report_document);
			IDocArchiveReader archive = new FileArchiveReader(report_document);
			IReportDocument reportDoc = engine.openReportDocument(report_document);

			// checkTOC(done)
			TOCNode root, tableNode, headerNode, groupNode, detailNode, footerNode;
			ITOCTree tree = null;
			tree = reportDoc.getTOCTree(null, ULocale.ENGLISH);
			root = tree.findTOC("/");
			assertNotNull("get root toc", root);
			assertNotNull("root contain no children toc", root.getChildren());
			tableNode = (TOCNode) root.getChildren().get(0);
			assertNotNull("table toc doesn't exist.", tableNode);
			assertEquals("table toc expression isn't TableSection", "TableSection", tableNode.getDisplayString());
			assertNotNull("table toc doesn't contain nodes", tableNode.getChildren());

			assertEquals("table toc doesn't contain 6 nodes", 6, tableNode.getChildren().size());
			headerNode = (TOCNode) tableNode.getChildren().get(0);
			groupNode = (TOCNode) ((TOCNode) tableNode.getChildren().get(1)).getChildren().get(0);
			footerNode = (TOCNode) tableNode.getChildren().get(5);
			assertNotNull("table header toc doesn't exist", headerNode);
			assertNotNull("table group toc doesn't exist", groupNode);
			assertNotNull("table footer toc doesn't exist", footerNode);
			assertEquals("header toc expression isn't HeaderSection", "HeaderSection", headerNode.getDisplayString());
			assertEquals("group toc expression isn't GroupSection", "GroupSection", groupNode.getDisplayString());
			assertEquals("footer toc expression isn't FooterSection", "FooterSection", footerNode.getDisplayString());

			assertNotNull("table group toc doesn't contain nodes", groupNode.getChildren());
			detailNode = (TOCNode) ((TOCNode) tableNode.getChildren().get(1)).getChildren().get(1);
			assertEquals("detail toc expression isn't DetailSection", "DetailSection", detailNode.getDisplayString());

			// check Archive(done)
			IDocArchiveReader arch = reportDoc.getArchive();
			assertNotNull("get null document archive", arch);
			assertEquals("get incorrect document archive", archive.getName(), arch.getName());

			// check bookmarks
			ArrayList bookmarks = (ArrayList) reportDoc.getBookmarks();
			String hMark = "TableHeaderMark", gMark = "TableGroupMark", dMark = "TableDetailMark";
			String fMark = "TableFooterMark", pMark = "ParameterMark";
			assertNotNull("get no bookmarks", bookmarks);
			// assertEquals("bookmarks doesn't contain 19
			// nodes",19,bookmarks.size());
			assertTrue("bookmarks doesn't contain " + hMark, bookmarks.contains(hMark));
			assertTrue("bookmarks doesn't contain " + gMark, bookmarks.contains(gMark));
			assertTrue("bookmarks doesn't contain " + dMark, bookmarks.contains(dMark));
			assertTrue("bookmarks doesn't contain " + fMark, bookmarks.contains(fMark));
			assertTrue("bookmarks doesn't contain " + pMark, bookmarks.contains(pMark));

			// check children(done)
			List root_children = reportDoc.getChildren("/");
			assertNotNull("cannot get root toc's children", root_children);
			assertNotNull("cannot get table toc", root_children.get(0));
			TOCNode table_toc = (TOCNode) root_children.get(0);
			List table_children = reportDoc.getChildren(table_toc.getNodeID());
			assertNotNull("table toc's children is null", table_children);
			assertEquals("table toc contains 6 children", 6, table_children.size());

			// check designStream
			InputStream designStream = reportDoc.getDesignStream();
			FileInputStream designFile = new FileInputStream(report_design);
			assertNotNull(designStream);
			// generate design file through designStream.
			int length = designStream.available();
			assertTrue(length > 0);
			designStream.close();
			designFile.close();

			// check page count(done)
			assertEquals("return wrong page count", 2, reportDoc.getPageCount());

			// check getPageNumber(InstanceID)
			// TODO:

			// check getPageNumber(bookmark)
			assertEquals("return wrong page number which contains bookmark", 2, reportDoc.getPageNumber(pMark));

			// check parameters(done)
			HashMap params = (HashMap) reportDoc.getParameterValues();
			assertNotNull("return no parameters", params);

			assertEquals("parameter amount isn't 2", 2, params.size());
			assertNotNull("p1 parameter returned null", params.get(new String("p1")));
			assertNotNull("p2 parameter returned null", params.get(new String("p2")));
			assertEquals("p1 parameter returned wrong value", "p1string", params.get(new String("p1")));
			assertEquals("p1 parameter returned wrong value", new Integer(2), params.get(new String("p2")));

			// check report document name
			String name = report_document.substring(report_document.indexOf("org"), report_document.length());
			name = name.replace('/', '\\');
			assertEquals("return wrong report document name", name.replace('\\', '/'), reportDoc.getName()
					.substring(reportDoc.getName().indexOf("org"), reportDoc.getName().length()).replace('\\', '/'));

			// check reportrunnable
			IReportRunnable report = reportDoc.getReportRunnable();

			assertNotNull("return null reportRunnable", report);
			assertEquals("return wrong reportRunnable", reportRunnable.getReportEngine(), report.getReportEngine());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * test abnormal input arguments
	 */
	public void testReportDocument_abnormal() {
		String report_design = path + INPUT_FOLDER + System.getProperty("file.separator") + "report_document.rptdesign";
		String report_document = this.genOutputFile("report_document");

		/*
		 * Stringreport_design=
		 * "D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/input/report_document.rptdesign"
		 * ; Stringreport_document=
		 * "D:/TEMP/workspace3.1/org.eclipse.birt.report.tests.engine/output/report_document"
		 * ;
		 */

		try {
			createReportDocument(report_design, report_document);
			IReportDocument reportDoc = engine.openReportDocument(report_document);

			// check findToc
			TOCNode tocNode;
			tocNode = (TOCNode) reportDoc.findTOC(null);
			assertEquals("findToc should return null", null, tocNode.getDisplayString());
			tocNode = (TOCNode) reportDoc.findTOC("");
			assertNull("1.findToc should return null toc node", tocNode);
			tocNode = (TOCNode) reportDoc.findTOC("1");
			assertNull("2.findToc should return null toc node", tocNode);
			tocNode = (TOCNode) reportDoc.findTOC("\":)_+^&*%$#@!~<>");
			assertNull("3.findToc should return null toc node", tocNode);

			// check getChildren
			List children;
			children = reportDoc.getChildren(null);
			assertNotNull(children);
			assertEquals("getChildren should return root node's children", 1, children.size());
			children = reportDoc.getChildren("");
			assertNull("1.getChildren should return null", children);
			children = reportDoc.getChildren("1");
			assertNull("2.getChildren should return null", children);
			children = reportDoc.getChildren("\":)_+^&*%$#@!~<>??");
			assertNull("3.getChildren should return null", children);

			// check getPageNumber(instanceID)
			// TODO:

			// check getPageNumber(bookmarks)
			long pageNum;
			String bookmark = null;
			pageNum = reportDoc.getPageNumber(bookmark);
			assertEquals("1.getPageNumber should return -1", -1, pageNum);
			bookmark = "";
			pageNum = reportDoc.getPageNumber(bookmark);
			assertEquals("2.getPageNumber should return -1", -1, pageNum);
			bookmark = "\":)_+^&*%$#@!~<>??";
			pageNum = reportDoc.getPageNumber(bookmark);
			assertEquals("3.getPageNumber should return -1", -1, pageNum);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * create the report document.
	 *
	 * @throws Exception
	 */
	protected void createReportDocument(String reportdesign, String reportdocument) throws Exception {
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter(reportdocument);
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(reportdesign);
		reportRunnable = report;
		// create an IRunTask
		IRunTask task = engine.createRunTask(report);
		// execute the report to create the report document.
		task.run(archive);
		// close the task, release the resource.
		task.close();
	}

	/**
	 * create the report document, set inputParams for it.
	 *
	 * @throws Exception
	 */
	protected void createReportDocument_param(String reportdesign, String reportdocument) throws Exception {
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter(reportdocument);
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(reportdesign);
		reportRunnable = report;
		// create an IRunTask
		IRunTask task = engine.createRunTask(report);
		// set parameter to the report
		HashMap inputParam = new HashMap();
		inputParam.put("p1", "p1string");
		inputParam.put("p2", new Integer(2));
		task.setParameterValues(inputParam);
		// execute the report to create the report document.
		task.run(archive);
		// close the task, release the resource.
		task.close();
	}

}
