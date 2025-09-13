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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Ignore;
import org.junit.Test;

public class Borders3ReportTest extends ReportRunner {

	@Test
	public void testRunReport() throws BirtException, IOException {

		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("Borders3.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Borders Test Report 3", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(26, firstNullRow(sheet));

			Borders2ReportTest.assertBorder(sheet, 0, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 1, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 1, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 1, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 15, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 16, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 17, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 19, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 21, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 21, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 22, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 22, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 23, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 23, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 24, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 25, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 25, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testRunReportXls() throws BirtException, IOException {

		debug = false;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("Borders3.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Borders Test Report 3", workbook.getSheetAt(0).getSheetName());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(26, firstNullRow(sheet));

			Borders2ReportTest.assertBorder(sheet, 0, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 0, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 1, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 1, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 1, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 1, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 2, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 2, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 3, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 4, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 5, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 5, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 6, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 6, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 7, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 8, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 9, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 9, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 10, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 11, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 11, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 12, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 13, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 14, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 15, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 15, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 16, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 16, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 17, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 17, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 4, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 18, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 19, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 19, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 20, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 21, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 21, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 21, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 1, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 22, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 22, 6, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 22, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 23, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 23, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 5, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 23, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 0, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 2, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 3, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 24, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 24, 7, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 25, 0, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */);
			Borders2ReportTest.assertBorder(sheet, 25, 1, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 2, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 3, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 4, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 5, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 6, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */, BorderStyle.NONE /* CellStyle.BORDER_NONE */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);
			Borders2ReportTest.assertBorder(sheet, 25, 7, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */, BorderStyle.MEDIUM /* CellStyle.BORDER_MEDIUM */,
					BorderStyle.NONE /* CellStyle.BORDER_NONE */);

		} finally {
			inputStream.close();
		}
	}

}
