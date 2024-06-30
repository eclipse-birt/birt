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

public class MultiSheetsPasswordTest extends ReportRunner {

	@Test
	public void testThreeTablesNoNastiness() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportAsOne("MultiSheetsPassword.rptdesign", "xlsx");
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

			assertEquals(true, workbook.getSheetAt(0).getProtect());
			assertEquals(false, workbook.getSheetAt(1).getProtect());
			assertEquals(true, workbook.getSheetAt(2).getProtect());
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testLists() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("ListsPassword.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(28, workbook.getNumberOfSheets());

			assertEquals(17, this.firstNullRow(workbook.getSheetAt(0)));
			assertEquals(11, this.firstNullRow(workbook.getSheetAt(1)));
			assertEquals(11, this.firstNullRow(workbook.getSheetAt(2)));
			assertEquals(13, this.firstNullRow(workbook.getSheetAt(3)));
			assertEquals(11, this.firstNullRow(workbook.getSheetAt(4)));
			assertEquals(13, this.firstNullRow(workbook.getSheetAt(5)));
			assertEquals(31, this.firstNullRow(workbook.getSheetAt(6)));
			assertEquals(33, this.firstNullRow(workbook.getSheetAt(7)));
			assertEquals(9, this.firstNullRow(workbook.getSheetAt(8)));
			assertEquals(11, this.firstNullRow(workbook.getSheetAt(9)));

			assertEquals("Australia", workbook.getSheetAt(0).getSheetName());
			assertEquals("Austria", workbook.getSheetAt(1).getSheetName());

			for (int i = 0; i < 28; ++i) {
				assertEquals(true, workbook.getSheetAt(i).getProtect());
			}

		} finally {
			inputStream.close();
		}
	}

}
