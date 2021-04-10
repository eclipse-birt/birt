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

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFName;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class AutoFilterTest extends ReportRunner {

	@Test
	public void autoFilter() throws Exception {
		debug = false;
		autoFilter = true;
		InputStream inputStream = runAndRenderReport("SideBySideMultiColumns.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Sheet0", workbook.getSheetAt(0).getSheetName());

			XSSFSheet sheet = workbook.getSheetAt(0);
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

			XSSFName name = workbook.getName(XSSFName.BUILTIN_FILTER_DB);
			assertEquals(0, name.getSheetIndex());
			assertEquals("Sheet0!$A$1:$M$2", name.getRefersToFormula());
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void autoFilterMultiSheets() throws Exception {
		debug = false;
		autoFilter = true;
		InputStream inputStream = runAndRenderReport("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());
			assertEquals("Number Formats 1", workbook.getSheetAt(0).getSheetName());
			assertEquals("Number Formats 2", workbook.getSheetAt(1).getSheetName());
			assertEquals("Number Formats 3", workbook.getSheetAt(2).getSheetName());

			assertEquals(4, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(4, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(3, firstNullRow(workbook.getSheetAt(2)));

			assertEquals(true, workbook.getSheetAt(0).isDisplayGridlines());
			assertEquals(false, workbook.getSheetAt(1).isDisplayGridlines());
			assertEquals(false, workbook.getSheetAt(2).isDisplayGridlines());
			assertEquals(true, workbook.getSheetAt(0).isDisplayRowColHeadings());
			assertEquals(false, workbook.getSheetAt(1).isDisplayGridlines());
			assertEquals(true, workbook.getSheetAt(2).isDisplayRowColHeadings());

			XSSFName name = workbook.getName(XSSFName.BUILTIN_FILTER_DB);
			assertEquals(0, name.getSheetIndex());
			assertEquals("'Number Formats 1'!$A$1:$H$3", name.getRefersToFormula());

			assertNotNull(workbook.getSheetAt(0).getCTWorksheet().getAutoFilter());
			assertNotNull(workbook.getSheetAt(1).getCTWorksheet().getAutoFilter());
			assertNotNull(workbook.getSheetAt(2).getCTWorksheet().getAutoFilter());
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void autoFilterMultiTables() throws Exception {
		debug = false;
		autoFilter = true;
		InputStream inputStream = runAndRenderReport("NumberFormats.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Number Formats Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(22, this.firstNullRow(sheet));

			assertEquals(3035, sheet.getColumnWidth(0));
			assertEquals(3913, sheet.getColumnWidth(1));
			assertEquals(7021, sheet.getColumnWidth(2));
			assertEquals(4205, sheet.getColumnWidth(3));
			assertEquals(3474, sheet.getColumnWidth(4));
			assertEquals(2852, sheet.getColumnWidth(5));
			assertEquals(3510, sheet.getColumnWidth(6));
			assertEquals(2889, sheet.getColumnWidth(7));
			assertEquals(2048, sheet.getColumnWidth(8));

			XSSFName name = workbook.getName(XSSFName.BUILTIN_FILTER_DB);
			assertEquals(0, name.getSheetIndex());
			assertEquals("'Number Formats Test Report'!$A$1:$H$3", name.getRefersToFormula());

			assertNotNull(workbook.getSheetAt(0).getCTWorksheet().getAutoFilter());
		} finally {
			inputStream.close();
		}
	}

}
