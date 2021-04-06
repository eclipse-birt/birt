/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IEngineTask;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.eclipse.birt.report.engine.api.impl.ReportEngine;
import org.eclipse.birt.report.model.api.IResourceLocator;

import uk.co.spudsoft.birt.emitters.bugfix.FixedRenderTask;
import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;
import uk.co.spudsoft.birt.emitters.excel.tests.framework.Activator;

public class ReportRunner {

	protected boolean debug;
	protected boolean removeEmptyRows = true;
	protected boolean htmlPagination;
	protected boolean singleSheet;
	protected boolean singleSheetWithPagination;
	protected boolean nestTableInLastCell;
	protected boolean autoFilter;
	protected boolean blankLineAfterTopLevelTable;

	protected Boolean displayFormulas = null;
	protected Boolean displayGridlines = null;
	protected Boolean displayRowColHeadings = null;
	protected Boolean displayZeros = null;
	protected Boolean disableGrouping = null;
	protected Boolean structuredHeader = null;
	protected Boolean groupSummaryHeader = null;

	protected Integer spannedRowHeight = null;

	protected String templateFile = null;

	protected Map<String, Object> parameters = new HashMap<String, Object>();
	protected long startTime;
	protected long runTime;
	protected long renderTime;

	private static byte[] getBytesFromFile(File file) throws IOException {
		InputStream is = new FileInputStream(file);
		try {
			byte[] data = new byte[(int) file.length()];
			int offset = 0;
			int read = 0;
			while (offset < data.length && (read = is.read(data, offset, data.length - offset)) >= 0) {
				offset += read;
			}
			return data;
		} finally {
			is.close();
		}
	}

	public boolean mergedRegion(Sheet sheet, int top, int left, int bottom, int right) {
		for (int i = 0; i < sheet.getNumMergedRegions(); ++i) {
			CellRangeAddress curRegion = sheet.getMergedRegion(i);
			if ((curRegion.getFirstRow() == top) && (curRegion.getFirstColumn() == left)
					&& (curRegion.getLastRow() == bottom) && (curRegion.getLastColumn() == right)) {
				return true;
			}
		}
		return false;
	}

	protected int firstNullRow(Sheet sheet) {
		int i = 0;
		while (sheet.getRow(i) != null) {
			++i;
		}
		return i;
	}

	protected int lastRow(Sheet sheet) {
		int max = 0;
		for (Row row : sheet) {
			max = row.getRowNum();
		}
		return max;
	}

	protected int greatestNumColumns(Sheet sheet) {
		int result = 0;
		for (Row row : sheet) {
			if (row.getLastCellNum() > result) {
				result = row.getLastCellNum();
			}
		}
		return result;
	}

	private void addParameters(IEngineTask reportRunTask) {
		for (Entry<String, Object> entry : parameters.entrySet()) {
			reportRunTask.setParameterValue(entry.getKey(), entry.getValue());
		}
		parameters.clear();
	}

	protected InputStream runAndRenderReport(String filename, String outputFormat) throws BirtException, IOException {
		return runAndRenderReportCustomTask(filename, outputFormat);
	}

	protected File createTempFile(String base, String extension) throws IOException {
		String tempDir = System.getProperty("java.io.tmpdir");

		for (int i = 0; i < Integer.MAX_VALUE; ++i) {
			File result = new File(tempDir + File.separator + base + Integer.toString(i) + extension);
			if (!result.exists()) {
				return result;
			}
		}
		throw new IOException("Temporary file not available");
	}

	protected String baseFilename(String filename) {
		int index = filename.lastIndexOf(File.separatorChar);
		if (index > 0) {
			filename = filename.substring(index);
		}
		index = filename.lastIndexOf(".");
		if (index > 0) {
			filename = filename.substring(0, index);
		}
		return filename;
	}

