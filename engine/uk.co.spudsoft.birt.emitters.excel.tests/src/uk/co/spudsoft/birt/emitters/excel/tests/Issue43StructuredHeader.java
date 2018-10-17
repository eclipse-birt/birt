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

import java.io.InputStream;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFPrintSetup;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class Issue43StructuredHeader extends ReportRunner {

	@Test
	public void testWithoutOption() throws Exception {
		
		debug = false;
		InputStream inputStream = runAndRenderReport("StructuredHeader.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
	
			assertEquals( 4, this.firstNullRow(workbook.getSheetAt(0)));
			assertEquals( 4, this.firstNullRow(workbook.getSheetAt(1)));
			assertEquals( 4, this.firstNullRow(workbook.getSheetAt(2)));

			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFPrintSetup printSetup = sheet0.getPrintSetup();
			assertEquals( XSSFPrintSetup.A4_PAPERSIZE,  printSetup.getPaperSize() );
			assertEquals( false, printSetup.getLandscape() );
			
			assertEquals( 0.7 / 2.54, printSetup.getHeaderMargin(), 0.01 );
			assertEquals( 0.7 / 2.54, printSetup.getFooterMargin(), 0.01 );
			assertEquals( 0.7 / 2.54, sheet0.getMargin( Sheet.LeftMargin ), 0.01 );
			assertEquals( 0.7 / 2.54, sheet0.getMargin( Sheet.RightMargin ), 0.01 );
			assertEquals( 2.7 / 2.54, sheet0.getMargin( Sheet.TopMargin ), 0.01 );
			assertEquals( 1.7 / 2.54, sheet0.getMargin( Sheet.BottomMargin ), 0.01 );
			
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testWithOption() throws Exception {
		
		debug = false;
		structuredHeader = true;
		InputStream inputStream = runAndRenderReport("StructuredHeader.rptdesign", "xlsx");
		structuredHeader = false;
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
	
			assertEquals( 8, this.firstNullRow(workbook.getSheetAt(0)));
			assertEquals( 8, this.firstNullRow(workbook.getSheetAt(1)));
			assertEquals( 8, this.firstNullRow(workbook.getSheetAt(2)));
			
			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFPrintSetup printSetup = sheet0.getPrintSetup();
			assertEquals( XSSFPrintSetup.A4_PAPERSIZE,  printSetup.getPaperSize() );
			assertEquals( false, printSetup.getLandscape() );
			
			assertEquals( 0.7 / 2.54, printSetup.getHeaderMargin(), 0.01 );
			assertEquals( 0.7 / 2.54, printSetup.getFooterMargin(), 0.01 );
			assertEquals( 0.7 / 2.54, sheet0.getMargin( Sheet.LeftMargin ), 0.01 );
			assertEquals( 0.7 / 2.54, sheet0.getMargin( Sheet.RightMargin ), 0.01 );
			assertEquals( 0.7 / 2.54, sheet0.getMargin( Sheet.TopMargin ), 0.01 );
			assertEquals( 0.7 / 2.54, sheet0.getMargin( Sheet.BottomMargin ), 0.01 );			
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testWithUserProperty() throws Exception {
		
		debug = false;
		structuredHeader = false;
		InputStream inputStream = runAndRenderReport("StructuredHeaderWithUserProperty.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 3, workbook.getNumberOfSheets() );
	
			assertEquals( 8, this.firstNullRow(workbook.getSheetAt(0)));
			assertEquals( 8, this.firstNullRow(workbook.getSheetAt(1)));
			assertEquals( 8, this.firstNullRow(workbook.getSheetAt(2)));
			
		} finally {
			inputStream.close();
		}
	}
}
