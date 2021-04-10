/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *  
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.tests;

import static org.junit.Assert.*;

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
