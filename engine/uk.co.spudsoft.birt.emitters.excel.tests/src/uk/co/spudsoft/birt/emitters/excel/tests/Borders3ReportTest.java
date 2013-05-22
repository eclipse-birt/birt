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
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;

public class Borders3ReportTest extends ReportRunner {
	
	/*
	private String translateBorderStyle( int style ) {
		switch( style ) {
		case CellStyle.BORDER_NONE:
			return "CellStyle.BORDER_NONE";
		case CellStyle.BORDER_THIN:
			return "CellStyle.BORDER_THIN";
		case CellStyle.BORDER_MEDIUM:
			return "CellStyle.BORDER_MEDIUM";
		case CellStyle.BORDER_DASHED:
			return "CellStyle.BORDER_DASHED";
		case CellStyle.BORDER_HAIR:
			return "CellStyle.BORDER_HAIR";
		case CellStyle.BORDER_THICK:
			return "CellStyle.BORDER_THICK";
		case CellStyle.BORDER_DOUBLE:
			return "CellStyle.BORDER_DOUBLE";
		case CellStyle.BORDER_DOTTED:
			return "CellStyle.BORDER_DOTTED";
		case CellStyle.BORDER_MEDIUM_DASHED:
			return "CellStyle.BORDER_MEDIUM_DASHED";
		case CellStyle.BORDER_DASH_DOT:
			return "CellStyle.BORDER_DASH_DOT";
		case CellStyle.BORDER_MEDIUM_DASH_DOT:
			return "CellStyle.BORDER_MEDIUM_DASH_DOT";
		case CellStyle.BORDER_DASH_DOT_DOT:
			return "CellStyle.BORDER_DASH_DOT_DOT";
		case CellStyle.BORDER_MEDIUM_DASH_DOT_DOT:
			return "CellStyle.BORDER_MEDIUM_DASH_DOT_DOT";
		case CellStyle.BORDER_SLANTED_DASH_DOT:
			return "CellStyle.BORDER_SLANTED_DASH_DOT";
		default:
			return "Unknown";
		}
	}
	*/
	
	@Test
	public void testRunReport() throws BirtException, IOException {

		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("Borders3.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			assertEquals( "Borders Test Report 3", workbook.getSheetAt(0).getSheetName());
			
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 26, firstNullRow(sheet));
			
			Borders2ReportTest.assertBorder( sheet, 0, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 1, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 1, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 1, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 15, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 16, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 17, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 19, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 21, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 21, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 22, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 22, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 23, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 23, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 24, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 25, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 25, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			
		} finally {
			inputStream.close();
		}
	}

	@Test
	public void testRunReportXls() throws BirtException, IOException {

		debug = false;
		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("Borders3.rptdesign", "xls");
		assertNotNull(inputStream);
		try {
			
			HSSFWorkbook workbook = new HSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 1, workbook.getNumberOfSheets() );
			assertEquals( "Borders Test Report 3", workbook.getSheetAt(0).getSheetName());
			
			Sheet sheet = workbook.getSheetAt(0);
			assertEquals( 26, firstNullRow(sheet));
			
			Borders2ReportTest.assertBorder( sheet, 0, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 0, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 1, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 1, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 1, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 1, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 2, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 2, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 3, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 4, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 5, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 5, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 6, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 6, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 7, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 8, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 9, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 9, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 10, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 11, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 11, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 12, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 13, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 14, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 15, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 15, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 16, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 16, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 17, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 17, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 4, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 18, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 19, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 19, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 20, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 21, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 21, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 21, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 1, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 22, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 22, 6, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 22, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 23, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 23, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 5, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 23, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 2, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 3, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 24, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 24, 7, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 25, 0, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM );
			Borders2ReportTest.assertBorder( sheet, 25, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 2, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 3, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 4, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 5, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 6, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			Borders2ReportTest.assertBorder( sheet, 25, 7, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE, CellStyle.BORDER_MEDIUM, CellStyle.BORDER_NONE );
			
		} finally {
			inputStream.close();
		}
	}

}
