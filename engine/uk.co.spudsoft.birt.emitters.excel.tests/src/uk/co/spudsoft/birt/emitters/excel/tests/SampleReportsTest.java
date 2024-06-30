/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.tests;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class SampleReportsTest extends ReportRunner {

	/*
	 * I really want these unit tests to cover the sample reports provided with
	 * BIRT, but I cannot package those reports up with my source due to licensing
	 * conflicts. If the BIRT source is available change this path to point to the
	 * root of the sample reports, otherwise set it to null and all these tests will
	 * be ignored.
	 */
	private static String basePath = "C:\\Temp\\birt-source-3_7_1\\plugins\\org.eclipse.birt.report.designer.samplereports\\";

	private InputStream runAndRenderSampleReport(String filename, String extension) throws IOException, BirtException {
		if (basePath != null) {
			File file = new File(basePath + filename);
			if (file.exists()) {
				return runAndRenderReport(file.getAbsolutePath(), extension);
			}
		}
		return null;
	}

	@Test
	public void productCatalogTest() throws BirtException, IOException {

		InputStream inputStream = runAndRenderSampleReport(
				"samplereports/Solution Reports/Listing/ProductCatalog.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void cascadeTest() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Cascaded Parameter Report/cascade.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void customerOrdersFinal() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Combination Chart/CustomerOrdersFinal.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void crosstabSampleRevenue() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Cross tab/CrosstabSampleRevenue.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void orderDetailAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Drill to Details/OrderDetailAfter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void orderMasterAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Drill to Details/OrderMasterAfter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void parameterToDataSetParameter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Dynamic Report Parameter/ParameterToDataSetParameter.rptdesign",
				"xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void masterDetailOneReport() throws BirtException, IOException {
		/*
		 * Currently does not come out well because it does not use a solo sub table,
		 * The nested table has a bunch of leading empty cells.
		 */

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Drill to Details/MasterDetailOneReport.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void expressions() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Expressions/Expressions.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void customerListAfterGrouping() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Grouping/CustomerListAfter_Grouping.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void productListAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Highlighting and Conditional Formatting/ProductListAfter.rptdesign",
				"xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void newsfeedsAfter() throws BirtException, IOException {

		/*
		 * InputStream inputStream = null; parameters.put( "customer", 103);
		 * parameters.put( "order", 10123); if( ( inputStream =
		 * runAndRenderSampleReport(
		 * "samplereports/Reporting Feature Examples/Libraries/Newsfeeds_After.rptdesign"
		 * , "xlsx") ) != null ) { try { assertNotNull(inputStream); } finally {
		 * inputStream.close(); } }
		 */
	}

	@Test
	public void customerListAfterMapping() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Mapping and Sorting/CustomerListAfter_Mapping.rptdesign",
				"xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void employeeAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Parallel Report/EmployeeAfter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void queryModificationNullParameter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Query Modification/NullParameter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void queryModificationOrderDetailsAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Query Modification/OrderDetailsAfter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void productLinesAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Report Elements/ProductLinesAfter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void sortTableByReportParameter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Sorting/SortTableByReportParameter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void subReportOrdersAfter() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/Sub Report/OrdersAfter.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void xmlDataSource() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport(
				"samplereports/Reporting Feature Examples/XML Data Source/XMLDS_After.rptdesign", "xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

	@Test
	public void salesInvoice() throws BirtException, IOException {

		InputStream inputStream;
		parameters.put("customer", 103);
		parameters.put("order", 10123);
		inputStream = runAndRenderSampleReport("samplereports/Solution Reports/Business Forms/SalesInvoice.rptdesign",
				"xlsx");
		if (inputStream != null) {
			try {
				assertNotNull(inputStream);
			} finally {
				inputStream.close();
			}
		}
	}

}
