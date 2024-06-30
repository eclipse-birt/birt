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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class SingleSheetsReportTest extends ReportRunner {

	public SingleSheetsReportTest() {
		singleSheet = true;
	}

	@Test
	public void testThreeTablesNoNastinessPdfCheck() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("MultiSheets1.rptdesign", "pdf");
		inputStream.close();
	}

	@Test
	public void testThreeTablesNoNastiness() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportAsOne("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));

			assertEquals(false, workbook.getSheetAt(0).isDisplayGridlines());
			assertEquals(false, workbook.getSheetAt(0).isDisplayRowColHeadings());
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testThreeTablesRenderPaginationBug() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportDefaultTask("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testThreeTablesRenderCustomTask() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testBreakInSubTable() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBreakInSubTable.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Multiple Sheets - Break in sub-", workbook.getSheetAt(0).getSheetName());

			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testNoNames() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsNoNames.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testTwoNames() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsTwoNames.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testBigTableDefaultInterval() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBigTableFortyInterval.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(196, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testBigTableZeroInterval() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBigTableZeroInterval.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(192, firstNullRow(workbook.getSheetAt(0)));

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testBigTableZeroIntervalWithPagination() throws BirtException, IOException {

		htmlPagination = true;
		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBigTableZeroInterval.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			assertEquals(196, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}
}
