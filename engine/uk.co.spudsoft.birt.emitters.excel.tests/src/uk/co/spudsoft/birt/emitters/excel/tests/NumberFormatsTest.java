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
import java.util.Locale;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class NumberFormatsTest extends ReportRunner {

	@Test
	public void testRunReport() throws BirtException, IOException {

		debug = false;
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

			DataFormatter formatter = new DataFormatter();
			Locale locale = Locale.getDefault();

			assertEquals("1", formatter.formatCellValue(sheet.getRow(1).getCell(1)));
			assertEquals("2019-10-11 13:18:46", formatter.formatCellValue(sheet.getRow(1).getCell(2)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(1).getCell(3)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(1).getCell(4)));
			assertEquals("false", formatter.formatCellValue(sheet.getRow(1).getCell(5)));
			if (locale.getDisplayName().equals("en-US")) {
				assertEquals("Oct 11, 2019", formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals("1:18:46 PM", formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			} else if (locale.getDisplayName().equals("en-GB")) {
				assertEquals("11-Oct-2019", formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals("13:18:46", formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			}

			assertEquals("2", formatter.formatCellValue(sheet.getRow(2).getCell(1)));
			assertEquals("2019-10-11 13:18:46", formatter.formatCellValue(sheet.getRow(2).getCell(2)));
			assertEquals("6.2831853072", formatter.formatCellValue(sheet.getRow(2).getCell(3)));
			assertEquals("6.2831853072", formatter.formatCellValue(sheet.getRow(2).getCell(4)));
			assertEquals("true", formatter.formatCellValue(sheet.getRow(2).getCell(5)));
			if (locale.getDisplayName().equals("en-US")) {
				assertEquals("Oct 11, 2019", formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals("1:18:46 PM", formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			} else if (locale.getDisplayName().equals("en-GB")) {
				assertEquals("11-Oct-2019", formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals("13:18:46", formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			}

			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(5).getCell(1)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(5).getCell(2)));
			assertEquals("�3.14", formatter.formatCellValue(sheet.getRow(5).getCell(3)));
			assertEquals("3.14", formatter.formatCellValue(sheet.getRow(5).getCell(4)));
			assertEquals("314.16%", formatter.formatCellValue(sheet.getRow(5).getCell(5)));
			assertEquals("3.14E00", formatter.formatCellValue(sheet.getRow(5).getCell(6)));
			assertEquals("3.14E00", formatter.formatCellValue(sheet.getRow(5).getCell(7)));

			assertEquals("6.2831853072", formatter.formatCellValue(sheet.getRow(6).getCell(1)));
			assertEquals("6.2831853072", formatter.formatCellValue(sheet.getRow(6).getCell(2)));
			assertEquals("�6.28", formatter.formatCellValue(sheet.getRow(6).getCell(3)));
			assertEquals("6.28", formatter.formatCellValue(sheet.getRow(6).getCell(4)));
			assertEquals("628.32%", formatter.formatCellValue(sheet.getRow(6).getCell(5)));
			assertEquals("6.28E00", formatter.formatCellValue(sheet.getRow(6).getCell(6)));
			assertEquals("6.28E00", formatter.formatCellValue(sheet.getRow(6).getCell(7)));

			assertEquals("1", formatter.formatCellValue(sheet.getRow(9).getCell(1)));
			if (locale.getDisplayName().equals("en-US")) {
				assertEquals("October 11, 2019 1:18:46 PM", formatter.formatCellValue(sheet.getRow(9).getCell(2)));
			} else if (locale.getDisplayName().equals("en-GB")) {
				assertEquals("11 October 2019 13:18:46", formatter.formatCellValue(sheet.getRow(9).getCell(2)));
			}
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(9).getCell(3)));
			assertEquals("3.1415926536", formatter.formatCellValue(sheet.getRow(9).getCell(4)));
			assertEquals("false", formatter.formatCellValue(sheet.getRow(9).getCell(5)));
			if (locale.getDisplayName().equals("en-US")) {
				assertEquals("10/11/19", formatter.formatCellValue(sheet.getRow(9).getCell(6)));
			} else if (locale.getDisplayName().equals("en-GB")) {
				assertEquals("11/10/19", formatter.formatCellValue(sheet.getRow(9).getCell(6)));
			}
			assertEquals("13:18", formatter.formatCellValue(sheet.getRow(9).getCell(7)));

			assertEquals("2", formatter.formatCellValue(sheet.getRow(10).getCell(1)));
			if (locale.getDisplayName().equals("en-US")) {
				assertEquals("October 11, 2019 1:18:46 PM", formatter.formatCellValue(sheet.getRow(9).getCell(2)));
			} else if (locale.getDisplayName().equals("en-GB")) {
				assertEquals("11 October 2019 13:18:46", formatter.formatCellValue(sheet.getRow(9).getCell(2)));
			}
			assertEquals("6.2831853072", formatter.formatCellValue(sheet.getRow(10).getCell(3)));
			assertEquals("6.2831853072", formatter.formatCellValue(sheet.getRow(10).getCell(4)));
			assertEquals("true", formatter.formatCellValue(sheet.getRow(10).getCell(5)));
			if (locale.getDisplayName().equals("en-US")) {
				assertEquals("10/11/19", formatter.formatCellValue(sheet.getRow(9).getCell(6)));
			} else if (locale.getDisplayName().equals("en-GB")) {
				assertEquals("11/10/19", formatter.formatCellValue(sheet.getRow(9).getCell(6)));
			}
			assertEquals("13:18", formatter.formatCellValue(sheet.getRow(10).getCell(7)));

			assertEquals("MSRP $3.14", formatter.formatCellValue(sheet.getRow(15).getCell(1)));

			assertEquals("_-�* #,##0.00_-;-�* #,##0.00_-;_-�* \"-\"??_-;_-@_-",
					sheet.getRow(19).getCell(1).getCellStyle().getDataFormatString());
			assertEquals(sheet.getRow(18).getCell(2).getStringCellValue(),
					sheet.getRow(19).getCell(2).getCellStyle().getDataFormatString());
			assertEquals(sheet.getRow(18).getCell(3).getStringCellValue(),
					sheet.getRow(19).getCell(3).getCellStyle().getDataFormatString());
			assertEquals(sheet.getRow(18).getCell(4).getStringCellValue(),
					sheet.getRow(19).getCell(4).getCellStyle().getDataFormatString());
			assertEquals(sheet.getRow(18).getCell(5).getStringCellValue(),
					sheet.getRow(19).getCell(5).getCellStyle().getDataFormatString());
			assertEquals(sheet.getRow(18).getCell(6).getStringCellValue(),
					sheet.getRow(19).getCell(6).getCellStyle().getDataFormatString());
			assertEquals(sheet.getRow(18).getCell(7).getStringCellValue(),
					sheet.getRow(19).getCell(7).getCellStyle().getDataFormatString());

		} finally {
			inputStream.close();
		}
	}

}
