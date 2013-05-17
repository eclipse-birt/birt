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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class AutoColWidthsTest extends ReportRunner {
	
	@Test
	public void testRunReport() throws BirtException, IOException {

		InputStream inputStream = runAndRenderReport("AutoColWidths.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			assertEquals( "AutoColWidths Test Report", workbook.getSheetAt(0).getSheetName());
			
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals(23, this.firstNullRow(sheet));
			
			assertEquals( 6127,                    sheet.getColumnWidth( 0 ) );
			assertEquals( 2048,                    sheet.getColumnWidth( 1 ) );
			assertEquals( 4999,                    sheet.getColumnWidth( 2 ) );
			assertEquals( 3812,                    sheet.getColumnWidth( 3 ) );
			assertEquals( 3812,                    sheet.getColumnWidth( 4 ) );
			assertEquals( 2048,                    sheet.getColumnWidth( 5 ) );
			assertTrue( ( sheet.getColumnWidth( 6 ) > 3000 ) && ( sheet.getColumnWidth( 6 ) < 3200 ) );
			assertTrue( ( sheet.getColumnWidth( 7 ) > 2100 ) && ( sheet.getColumnWidth( 7 ) < 2900 ) );
			assertEquals( 2048,                    sheet.getColumnWidth( 8 ) );
						
			DataFormatter formatter = new DataFormatter();
			
			assertEquals( "1",                     formatter.formatCellValue(sheet.getRow(2).getCell(1)));
			assertEquals( "2019-10-11 13:18:46",   formatter.formatCellValue(sheet.getRow(2).getCell(2)));
			assertEquals( "3.1415926536",          formatter.formatCellValue(sheet.getRow(2).getCell(3)));
			assertEquals( "3.1415926536",          formatter.formatCellValue(sheet.getRow(2).getCell(4)));
			assertEquals( "false",                 formatter.formatCellValue(sheet.getRow(2).getCell(5)));

		} finally {
			inputStream.close();
		}
	}
	
}
