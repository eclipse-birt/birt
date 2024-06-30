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
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class BackgroundFormatsTests extends ReportRunner {

	@Test
	public void testRunReportXlsx() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("BackgroundColours.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Background Colours Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(3, this.firstNullRow(sheet));

			DataFormatter formatter = new DataFormatter();

			assertEquals("1", formatter.formatCellValue(sheet.getRow(1).getCell(1)));
			assertEquals("2019-10-11 13:18:46", formatter.formatCellValue(sheet.getRow(1).getCell(2)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(1).getCell(3)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(1).getCell(4)));
			assertEquals("false", formatter.formatCellValue(sheet.getRow(1).getCell(5)));

			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(0).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(1).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(2).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(3).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(4).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(5).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(0).getCell(6).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			/*
			 * assertEquals( null, ((XSSFColor)sheet.getRow(0).getCell(0).getCellStyle().
			 * getFillForegroundColorColor())); assertEquals( null,
			 * ((XSSFColor)sheet.getRow(0).getCell(1).getCellStyle().
			 * getFillForegroundColorColor())); assertEquals( null,
			 * ((XSSFColor)sheet.getRow(0).getCell(2).getCellStyle().
			 * getFillForegroundColorColor())); assertEquals( null,
			 * ((XSSFColor)sheet.getRow(0).getCell(3).getCellStyle().
			 * getFillForegroundColorColor())); assertEquals( null,
			 * ((XSSFColor)sheet.getRow(0).getCell(4).getCellStyle().
			 * getFillForegroundColorColor())); assertEquals( null,
			 * ((XSSFColor)sheet.getRow(0).getCell(5).getCellStyle().
			 * getFillForegroundColorColor())); assertEquals( null,
			 * ((XSSFColor)sheet.getRow(0).getCell(6).getCellStyle().
			 * getFillForegroundColorColor()));
			 */
			assertEquals("FFFF0000",
					((XSSFColor) sheet.getRow(1).getCell(1).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FFFFA500",
					((XSSFColor) sheet.getRow(1).getCell(2).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FFFFFF00",
					((XSSFColor) sheet.getRow(1).getCell(3).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF008000",
					((XSSFColor) sheet.getRow(1).getCell(4).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF0000FF",
					((XSSFColor) sheet.getRow(1).getCell(5).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF800080",
					((XSSFColor) sheet.getRow(1).getCell(6).getCellStyle().getFillForegroundColorColor()).getARGBHex());
			assertEquals("FF000000",
					((XSSFColor) sheet.getRow(1).getCell(7).getCellStyle().getFillForegroundColorColor()).getARGBHex());

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testRunReportXls() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("BackgroundColours.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Background Colours Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(3, this.firstNullRow(sheet));

			DataFormatter formatter = new DataFormatter();

			assertEquals("1", formatter.formatCellValue(sheet.getRow(1).getCell(1)));
			assertEquals("2019-10-11 13:18:46", formatter.formatCellValue(sheet.getRow(1).getCell(2)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(1).getCell(3)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(1).getCell(4)));
			assertEquals("false", formatter.formatCellValue(sheet.getRow(1).getCell(5)));

			assertEquals("FFFF:0:0",
					((HSSFColor) sheet.getRow(1).getCell(1).getCellStyle().getFillForegroundColorColor())
							.getHexString());
			assertEquals("FFFF:A5A5:0",
					((HSSFColor) sheet.getRow(1).getCell(2).getCellStyle().getFillForegroundColorColor())
							.getHexString());
			assertEquals("FFFF:FFFF:0",
					((HSSFColor) sheet.getRow(1).getCell(3).getCellStyle().getFillForegroundColorColor())
							.getHexString());
			assertEquals("0:8080:0",
					((HSSFColor) sheet.getRow(1).getCell(4).getCellStyle().getFillForegroundColorColor())
							.getHexString());
			assertEquals("0:0:FFFF",
					((HSSFColor) sheet.getRow(1).getCell(5).getCellStyle().getFillForegroundColorColor())
							.getHexString());
			assertEquals("8080:0:8080",
					((HSSFColor) sheet.getRow(1).getCell(6).getCellStyle().getFillForegroundColorColor())
							.getHexString());
			assertEquals("0:0:0", ((HSSFColor) sheet.getRow(1).getCell(7).getCellStyle().getFillForegroundColorColor())
					.getHexString());

		} finally {
			inputStream.close();
		}
	}
}
