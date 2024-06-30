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

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class WrapTest extends ReportRunner {

	@Test
	public void testRunReport() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Wrap.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(4, workbook.getNumberOfSheets());
			assertEquals("Auto", workbook.getSheetAt(0).getSheetName());
			assertEquals("NoWrap", workbook.getSheetAt(1).getSheetName());
			assertEquals("Normal", workbook.getSheetAt(2).getSheetName());
			assertEquals("Preformatted", workbook.getSheetAt(3).getSheetName());

			assertTrue(!workbook.getSheetAt(0).getRow(1).getCell(1).getCellStyle().getWrapText());
			assertTrue(workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getWrapText());

			assertTrue(!workbook.getSheetAt(1).getRow(1).getCell(1).getCellStyle().getWrapText());
			assertTrue(!workbook.getSheetAt(1).getRow(1).getCell(2).getCellStyle().getWrapText());

			assertTrue(!workbook.getSheetAt(2).getRow(1).getCell(1).getCellStyle().getWrapText());
			assertTrue(workbook.getSheetAt(2).getRow(1).getCell(2).getCellStyle().getWrapText());

			assertTrue(workbook.getSheetAt(3).getRow(1).getCell(1).getCellStyle().getWrapText());
			assertTrue(workbook.getSheetAt(3).getRow(1).getCell(2).getCellStyle().getWrapText());

		} finally {
			inputStream.close();
		}
	}

}
