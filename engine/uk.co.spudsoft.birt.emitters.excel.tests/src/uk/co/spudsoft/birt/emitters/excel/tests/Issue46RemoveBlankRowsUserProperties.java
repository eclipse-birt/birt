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

import java.io.InputStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class Issue46RemoveBlankRowsUserProperties extends ReportRunner {

	@Test
	public void testWithoutOption() throws Exception {

		debug = false;
		InputStream inputStream = runAndRenderReport("BlankRows.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			assertEquals(9, this.firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testWithOption() throws Exception {

		debug = false;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("BlankRows.rptdesign", "xlsx");
		removeEmptyRows = true;
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			assertEquals(12, this.firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testWithUserPropertyOnReport() throws Exception {

		debug = false;
		structuredHeader = false;
		InputStream inputStream = runAndRenderReport("BlankRowsDisabledAtReport.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			assertEquals(12, this.firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testWithUserPropertyOnTable() throws Exception {

		debug = false;
		structuredHeader = false;
		InputStream inputStream = runAndRenderReport("BlankRowsDisabledAtTable.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			assertEquals(11, this.firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

}
