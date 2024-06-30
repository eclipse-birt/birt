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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class CurrencyFormatsTest extends ReportRunner {

	@Test
	public void testRunReportXls() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("CurrencyFormats.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Currency Formats Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(5, this.firstNullRow(sheet));

			DataFormatter formatter = new DataFormatter();

			assertEquals("�3141.59", formatter.formatCellValue(sheet.getRow(1).getCell(1)));
			assertEquals("$3141.59", formatter.formatCellValue(sheet.getRow(2).getCell(1)));
			assertEquals("�3141.59", formatter.formatCellValue(sheet.getRow(3).getCell(1)));
			assertEquals("�3141.59", formatter.formatCellValue(sheet.getRow(4).getCell(1)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testRunReportXlsx() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("CurrencyFormats.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Currency Formats Test Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(5, this.firstNullRow(sheet));

			DataFormatter formatter = new DataFormatter();

			assertEquals("�3141.59", formatter.formatCellValue(sheet.getRow(1).getCell(1)));
			assertEquals("$3141.59", formatter.formatCellValue(sheet.getRow(2).getCell(1)));
			assertEquals("�3141.59", formatter.formatCellValue(sheet.getRow(3).getCell(1)));
			assertEquals("�3141.59", formatter.formatCellValue(sheet.getRow(4).getCell(1)));
		} finally {
			inputStream.close();
		}
	}

}
