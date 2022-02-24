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

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue94DisableRowSpanAutoHeight extends ReportRunner {

	/*
	 * @Test public void testIssue94DisableRowSpanAutoHeight() throws BirtException,
	 * IOException {
	 * 
	 * debug = false; removeEmptyRows = false; spannedRowHeight = null; InputStream
	 * inputStream = runAndRenderReport("Issue94DisableRowSpanAutoHeight.rptdesign",
	 * "xlsx"); assertNotNull(inputStream); try { XSSFWorkbook workbook = new
	 * XSSFWorkbook(inputStream); assertNotNull(workbook);
	 * 
	 * assertEquals( 1, workbook.getNumberOfSheets() );
	 * 
	 * Sheet sheet = workbook.getSheetAt(0); assertEquals( 9, this.lastRow(sheet));
	 * 
	 * assertEquals( sheet.getRow(0).getHeight(), sheet.getRow(1).getHeight() );
	 * assertEquals( sheet.getRow(0).getHeight(), sheet.getRow(3).getHeight() );
	 * assertEquals( sheet.getRow(0).getHeight(), sheet.getRow(4).getHeight() );
	 * assertEquals( sheet.getRow(0).getHeight(), sheet.getRow(6).getHeight() );
	 * assertEquals( sheet.getRow(0).getHeight(), sheet.getRow(8).getHeight() );
	 * assertThat( sheet.getRow(7).getHeight(), greaterThan(
	 * sheet.getRow(8).getHeight() ) ); } finally { inputStream.close(); } }
	 */

	@Test
	public void testIssue94DisableRowSpanAutoHeightAtRow() throws BirtException, IOException {

		debug = false;
		removeEmptyRows = false;
		spannedRowHeight = null;
		InputStream inputStream = runAndRenderReport("Issue94DisableRowSpanAutoHeightAtRow.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(9, this.lastRow(sheet));

			assertEquals(sheet.getRow(0).getHeight(), sheet.getRow(1).getHeight());
			assertEquals(sheet.getRow(3).getHeight(), sheet.getRow(4).getHeight());
			assertTrue(sheet.getRow(6).getHeight() < sheet.getRow(7).getHeight());
			assertTrue(sheet.getRow(6).getHeight() > sheet.getRow(8).getHeight());
			assertTrue(sheet.getRow(7).getHeight() > sheet.getRow(8).getHeight());
		} finally {
			inputStream.close();
		}
	}

}
