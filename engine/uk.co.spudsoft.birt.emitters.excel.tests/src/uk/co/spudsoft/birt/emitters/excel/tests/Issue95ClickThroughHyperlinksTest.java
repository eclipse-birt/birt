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

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;
import org.junit.Ignore;

@Ignore
public class Issue95ClickThroughHyperlinksTest extends CellRangeTester {
	
	@Test
	public void testHyperlinksXlsx() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue95ClickThroughHyperlinks.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 2002, this.firstNullRow(sheet));

			for(int i = 1; i < 2000; ++i ) {
				assertEquals( "http://www.spudsoft.co.uk/?p=" + i,              sheet.getRow(i).getCell(0).getHyperlink().getAddress());

				assertEquals( "run?__report=Issue95ClickThroughHyperlinks.rptdesign&Pointless=" + i + "&__overwrite=true"
						, sheet.getRow(i).getCell(1).getHyperlink().getAddress() );
			}
		
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testHyperlinksXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue95ClickThroughHyperlinks.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 2002, this.firstNullRow(sheet));

			for(int i = 1; i < 2000; ++i ) {
				assertEquals( "http://www.spudsoft.co.uk/?p=" + i,              sheet.getRow(i).getCell(0).getHyperlink().getAddress());

				assertEquals( "run?__report=Issue95ClickThroughHyperlinks.rptdesign&Pointless=" + i + "&__overwrite=true"
						, sheet.getRow(i).getCell(1).getHyperlink().getAddress() );
			}
		
		} finally {
			inputStream.close();
		}
	}
	

}
