package org.eclipse.birt.report.tests.engine.api;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.birt.core.archive.FileArchiveWriter;
import org.eclipse.birt.core.archive.IDocArchiveWriter;
import org.eclipse.birt.data.engine.api.IFilterDefinition;
import org.eclipse.birt.data.engine.api.querydefn.ConditionalExpression;
import org.eclipse.birt.data.engine.api.querydefn.FilterDefinition;
import org.eclipse.birt.report.engine.api.EngineException;
import org.eclipse.birt.report.engine.api.HTMLRenderOption;
import org.eclipse.birt.report.engine.api.IDataExtractionTask;
import org.eclipse.birt.report.engine.api.IDataIterator;
import org.eclipse.birt.report.engine.api.IExtractionResults;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IRenderTask;
import org.eclipse.birt.report.engine.api.IReportDocument;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunTask;
import org.eclipse.birt.report.engine.api.InstanceID;
import org.eclipse.birt.report.tests.engine.EngineCase;

/**
 * <b>DataExtractionTask test</b>
 * <p>
 * This case tests extracting data with filter rule from report through
 * IDataExtractionTask interface.
 * 
 */

public class DataExtractionTaskTest extends EngineCase {

	private String report_design;

	private String report_document;

	private IReportDocument reportDoc;

	private String separator = System.getProperty("file.separator");

	protected String path = getClassFolder() + separator;

	private String outputPath = path + OUTPUT_FOLDER + separator;

	private String inputPath = path + INPUT_FOLDER + separator;

	public DataExtractionTaskTest(String name)

	{
		super(name);
	}

