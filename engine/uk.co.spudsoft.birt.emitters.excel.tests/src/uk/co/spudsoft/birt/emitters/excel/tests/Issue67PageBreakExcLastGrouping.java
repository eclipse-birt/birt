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

public class Issue67PageBreakExcLastGrouping extends ReportRunner {

	@Test
	public void testSheetNamesAlways() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Issue67PageBreakAlwaysGrouping.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(11, workbook.getNumberOfSheets());
			assertEquals("Row 0", workbook.getSheetAt(0).getSheetName());
			assertEquals("Row 1", workbook.getSheetAt(1).getSheetName());
			assertEquals("Row 2", workbook.getSheetAt(2).getSheetName());
			assertEquals("Row 3", workbook.getSheetAt(3).getSheetName());
			assertEquals("Row 4", workbook.getSheetAt(4).getSheetName());
			assertEquals("Row 5", workbook.getSheetAt(5).getSheetName());
			assertEquals("Row 6", workbook.getSheetAt(6).getSheetName());
			assertEquals("Row 7", workbook.getSheetAt(7).getSheetName());
			assertEquals("Row 8", workbook.getSheetAt(8).getSheetName());
			assertEquals("Row 9", workbook.getSheetAt(9).getSheetName());
			assertEquals("HeaderAndFooter", workbook.getSheetAt(10).getSheetName());

		} finally {
			inputStream.close();
		}
		inputStream.close();
	}

	@Test
	public void testSheetNamesExclLast() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Issue67PageBreakExcLastGrouping.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(10, workbook.getNumberOfSheets());
			assertEquals("Row 0", workbook.getSheetAt(0).getSheetName());
			assertEquals("Row 1", workbook.getSheetAt(1).getSheetName());
			assertEquals("Row 2", workbook.getSheetAt(2).getSheetName());
			assertEquals("Row 3", workbook.getSheetAt(3).getSheetName());
			assertEquals("Row 4", workbook.getSheetAt(4).getSheetName());
			assertEquals("Row 5", workbook.getSheetAt(5).getSheetName());
			assertEquals("Row 6", workbook.getSheetAt(6).getSheetName());
			assertEquals("Row 7", workbook.getSheetAt(7).getSheetName());
			assertEquals("Row 8", workbook.getSheetAt(8).getSheetName());
			assertEquals("Row 9", workbook.getSheetAt(9).getSheetName());

		} finally {
			inputStream.close();
		}
		inputStream.close();
	}

}
