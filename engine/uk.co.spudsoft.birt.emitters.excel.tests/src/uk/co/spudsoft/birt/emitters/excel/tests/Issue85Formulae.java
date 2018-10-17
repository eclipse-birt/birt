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
import java.util.Locale;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Issue85Formulae extends ReportRunner {
	
	@Test
	public void testRunReport() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue85Formulae.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			assertEquals( "Number Formats Test Report", workbook.getSheetAt(0).getSheetName());
			
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(4, this.firstNullRow(sheet));
			
			assertEquals( 3035,                    sheet.getColumnWidth( 0 ) );
			assertEquals( 2157,                    sheet.getColumnWidth( 1 ) );
			assertEquals( 7021,                    sheet.getColumnWidth( 2 ) );
			assertEquals( 4205,                    sheet.getColumnWidth( 3 ) );
			assertEquals( 3474,                    sheet.getColumnWidth( 4 ) );
			assertEquals( 2852,                    sheet.getColumnWidth( 5 ) );
			assertEquals( 3510,                    sheet.getColumnWidth( 6 ) );
			assertEquals( 2889,                    sheet.getColumnWidth( 7 ) );
			assertEquals( 2048,                    sheet.getColumnWidth( 8 ) );
						
			DataFormatter formatter = new DataFormatter();
			Locale locale = Locale.getDefault();
			
			assertEquals( "1",                     formatter.formatCellValue(sheet.getRow(1).getCell(1)));
			assertEquals( "2019-10-11 13:18:46",   formatter.formatCellValue(sheet.getRow(1).getCell(2)));
			assertEquals( "3.1415926536",          formatter.formatCellValue(sheet.getRow(1).getCell(3)));
			assertEquals( "3.1415926536",          formatter.formatCellValue(sheet.getRow(1).getCell(4)));
			assertEquals( "false",                 formatter.formatCellValue(sheet.getRow(1).getCell(5)));
			if( locale.getDisplayName().equals( "en-US" ) ) {
				assertEquals( "Oct 11, 2019",          formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals( "1:18:46 PM",            formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			} else if( locale.getDisplayName().equals( "en-GB" ) ) {
				assertEquals( "11-Oct-2019",           formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals( "13:18:46",              formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			} 

			assertEquals( "2",                     formatter.formatCellValue(sheet.getRow(2).getCell(1)));
			assertEquals( "2019-10-11 13:18:46",   formatter.formatCellValue(sheet.getRow(2).getCell(2)));
			assertEquals( "6.2831853072",          formatter.formatCellValue(sheet.getRow(2).getCell(3)));
			assertEquals( "6.2831853072",          formatter.formatCellValue(sheet.getRow(2).getCell(4)));
			assertEquals( "true",                  formatter.formatCellValue(sheet.getRow(2).getCell(5)));
			if( locale.getDisplayName().equals( "en-US" ) ) {
				assertEquals( "Oct 11, 2019",          formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals( "1:18:46 PM",            formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			} else if( locale.getDisplayName().equals( "en-GB" ) ) {
				assertEquals( "11-Oct-2019",           formatter.formatCellValue(sheet.getRow(1).getCell(6)));
				assertEquals( "13:18:46",              formatter.formatCellValue(sheet.getRow(1).getCell(7)));
			} 
			
			assertEquals( "B2+B3",                formatter.formatCellValue(sheet.getRow(3).getCell(1))); 
			assertEquals( "D2+D3",                formatter.formatCellValue(sheet.getRow(3).getCell(3))); 
		} finally {
			inputStream.close();
		}
	}
	
}
