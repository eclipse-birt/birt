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

import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Groupings extends ReportRunner {

	@Test
	public void testGroupings() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("Grouping.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			XSSFSheet sheet2 = workbook.getSheetAt(2);
			assertEquals("HeaderAndFooter", sheet0.getSheetName());

			int rowNum0 = 1;
			int rowNum1 = 1;
			int rowNum2 = 1;
			for (int i = 1; i < 9; ++i) {
				System.out.println("i==" + i);
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 1, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
				for (int j = 0; j < i; ++j) {
					assertEquals("rowNum=" + rowNum0, 1, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
					if (j < i - 1) {
						assertEquals("rowNum=" + rowNum1, i == 1 ? 0 : 1,
								sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
						assertEquals("rowNum=" + rowNum2, i == 1 ? 0 : 1,
								sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
					}
				}
				assertEquals("rowNum=" + rowNum0, 1, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 1, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
			}
			assertTrue(rowNum0 > 50);
			assertTrue(rowNum1 > 40);
			assertTrue(rowNum2 > 40);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testGroupingsBlockedByContext() throws BirtException, IOException {

		disableGrouping = Boolean.TRUE;
		InputStream inputStream = runAndRenderReport("Grouping.rptdesign", "xlsx");
		disableGrouping = null;
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			XSSFSheet sheet2 = workbook.getSheetAt(2);
			assertEquals("HeaderAndFooter", sheet0.getSheetName());

			int rowNum0 = 1;
			int rowNum1 = 1;
			int rowNum2 = 1;
			for (int i = 1; i < 9; ++i) {
				System.out.println("i==" + i);
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
				for (int j = 0; j < i; ++j) {
					assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
					if (j < i - 1) {
						assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
						assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
					}
				}
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
			}
			assertTrue(rowNum0 > 50);
			assertTrue(rowNum1 > 40);
			assertTrue(rowNum2 > 40);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testGroupingsBlockedByReport() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("GroupingDisabledAtReport.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			XSSFSheet sheet2 = workbook.getSheetAt(2);
			assertEquals("HeaderAndFooter", sheet0.getSheetName());

			int rowNum0 = 1;
			int rowNum1 = 1;
			int rowNum2 = 1;
			for (int i = 1; i < 9; ++i) {
				System.out.println("i==" + i);
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
				for (int j = 0; j < i; ++j) {
					assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
					if (j < i - 1) {
						assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
						assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
					}
				}
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
			}
			assertTrue(rowNum0 > 50);
			assertTrue(rowNum1 > 40);
			assertTrue(rowNum2 > 40);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testGroupingsBlockedByTable() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("GroupingDisabledAtTable.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(3, workbook.getNumberOfSheets());

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			XSSFSheet sheet2 = workbook.getSheetAt(2);
			assertEquals("HeaderAndFooter", sheet0.getSheetName());

			int rowNum0 = 1;
			int rowNum1 = 1;
			int rowNum2 = 1;
			for (int i = 1; i < 9; ++i) {
				System.out.println("i==" + i);
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 1, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
				for (int j = 0; j < i; ++j) {
					assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
					if (j < i - 1) {
						assertEquals("rowNum=" + rowNum1, i == 1 ? 0 : 1,
								sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
						assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
					}
				}
				assertEquals("rowNum=" + rowNum0, 0, sheet0.getRow(rowNum0++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum1, 0, sheet1.getRow(rowNum1++).getCTRow().getOutlineLevel());
				assertEquals("rowNum=" + rowNum2, 0, sheet2.getRow(rowNum2++).getCTRow().getOutlineLevel());
			}
			assertTrue(rowNum0 > 50);
			assertTrue(rowNum1 > 40);
			assertTrue(rowNum2 > 40);

		} finally {
			inputStream.close();
		}
	}
}