	public static Test Suite() {
		return new TestSuite(DataExtractionTaskTest.class);
	}

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testDataExtractionWithFilter() {
		report_design = inputPath + "DataExtraction_table.rptdesign";
		report_document = outputPath + "DataExtraction_table.rptdocument";
		try {
			createReportDocument(report_design, report_document);

			reportDoc = engine.openReportDocument(report_document);
			IDataExtractionTask extractTask = engine
					.createDataExtractionTask(reportDoc);

			extractTask.selectResultSet("t1");
			IFilterDefinition[] filterExpression = new IFilterDefinition[1];
			filterExpression[0] = new FilterDefinition(
					new ConditionalExpression("row[\"territory\"]",
							ConditionalExpression.OP_EQ, "\"EMEA\"", null));
			extractTask.setFilters(filterExpression);

			IExtractionResults result = extractTask.extract();

			if (result != null) {
				int officecode = 0;
				IDataIterator data = result.nextResultIterator();
				if (data != null) {
					data.next();
					officecode = Integer.parseInt(data.getValue("code")
							.toString());
					assertEquals("Fail to extract filtered data", 4, officecode);
					if (data.next()) {
						officecode = Integer.parseInt(data.getValue("code")
								.toString());
						assertEquals("Fail to extract filtered data", 7,
								officecode);
					}
				}
			} else {
				fail("Fail to extract filtered data");
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("Fail to extract filtered data");
		}
	}

	/**
	 * test setInstanceID in DataExtractionTask with subquery structure
	 * 
	 */
	public void testDataExtractionFromIID_subquery() {
		report_design = inputPath + "DataExtraction_subquery.rptdesign";
		report_document = outputPath + "DataExtraction_subquery.rptdocument";
		try {
			createReportDocument(report_design, report_document);

			reportDoc = engine.openReportDocument(report_document);
			IDataExtractionTask extractTask = engine
					.createDataExtractionTask(reportDoc);

			/* extract master query data from subquery structure */
			ArrayList iids = findIID(report_document,
					"LIST");
			assertEquals(1, iids.size());

			extractTask.setInstanceID((InstanceID) iids.get(0));
			IExtractionResults result = extractTask.extract();

			if (result != null) {
				String name = null;
				IDataIterator data = result.nextResultIterator();
				if (data != null) {
					data.next();
					name = data.getValue("name").toString();
					assertTrue(name.equalsIgnoreCase("Atelier graphique"));
					if (data.next()) {
						name = data.getValue("name").toString();
						assertTrue(name.equalsIgnoreCase("Signal Gift Stores"));
					}
				}
			} else {
				fail("Fail to extract data from subquery");
			}

			/* extract sub query data from subquery structure */
			iids = findIID(report_document, "TABLE");
			assertEquals(2, iids.size());

			extractTask.setInstanceID((InstanceID) iids.get(0));
			result = extractTask.extract();
			extractTask.close();

			if (result != null) {
				int num = 0;
				IDataIterator data = result.nextResultIterator();
				if (data != null) {
					data.next();
					num = Integer.parseInt(data.getValue("number").toString());
					assertEquals(103, num);
					if (data.next()) {
						num = Integer.parseInt(data.getValue("number")
								.toString());
						assertEquals(112, num);
					}
				}

			} else {
				fail("Fail to extract data from subquery");
			}
		} catch (Exception e) {
			e.printStackTrace();
			fail("Fail to extract data from subquery");
		}
	}

	/**
	 * test setInstanceID in DataExtractionTask with subquery structure
	 * 
	 */
	public void testDataExtractionFromIID_nestquery() {
		report_design = inputPath + "DataExtraction_nestquery.rptdesign";
		report_document = outputPath + "DataExtraction_nestquery.rptdocument";
		try {
			createReportDocument(report_design, report_document);

			reportDoc = engine.openReportDocument(report_document);
			IDataExtractionTask extractTask = engine
					.createDataExtractionTask(reportDoc);

			/* extract master query data from subquery structure */
			ArrayList iids = findIID(report_document, "LIST");
			assertEquals(1, iids.size());

			extractTask.setInstanceID((InstanceID) iids.get(0));
			IExtractionResults result = extractTask.extract();

			if (result != null) {
				String name = null;
				IDataIterator data = result.nextResultIterator();
				if (data != null) {
					data.next();
					name = data.getValue("name").toString();
					assertTrue(name.equalsIgnoreCase("Atelier graphique"));
					if (data.next()) {
						name = data.getValue("name").toString();
						assertTrue(name.equalsIgnoreCase("Signal Gift Stores"));
					}
				}
			} else {
				fail("Fail to extract data from subquery");
			}

			/* extract sub query data from subquery structure */
			iids = findIID(report_document, "TABLE");
			assertEquals(2, iids.size());
			// get first table instance
			extractTask.setInstanceID((InstanceID) iids.get(0));
			result = extractTask.extract();
			extractTask.close();

			if (result != null) {
				int num = 0;
				IDataIterator data = result.nextResultIterator();
				if (data != null) {
					data.next();
					num = Integer.parseInt(data.getValue("number").toString());
					assertEquals(103, num);
					if (data.next()) {
						num = Integer.parseInt(data.getValue("number")
								.toString());
						assertEquals(112, num);
					}
				}

			} else {
				fail("Fail to extract data from subquery");
			}

		} catch (Exception e) {
			e.printStackTrace();
			fail("Fail to extract data from nestquery");
		}
	}

	/**
	 * create the report document.
	 * 
	 * @throws Exception
	 */
	protected void createReportDocument(String reportdesign,
			String reportdocument) throws Exception {
		// open an report archive, it is a folder archive.
		IDocArchiveWriter archive = new FileArchiveWriter(reportdocument);
		// open the report runnable to execute.
		IReportRunnable report = engine.openReportDesign(reportdesign);
		// create an IRunTask
		IRunTask runTask = engine.createRunTask(report);
		// execute the report to create the report document.
		runTask.setAppContext(new HashMap());
		runTask.run(archive);
		// close the task, release the resource.
		runTask.close();
	}

	private ArrayList findIID(String doc, String type) throws EngineException,
			UnsupportedEncodingException {
		ArrayList iids = new ArrayList();
		IRenderTask task = null;
		IReportDocument reportDoc = null;
		reportDoc = engine.openReportDocument(doc);
		task = engine.createRenderTask(reportDoc);

		IRenderOption htmlRenderOptions = new HTMLRenderOption();
		HashMap appContext = new HashMap();
		task.setAppContext(appContext);

		ByteArrayOutputStream ostream = new ByteArrayOutputStream();
		htmlRenderOptions.setOutputStream(ostream);
		htmlRenderOptions.setOutputFormat("html");
		((HTMLRenderOption) htmlRenderOptions).setEnableMetadata(true);

		task.setRenderOption(htmlRenderOptions);
		task.render();
		task.close();

		String content = ostream.toString("utf-8");
		Pattern typePattern = Pattern.compile("(element_type=\"" + type
				+ "\".*iid=\".*\")");
		Matcher matcher = typePattern.matcher(content);

		while (matcher.find()) {
			String tmp_type = null, strIid = null;
			tmp_type = matcher.group(0);
			strIid = tmp_type.substring(tmp_type.indexOf("iid"));
			strIid = strIid.substring(5, strIid.indexOf("\"", 6));
			iids.add(InstanceID.parse(strIid));
		}

		return iids;
	}
}
