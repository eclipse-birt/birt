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

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Borders1ReportTest extends ReportRunner {

	private void assertSingleBorder(Sheet sheet, int row, String border, BorderStyle expected, BorderStyle actual) {
		if (BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */.equals(expected) && BorderStyle.HAIR /* CellStyle.BORDER_HAIR */.equals(actual)
				&& (sheet instanceof XSSFSheet)) {
			// Hopefully a temporary fudge to work around what is believe to be a bug in POI
			return;
		}
		if (expected != actual) {
			System.out.println("Row " + row + ", border \"" + border + "\": " + actual + " != " + expected);
		}
		assertEquals("Row " + row + ", border \"" + border + "\": ", expected, actual);
	}

	private void assertBorder(Sheet sheet, int row, int col, BorderStyle bottom, BorderStyle left, BorderStyle right, BorderStyle top) {

		Cell cell = sheet.getRow(row).getCell(col);
		CellStyle style = cell.getCellStyle();

		assertSingleBorder(sheet, row, "bottom", bottom, style.getBorderBottom());
		assertSingleBorder(sheet, row, "left", left, style.getBorderLeft());
		assertSingleBorder(sheet, row, "right", right, style.getBorderRight());
		assertSingleBorder(sheet, row, "top", top, style.getBorderTop());
	}

	@Test
	public void testRunReport() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Borders1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Borders Test Report 1", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(12, firstNullRow(sheet));

			int i = 0;
			assertBorder(sheet, i++, 0, BorderStyle.THIN /* CellStyle.BORDER_THIN */, BorderStyle.THIN /* CellStyle.BORDER_THIN */, BorderStyle.THIN /* CellStyle.BORDER_THIN */,
					BorderStyle.THIN /* CellStyle.BORDER_THIN */);
			assertBorder(sheet, i++, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			assertBorder(sheet, i++, 0, BorderStyle.THICK /* CellStyle.BORDER_THICK */, BorderStyle.THICK /* CellStyle.BORDER_THICK */, BorderStyle.THICK /* CellStyle.BORDER_THICK */,
					BorderStyle.THICK /* CellStyle.BORDER_THICK */);

			assertBorder(sheet, i++, 0, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */,
					BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */);
			assertBorder(sheet, i++, 0, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */,
					BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */);
			assertBorder(sheet, i++, 0, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */,
					BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */);

			assertBorder(sheet, i++, 0, BorderStyle.DASHED /* CellStyle.BORDER_DASHED */, BorderStyle.DASHED /* CellStyle.BORDER_DASHED */, BorderStyle.DASHED /* CellStyle.BORDER_DASHED */,
					BorderStyle.DASHED /* CellStyle.BORDER_DASHED */);
			assertBorder(sheet, i++, 0, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */,
					BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */);
			assertBorder(sheet, i++, 0, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */,
					BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */);

			assertBorder(sheet, i++, 0, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */,
					BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */);
			assertBorder(sheet, i++, 0, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */,
					BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */);
			assertBorder(sheet, i++, 0, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */,
					BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testRunReportXls() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Borders1.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Borders Test Report 1", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(12, firstNullRow(sheet));

			int i = 0;
			assertBorder(sheet, i++, 0, BorderStyle.THIN /* CellStyle.BORDER_THIN */, BorderStyle.THIN /* CellStyle.BORDER_THIN */, BorderStyle.THIN /* CellStyle.BORDER_THIN */,
					BorderStyle.THIN /* CellStyle.BORDER_THIN */);
			assertBorder(sheet, i++, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			assertBorder(sheet, i++, 0, BorderStyle.THICK /* CellStyle.BORDER_THICK */, BorderStyle.THICK /* CellStyle.BORDER_THICK */, BorderStyle.THICK /* CellStyle.BORDER_THICK */,
					BorderStyle.THICK /* CellStyle.BORDER_THICK */);

			assertBorder(sheet, i++, 0, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */,
					BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */);
			assertBorder(sheet, i++, 0, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */,
					BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */);
			assertBorder(sheet, i++, 0, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */, BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */,
					BorderStyle.DOTTED /* CellStyle.BORDER_DOTTED */);

			assertBorder(sheet, i++, 0, BorderStyle.DASHED /* CellStyle.BORDER_DASHED */, BorderStyle.DASHED /* CellStyle.BORDER_DASHED */, BorderStyle.DASHED /* CellStyle.BORDER_DASHED */,
					BorderStyle.DASHED /* CellStyle.BORDER_DASHED */);
			assertBorder(sheet, i++, 0, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */,
					BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */);
			assertBorder(sheet, i++, 0, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */,
					BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */, BorderStyle.MEDIUM_DASHED /* CellStyle.BORDER_MEDIUM_DASHED */);

			assertBorder(sheet, i++, 0, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */,
					BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */);
			assertBorder(sheet, i++, 0, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */,
					BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */);
			assertBorder(sheet, i++, 0, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */, BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */,
					BorderStyle.DOUBLE /* CellStyle.BORDER_DOUBLE */);

		} finally {
			inputStream.close();
		}
	}

}
