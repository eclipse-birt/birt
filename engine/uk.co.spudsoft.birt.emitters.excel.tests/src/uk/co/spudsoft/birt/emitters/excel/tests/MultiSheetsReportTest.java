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

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;
import org.junit.Ignore;

@Ignore
public class MultiSheetsReportTest extends ReportRunner {

	@Test
	public void testThreeTablesNoNastinessPdfCheck() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("MultiSheets1.rptdesign", "pdf");
		inputStream.close();
	}

	@Test
	public void testThreeTablesNoNastiness() throws BirtException, IOException {

		debug = true;
		InputStream inputStream = runAndRenderReportAsOne("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats 1", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Number Formats 2", workbook.getSheetAt(1).getSheetName());
			assertEquals( "Number Formats 3", workbook.getSheetAt(2).getSheetName());
			
			assertEquals(4, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(4, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(3, firstNullRow(workbook.getSheetAt(2)));
			
			// assertEquals( "3", workbook.getSheetAt(0).getHeader().getRight() );
			
			assertEquals( true, workbook.getSheetAt(0).isDisplayGridlines());
			assertEquals( false, workbook.getSheetAt(1).isDisplayGridlines());
			assertEquals( false, workbook.getSheetAt(2).isDisplayGridlines());
			assertEquals( true, workbook.getSheetAt(0).isDisplayRowColHeadings());
			assertEquals( false, workbook.getSheetAt(1).isDisplayGridlines());
			assertEquals( true, workbook.getSheetAt(2).isDisplayRowColHeadings());
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testThreeTablesRenderPaginationBug() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportDefaultTask("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats Test Report", workbook.getSheetAt(0).getSheetName());
			
			assertEquals( "3", workbook.getSheetAt(0).getHeader().getRight() );
			
			assertEquals(11, firstNullRow(workbook.getSheetAt(0)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testThreeTablesRenderCustomTask() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheets1.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats 1", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Number Formats 2", workbook.getSheetAt(1).getSheetName());
			assertEquals( "Number Formats 3", workbook.getSheetAt(2).getSheetName());
			
			assertEquals( "3", workbook.getSheetAt(0).getHeader().getRight() );
			
			assertEquals(4, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(4, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(3, firstNullRow(workbook.getSheetAt(2)));
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testBreakInSubTable() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBreakInSubTable.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats 1", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Number Formats 2", workbook.getSheetAt(1).getSheetName());
			assertEquals( "Number Formats 3", workbook.getSheetAt(2).getSheetName());
			
			assertEquals(4, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(4, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(3, firstNullRow(workbook.getSheetAt(2)));
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testNoNames() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsNoNames.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
			assertEquals( "Sheet0", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Sheet1", workbook.getSheetAt(1).getSheetName());
			assertEquals( "Sheet2", workbook.getSheetAt(2).getSheetName());
			
			assertEquals(4, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(4, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(3, firstNullRow(workbook.getSheetAt(2)));
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testTwoNames() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsTwoNames.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 2, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats 2", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Number Formats 3", workbook.getSheetAt(1).getSheetName());
			
			assertEquals(8, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(3, firstNullRow(workbook.getSheetAt(1)));
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testBigTableDefaultInterval() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBigTableFortyInterval.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 5, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats 1", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Number Formats 1 2", workbook.getSheetAt(1).getSheetName());
			assertEquals( "Number Formats 1 3", workbook.getSheetAt(2).getSheetName());
			assertEquals( "Number Formats 1 4", workbook.getSheetAt(3).getSheetName());
			assertEquals( "Number Formats 1 5", workbook.getSheetAt(4).getSheetName());
			
			assertEquals(41, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(41, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(41, firstNullRow(workbook.getSheetAt(2)));
			assertEquals(41, firstNullRow(workbook.getSheetAt(3)));
			assertEquals(32, firstNullRow(workbook.getSheetAt(4)));
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testBigTableZeroInterval() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBigTableZeroInterval.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats Test Report", workbook.getSheetAt(0).getSheetName());
			
			assertEquals(192, firstNullRow(workbook.getSheetAt(0)));
			
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testBigTableZeroIntervalWithPagination() throws BirtException, IOException {

		htmlPagination = true;
		debug = true;
		InputStream inputStream = runAndRenderReportCustomTask("MultiSheetsBigTableZeroInterval.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 5, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats 1", workbook.getSheetAt(0).getSheetName());
			assertEquals( "Number Formats 1 2", workbook.getSheetAt(1).getSheetName());
			assertEquals( "Number Formats 1 3", workbook.getSheetAt(2).getSheetName());
			assertEquals( "Number Formats 1 4", workbook.getSheetAt(3).getSheetName());
			assertEquals( "Number Formats 1 5", workbook.getSheetAt(4).getSheetName());
			
			assertEquals(48, firstNullRow(workbook.getSheetAt(0)));
			assertEquals(48, firstNullRow(workbook.getSheetAt(1)));
			assertEquals(48, firstNullRow(workbook.getSheetAt(2)));
			assertEquals(48, firstNullRow(workbook.getSheetAt(3)));
			assertEquals(4, firstNullRow(workbook.getSheetAt(4)));
			
		} finally {
			inputStream.close();
		}
	}
	
}
