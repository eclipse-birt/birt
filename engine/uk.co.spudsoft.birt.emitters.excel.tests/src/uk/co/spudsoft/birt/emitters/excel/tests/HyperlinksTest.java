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
import java.util.regex.Matcher;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.AreaReference;
import org.apache.poi.ss.util.CellReference;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Ignore;
import org.junit.Test;

public class HyperlinksTest extends CellRangeTester {

	private void validateNamedRange(Workbook workbook, int index, String name, int sheetIndex, int row1, int col1,
			int row2, int col2) {

		Name namedRange = workbook.getAllNames().get(index);
		assertEquals(name, namedRange.getNameName());
		assertEquals(sheetIndex, namedRange.getSheetIndex());

		AreaReference ref = new AreaReference(namedRange.getRefersToFormula(), SpreadsheetVersion.EXCEL2007);

		if ((row1 == row2) && (col1 == col2)) {
			assertTrue(ref.isSingleCell());
			assertEquals(row1, ref.getFirstCell().getRow());
			assertEquals(col1, ref.getFirstCell().getCol());
		} else {
			assertTrue(AreaReference.isContiguous(namedRange.getRefersToFormula()));
			assertEquals(row1, Math.min(ref.getFirstCell().getRow(), ref.getLastCell().getRow()));
			assertEquals(col1, Math.min(ref.getFirstCell().getCol(), ref.getLastCell().getCol()));
			assertEquals(row2, Math.max(ref.getFirstCell().getRow(), ref.getLastCell().getRow()));
			assertEquals(col2, Math.max(ref.getFirstCell().getCol(), ref.getLastCell().getCol()));
		}
	}

	protected static String buildCellReference(int zeroBasedRow, int zeroBasedCol) {
		CellReference cr = new CellReference(zeroBasedRow, zeroBasedCol);
		return cr.formatAsString();
	}

	@Test
	public void testBuildCellReference() {
		assertEquals("C2", buildCellReference(1, 2));
		assertEquals("Z3", buildCellReference(2, 25));
		assertEquals("AA4", buildCellReference(3, 26));
		assertEquals("AZ5", buildCellReference(4, 51));
		assertEquals("BA6", buildCellReference(5, 52));
		assertEquals("ZZ7", buildCellReference(6, 701));
		assertEquals("AAA8", buildCellReference(7, 702));
		assertEquals("BCZ9", buildCellReference(8, 1455));
		assertEquals("BDA10", buildCellReference(9, 1456));
	}

	@Test
	@Ignore // FIXME
	public void testBookmarksXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Bookmarks.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			Sheet sheet = workbook.getSheetAt(0);
			int rangesValidated = 0;

			for (Row row : sheet) {
				for (Cell cell : row) {
					if (CellType.STRING /* Cell.CELL_TYPE_STRING */.equals(cell.getCellType())) {
						String cellValue = cell.getStringCellValue();
						Matcher matcher = pattern.matcher(cellValue);
						if (matcher.matches()) {
							validateCellRange(matcher, cell);
							++rangesValidated;
						}
					}
				}
			}
			assertEquals(7, rangesValidated);

			assertEquals(18, workbook.getNumberOfNames());
			int index = 0;

			validateNamedRange(workbook, index++, "DataItemOne", -1, 1, 0, 1, 0);
			validateNamedRange(workbook, index++, "DataItem2", -1, 1, 1, 1, 1);
			validateNamedRange(workbook, index++, "Row0", -1, 1, 0, 1, 2);
			validateNamedRange(workbook, index++, "_recreated__bookmark__1", -1, 2, 0, 2, 0);
			validateNamedRange(workbook, index++, "DataItem4", -1, 2, 1, 2, 1);
			validateNamedRange(workbook, index++, "_Row1", -1, 2, 0, 2, 2);
			validateNamedRange(workbook, index++, "_recreated__bookmark__2", -1, 3, 0, 3, 0);
			validateNamedRange(workbook, index++, "DataItem6", -1, 3, 1, 3, 1);
			validateNamedRange(workbook, index++, "_Row2", -1, 3, 0, 3, 2);
			validateNamedRange(workbook, index++, "Table_1", -1, 0, 0, 3, 2);

