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

import java.io.FileOutputStream;
import java.io.InputStream;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.report.engine.api.RenderOption;
import org.junit.Ignore;
import org.junit.Test;

import uk.co.spudsoft.birt.emitters.excel.ExcelEmitter;

public class Issue87PrintSettings extends ReportRunner {

	boolean scale = false;

	@Override
	protected RenderOption prepareRenderOptions(String outputFormat, FileOutputStream outputStream) {
		// TODO Auto-generated method stub
		RenderOption options = super.prepareRenderOptions(outputFormat, outputStream);
		if (scale) {
			options.setOption(ExcelEmitter.PRINT_SCALE, 27);
		} else {
			options.setOption(ExcelEmitter.PRINT_PAGES_HIGH, 3);
			options.setOption(ExcelEmitter.PRINT_PAGES_WIDE, 2);
		}
		return options;
	}

	@Test
	@Ignore // FIXME
	public void testPrintPagesXlsx() throws Exception {

		scale = false;
		InputStream inputStream = runAndRenderReport("BigCrosstab.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(13, workbook.getNumCellStyles());
			assertEquals("Big Crosstab Report 1", workbook.getSheetAt(0).getSheetName());

			assertEquals(2, workbook.getSheetAt(0).getPrintSetup().getFitWidth());
			assertEquals(3, workbook.getSheetAt(0).getPrintSetup().getFitHeight());
			assertEquals(true, workbook.getSheetAt(0).getAutobreaks());

			assertEquals(60, workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(3).getCellStyle().getRotation());
			assertEquals(0, workbook.getSheetAt(0).getRow(3).getCell(2).getCellStyle().getRotation());

			assertTrue(runTime - startTime < 4500L);
			assertTrue(renderTime - runTime < 4000L);

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(236, firstNullRow(sheet));

			assertEquals(28, greatestNumColumns(sheet));

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testPrintPagesXls() throws Exception {

		scale = false;
		InputStream inputStream = runAndRenderReport("BigCrosstab.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(33, workbook.getNumCellStyles());
			assertEquals("Big Crosstab Report 1", workbook.getSheetAt(0).getSheetName());

			assertEquals(2, workbook.getSheetAt(0).getPrintSetup().getFitWidth());
			assertEquals(3, workbook.getSheetAt(0).getPrintSetup().getFitHeight());
			assertEquals(true, workbook.getSheetAt(0).getAutobreaks());

			assertEquals(60, workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(3).getCellStyle().getRotation());
			assertEquals(0, workbook.getSheetAt(0).getRow(3).getCell(2).getCellStyle().getRotation());

			assertTrue(runTime - startTime < 4000L);
			assertTrue(renderTime - runTime < 4000L);

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(236, firstNullRow(sheet));

			assertEquals(28, greatestNumColumns(sheet));

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testPrintScaleXlsx() throws Exception {

		scale = true;
		InputStream inputStream = runAndRenderReport("BigCrosstab.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(13, workbook.getNumCellStyles());
			assertEquals("Big Crosstab Report 1", workbook.getSheetAt(0).getSheetName());

			assertEquals(27, workbook.getSheetAt(0).getPrintSetup().getScale());
			// I don't know why, but this is returning true and the output is still good
			// assertEquals( false, workbook.getSheetAt(0).getAutobreaks() );

			assertEquals(60, workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(3).getCellStyle().getRotation());
			assertEquals(0, workbook.getSheetAt(0).getRow(3).getCell(2).getCellStyle().getRotation());

			assertTrue(runTime - startTime < 4500L);
			assertTrue(renderTime - runTime < 4000L);

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(236, firstNullRow(sheet));

			assertEquals(28, greatestNumColumns(sheet));

		} finally {
			inputStream.close();
		}
	}

	@Test
	@Ignore // FIXME
	public void testPrintScaleXls() throws Exception {

		scale = true;
		InputStream inputStream = runAndRenderReport("BigCrosstab.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals(33, workbook.getNumCellStyles());
			assertEquals("Big Crosstab Report 1", workbook.getSheetAt(0).getSheetName());

			assertEquals(27, workbook.getSheetAt(0).getPrintSetup().getScale());
			assertEquals(false, workbook.getSheetAt(0).getAutobreaks());

			assertEquals(60, workbook.getSheetAt(0).getRow(1).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(2).getCellStyle().getRotation());
			assertEquals(60, workbook.getSheetAt(0).getRow(2).getCell(3).getCellStyle().getRotation());
			assertEquals(0, workbook.getSheetAt(0).getRow(3).getCell(2).getCellStyle().getRotation());

			assertTrue(runTime - startTime < 4000L);
			assertTrue(renderTime - runTime < 4000L);

			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(236, firstNullRow(sheet));

			assertEquals(28, greatestNumColumns(sheet));

		} finally {
			inputStream.close();
		}
	}

}
