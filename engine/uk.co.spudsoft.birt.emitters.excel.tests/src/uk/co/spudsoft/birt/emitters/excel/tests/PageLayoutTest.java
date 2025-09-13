/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013, 2024 James Talbut and others
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

import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.PageMargin;
import org.apache.poi.ss.usermodel.PaperSize;
import org.apache.poi.ss.usermodel.PrintOrientation;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Ignore;
import org.junit.Test;

/**
 * JUnit test of the page layout
 *
 * @since 3.3
 *
 */
public class PageLayoutTest extends ReportRunner {

	/**
	 * Constructor
	 *
	 * @throws BirtException
	 * @throws IOException
	 */
	@Test
	public void testRunReportXlsx() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("PageLayout.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Page Layout Test", workbook.getSheetAt(0).getSheetName());

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFPrintSetup printSetup = sheet0.getPrintSetup();
			assertEquals(PaperSize.A4_PAPER, printSetup.getPaperSizeEnum());
			assertEquals(PrintOrientation.LANDSCAPE, printSetup.getOrientation());
			assertEquals(1.0 / 2.54, printSetup.getHeaderMargin(), 0.01);
			assertEquals(1.0 / 2.54, printSetup.getFooterMargin(), 0.01);
			assertEquals(0.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.LeftMargin)), 0.01);
			assertEquals(0.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.RightMargin)), 0.01);
			assertEquals(1.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.TopMargin)), 0.01);
			assertEquals(1.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.BottomMargin)), 0.01);

		} finally {
			inputStream.close();
		}
	}

	/**
	 * Runner method to execute the XLS creation
	 *
	 * @throws BirtException
	 * @throws IOException
	 */
	@Test
	@Ignore // FIXME
	public void testRunReportXls() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("PageLayout.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Page Layout Test", workbook.getSheetAt(0).getSheetName());

			HSSFSheet sheet0 = workbook.getSheetAt(0);
			HSSFPrintSetup printSetup = sheet0.getPrintSetup();
			assertEquals(PrintSetup.A4_PAPERSIZE, printSetup.getPaperSize());
			assertEquals(true, printSetup.getLandscape());
			assertEquals(1.0 / 2.54, printSetup.getHeaderMargin(), 0.01);
			assertEquals(1.0 / 2.54, printSetup.getFooterMargin(), 0.01);
			assertEquals(0.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.LeftMargin)), 0.01);
			assertEquals(0.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.RightMargin)), 0.01);
			assertEquals(1.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.TopMargin)), 0.01);
			assertEquals(1.7 / 2.54, sheet0.getMargin(PageMargin.getByShortValue(Sheet.BottomMargin)), 0.01);

		} finally {
			inputStream.close();
		}
	}

	/**
	 * Runner method to execute the XLSX creation (pixel-report)
	 *
	 * @throws BirtException
	 * @throws IOException
	 */
	@Test
	public void testRunReportPixelsXlsx() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("PageLayoutPixels.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {

			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Page Layout Test", workbook.getSheetAt(0).getSheetName());

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFPrintSetup printSetup = sheet0.getPrintSetup();
			assertEquals(PaperSize.A4_PAPER, printSetup.getPaperSizeEnum());
			assertEquals(PrintOrientation.LANDSCAPE, printSetup.getOrientation());
			assertEquals(0.3, printSetup.getHeaderMargin(), 0.01);
			assertEquals(0.3, printSetup.getFooterMargin(), 0.01);
			assertEquals(0.7, sheet0.getMargin(PageMargin.getByShortValue(Sheet.LeftMargin)), 0.01);
			assertEquals(0.7, sheet0.getMargin(PageMargin.getByShortValue(Sheet.RightMargin)), 0.01);
			assertEquals(0.75, sheet0.getMargin(PageMargin.getByShortValue(Sheet.TopMargin)), 0.01);
			assertEquals(0.75, sheet0.getMargin(PageMargin.getByShortValue(Sheet.BottomMargin)), 0.01);

		} finally {
			inputStream.close();
		}
	}

	/**
	 * Runner method to execute the XLS creation (pixel-report)
	 *
	 * @throws BirtException
	 * @throws IOException
	 */
	@Test
	@Ignore // FIXME
	public void testRunReportPixelsXls() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("PageLayoutPixels.rptdesign", "xls");
		assertNotNull(inputStream);
		try {

			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);

			assertEquals(1, workbook.getNumberOfSheets());
			assertEquals("Page Layout Test", workbook.getSheetAt(0).getSheetName());

			HSSFSheet sheet0 = workbook.getSheetAt(0);
			HSSFPrintSetup printSetup = sheet0.getPrintSetup();
			assertEquals(PrintSetup.A4_PAPERSIZE, printSetup.getPaperSize());
			assertEquals(true, printSetup.getLandscape());
			assertEquals(0.5, printSetup.getHeaderMargin(), 0.01);
			assertEquals(0.5, printSetup.getFooterMargin(), 0.01);
			assertEquals(0.75, sheet0.getMargin(PageMargin.getByShortValue(Sheet.LeftMargin)), 0.01);
			assertEquals(0.75, sheet0.getMargin(PageMargin.getByShortValue(Sheet.RightMargin)), 0.01);
			assertEquals(1.0, sheet0.getMargin(PageMargin.getByShortValue(Sheet.TopMargin)), 0.01);
			assertEquals(1.0, sheet0.getMargin(PageMargin.getByShortValue(Sheet.BottomMargin)), 0.01);

		} finally {
			inputStream.close();
		}
	}

}