			validateNamedRange(workbook, index++, "R5C1_R5C1", -1, 4, 0, 4, 0);
			validateNamedRange(workbook, index++, "R6C1_R6C2", -1, 5, 0, 5, 0);
			validateNamedRange(workbook, index++, "R7C1_R7C3", -1, 6, 0, 6, 0);
			validateNamedRange(workbook, index++, "R8C1_R9C1", -1, 7, 0, 7, 0);
			validateNamedRange(workbook, index++, "R10C1_R12C1", -1, 9, 0, 9, 0);
			validateNamedRange(workbook, index++, "R13C1_R14C2", -1, 12, 0, 12, 0);
			validateNamedRange(workbook, index++, "R15C1_R17C3", -1, 14, 0, 14, 0);

			validateNamedRange(workbook, index++, "Grid_1", -1, 4, 0, 15, 2);
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testBookmarksXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Bookmarks.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			Sheet sheet = workbook.getSheetAt(0);
			int rangesValidated = 0;

			for (Row row : sheet) {
				for (Cell cell : row) {
					if (CellType.STRING /* Cell.CELL_TYPE_STRING */.equals(cell.getCellType())) {
						String cellValue = cell.getStringCellValue();

						Matcher matcher = pattern.matcher(cellValue);
						if (matcher.matches()) {
							validateCellRange(matcher, cell);
							++rangesValidated;
						}
					}
				}
			}
			assertEquals(7, rangesValidated);

			assertEquals(18, workbook.getNumberOfNames());
			int index = 0;

			validateNamedRange(workbook, index++, "DataItemOne", -1, 1, 0, 1, 0);
			validateNamedRange(workbook, index++, "DataItem2", -1, 1, 1, 1, 1);
			validateNamedRange(workbook, index++, "Row0", -1, 1, 0, 1, 2);
			validateNamedRange(workbook, index++, "_recreated__bookmark__1", -1, 2, 0, 2, 0);
			validateNamedRange(workbook, index++, "DataItem4", -1, 2, 1, 2, 1);
			validateNamedRange(workbook, index++, "_Row1", -1, 2, 0, 2, 2);
			validateNamedRange(workbook, index++, "_recreated__bookmark__2", -1, 3, 0, 3, 0);
			validateNamedRange(workbook, index++, "DataItem6", -1, 3, 1, 3, 1);
			validateNamedRange(workbook, index++, "_Row2", -1, 3, 0, 3, 2);
			validateNamedRange(workbook, index++, "Table_1", -1, 0, 0, 3, 2);

			validateNamedRange(workbook, index++, "R5C1_R5C1", -1, 4, 0, 4, 0);
			validateNamedRange(workbook, index++, "R6C1_R6C2", -1, 5, 0, 5, 0);
			validateNamedRange(workbook, index++, "R7C1_R7C3", -1, 6, 0, 6, 0);
			validateNamedRange(workbook, index++, "R8C1_R9C1", -1, 7, 0, 7, 0);
			validateNamedRange(workbook, index++, "R10C1_R12C1", -1, 9, 0, 9, 0);
			validateNamedRange(workbook, index++, "R13C1_R14C2", -1, 12, 0, 12, 0);
			validateNamedRange(workbook, index++, "R15C1_R17C3", -1, 14, 0, 14, 0);

			validateNamedRange(workbook, index++, "Grid_1", -1, 4, 0, 15, 2);
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testHyperlinksXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Hyperlinks.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(2002, this.firstNullRow(sheet));

			for (int i = 1; i < 2000; ++i) {
				assertEquals("http://www.spudsoft.co.uk/?p=" + i,
						sheet.getRow(i).getCell(0).getHyperlink().getAddress());

				assertEquals("_BK" + (i + 1000), sheet.getRow(i).getCell(1).getHyperlink().getAddress());
			}

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testHyperlinksXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Hyperlinks.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(2002, this.firstNullRow(sheet));

			for (int i = 1; i < 2000; ++i) {
				assertEquals("http://www.spudsoft.co.uk/?p=" + i,
						sheet.getRow(i).getCell(0).getHyperlink().getAddress());

				assertEquals("_BK" + (i + 1000), sheet.getRow(i).getCell(1).getHyperlink().getAddress());
			}

		} finally {
			inputStream.close();
		}
	}

}
