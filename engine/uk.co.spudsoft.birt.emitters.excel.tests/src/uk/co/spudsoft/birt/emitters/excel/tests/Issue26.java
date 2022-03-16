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
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue26 extends ReportRunner {

	@Test
	public void testMultiRowEmptinessXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue26.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(3, this.firstNullRow(sheet));

			assertEquals("Hello\n\nMatey", sheet.getRow(1).getCell(0).getStringCellValue());
			assertEquals(41.3, sheet.getRow(1).getHeightInPoints(), 0.01);
			assertEquals(55.1, sheet.getRow(2).getHeightInPoints(), 0.01);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testMultiRowEmptinessXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue26.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(3, this.firstNullRow(sheet));

			assertEquals("Hello\n\nMatey", sheet.getRow(1).getCell(0).getStringCellValue());
			assertEquals(41.3, sheet.getRow(1).getHeightInPoints(), 0.01);
			assertEquals(55.1, sheet.getRow(2).getHeightInPoints(), 0.01);

		} finally {
			inputStream.close();
		}
	}

}