	protected InputStream runAndRenderReportDefaultTask(String filename, String outputFormat)
			throws BirtException, IOException {

		IReportEngine reportEngine = createReportEngine();

		String filepath = deriveFilepath(filename);

		InputStream resourceStream = openFileStream(filename);

		assertNotNull(resourceStream);
		try {
			IReportRunnable reportRunnable = reportEngine.openReportDesign(resourceStream);
			assertNotNull(reportRunnable);

			File tempDoc = createTempFile(baseFilename(filename), ".rptdocument");
			assertNotNull(tempDoc);

			try {
				IRunTask reportRunTask = reportEngine.createRunTask(reportRunnable);
				assertNotNull(reportRunTask);
				try {
					addParameters(reportRunTask);
					addFilepathToAppContext(filepath, reportRunTask);

					startTime = System.currentTimeMillis();
					IReportDocument reportDocument = runReport(reportEngine, reportRunTask, tempDoc);
					runTime = System.currentTimeMillis();

					IRenderTask renderTask = reportEngine.createRenderTask(reportDocument);
					assertNotNull(renderTask);
					try {
						File tempOutput = createTempFile(baseFilename(filename), "." + outputFormat);
						System.err.println(tempOutput);
						FileOutputStream outputStream = new FileOutputStream(tempOutput);

						assertNotNull(outputStream);
						try {
							renderTask.setRenderOption(prepareRenderOptions(outputFormat, outputStream));

							renderTask.render();
							renderTime = System.currentTimeMillis();
							assertEquals(0, renderTask.getErrors().size());
						} finally {
							outputStream.close();
						}

						return new ByteArrayInputStream(getBytesFromFile(tempOutput));
					} finally {
						renderTask.close();
					}
				} finally {
					reportRunTask.close();
				}
			} finally {
				tempDoc.delete();
			}
		} finally {
			resourceStream.close();
		}
	}

	protected InputStream runAndRenderReportFileNotStream(String filename, String outputFormat)
			throws BirtException, IOException {

		IReportEngine reportEngine = createReportEngine();

		String filepath = deriveFilepath(filename);

		InputStream resourceStream = openFileStream(filename);

		assertNotNull(resourceStream);
		try {
			IReportRunnable reportRunnable = reportEngine.openReportDesign(resourceStream);
			assertNotNull(reportRunnable);

			File tempDoc = createTempFile(baseFilename(filename), ".rptdocument");
			assertNotNull(tempDoc);

			try {
				IRunTask reportRunTask = reportEngine.createRunTask(reportRunnable);
				assertNotNull(reportRunTask);
				try {
					addParameters(reportRunTask);
					addFilepathToAppContext(filepath, reportRunTask);

					IReportDocument reportDocument = runReport(reportEngine, reportRunTask, tempDoc);

					IRenderTask renderTask = reportEngine.createRenderTask(reportDocument);
					assertNotNull(renderTask);
					try {
						File outputFile = createTempFile(baseFilename(filename), "." + outputFormat);
						System.err.println(outputFile);

						assertNotNull(outputFile);
						renderTask.setRenderOption(prepareRenderOptions(outputFormat, null));
						renderTask.getRenderOption().setOutputFileName(outputFile.getCanonicalPath());

						renderTask.render();
						assertEquals(0, renderTask.getErrors().size());

						InputStream result = new ByteArrayInputStream(getBytesFromFile(outputFile));

						boolean deleted = outputFile.delete();
						assertTrue(deleted);

						return result;
					} finally {
						renderTask.close();
					}
				} finally {
					reportRunTask.close();
				}
			} finally {
				tempDoc.delete();
			}
		} finally {
			resourceStream.close();
		}
	}

	protected InputStream runAndRenderReportAsOne(String filename, String outputFormat)
			throws BirtException, IOException {

		IReportEngine reportEngine = createReportEngine();

		String filepath = deriveFilepath(filename);

		InputStream resourceStream = openFileStream(filename);

		assertNotNull(resourceStream);
		try {
			IReportRunnable reportRunnable = reportEngine.openReportDesign(resourceStream);
			assertNotNull(reportRunnable);

			File tempDoc = createTempFile(baseFilename(filename), ".rptdocument");
			assertNotNull(tempDoc);

			try {
				IRunAndRenderTask reportRunRenderTask = reportEngine.createRunAndRenderTask(reportRunnable);
				assertNotNull(reportRunRenderTask);
				try {
					addParameters(reportRunRenderTask);
					addFilepathToAppContext(filepath, reportRunRenderTask);

					File tempOutput = createTempFile(baseFilename(filename), "." + outputFormat);
					System.err.println(tempOutput);
					FileOutputStream outputStream = new FileOutputStream(tempOutput);

					assertNotNull(outputStream);
					try {

						reportRunRenderTask.setRenderOption(prepareRenderOptions(outputFormat, outputStream));

						reportRunRenderTask.run();
						assertEquals(0, reportRunRenderTask.getErrors().size());
					} finally {
						outputStream.close();
					}

					return new ByteArrayInputStream(getBytesFromFile(tempOutput));
				} finally {
					reportRunRenderTask.close();
				}
			} finally {
				tempDoc.delete();
			}
		} finally {
			resourceStream.close();
		}
	}

