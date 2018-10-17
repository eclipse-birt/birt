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

public class Issue110ListElementsOffset extends ReportRunner {

	@Test
	public void testIssue110ListOnly() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue110ListElementsOffsetListOnly.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 112, this.firstNullRow(sheet));
			assertEquals( "18th Century Vintage Horse Carriage", sheet.getRow(2).getCell(0).getStringCellValue());
		
		} finally {
			inputStream.close();
		}
	}
	

	@Test
	public void testIssue110XlsListOnly() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue110ListElementsOffsetListOnly.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 112, this.firstNullRow(sheet));
			assertEquals( "18th Century Vintage Horse Carriage", sheet.getRow(2).getCell(0).getStringCellValue());
		
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testIssue110() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue110ListElementsOffset.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 224, this.firstNullRow(sheet));
			assertEquals( "18th Century Vintage Horse Carriage", sheet.getRow(114).getCell(0).getStringCellValue());
		
		} finally {
			inputStream.close();
		}
	}
	

	@Test
	public void testIssue110Xls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue110ListElementsOffset.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 224, this.firstNullRow(sheet));
			assertEquals( "18th Century Vintage Horse Carriage", sheet.getRow(114).getCell(0).getStringCellValue());
		
		} finally {
			inputStream.close();
		}
	}
	
	@Test
	public void testIssue110MultiNested() throws BirtException, IOException {

		debug = true;
		InputStream inputStream = runAndRenderReport("Issue110ListElementsOffsetMultiNestedTables.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 17, this.firstNullRow(sheet));
			assertEquals( "1969 Harley Davidson Ultimate Chopper", sheet.getRow(10).getCell(0).getStringCellValue());
		
		} finally {
			inputStream.close();
		}
	}
	

	@Test
	public void testIssue110MultiNestedXls() throws BirtException, IOException {

		debug = false;
		InputStream inputStream = runAndRenderReport("Issue110ListElementsOffsetMultiNestedTables.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
	
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 17, this.firstNullRow(sheet));
			assertEquals( "1969 Harley Davidson Ultimate Chopper", sheet.getRow(10).getCell(0).getStringCellValue());
		
		} finally {
			inputStream.close();
		}
	}
	
}

