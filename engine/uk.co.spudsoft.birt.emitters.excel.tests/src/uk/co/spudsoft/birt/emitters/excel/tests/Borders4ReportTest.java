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

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.eclipse.birt.core.exception.BirtException;
import org.junit.Test;
import org.junit.Ignore;

@Ignore
public class Borders4ReportTest extends ReportRunner {

	/**
	 * Check that the borders for a given cell match the expected values.
	 * This is complicated by the fact that POI will not always give a particular cell the borders that are seen in Excel
	 * - neighbouring cells may override the values for the chosen cell.
	 * I don't know how to tell which takes precedence, but the following works for the tests I've carried out.
	 */
	public static void assertBorder( Sheet sheet, int row, int col, short bottom, short left, short right, short top ) {
		
		Row curRow = sheet.getRow( row );
		Row prevRow = ( row > 0 ) ? sheet.getRow( row - 1 ) : null;
		Row nextRow = sheet.getRow( row + 1 );
		Cell cell = curRow.getCell(col);
		CellStyle style = cell.getCellStyle();
		
		Cell cellUp = ( prevRow == null ) ? null : prevRow.getCell( col );
		Cell cellDown = ( nextRow == null ) ? null : nextRow.getCell( col );
		Cell cellLeft = ( col == 0 ) ? null : curRow.getCell( col - 1 ); 
		Cell cellRight = curRow.getCell( col + 1 ); 
		
		CellStyle styleUp = ( cellUp == null ) ? null : cellUp.getCellStyle();
		CellStyle styleDown = ( cellDown == null ) ? null : cellDown.getCellStyle();
		CellStyle styleLeft = ( cellLeft == null ) ? null : cellLeft.getCellStyle();
		CellStyle styleRight = ( cellRight == null ) ? null : cellRight.getCellStyle();
		
		System.out.println( "style == " + style );
		System.out.println( "style == " + style );
		
		if( ( top != style.getBorderTop() ) && 
				( ( styleUp == null ) || ( top != styleUp.getBorderBottom() ) ) ) {
			assertEquals( top,    style.getBorderTop() );
		}
		if( ( bottom != style.getBorderBottom() ) && 
				( ( styleDown == null ) || ( bottom != styleDown.getBorderTop() ) ) ) {
			assertEquals( bottom, style.getBorderBottom() );
		}
		if( ( left != style.getBorderLeft() ) && 
				( ( styleLeft == null ) || ( left != styleLeft.getBorderRight() ) ) ) {
			assertEquals( left,   style.getBorderLeft() );
		}
		if( ( right != style.getBorderRight() ) && 
				( ( styleRight == null ) || ( right != styleRight.getBorderLeft() ) ) ) {
			assertEquals( right,  style.getBorderRight() );
		}
	}
	
	@Test
	public void testRunReport() throws BirtException, IOException {

		removeEmptyRows = false;
		InputStream inputStream = runAndRenderReport("Borders4.rptdesign", "xlsx");
		assertNotNull(inputStream);
		try {
			
			XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
			assertNotNull(workbook);
			
			assertEquals( 2, workbook.getNumberOfSheets() );
			
			Sheet sheet = workbook.getSheetAt(1);
			assertEquals( 2, firstNullRow(sheet));
			
			assertBorder( sheet, 0, 0, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );

			assertBorder( sheet, 0, 1, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE, CellStyle.BORDER_NONE );
			
		} finally {
			inputStream.close();
		}
	}

}
