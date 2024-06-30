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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class BigCrosstabTest extends ReportRunner {

	@Test
	public void testXlsx() throws Exception {

		InputStream inputStream = runAndRenderReport("BigCrosstab.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(13, workbook.getNumCellStyles());
			assertEquals("Big Crosstab Report 1", workbook.getSheetAt(0).getSheetName());

			assertEquals(60, workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(3).getCellStyle().getRotation());
			assertEquals(0, workbook.getSheetAt(0).getRow(3).getCell(2).getCellStyle().getRotation());

			assertTrue(runTime - startTime < 4500L);
			assertTrue(renderTime - runTime < 4000L);

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(236, firstNullRow(sheet));

			assertEquals(28, greatestNumColumns(sheet));

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testXls() throws Exception {

		InputStream inputStream = runAndRenderReport("BigCrosstab.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(33, workbook.getNumCellStyles());
			assertEquals("Big Crosstab Report 1", workbook.getSheetAt(0).getSheetName());

			assertEquals(60, workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(3).getCellStyle().getRotation());
			assertEquals(0, workbook.getSheetAt(0).getRow(3).getCell(2).getCellStyle().getRotation());

			assertTrue(runTime - startTime < 4000L);
			assertTrue(renderTime - runTime < 4000L);

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(236, firstNullRow(sheet));

			assertEquals(28, greatestNumColumns(sheet));

		} finally {
			inputStream.close();
		}
	}
}
