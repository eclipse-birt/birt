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

public class Issue72SheetNameWithGroups extends ReportRunner {

	@Test
	public void testPrintBreaksInserted() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Issue72SheetNameWithGroups.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(4, workbook.getNumberOfSheets());
			assertEquals(5, workbook.getSheetAt(0).getRowBreaks().length);
			assertEquals(9, workbook.getSheetAt(1).getRowBreaks().length);
			assertEquals(1, workbook.getSheetAt(2).getRowBreaks().length);
			assertEquals(3, workbook.getSheetAt(3).getRowBreaks().length);

			assertEquals("Australia", workbook.getSheetAt(0).getSheetName());
			assertEquals("France", workbook.getSheetAt(1).getSheetName());
			assertEquals("Israel", workbook.getSheetAt(2).getSheetName());
			assertEquals("New Zealand", workbook.getSheetAt(3).getSheetName());

		} finally {
			inputStream.close();
		}
		inputStream.close();
	}
}
