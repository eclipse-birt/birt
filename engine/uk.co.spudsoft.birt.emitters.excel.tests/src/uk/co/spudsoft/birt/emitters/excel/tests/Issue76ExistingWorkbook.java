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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFChartSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue76ExistingWorkbook extends ReportRunner {

	@Test
	public void testVariousOptions() throws BirtException, IOException {

		InputStream inputStream = null;

		inputStream = openFileStream("MannedSpaceMissions.xlsx");
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());
			assertTrue(workbook.getSheetAt(0) instanceof XSSFChartSheet);
			assertEquals(3, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(5, firstNullRow(workbook.getSheetAt(2)));
		} finally {
			inputStream.close();
		}

		// Output to new XLSX file
		inputStream = runAndRenderReport("Issue76ExistingWorkbook.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(280, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}

		// Output using XLSX template
		String file = deriveFilepath("MannedSpaceMissions.xlsx");
		File template = new File(file);

		assertTrue(template.exists());
		templateFile = template.getAbsolutePath();
		inputStream = runAndRenderReport("Issue76ExistingWorkbook.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());
			assertTrue(workbook.getSheetAt(0) instanceof XSSFChartSheet);
			assertEquals(280, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(5, firstNullRow(workbook.getSheetAt(2)));
		} finally {
			inputStream.close();
		}

	}

}
