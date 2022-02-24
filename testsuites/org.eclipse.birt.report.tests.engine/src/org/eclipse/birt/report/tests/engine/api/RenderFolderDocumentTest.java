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
import java.io.IOException;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Locale;

import org.eclipse.birt.core.archive.FolderArchiveReader;
import org.eclipse.birt.core.archive.FolderArchiveWriter;
import org.eclipse.birt.report.engine.api.EngineConstants;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderContext;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.impl.ReportDocumentConstants;
import org.eclipse.birt.report.tests.engine.EngineCase;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * <b>RenderFolderDocument test</b>
 * <p>
 * This case tests rendering folder-based report document.
 */
public class RenderFolderDocumentTest extends EngineCase {

	// final static String INPUT = "";
	private String separator = FileSystems.getDefault().getSeparator();
	private String inputFolder = this.genInputFolder() + separator;
	private String outputFolder = this.genOutputFolder() + separator;
	private String folderArchive, htmlOutput;
	private IReportDocument reportDoc;
	private IRenderTask renderTask;
	private IRenderOption htmlOption;

	public RenderFolderDocumentTest(String name) {
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(RenderTaskTest.class);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		removeResource();
		htmlOption = new HTMLRenderOption();
		htmlOption.setOutputFormat(HTMLRenderOption.HTML);
	}

	@Override
	public void tearDown() throws Exception {
		super.tearDown();
		removeResource();
	}

	public void testRenderFolderDocument_simple() {
		String renderDoc = "folderdocument_case1";

		renderFolderDocument(renderDoc);
	}

	public void testRenderFolderDocument_longtext() {
		String renderDoc = "folderdocument_long_text";
		renderFolderDocument(renderDoc);

	}

	public void testRenderFolderDocument_masterpage() {
		String renderDoc = "folderdocument_master_page";
		renderFolderDocument(renderDoc);

	}

	public void testRenderFolderDocument_multidatasets() {
		String renderDoc = "folderdocument_multiple_datasets";
		renderFolderDocument(renderDoc);

	}

	public void testRenderFolderDocument_multipages() {
		String renderDoc = "folderdocument_pages9";
		renderFolderDocument(renderDoc);

	}

	public void testRenderFolderDocument_nesttable() {
		String renderDoc = "folderdocument_table_nest_pages";
		renderFolderDocument(renderDoc);

	}

	public void testRenderFolderDocument_chart() {
		String renderDoc = "folderdocument_chart";
		renderFolderDocument(renderDoc);

	}

	/*
	 * Verification: Test whether folder and files can be dropped from folder-based
	 * report document Test Method: FolderArchiveWriter.dropStream( String
	 * relativePath )
	 */
	public void testDropDocumentFolder_content() {
		String report_design = "report_document";
		dropFolder(report_design, "content");
	}

	public void testDropDocumentFolder_data() {
		String report_design = "report_document";
		dropFolder(report_design, "Data");
	}

	public void testDropDocumentFolder_design() {
		String report_design = "report_document";
		dropFolder(report_design, "design");
	}

	public void testDropDocumentFolder_blank() {
		String report_design = "report_document";
		dropFolder(report_design, "");
	}

	public void testDropDocumentFolder_nonexist() {
		String report_design = "report_document";
		dropFolder(report_design, "nonexist");
	}

	private void dropFolder(String report_design, String dropDir) {
		folderArchive = outputFolder + "drop_" + report_design + separator;
		String design = inputFolder + report_design + ".rptdesign";
		copyResource_INPUT(report_design + ".rptdesign", report_design + ".rptdesign");
		try {
			// createFolderDocument relies on the implementation of ReportDocumentBuilder
			// Three internal streams are implicitly created as follows:
//						ReportDocumentConstants.CONTENT_STREAM );
//						ReportDocumentConstants.PAGE_STREAM );
//						ReportDocumentConstants.PAGE_INDEX_STREAM );
			// All of these stream are created in a folder called content
			// This test was then trying to remote the content folder via writer.dropStream
			// This doesn't work because the folder is a container for the streams held
			// within
			// So the test is rewritten to drop the individual streams

			createFolderDocument(design, folderArchive);
			FolderArchiveWriter writer = new FolderArchiveWriter(folderArchive);

			dropStream(writer, ReportDocumentConstants.CONTENT_STREAM);
			dropStream(writer, ReportDocumentConstants.PAGE_STREAM);
			dropStream(writer, ReportDocumentConstants.PAGE_INDEX_STREAM);

			writer.finish();
		} catch (EngineException | IOException e) {
			e.printStackTrace();
			fail("RunTask failed to create folder-based document!" + e.getLocalizedMessage());
		}
	}

	private void dropStream(FolderArchiveWriter writer, String name) {
		if (writer.exists(name)) {
			writer.dropStream(name);
			assertFalse("FolderArchiveWriter failed to drop stream" + name + " in document", writer.exists(name));
		}
	}

	/**
	 * create folder-based report document
	 *
	 * @param design    source report design with absolute path
	 * @param folderDoc folderdocument with absolute path like "c:/doc/"
	 * @throws IOException
	 * @throws EngineException
	 */
	private void createFolderDocument(String design, String folderDoc) throws IOException, EngineException {
		IRunTask runTask;
		FolderArchiveWriter writer;
		folderArchive = folderDoc;

		writer = new FolderArchiveWriter(folderArchive);
		IReportRunnable runnable = engine.openReportDesign(design);
		runTask = engine.createRunTask(runnable);
		runTask.run(writer);
		runTask.close();
		writer.finish();

	}

	/**
	 * render output html from folder-based document
	 *
	 * @param docName . The value must be "folderdocument_reportname"
	 */
	private void renderFolderDocument(String docName) {

		String designName, report_design;
		designName = docName.substring(15);
		report_design = inputFolder + designName + ".rptdesign";
		copyResource_INPUT(designName + ".rptdesign", designName + ".rptdesign");

		folderArchive = outputFolder + docName + separator;
		htmlOutput = outputFolder + docName + ".html";
		new File(outputFolder).mkdirs();
		try {
			createFolderDocument(report_design, folderArchive);

			FolderArchiveReader reader = new FolderArchiveReader(folderArchive);
			reportDoc = engine.openReportDocument(folderArchive);
			renderTask = engine.createRenderTask(reportDoc);

			htmlOption.setOutputFileName(htmlOutput);
			HTMLRenderContext renderContext = new HTMLRenderContext();
			renderContext.setImageDirectory("image");
			HashMap appContext = new HashMap();
			appContext.put(EngineConstants.APPCONTEXT_HTML_RENDER_CONTEXT, renderContext);

			renderTask.setRenderOption(htmlOption);
			renderTask.setAppContext(appContext);
			renderTask.setLocale(Locale.ENGLISH);
			renderTask.setPageRange("All");
			renderTask.render();
			renderTask.close();

			assertNotNull(docName + ".html failed to render from folder-based document", htmlOutput);

			reader.close();

		} catch (IOException ioe) {
			ioe.printStackTrace();
			assertTrue("IOException (when create and render " + docName + " folder-based document)", false);
		} catch (EngineException ee) {
			ee.printStackTrace();
			assertTrue("EngineException (when create and render " + docName + " folder-based document)", false);
		}

	}

}
