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
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue29 extends ReportRunner {

	@Test
	public void testMultiRowEmptinessXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue29.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(6, this.firstNullRow(sheet));

			for (int i = 0; i < 4; ++i) {
				for (Cell cell : sheet.getRow(i)) {
					assertEquals(0, cell.getCellStyle().getBorderTop());
					assertEquals(0, cell.getCellStyle().getBorderLeft());
					assertEquals(0, cell.getCellStyle().getBorderRight());
					assertEquals(0, cell.getCellStyle().getBorderBottom());
				}
			}
			assertEquals("Bibble", sheet.getRow(5).getCell(0).getStringCellValue());
			assertEquals(24.0, sheet.getRow(0).getHeightInPoints(), 0.1);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testMultiRowEmptinessXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue29.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(6, this.firstNullRow(sheet));

			for (int i = 0; i < 4; ++i) {
				for (Cell cell : sheet.getRow(i)) {
					assertEquals(0, cell.getCellStyle().getBorderTop());
					assertEquals(0, cell.getCellStyle().getBorderLeft());
					assertEquals(0, cell.getCellStyle().getBorderRight());
					assertEquals(0, cell.getCellStyle().getBorderBottom());
				}
			}
			assertEquals("Bibble", sheet.getRow(5).getCell(0).getStringCellValue());
			assertEquals(24.0, sheet.getRow(0).getHeightInPoints(), 0.1);

		} finally {
			inputStream.close();
		}
	}

}