	protected InputStream runAndRenderReportCustomTask(String filename, String outputFormat)
			throws BirtException, IOException {

		IReportEngine reportEngine = createReportEngine();

		String filepath = deriveFilepath(filename);

		InputStream resourceStream = openFileStream(filename);

		assertNotNull(resourceStream);
		try {
			File designFile = new File(filepath);
			IResourceLocator resourceLocator = new ResourceLocator(designFile.getParentFile());

			IReportRunnable reportRunnable = reportEngine.openReportDesign(filename, resourceStream, resourceLocator);
			assertNotNull(reportRunnable);

			File tempDoc = createTempFile(baseFilename(filename), ".rptdocument");
			assertNotNull(tempDoc);

			try {
				IRunTask reportRunTask = reportEngine.createRunTask(reportRunnable);

				// reportRunTask.enableProgressiveViewing(true);

				assertNotNull(reportRunTask);
				try {
					addParameters(reportRunTask);
					addFilepathToAppContext(filepath, reportRunTask);
					addToAppContext(reportRunTask, "org.eclipse.birt.data.query.ResultBufferSize", 256);

					startTime = System.currentTimeMillis();
					IReportDocument reportDocument = runReport(reportEngine, reportRunTask, tempDoc);
					runTime = System.currentTimeMillis();
					System.err
							.println("Run " + baseFilename(filename) + " : " + ((runTime - startTime) / 1000.0) + "s");

					// IRenderTask renderTask = reportEngine.createRenderTask( reportDocument );
					IRenderTask renderTask = new FixedRenderTask((ReportEngine) reportEngine, reportRunnable,
							reportDocument);
					assertNotNull(renderTask);
					try {
						File tempOutput = createTempFile(baseFilename(filename), "." + outputFormat);
						System.err.println(tempOutput);
						FileOutputStream outputStream = new FileOutputStream(tempOutput);

						assertNotNull(outputStream);
						try {

							renderTask.setRenderOption(prepareRenderOptions(outputFormat, outputStream));

							System.err.println("Starting Render");
							renderTask.render();
							assertEquals(0, renderTask.getErrors().size());
						} finally {
							renderTime = System.currentTimeMillis();
							System.err.println(
									"Run " + baseFilename(filename) + " : " + ((runTime - startTime) / 1000.0) + "s");
							System.err.println("Render " + baseFilename(filename) + " : "
									+ ((renderTime - runTime) / 1000.0) + "s");
							outputStream.close();
						}

						return new ByteArrayInputStream(getBytesFromFile(tempOutput));
					} finally {
						renderTask.close();
					}
				} finally {
					reportRunTask.close();
				}
			} finally {
				tempDoc.delete();
			}
		} finally {
			resourceStream.close();
		}
	}

	protected IReportEngine createReportEngine() {
		EngineConfig config = new EngineConfig();

		IReportEngineFactory engineFactory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		assertNotNull(engineFactory);

		IReportEngine reportEngine = engineFactory.createReportEngine(config);
		assertNotNull(reportEngine);
		return reportEngine;
	}

	protected String deriveFilepath(String filename) throws MalformedURLException {
		String filepath = null;

		File file = new File(filename);
		if ((file.isAbsolute()) && (file.exists())) {
			return filename;
		} else if (Activator.getContext() != null) {
			URL bundleLocation = new URL(Activator.getContext().getBundle().getLocation());
			// System.err.println( "Activator.getContext().getBundle().getLocation() = " +
			// bundleLocation );
			String bundleLocationFile = bundleLocation.getFile();
			if (bundleLocationFile.startsWith("file:/")) {
				bundleLocationFile = bundleLocationFile.substring(6);
			}
			// System.err.println( "bundleLocationFile = " + bundleLocationFile );

			URL resourceLocation = this.getClass().getResource(filename);
			String resourceLocationFile = resourceLocation.getFile();
			// System.err.println( "resourceLocationFile = " + resourceLocationFile );

			filepath = bundleLocationFile + "bin" + resourceLocationFile;
			// System.err.println( "filepath = " + filepath );
		}
		return filepath;
	}

