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

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.junit.Ignore;
import org.junit.Test;

public class Issue27 extends CellRangeTester {

	@Test
	@Ignore // FIXME
	public void testRowSpanXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue27.rptdesign", "xls");
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
			assertEquals(12, rangesValidated);

		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testRowSpanXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue27.rptdesign", "xlsx");
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
			assertEquals(12, rangesValidated);

		} finally {
			inputStream.close();
		}
	}

	@Override
	protected RenderOption prepareRenderOptions(String outputFormat, FileOutputStream outputStream) {
		RenderOption option = super.prepareRenderOptions(outputFormat, outputStream);
		option.setOption("ExcelEmitter.RemoveBlankRows", Boolean.FALSE);
		return option;
	}

}
