/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Actuate Corporation - initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.report.model.api;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.IColorConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.elements.Style;
import org.eclipse.birt.report.model.elements.TableColumn;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.util.BaseTestCase;

import com.ibm.icu.util.ULocale;

/**
 * Test cases for copy/paste columns, copy/insert & paste columns and shift
 * columns between tables.
 * 
 */

public class TableColumnBandTest extends BaseTestCase
{

	private String fileName = "TableColumnBandTest.xml"; //$NON-NLS-1$
	
	private String shiftFileName1 = "n1.rptdesign"; //$NON-NLS-1$

	private String shiftFileName = "TableShiftColumnBandTest.xml"; //$NON-NLS-1$
	
	private String shiftWithColumnSpan = "rptWithColSpan.rptdesign";

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Tests copy algorithm on tables.
	 * 
	 * <ul>
	 * <li>without dropping, and col span are all 1, no column information.</li>
	 * <li>without dropping, and col span are all 1, with column information.</li>
	 * <li>without dropping, and one cell's col span are 2, with column
	 * information.</li>
	 * <li>with dropping, and one cell's col span are 2, with column
	 * information.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testColumnCopy( ) throws Exception
	{
		openDesign( fileName );

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		assertNotNull( table );

		ColumnBandData data = table.copyColumn( 1 );
		assertEquals( 2, ApiTestUtil.getCopiedCells( data ).size( ) );

		table = (TableHandle) designHandle.findElement( "My table2" ); //$NON-NLS-1$
		assertNotNull( table );

		data = table.copyColumn( 2 );
		assertEquals( 1, ApiTestUtil.getCopiedCells( data ).size( ) );
		TableColumn column = ApiTestUtil.getCopiedColumn( data );
		assertEquals( 1, column
				.getIntProperty( design, TableColumn.REPEAT_PROP ) );
		assertEquals(
				"red", column.getStringProperty( design, Style.COLOR_PROP ) ); //$NON-NLS-1$		
		// CellContextInfo contextInfo = (CellContextInfo) ApiTestUtil
		// .getCopiedCells( data ).get( 0 );
		assertEquals( 2, ApiTestUtil.getCopiedCell( data, 0 ).getRowSpan(
				design ) );

		table = (TableHandle) designHandle.findElement( "My table3" ); //$NON-NLS-1$
		assertNotNull( table );
		assertFalse( table.canCopyColumn( 1 ) );
		try
		{
			data = table.copyColumn( 1 );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN,
					e.getErrorCode( ) );
		}

