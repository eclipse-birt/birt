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

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class NestedTablesFlexible extends ReportRunner {

	@Test
	public void testNestedTables3() throws Exception {

		// debug = true;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("NestedTables3.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Nested Tables Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(13, firstNullRow(sheet));

			assertEquals("Header", sheet.getRow(0).getCell(0).getStringCellValue());
			assertEquals("First Column", sheet.getRow(1).getCell(0).getStringCellValue());
			assertEquals("Wibble", sheet.getRow(1).getCell(3).getStringCellValue());
			assertEquals(1.0, sheet.getRow(1).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(2).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(3).getCell(1).getNumericCellValue(), 0.0);
			assertEquals("First Column", sheet.getRow(5).getCell(0).getStringCellValue());
			assertEquals("Wibble", sheet.getRow(5).getCell(3).getStringCellValue());
			assertEquals(2.0, sheet.getRow(5).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(4.0, sheet.getRow(6).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(7).getCell(1).getNumericCellValue(), 0.0);
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testNestedTables4() throws Exception {

		// debug = true;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("NestedTables4.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Nested Tables Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(13, firstNullRow(sheet));

			assertEquals("Header", sheet.getRow(0).getCell(0).getStringCellValue());
			assertEquals("First Column", sheet.getRow(1).getCell(0).getStringCellValue());
			assertEquals("Wibble", sheet.getRow(1).getCell(3).getStringCellValue());
			assertEquals("Wobble", sheet.getRow(1).getCell(5).getStringCellValue());
			assertEquals(1.0, sheet.getRow(1).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(2).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(3).getCell(1).getNumericCellValue(), 0.0);
			assertEquals("First Column", sheet.getRow(5).getCell(0).getStringCellValue());
			assertEquals("Wibble", sheet.getRow(5).getCell(3).getStringCellValue());
			assertEquals("Wobble", sheet.getRow(5).getCell(5).getStringCellValue());
			assertEquals(2.0, sheet.getRow(5).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(4.0, sheet.getRow(6).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(7).getCell(1).getNumericCellValue(), 0.0);
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testNestedTables5() throws Exception {

		debug = false;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("NestedTables5.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Nested Tables Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(18, firstNullRow(sheet));

			assertEquals(1.0, sheet.getRow(0).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(1).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(2).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(1.0, sheet.getRow(5).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(5).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(5).getCell(2).getNumericCellValue(), 0.0);
			assertEquals("Row1", sheet.getRow(0).getCell(2).getStringCellValue());
			assertEquals("Row2", sheet.getRow(1).getCell(2).getStringCellValue());
			assertEquals("Row3", sheet.getRow(2).getCell(2).getStringCellValue());
			assertEquals("Row4", sheet.getRow(3).getCell(2).getStringCellValue());
			assertEquals("Row5", sheet.getRow(4).getCell(2).getStringCellValue());
			assertEquals("Solo", sheet.getRow(0).getCell(3).getStringCellValue());

			assertEquals(2.0, sheet.getRow(6).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(4.0, sheet.getRow(7).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(8).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(11).getCell(0).getNumericCellValue(), 0.0);
			assertEquals(4.0, sheet.getRow(11).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(11).getCell(2).getNumericCellValue(), 0.0);
			assertEquals("Row1", sheet.getRow(6).getCell(2).getStringCellValue());
			assertEquals("Row2", sheet.getRow(7).getCell(2).getStringCellValue());
			assertEquals("Row3", sheet.getRow(8).getCell(2).getStringCellValue());
			assertEquals("Row4", sheet.getRow(9).getCell(2).getStringCellValue());
			assertEquals("Row5", sheet.getRow(10).getCell(2).getStringCellValue());
			assertEquals("Solo", sheet.getRow(6).getCell(3).getStringCellValue());

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testNestedTables6() throws Exception {

		// debug = true;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("NestedTables6.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Nested Tables Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(13, firstNullRow(sheet));

			assertEquals("Header", sheet.getRow(0).getCell(0).getStringCellValue());
			assertEquals("H2", sheet.getRow(0).getCell(1).getStringCellValue());
			assertEquals("H3", sheet.getRow(0).getCell(2).getStringCellValue());
			assertEquals("H4", sheet.getRow(0).getCell(3).getStringCellValue());
			assertEquals("H5", sheet.getRow(0).getCell(4).getStringCellValue());
			assertEquals("H6", sheet.getRow(0).getCell(5).getStringCellValue());
			assertEquals("H7", sheet.getRow(0).getCell(6).getStringCellValue());

			assertEquals("First Column", sheet.getRow(1).getCell(0).getStringCellValue());
			assertEquals(1.0, sheet.getRow(1).getCell(1).getNumericCellValue(), 0.0);
			assertEquals("Header", sheet.getRow(1).getCell(3).getStringCellValue());
			assertEquals(2.0, sheet.getRow(2).getCell(1).getNumericCellValue(), 0.0);
			assertEquals("Wibble", sheet.getRow(2).getCell(3).getStringCellValue());
			assertEquals("Wobble", sheet.getRow(2).getCell(5).getStringCellValue());
			assertEquals(3.0, sheet.getRow(3).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(1.0, sheet.getRow(4).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(4).getCell(2).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(4).getCell(3).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(4).getCell(4).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(4).getCell(5).getNumericCellValue(), 0.0);
			assertEquals(3.0, sheet.getRow(4).getCell(6).getNumericCellValue(), 0.0);

			assertEquals("First Column", sheet.getRow(5).getCell(0).getStringCellValue());
			assertEquals(2.0, sheet.getRow(5).getCell(1).getNumericCellValue(), 0.0);
			assertEquals("Header", sheet.getRow(5).getCell(3).getStringCellValue());
			assertEquals(4.0, sheet.getRow(6).getCell(1).getNumericCellValue(), 0.0);
			assertEquals("Wibble", sheet.getRow(6).getCell(3).getStringCellValue());
			assertEquals("Wobble", sheet.getRow(6).getCell(5).getStringCellValue());
			assertEquals(6.0, sheet.getRow(7).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(2.0, sheet.getRow(8).getCell(1).getNumericCellValue(), 0.0);
			assertEquals(4.0, sheet.getRow(8).getCell(2).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(8).getCell(3).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(8).getCell(4).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(8).getCell(5).getNumericCellValue(), 0.0);
			assertEquals(6.0, sheet.getRow(8).getCell(6).getNumericCellValue(), 0.0);

		} finally {
			inputStream.close();
		}
	}
}
