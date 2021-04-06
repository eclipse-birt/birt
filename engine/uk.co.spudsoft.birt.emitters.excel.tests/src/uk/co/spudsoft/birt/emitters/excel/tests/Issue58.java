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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue58 extends ReportRunner {

	@Test
	public void testIssue58WithoutOption() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue58.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(21, this.firstNullRow(sheet));
			assertEquals(20, this.lastRow(sheet));

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testIssue58WithOption() throws BirtException, IOException {

		debug = false;
		blankLineAfterTopLevelTable = true;
		InputStream inputStream = runAndRenderReport("Issue58.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(6, this.firstNullRow(sheet));
			assertEquals(21, this.lastRow(sheet));

		} finally {
			inputStream.close();
		}
	}

}