		assertFalse( table.canCopyColumn( 2 ) );
		try
		{
			data = table.copyColumn( 2 );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN,
					e.getErrorCode( ) );
		}

		table = (TableHandle) designHandle.findElement( "My table4" ); //$NON-NLS-1$
		assertNotNull( table );
		assertFalse( table.canCopyColumn( 1 ) );
		try
		{
			data = table.copyColumn( 1 );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN,
					e.getErrorCode( ) );
		}

		data = table.copyColumn( 2 );
		assertEquals( 2, ApiTestUtil.getCopiedCells( data ).size( ) );
		assertEquals( IColorConstants.BLACK, ApiTestUtil
				.getCopiedCell( data, 1 )
				.getProperty( design, Style.COLOR_PROP ) );

		// TODO test on cells with dropping in group header
	}

	/**
	 * Tests copy actions on tables.
	 * 
	 * <ul>
	 * <li>without dropping, and col span are all 1, the source has no column
	 * information but the target has.</li>
	 * </ul>
	 * 
	 * @throws Exception
	 * 
	 */

	public void testCopyPasteWithForbiddenLayout( ) throws Exception
	{
		openDesign( fileName );

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		assertNotNull( table );

		ColumnBandData data = table.copyColumn( 1 );

		ElementFactory factory = table.getElementFactory( );
		TableHandle newTable = factory.newTableItem( "newTable1", 2, 0, 1, 0 ); //$NON-NLS-1$
		assertEquals( 2, newTable.getColumns( ).getCount( ) );

		// cannot be pasted since no group row in the new table

		assertFalse( newTable.canPasteColumn( data, 1, true ) );
		try
		{
			newTable.pasteColumn( data, 1, true );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN, e
							.getErrorCode( ) );
		}
	}

	/**
	 * Tests the algorithm to copy one column without a column header to another
	 * table that has a column header.
	 * 
	 * @throws Exception
	 */

	public void testCopyNoColumnHeader2HasColumn( ) throws Exception
	{
		designHandle = new SessionHandle( ULocale.getDefault( ) )
				.createDesign( );
		design = (ReportDesign) designHandle.getModule( );

		ElementFactory factory = new ElementFactory( design );

		TableHandle table1 = factory.newTableItem( "table1", 2, 0, 2, 0 ); //$NON-NLS-1$
		TableHandle table2 = factory.newTableItem( "table2", 2, 0, 2, 0 ); //$NON-NLS-1$
		SlotHandle columns2 = table2.getColumns( );
		ColumnHandle column1InTable2 = (ColumnHandle) columns2.get( 0 );
		ColumnHandle column2InTable2 = (ColumnHandle) columns2.get( 1 );

		column1InTable2.setStringProperty( Style.COLOR_PROP,
				IColorConstants.AQUA );
		column2InTable2.setStringProperty( Style.COLOR_PROP,
				IColorConstants.AQUA );

		// removes all columns in the table 1.

		int numOfColumnsInTable1 = table1.getColumns( ).getCount( );
		for ( int i = 0; i < numOfColumnsInTable1; i++ )
			table1.getColumns( ).dropAndClear( 0 );
		assertEquals( 0, table1.getColumns( ).getCount( ) );

		ColumnBandData data = table1.copyColumn( 1 );
		table2.pasteColumn( data, 1, true );

		assertEquals( 2, columns2.getCount( ) );
		column1InTable2 = (ColumnHandle) columns2.get( 0 );
		assertEquals( IColorConstants.BLACK, column1InTable2
				.getProperty( Style.COLOR_PROP ) );

		column2InTable2 = (ColumnHandle) columns2.get( 1 );
		assertEquals( IColorConstants.AQUA, column2InTable2
				.getProperty( Style.COLOR_PROP ) );
	}

	/**
	 * Tests the algorithm to copy one column header to another table that has
	 * no column header.
	 * 
	 * @throws Exception
	 */

	public void testCopyHasColumnHeader2NoColumn( ) throws Exception
	{
		designHandle = new SessionHandle( ULocale.getDefault( ) )
				.createDesign( );
		design = (ReportDesign) designHandle.getModule( );

		ElementFactory factory = new ElementFactory( design );

		TableHandle table1 = factory.newTableItem( "table1", 2, 0, 2, 0 ); //$NON-NLS-1$
		TableHandle table2 = factory.newTableItem( "table2", 2, 0, 2, 0 ); //$NON-NLS-1$
		SlotHandle columns1 = table1.getColumns( );
		ColumnHandle column1InTable1 = (ColumnHandle) columns1.get( 0 );
		ColumnHandle column2InTable1 = (ColumnHandle) columns1.get( 1 );

		column1InTable1.setStringProperty( Style.COLOR_PROP,
				IColorConstants.AQUA );
		column2InTable1.setStringProperty( Style.COLOR_PROP,
				IColorConstants.AQUA );

		// removes all columns in the table 1.

		int numOfColumnsInTable1 = table2.getColumns( ).getCount( );
		for ( int i = 0; i < numOfColumnsInTable1; i++ )
			table2.getColumns( ).dropAndClear( 0 );
		assertEquals( 0, table2.getColumns( ).getCount( ) );

		ColumnBandData data = table1.copyColumn( 1 );
		table2.pasteColumn( data, 1, true );

		SlotHandle columns2 = table2.getColumns( );

		assertEquals( 2, columns2.getCount( ) );
		ColumnHandle column1InTable2 = (ColumnHandle) columns2.get( 0 );
		assertEquals( IColorConstants.AQUA, column1InTable2
				.getProperty( Style.COLOR_PROP ) );

		ColumnHandle column2InTable2 = (ColumnHandle) columns2.get( 1 );
		column2InTable2 = (ColumnHandle) columns2.get( 1 );
		assertEquals( IColorConstants.BLACK, column2InTable2
				.getProperty( Style.COLOR_PROP ) );
	}

	/**
	 * Tests the algorithm to copy one column to another column.
	 * 
	 * @throws Exception
	 */

	public void testPasteColumnHeader( ) throws Exception
	{
		designHandle = new SessionHandle( ULocale.getDefault( ) )
				.createDesign( );
		design = (ReportDesign) designHandle.getModule( );

		ElementFactory factory = new ElementFactory( design );

		TableHandle table1 = factory.newTableItem( "table1", 3, 0, 2, 0 ); //$NON-NLS-1$
		TableHandle table2 = factory.newTableItem( "table2", 3, 0, 2, 0 ); //$NON-NLS-1$
		SlotHandle columns1 = table1.getColumns( );

		// from column 1 to column 1.

		ColumnHandle column1InTable1 = (ColumnHandle) columns1.get( 0 );
		column1InTable1.setStringProperty( Style.COLOR_PROP,
				IColorConstants.AQUA );

		// make only 1 column in table 2.

		SlotHandle columns2 = table2.getColumns( );

		int numOfColumnsInTable2 = columns2.getCount( );
		for ( int i = 0; i < numOfColumnsInTable2 - 1; i++ )
			columns2.dropAndClear( 0 );
		ColumnHandle columnInTable2 = (ColumnHandle) columns2.get( 0 );
		columnInTable2.setRepeatCount( 3 );
		assertEquals( 1, columns2.getCount( ) );

		// copy from column 1 to column 1, splitting columns is required.

		ColumnBandData data = table1.copyColumn( 1 );
		table2.pasteColumn( data, 1, true );

		assertEquals( 2, columns2.getCount( ) );
		ColumnHandle column1InTable2 = (ColumnHandle) columns2.get( 0 );
		assertEquals( IColorConstants.AQUA, column1InTable2
				.getProperty( Style.COLOR_PROP ) );

		ColumnHandle column2InTable2 = (ColumnHandle) columns2.get( 1 );
		assertEquals( 2, column2InTable2.getRepeatCount( ) );
		assertEquals( IColorConstants.BLACK, column2InTable2
				.getProperty( Style.COLOR_PROP ) );

		// make only 1 column in table 2.

		numOfColumnsInTable2 = columns2.getCount( );
		for ( int i = 0; i < numOfColumnsInTable2 - 1; i++ )
			columns2.dropAndClear( 0 );
		columnInTable2 = (ColumnHandle) columns2.get( 0 );
		columnInTable2.setRepeatCount( 3 );
		assertEquals( 1, columns2.getCount( ) );

		// copy from column 1 to column 3, splitting columns is required.

		data = table1.copyColumn( 1 );
		table2.pasteColumn( data, 3, true );
		assertEquals( 2, columns2.getCount( ) );

		column1InTable2 = (ColumnHandle) columns2.get( 0 );
		assertEquals( 2, column1InTable2.getRepeatCount( ) );
		assertEquals( IColorConstants.BLACK, column1InTable2
				.getProperty( Style.COLOR_PROP ) );

		column2InTable2 = (ColumnHandle) columns2.get( 1 );
		assertEquals( IColorConstants.AQUA, column2InTable2
				.getProperty( Style.COLOR_PROP ) );

		// make only 1 column in table 2.

		numOfColumnsInTable2 = columns2.getCount( );
		columns2.dropAndClear( 1 );
		columnInTable2 = (ColumnHandle) columns2.get( 0 );
		columnInTable2.setRepeatCount( 3 );
		assertEquals( 1, columns2.getCount( ) );

		// copy from column 1 to column 2, splitting columns is required.
		// Becomes 3 columns

		data = table1.copyColumn( 1 );
		table2.pasteColumn( data, 2, true );
		assertEquals( 3, columns2.getCount( ) );

		// verify column 1.

		column1InTable2 = (ColumnHandle) columns2.get( 0 );
		assertEquals( 1, column1InTable2.getRepeatCount( ) );
		assertEquals( IColorConstants.BLACK, column1InTable2
				.getProperty( Style.COLOR_PROP ) );

		// verify column 2.

		column2InTable2 = (ColumnHandle) columns2.get( 1 );
		assertEquals( 1, column2InTable2.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column2InTable2
				.getProperty( Style.COLOR_PROP ) );

		// verify column 3.

		ColumnHandle column3InTable2 = (ColumnHandle) columns2.get( 2 );
		assertEquals( 1, column3InTable2.getRepeatCount( ) );
		assertEquals( IColorConstants.BLACK, column3InTable2
				.getProperty( Style.COLOR_PROP ) );
	}

	/**
	 * Copies non-merged cells in the source table to another table with merged
	 * cells.
	 * 
	 * @throws Exception
	 */

	public void testPasteNonMergedCells2MergedCells( ) throws Exception
	{
		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable1" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable1" ); //$NON-NLS-1$
		assertNotNull( pasteTable );

		SlotHandle detail = pasteTable.getDetail( );
		RowHandle row1 = (RowHandle) detail.get( 0 );
		RowHandle row2 = (RowHandle) detail.get( 1 );
		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 1, row2.getCells( ).getCount( ) );

		CellHandle cell1 = (CellHandle) row1.getCells( ).get( 0 );
		assertEquals( 2, cell1.getRowSpan( ) );

		SlotHandle columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );
		ColumnHandle column2 = (ColumnHandle) columns.get( 1 );
		assertEquals( IColorConstants.RED, column2
				.getStringProperty( Style.COLOR_PROP ) );

		ColumnBandData data = copyTable.copyColumn( 1 );

		// different layout, the exception is thrown.

		assertFalse( pasteTable.canPasteColumn( data, 1, false ) );
		try
		{
			pasteTable.pasteColumn( data, 1, false );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT,
					e.getErrorCode( ) );
		}

		// paste cells in force.

		// verify the layout of the pasted table.

		pasteTable.pasteColumn( data, 1, true );

		detail = pasteTable.getDetail( );
		assertEquals( 2, detail.getCount( ) );

		row1 = (RowHandle) detail.get( 0 );
		row2 = (RowHandle) detail.get( 1 );

		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 2, row2.getCells( ).getCount( ) );

		cell1 = (CellHandle) row1.getCells( ).get( 0 );
		assertEquals( 1, cell1.getRowSpan( ) );

		columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );

		// verify column information

		column2 = (ColumnHandle) columns.get( 0 );
		assertEquals( IColorConstants.AQUA, column2
				.getStringProperty( Style.COLOR_PROP ) );
		assertEquals( 1, column2.getRepeatCount( ) );

		ColumnHandle column1 = (ColumnHandle) columns.get( 1 );
		assertEquals( IColorConstants.RED, column1
				.getStringProperty( Style.COLOR_PROP ) );
		assertEquals( 1, column2.getRepeatCount( ) );
	}

	/**
	 * Copies merged cells in the source table to another table without merged
	 * cells.
	 * 
	 * @throws Exception
	 */

	public void testPasteMergedCells2NonMergedCells( ) throws Exception
	{

		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable2" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable2" ); //$NON-NLS-1$
		assertNotNull( pasteTable );

		SlotHandle detail = pasteTable.getDetail( );
		RowHandle row1 = (RowHandle) detail.get( 0 );
		RowHandle row2 = (RowHandle) detail.get( 1 );
		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 2, row2.getCells( ).getCount( ) );

		SlotHandle columns = pasteTable.getColumns( );
		assertEquals( 1, columns.getCount( ) );
		ColumnHandle column1 = (ColumnHandle) columns.get( 0 );
		assertEquals( 2, column1.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column1
				.getStringProperty( Style.COLOR_PROP ) );

		ColumnBandData data = copyTable.copyColumn( 1 );
		assertEquals( 1, ApiTestUtil.getCopiedCells( data ).size( ) );

		// different layout, the exception is thrown.

		assertFalse( pasteTable.canPasteColumn( data, 2, false ) );
		assertTrue( pasteTable.canPasteColumn( data, 2, true ) );
		try
		{
			pasteTable.pasteColumn( data, 2, false );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_DIFFERENT_LAYOUT,
					e.getErrorCode( ) );
		}

		// paste cells in force.

		// verify the layout of the pasted table.

		pasteTable.pasteColumn( data, 2, true );
		detail = pasteTable.getDetail( );
		assertEquals( 2, detail.getCount( ) );

		row1 = (RowHandle) detail.get( 0 );
		row2 = (RowHandle) detail.get( 1 );

		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 1, row2.getCells( ).getCount( ) );

		CellHandle cell1 = (CellHandle) row1.getCells( ).get( 1 );
		assertEquals( 2, cell1.getRowSpan( ) );

		// vefiry that the new cell has a label element.

		assertEquals( 1, cell1.getContent( ).getCount( ) );

		columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );

		// verify column information

		ColumnHandle column2 = (ColumnHandle) columns.get( 1 );
		assertEquals( IColorConstants.YELLOW, column2
				.getStringProperty( Style.COLOR_PROP ) );
		assertEquals( 1, column2.getRepeatCount( ) );

		column1 = (ColumnHandle) columns.get( 0 );
		assertEquals( IColorConstants.AQUA, column1
				.getStringProperty( Style.COLOR_PROP ) );
		assertEquals( 1, column2.getRepeatCount( ) );
	}

	/**
	 * Copies and pastes columns between tables with undo/redo supports.
	 * 
	 * @throws Exception
	 */

	public void testPasteWithUndoRedo( ) throws Exception
	{
		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable2" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable2" ); //$NON-NLS-1$
		assertNotNull( pasteTable );

		SlotHandle detail = pasteTable.getDetail( );
		RowHandle row1 = (RowHandle) detail.get( 0 );
		RowHandle row2 = (RowHandle) detail.get( 1 );
		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 2, row2.getCells( ).getCount( ) );

		SlotHandle columns = pasteTable.getColumns( );
		assertEquals( 1, columns.getCount( ) );

		ColumnBandData data = copyTable.copyColumn( 1 );

		// paste cells in force.

		// verify the layout of the pasted table.

		pasteTable.pasteColumn( data, 2, true );
		detail = pasteTable.getDetail( );
		assertEquals( 2, detail.getCount( ) );

		row1 = (RowHandle) detail.get( 0 );
		row2 = (RowHandle) detail.get( 1 );

		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 1, row2.getCells( ).getCount( ) );

		columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );

		design.getActivityStack( ).undo( );

		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 2, row2.getCells( ).getCount( ) );

		columns = pasteTable.getColumns( );
		assertEquals( 1, columns.getCount( ) );

		design.getActivityStack( ).redo( );

		assertEquals( 2, row1.getCells( ).getCount( ) );
		assertEquals( 1, row2.getCells( ).getCount( ) );

		columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );
	}

	/**
	 * Test case is:
	 * <p>
	 * Copies one column to another table. The content in the cell is replaced.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testColumnWithDifferentLayout( ) throws Exception
	{
		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable3" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable3" ); //$NON-NLS-1$
		assertNotNull( pasteTable );
		SlotHandle pasteFooter = pasteTable.getFooter( );
		RowHandle pasteFooterRow = (RowHandle) pasteFooter.get( 0 );
		CellHandle pasteFooterCell = (CellHandle) pasteFooterRow.getCells( )
				.get( 1 );
		DesignElementHandle pasteFooterCellContent = pasteFooterCell
				.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof LabelHandle );
		assertEquals( "toReplacedLabel", pasteFooterCellContent.getName( ) ); //$NON-NLS-1$

		ColumnBandData data = copyTable.copyColumn( 2 );
		pasteTable.pasteColumn( data, 2, true );

		// check after the paste operation.

		SlotHandle pasteColumns = pasteTable.getColumns( );
		assertEquals( 3, pasteColumns.getCount( ) );

		ColumnHandle column = (ColumnHandle) pasteColumns.get( 0 );
		assertEquals( IColorConstants.AQUA, column
				.getProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) pasteColumns.get( 1 );
		assertEquals( IColorConstants.RED, column
				.getProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) pasteColumns.get( 2 );
		assertEquals( IColorConstants.AQUA, column
				.getProperty( Style.COLOR_PROP ) );

		pasteFooter = pasteTable.getFooter( );
		pasteFooterRow = (RowHandle) pasteFooter.get( 0 );
		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 1 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof TextItemHandle );

	}

	/**
	 * Test case is:
	 * <p>
	 * Copies one column and paste it multiple times.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testColumnWithMultiplePaste( ) throws Exception
	{
		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable3" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable3" ); //$NON-NLS-1$
		assertNotNull( pasteTable );

		ColumnBandData data = copyTable.copyColumn( 2 );
		pasteTable.pasteColumn( data, 2, true );

		ColumnBandData clonedData = data.copy( );
		pasteTable.pasteColumn( clonedData, 3, true );

		SlotHandle pasteFooter = pasteTable.getFooter( );
		RowHandle pasteFooterRow = (RowHandle) pasteFooter.get( 0 );
		CellHandle pasteFooterCell = (CellHandle) pasteFooterRow.getCells( )
				.get( 1 );
		DesignElementHandle pasteFooterCellContent = pasteFooterCell
				.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof TextItemHandle );

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 2 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof TextItemHandle );
		// assertNull( pasteFooterCellContent.getName( ) );

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 0 );
		assertEquals( 0, pasteFooterCell.getContent( ).getCount( ) );
	}

	/**
	 * 
	 * @throws Exception
	 */

	public void testInsertAndPasteWithColumn( ) throws Exception
	{
		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable3" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable3" ); //$NON-NLS-1$
		assertNotNull( pasteTable );

		// paste to the middle of a table

		ColumnBandData data = copyTable.copyColumn( 2 );
		pasteTable.insertAndPasteColumn( data, 2 );

		SlotHandle columns = pasteTable.getColumns( );
		assertEquals( 3, columns.getCount( ) );

		ColumnHandle column = (ColumnHandle) columns.get( 0 );
		assertEquals( 2, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 1 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.RED, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 2 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		assertEquals( 4, pasteTable.getColumnCount( ) );

		SlotHandle pasteFooter = pasteTable.getFooter( );
		RowHandle pasteFooterRow = (RowHandle) pasteFooter.get( 0 );
		assertEquals( 4, pasteFooterRow.getCells( ).getCount( ) );

		CellHandle pasteFooterCell = (CellHandle) pasteFooterRow.getCells( )
				.get( 1 );
		DesignElementHandle pasteFooterCellContent = pasteFooterCell
				.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof LabelHandle );
		assertEquals( "toReplacedLabel", pasteFooterCellContent.getName( ) ); //$NON-NLS-1$

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 2 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof TextItemHandle );
		// assertNull( pasteFooterCellContent.getName( ) );

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 0 );
		assertEquals( 0, pasteFooterCell.getContent( ).getCount( ) );

		designHandle.getCommandStack( ).undo( );

		// paste to the end of a table

		pasteTable.insertAndPasteColumn( data, 3 );

		columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );

		column = (ColumnHandle) columns.get( 0 );
		assertEquals( 3, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 1 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.RED, column
				.getStringProperty( Style.COLOR_PROP ) );

		assertEquals( 4, pasteTable.getColumnCount( ) );

		pasteFooter = pasteTable.getFooter( );
		pasteFooterRow = (RowHandle) pasteFooter.get( 0 );
		assertEquals( 4, pasteFooterRow.getCells( ).getCount( ) );

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 1 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof LabelHandle );
		assertEquals( "toReplacedLabel", pasteFooterCellContent.getName( ) ); //$NON-NLS-1$

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 3 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof TextItemHandle );
		// assertNull( pasteFooterCellContent.getName( ) );

		designHandle.getCommandStack( ).undo( );

		// paste to the start of a table

		assertTrue( pasteTable.canInsertAndPasteColumn( data, 0 ) );
		pasteTable.insertAndPasteColumn( data, 0 );

		columns = pasteTable.getColumns( );
		assertEquals( 2, columns.getCount( ) );

		column = (ColumnHandle) columns.get( 0 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.RED, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 1 );
		assertEquals( 3, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		assertEquals( 4, pasteTable.getColumnCount( ) );

		pasteFooter = pasteTable.getFooter( );
		pasteFooterRow = (RowHandle) pasteFooter.get( 0 );
		assertEquals( 4, pasteFooterRow.getCells( ).getCount( ) );

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 0 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof TextItemHandle );
		// assertNull( pasteFooterCellContent.getName( ) );

		pasteFooterCell = (CellHandle) pasteFooterRow.getCells( ).get( 2 );
		pasteFooterCellContent = pasteFooterCell.getContent( ).get( 0 );
		assertTrue( pasteFooterCellContent instanceof LabelHandle );
		assertEquals( "toReplacedLabel", pasteFooterCellContent.getName( ) ); //$NON-NLS-1$		

	}

	/**
	 * Copies merged cells in the source table to another table without merged
	 * cells.
	 * 
	 * @throws Exception
	 */

	public void testCanInsertAndPaste( ) throws Exception
	{
		openDesign( fileName );

		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable2" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable2" ); //$NON-NLS-1$
		assertNotNull( pasteTable );

		ColumnBandData data = copyTable.copyColumn( 1 );
		assertEquals( 1, ApiTestUtil.getCopiedCells( data ).size( ) );

		assertTrue( pasteTable.canInsertAndPasteColumn( data, 1 ) );
		assertTrue( pasteTable.canInsertAndPasteColumn( data, 2 ) );

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		assertNotNull( table );

		data = table.copyColumn( 1 );

		ElementFactory factory = table.getElementFactory( );
		TableHandle newTable = factory.newTableItem( "newTable1", 2, 2, 0, 2 ); //$NON-NLS-1$
		assertEquals( 2, newTable.getColumns( ).getCount( ) );

		// cannot be pasted since no group row in the new table

		assertFalse( newTable.canInsertAndPasteColumn( data, 1 ) );

		newTable = factory.newTableItem( "newTable1", 2, 2, 0, 0 ); //$NON-NLS-1$

		table = (TableHandle) designHandle.findElement( "My table5" ); //$NON-NLS-1$
		assertNotNull( table );

		data = newTable.copyColumn( 1 );

		// cannot be pasted since no group row in the new table

		assertFalse( table.canInsertAndPasteColumn( data, 1 ) );
		assertFalse( table.canInsertAndPasteColumn( data, 2 ) );
	}

	/**
	 * Copies merged cells in the source table to another table without merged
	 * cells.
	 * 
	 * @throws Exception
	 */

	public void testInsertPasteWithoutColumns( ) throws Exception
	{
		openDesign( fileName );
		design = (ReportDesign) designHandle.getModule( );

		ElementFactory factory = new ElementFactory( design );

		TableHandle newTable = factory.newTableItem( "newTable1", 2, 0, 2, 0 ); //$NON-NLS-1$
		assertEquals( 2, newTable.getColumns( ).getCount( ) );

		openDesign( fileName );
		TableHandle table = (TableHandle) designHandle
				.findElement( "My table5" ); //$NON-NLS-1$
		assertNotNull( table );

		ColumnBandData data = newTable.copyColumn( 1 );

		// cannot be pasted there is a column span in the middle

		assertFalse( table.canInsertAndPasteColumn( data, 1 ) );

		// append to the end of the table, can be inserted and pasted.

		assertTrue( table.canInsertAndPasteColumn( data, 2 ) );

		table.insertAndPasteColumn( data, 2 );

		assertEquals( 3, table.getColumnCount( ) );
		SlotHandle columns = table.getColumns( );
		assertEquals( 3, columns.getCount( ) );

		SlotHandle detail = table.getDetail( );
		RowHandle row1 = (RowHandle) detail.get( 0 );
		assertEquals( 2, row1.getCells( ).getCount( ) );
		row1 = (RowHandle) detail.get( 1 );
		assertEquals( 3, row1.getCells( ).getCount( ) );
	}

	/**
	 * Tests the algorithm to insert and paste one column to another column.
	 * 
	 * @throws Exception
	 */

	public void testInsertAndPasteForColumn( ) throws Exception
	{
		openDesign( fileName );
		TableHandle copyTable = (TableHandle) designHandle
				.findElement( "CopyTable4" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		TableHandle pasteTable = (TableHandle) designHandle
				.findElement( "PasteTable4" ); //$NON-NLS-1$
		assertNotNull( copyTable );

		ColumnBandData data = copyTable.copyColumn( 1 );
		pasteTable.insertAndPasteColumn( data, 1 );

		SlotHandle columns = pasteTable.getColumns( );
		assertEquals( 3, columns.getCount( ) );

		ColumnHandle column = (ColumnHandle) columns.get( 0 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 1 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.RED, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 2 );
		assertEquals( 2, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		RowHandle row = (RowHandle) pasteTable.getDetail( ).get( 0 );
		CellHandle cell = (CellHandle) row.getCells( ).get( 0 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 1 );
		assertEquals( 1, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 2 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 3 );
		assertEquals( 2, cell.getRowSpan( ) );

		design.getActivityStack( ).undo( );

		data = data.copy( );
		pasteTable.insertAndPasteColumn( data, 2 );

		column = (ColumnHandle) columns.get( 0 );
		assertEquals( 2, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 1 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.RED, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 2 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		row = (RowHandle) pasteTable.getDetail( ).get( 0 );
		cell = (CellHandle) row.getCells( ).get( 0 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 1 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 2 );
		assertEquals( 1, cell.getRowSpan( ) );
		assertEquals( 1, cell.getContent( ).getCount( ) );

		cell = (CellHandle) row.getCells( ).get( 3 );
		assertEquals( 2, cell.getRowSpan( ) );

		design.getActivityStack( ).undo( );

		data = data.copy( );
		pasteTable.insertAndPasteColumn( data, 3 );

		assertEquals( 2, columns.getCount( ) );
		column = (ColumnHandle) columns.get( 0 );
		assertEquals( 3, column.getRepeatCount( ) );
		assertEquals( IColorConstants.AQUA, column
				.getStringProperty( Style.COLOR_PROP ) );

		column = (ColumnHandle) columns.get( 1 );
		assertEquals( 1, column.getRepeatCount( ) );
		assertEquals( IColorConstants.RED, column
				.getStringProperty( Style.COLOR_PROP ) );

		row = (RowHandle) pasteTable.getDetail( ).get( 0 );
		cell = (CellHandle) row.getCells( ).get( 0 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 1 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) row.getCells( ).get( 2 );
		assertEquals( 2, cell.getRowSpan( ) );
		assertEquals( 0, cell.getContent( ).getCount( ) );

		cell = (CellHandle) row.getCells( ).get( 3 );
		assertEquals( 1, cell.getRowSpan( ) );
		assertEquals( 1, cell.getContent( ).getCount( ) );
	}
	
	
	public void testShiftSuccessfullyWithSpan( ) throws Exception
	{
		openDesign( shiftFileName1 );

		TableHandle table = (TableHandle) designHandle.findElement( "Table1" ); //$NON-NLS-1$
		assertNotNull( table );

		table.shiftColumn( 3, 4 );

		save( );
		assertTrue( compareFile( "n1_golden.xml" ) ); //$NON-NLS-1$

		openDesign( shiftFileName1 );
		
		table = (TableHandle) designHandle.findElement( "Table1" ); //$NON-NLS-1$
		assertNotNull( table );
		
		table.shiftColumn( 3, 2 );
		
		save( );
		assertTrue( compareFile( "n1_golden2.rptdesign" ) ); //$NON-NLS-1$
		
		openDesign( shiftWithColumnSpan );
		
		table = (TableHandle) designHandle.findElement( "Table1" ); //$NON-NLS-1$
		assertNotNull( table );
		
		table.shiftColumn( 2, 3 );
		
		save( );
		assertTrue( compareFile( "rptWithColSpan_golden.rptdesign" ) ); //$NON-NLS-1$
	}
	
	public void testShiftlast( ) throws Exception
	{
	}


	/**
	 * Tests the algorithm to shift one column to another in the same table.
	 * 
	 * @throws Exception
	 */

	public void testShiftSuccessfully( ) throws Exception
	{
		openDesign( shiftFileName );

		TableHandle table = (TableHandle) designHandle.findElement( "Table1" ); //$NON-NLS-1$
		assertNotNull( table );

		table.shiftColumn( 2, 3 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_1.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.shiftColumn( 2, 4 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_2.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		assertTrue( table.canShiftColumn( 2, 2 ) );

		table.shiftColumn( 2, 2 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_3.xml" ) ); //$NON-NLS-1$

		table.shiftColumn( 4, 1 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_4.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.shiftColumn( 4, 2 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_5.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.shiftColumn( 3, 2 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_6.xml" ) ); //$NON-NLS-1$

		table.shiftColumn( 3, 0 );

		save( );
		assertTrue( compareFile( "TableShiftColumnBand_golden_7.xml" ) ); //$NON-NLS-1$
	}

	/**
	 * Tests the algorithm to shift one column to another in the same table.
	 * 
	 * @throws Exception
	 */

	public void testShiftFailed( ) throws Exception
	{
		openDesign( fileName );

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table3" ); //$NON-NLS-1$
		assertNotNull( table );

		assertFalse( table.canShiftColumn( 2, 0 ) );
		try
		{
			table.shiftColumn( 2, 0, false );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN,
					e.getErrorCode( ) );
		}

		assertFalse( table.canShiftColumn( 1, 2 ) );
		try
		{
			table.shiftColumn( 1, 2, false );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN,
					e.getErrorCode( ) );
		}

		table = (TableHandle) designHandle.findElement( "My table4" ); //$NON-NLS-1$
		assertNotNull( table );

		assertFalse( table.canShiftColumn( 1, 2 ) );
		try
		{
			table.shiftColumn( 1, 2, false );
			fail( );
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_COLUMN_COPY_FORBIDDEN,
					e.getErrorCode( ) );
		}

	}

	/**
	 * Tests canCopy() methods on cloned tables.
	 * 
	 * <ul>
	 * <li>uses copy methods on DesignElementHandle to test.
	 * <li>uses IDesignElement clone to test.
	 * </ul>
	 * 
	 * @throws SemanticException
	 *             if error occurs during copy the column.
	 * @throws CloneNotSupportedException
	 *             not happen
	 * 
	 */

	public void testOperationsOnCopiedTable( ) throws SemanticException,
			CloneNotSupportedException
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"table1", 3 ); //$NON-NLS-1$
		designHandle.getBody( ).add( table );

		TableHandle table1 = (TableHandle) table.copy( ).getHandle(
				designHandle.getModule( ) );
		table1.setName( "table2" ); //$NON-NLS-1$
		assertNotNull( table1.getLayoutModel( ) );
		designHandle.getBody( ).add( table1 );

		assertTrue( table1.canCopyColumn( 1 ) );
		assertTrue( table1.canCopyColumn( 2 ) );

		assertTrue( table1.canPasteColumn( table1.copyColumn( 1 ), 1, false ) );

		TableItem table2 = (TableItem) table.getElement( ).clone( );
		assertNotNull( table2.getLayoutModel( designHandle.getModule( ) ) );

		assertTrue( ( (TableHandle) table2
				.getHandle( designHandle.getModule( ) ) ).canCopyColumn( 1 ) );

		assertTrue( ( (TableHandle) table2
				.getHandle( designHandle.getModule( ) ) ).canCopyColumn( 2 ) );
	}

	/**
	 * Tests paste column with complex cases.
	 * 
	 * @throws Exception
	 *             if error occurs during copy the column.
	 */

	public void testPasteColumn( ) throws Exception
	{
		openDesign( "TableColumnBandTest_1.xml" ); //$NON-NLS-1$

		// test copy and paste

		TableHandle table = (TableHandle) designHandle.findElement( "test1" ); //$NON-NLS-1$

		assertEquals( 4, table.getColumnCount( ) );
		RowHandle row = (RowHandle) table.getDetail( ).get( 1 );
		CellHandle cell = (CellHandle) row.getCells( ).get( 0 );
		assertEquals( 0, cell.getContent( ).getCount( ) );

		cell = (CellHandle) row.getCells( ).get( 1 );
		String expected = ( (LabelHandle) cell.getContent( ).getContents( )
				.get( 0 ) ).getText( );

		ColumnBandData data = table.copyColumn( 2 );
		table.pasteColumn( data, 3, true );

		assertEquals( 4, table.getColumnCount( ) );
		cell = (CellHandle) row.getCells( ).get( 2 );
		assertEquals( 1, cell.getContent( ).getCount( ) );
		String result = ( (LabelHandle) cell.getContent( ).getContents( ).get(
				0 ) ).getText( );
		assertEquals( expected, result );

		// test copy and insert

		table = (TableHandle) designHandle.findElement( "test2" ); //$NON-NLS-1$

		assertEquals( 4, table.getColumnCount( ) );
		row = (RowHandle) table.getDetail( ).get( 1 );
		cell = (CellHandle) row.getCells( ).get( 0 );
		expected = ( (LabelHandle) cell.getContent( ).getContents( ).get( 0 ) )
				.getText( );

		cell = (CellHandle) row.getCells( ).get( 2 );
		assertEquals( 0, cell.getContent( ).getCount( ) );

		data = table.copyColumn( 1 );
		table.insertAndPasteColumn( data, 3 );

		assertEquals( 5, table.getColumnCount( ) );
		cell = (CellHandle) row.getCells( ).get( 2 );
		result = ( (LabelHandle) cell.getContent( ).getContents( ).get( 0 ) )
				.getText( );
		assertEquals( expected, result );
	}

	/**
	 * Tests inserting column with complex cases.
	 * 
	 * @throws Exception
	 *             if error occurs during copy the column.
	 */

	public void testInsertColumn( ) throws Exception
	{
		openDesign( "TableInsertColumnBandTest.xml" ); //$NON-NLS-1$

		long maxId = design.getNextID( );

		// test insert

		TableHandle table = (TableHandle) designHandle.findElement( "test1" ); //$NON-NLS-1$
		table.insertColumn( 1, -1 );

		SlotHandle detail = table.getDetail( );
		assertEquals( 5, ( (RowHandle) detail.get( 0 ) ).getCells( ).getCount( ) );
		assertEquals( 5, ( (RowHandle) detail.get( 1 ) ).getCells( ).getCount( ) );

		CellHandle cell = (CellHandle) ( (RowHandle) detail.get( 0 ) )
				.getCells( ).get( 1 );
		assertEquals( 2, cell.getRowSpan( ) );
		assertEquals( 11, cell.getID( ) );

		cell = (CellHandle) ( (RowHandle) detail.get( 1 ) ).getCells( ).get( 0 );
		assertEquals( 1, cell.getRowSpan( ) );
		assertEquals( 1, cell.getColumnSpan( ) );

		designHandle.getCommandStack( ).undo( );
		table.insertColumn( 1, 1 );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 0 );
		assertEquals( 2, cell.getRowSpan( ) );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 1 );
		assertEquals( 0, cell.getContent( ).getCount( ) );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 2 );
		assertEquals( 1, cell.getContent( ).getCount( ) );

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 2, 1 );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 1 );
		assertEquals( 1, cell.getContent( ).getCount( ) );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 2 );
		assertEquals( 1, cell.getRowSpan( ) );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 3 );
		assertEquals( 2, cell.getRowSpan( ) );

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 4, 1 );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 3 );
		assertTrue( cell.getID( ) == 14 );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 4 );
		assertTrue( cell.getID( ) > maxId );

		cell = (CellHandle) ( (RowHandle) detail.get( 1 ) ).getCells( ).get( 4 );
		assertTrue( cell.getID( ) > maxId );

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 4, -1 );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 3 );
		assertTrue( cell.getID( ) > maxId );

		cell = (CellHandle) ( (RowHandle) detail.get( 0 ) ).getCells( ).get( 4 );
		assertTrue( cell.getID( ) == 14 );

		cell = (CellHandle) ( (RowHandle) detail.get( 1 ) ).getCells( ).get( 3 );
		assertEquals( 18, cell.getID( ) );

		// test cases for failure insertion becuase of column span > 1

		table = (TableHandle) designHandle.findElement( "test2" ); //$NON-NLS-1$

		table.insertColumn( 1, 1 );

		save( );
		assertTrue( compareFile( "TableInsertColumnBandTest_golden.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 2, -1 );

		save( );
		assertTrue( compareFile( "TableInsertColumnBandTest_golden_1.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 2, 1 );

		save( );
		assertTrue( compareFile( "TableInsertColumnBandTest_golden_2.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 3, -1 );

		save( );
		assertTrue( compareFile( "TableInsertColumnBandTest_golden_3.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		table.insertColumn( 3, 1 );

		save( );
		assertTrue( compareFile( "TableInsertColumnBandTest_golden_4.xml" ) ); //$NON-NLS-1$

		openDesign( "TableInsertColumnBandTest_1.xml" ); //$NON-NLS-1$
		table = (TableHandle) designHandle.findElement( "Table" ); //$NON-NLS-1$

		table.insertColumn( 1, 1 );
		save( );
		
		assertTrue( compareFile( "TableInsertColumnBandTest_rowSpan_golden_1.xml" ) ); //$NON-NLS-1$
		
		openDesign( "TableInsertColumnBandTest_2.xml" ); //$NON-NLS-1$
		table = (TableHandle) designHandle.findElement( "Table" ); //$NON-NLS-1$

		table.insertColumn( 2, 1 );
		save( );
		assertTrue( compareFile( "TableInsertColumnBandTest_colSpan_golden_1.xml" ) ); //$NON-NLS-1$

	}

	/**
	 * Tests to reorder table columns.
	 * 
	 * @param tableHandle
	 * @param indexMappings
	 * @return <code>true</code> if the action performs successfully.
	 * @throws SemanticException
	 */

	private boolean reorderColumns( TableHandle tableHandle, int[] indexMappings )
			throws SemanticException
	{
		if ( indexMappings == null )
			return false;

		int columnCount = tableHandle.getColumnCount( );
		if ( indexMappings.length != columnCount )
			return false;

		// check the integrality of the input parameter. Must be from 0 to
		// columnCount-1. And no duplicates.

		int[] checkedArray = new int[indexMappings.length];
		System.arraycopy( indexMappings, 0, checkedArray, 0,
				indexMappings.length );
		Arrays.sort( checkedArray );
		for ( int i = 0; i < checkedArray.length; i++ )
		{
			if ( checkedArray[i] != i )
				return false;
		}

		// the list to track changes for table columns.

		List cachedIndices = new ArrayList( );
		for ( int i = 1; i <= columnCount; i++ )
		{
			cachedIndices.add( new Integer( i ) );
		}

		try
		{
			designHandle.getCommandStack( ).startTrans( "reorder columns" ); //$NON-NLS-1$
			for ( int i = 0; i < columnCount; i++ )
			{
				Integer targetColumn = new Integer( indexMappings[i] + 1 );
				int currentIndex = cachedIndices.indexOf( targetColumn );
				assert currentIndex != -1;

				if ( currentIndex == i || currentIndex == i + 1 )
					continue;

				// adjust the position. since source position affect the target
				// index.

				int target = i + 1;
				if ( currentIndex + 1 > i + 1 )
					target = i;

				tableHandle.shiftColumn( currentIndex + 1, target );

				if ( currentIndex < i )
				{
					cachedIndices.add( i + 1, targetColumn );
					cachedIndices.remove( currentIndex );
				}
				else
				{
					cachedIndices.remove( currentIndex );
					cachedIndices.add( i + 1 - 1, targetColumn );
				}
			}
		}
		catch ( SemanticException e )
		{
			designHandle.getCommandStack( ).rollback( );
			throw e;
		}

		designHandle.getCommandStack( ).commit( );

		return true;
	}

	/**
	 * Test the function to reorder columns in the table.
	 * 
	 * @throws Exception
	 */

	public void testReorderColumns( ) throws Exception
	{

		openDesign( shiftFileName );

		TableHandle table = (TableHandle) designHandle.findElement( "Table1" ); //$NON-NLS-1$
		assertNotNull( table );

		reorderColumns( table, new int[]{0, 2, 1, 3} );
		save( );
		assertTrue( compareFile( "TableReorderColumnBand_golden_1.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		reorderColumns( table, new int[]{3, 2, 1, 0} );
		save( );
		assertTrue( compareFile( "TableReorderColumnBand_golden_2.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		reorderColumns( table, new int[]{1, 0, 2, 3} );
		save( );
		assertTrue( compareFile( "TableReorderColumnBand_golden_3.xml" ) ); //$NON-NLS-1$

		designHandle.getCommandStack( ).undo( );

		reorderColumns( table, new int[]{0, 1, 2, 3} );
		save( );
		assertTrue( compareFile( "TableReorderColumnBand_golden_4.xml" ) ); //$NON-NLS-1$

		reorderColumns( table, new int[]{3, 1, 2, 0} );
		save( );
		assertTrue( compareFile( "TableReorderColumnBand_golden_5.xml" ) ); //$NON-NLS-1$

	}

}
