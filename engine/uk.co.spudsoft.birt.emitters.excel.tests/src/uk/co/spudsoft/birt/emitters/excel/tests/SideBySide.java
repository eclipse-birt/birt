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
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Ignore;
import org.junit.Test;

public class SideBySide extends ReportRunner {

	@Test
	@Ignore // FIXME
	public void singleCells() throws Exception {
		debug = false;
		InputStream inputStream = runAndRenderReport("SideBySideOneCellEach.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Sheet0", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(1, this.firstNullRow(sheet));

			assertEquals(305, sheet.getRow(0).getHeightInPoints(), 1.0);
			assertEquals(19346, sheet.getColumnWidth(0));
			assertEquals(19346, sheet.getColumnWidth(1));
		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void multiColumns() throws Exception {
		debug = false;
		InputStream inputStream = runAndRenderReport("SideBySideMultiColumns.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Sheet0", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(124, this.firstNullRow(sheet));

			assertEquals(297, sheet.getRow(0).getHeightInPoints(), 1.0);
			assertEquals(2048, sheet.getColumnWidth(0));
			assertEquals(6196, sheet.getColumnWidth(1));
			assertEquals(3749, sheet.getColumnWidth(2));
			assertEquals(2396, sheet.getColumnWidth(3));
			assertEquals(4516, sheet.getColumnWidth(4));
			assertEquals(7072, sheet.getColumnWidth(5));
			assertEquals(2048, sheet.getColumnWidth(6));
			assertEquals(3509, sheet.getColumnWidth(7));
			assertEquals(2048, sheet.getColumnWidth(8));
			assertEquals(2314, sheet.getColumnWidth(9));
			assertEquals(2338, sheet.getColumnWidth(10));
			assertEquals(2048, sheet.getColumnWidth(11));
			assertEquals(2048, sheet.getColumnWidth(12));

			assertTrue(mergedRegion(sheet, 0, 0, 0, 5));
			assertTrue(mergedRegion(sheet, 0, 7, 0, 12));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void tables() throws Exception {
		debug = false;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("SideBySideTables.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Sheet0", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(29, this.firstNullRow(sheet));

			assertEquals(5522, sheet.getColumnWidth(0));
			assertEquals(2742, sheet.getColumnWidth(1));
			assertEquals(1353, sheet.getColumnWidth(2));
			assertEquals(5522, sheet.getColumnWidth(3));
			assertEquals(2742, sheet.getColumnWidth(4));

			assertTrue(mergedRegion(sheet, 0, 2, 28, 2));
			assertTrue(mergedRegion(sheet, 8, 3, 28, 3));
			assertTrue(mergedRegion(sheet, 8, 4, 28, 4));

		} finally {
			inputStream.close();
		}

	}
}