	protected InputStream openFileStream(String filename) throws FileNotFoundException {
		File file = new File(filename);
		if (file.exists()) {
			return new FileInputStream(file);
		} else {
			return this.getClass().getResourceAsStream(filename);
		}
	}

	protected void addFilepathToAppContext(String filepath, IEngineTask task) {
		if (filepath != null) {
			addToAppContext(task, "__report", filepath);
		}
	}

	private void addToAppContext(IEngineTask task, String key, Object value) {
		@SuppressWarnings("unchecked")
		Map<String, Object> appContext = (Map<String, Object>) task.getAppContext();
		if (appContext == null) {
			appContext = new HashMap<String, Object>();
			task.setAppContext(appContext);
		}
		appContext.put(key, value);
	}

	protected IReportDocument runReport(IReportEngine reportEngine, IRunTask reportRunTask, File tempDoc)
			throws IOException, EngineException {
		IDocArchiveWriter archiveWriter = new FileArchiveWriter(tempDoc.getCanonicalPath());
		assertNotNull(archiveWriter);

		reportRunTask.run(archiveWriter);
		for (Object errorObject : reportRunTask.getErrors()) {
			System.err.println("Error: " + errorObject);
		}
		assertEquals(0, reportRunTask.getErrors().size());

		reportRunTask.close();

		IReportDocument reportDocument = reportEngine.openReportDocument(tempDoc.getCanonicalPath());
		assertNotNull(reportDocument);
		return reportDocument;
	}

	protected RenderOption prepareRenderOptions(String outputFormat, FileOutputStream outputStream) {
		RenderOption renderOptions = new RenderOption();
		renderOptions.setOutputFormat(outputFormat);
		if (outputStream != null) {
			renderOptions.setOutputStream(outputStream);
		}
		if (debug) {
			renderOptions.setOption("ExcelEmitter.DEBUG", Boolean.TRUE);
			debug = false;
		}
		if (!removeEmptyRows) {
			renderOptions.setOption("ExcelEmitter.RemoveBlankRows", Boolean.FALSE);
		}
		if (htmlPagination) {
			renderOptions.setOption(HTMLRenderOption.HTML_PAGINATION, Boolean.TRUE);
		}
		if (autoFilter) {
			renderOptions.setOption(ExcelEmitter.AUTO_FILTER, Boolean.TRUE);
		}
		if (singleSheet) {
			renderOptions.setOption("ExcelEmitter.SingleSheet", true);
		}
		if (singleSheetWithPagination) {
			renderOptions.setOption(ExcelEmitter.SINGLE_SHEET_PAGE_BREAKS, true);
		}
		if (nestTableInLastCell) {
			renderOptions.setOption(ExcelEmitter.NEST_TABLE_IN_LAST_CELL, true);
		}
		if (displayFormulas != null) {
			renderOptions.setOption("ExcelEmitter.DisplayFormulas", displayFormulas);
		}
		if (displayGridlines != null) {
			renderOptions.setOption("ExcelEmitter.DisplayGridlines", displayGridlines);
		}
		if (displayRowColHeadings != null) {
			renderOptions.setOption("ExcelEmitter.DisplayRowColHeadings", displayRowColHeadings);
		}
		if (displayZeros != null) {
			renderOptions.setOption("ExcelEmitter.DisplayZeros", displayZeros);
		}
		if (disableGrouping != null) {
			renderOptions.setOption(ExcelEmitter.DISABLE_GROUPING, disableGrouping);
		}
		if (structuredHeader != null) {
			renderOptions.setOption(ExcelEmitter.STRUCTURED_HEADER, structuredHeader);
		}
		if (groupSummaryHeader != null) {
			renderOptions.setOption(ExcelEmitter.GROUP_SUMMARY_HEADER, groupSummaryHeader);
		}
		if (blankLineAfterTopLevelTable) {
			renderOptions.setOption(ExcelEmitter.BLANK_ROW_AFTER_TOP_LEVEL_TABLE, true);
		}
		if (spannedRowHeight != null) {
			renderOptions.setOption(ExcelEmitter.SPANNED_ROW_HEIGHT, spannedRowHeight);
		}
		if (templateFile != null) {
			renderOptions.setOption(ExcelEmitter.TEMPLATE_FILE, templateFile);
		}

		return renderOptions;
	}

}
