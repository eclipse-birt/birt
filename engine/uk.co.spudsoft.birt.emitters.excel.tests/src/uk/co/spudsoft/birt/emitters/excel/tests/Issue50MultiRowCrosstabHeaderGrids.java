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
import static org.junit.Assert.assertTrue;

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class Issue50MultiRowCrosstabHeaderGrids extends ReportRunner {

	@Test
	public void testHeader() throws Exception {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue50MultiRowCrosstabHeaderGrids.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals("Atelier graphique", sheet.getRow(2).getCell(1).getStringCellValue());
			assertTrue(mergedRegion(sheet, 0, 0, 1, 0));
			assertTrue(mergedRegion(sheet, 0, 1, 1, 1));
			assertEquals(34, sheet.getNumMergedRegions());

			assertEquals(100, this.firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}

	}
}
