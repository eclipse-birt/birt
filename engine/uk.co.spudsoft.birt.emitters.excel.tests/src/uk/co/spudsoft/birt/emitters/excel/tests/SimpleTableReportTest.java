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

import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class SimpleTableReportTest extends ReportRunner {

	@Test
	public void testRunReport() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("SimpleTable.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Simple Table Report", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(2, firstNullRow(sheet));

			assertEquals("1", sheet.getRow(0).getCell(0).getStringCellValue());
			assertEquals("2", sheet.getRow(1).getCell(0).getStringCellValue());
			assertEquals(3.0, sheet.getRow(0).getCell(1).getNumericCellValue(), 0.001);
			assertEquals(CellType.BLANK /* Cell.CELL_TYPE_BLANK */, sheet.getRow(1).getCell(1).getCellType());

			assertEquals("Title\nSubtitle", sheet.getHeader().getLeft());
			assertEquals("The Writer", sheet.getFooter().getLeft());
			assertEquals("1", sheet.getFooter().getCenter());
		} finally {
			inputStream.close();
		}
	}
}
