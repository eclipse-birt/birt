/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.model.api;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.api.metadata.DimensionValue;
import org.eclipse.birt.report.model.elements.Cell;
import org.eclipse.birt.report.model.elements.DataItem;
import org.eclipse.birt.report.model.elements.TableItem;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.elements.interfaces.IListingElementModel;
import org.eclipse.birt.report.model.elements.interfaces.IStyleModel;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ColorPropertyType;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of the method <code>getColumnCount</code> in
 * <code>TableItemHandle</code>.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse: * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>{@link #testGetColumnCount()}</td>
 * <td>Gets the maximal column count of tables with dropping headers.</td>
 * <td>Results match with the expected.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testInsertGroup()}</td>
 * <td>Insert a group to a newly created Table.</td>
 * <td>No exception is thrown.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testGetFilters()}</td>
 * <td>Returns filters defined on the given column.</td>
 * <td>Results match with the expected.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testRowCopyPasteAction()}</td>
 * <td>Copy,paste,insert,shift table row.</td>
 * <td>Results match with the expected.</td>
 * </tr>
 * 
 * <tr>
 * <td>{@link #testTableWidthCalculation()}</td>
 * <td>Calculates multiple tables' width</td>
 * <td>Results match with the expected.</td>
 * </tr>
 * 
 * </table>
 * 
 * @see TableItem
 */

public class TableItemHandleTest extends BaseTestCase
{

	private static final String fileName = "TableItemHandleTest.xml"; //$NON-NLS-1$

	private static final String fileCopyName = "TableItemHandleTest_1.xml";//$NON-NLS-1$

	private static final String tableWidthTestFileName = "TableItemHandleTest_2.xml";//$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Test copy , paste , insert , shift table row.
	 * 
	 * @throws Exception
	 */
	public void testRowCopyPasteAction( ) throws Exception
	{
		openDesign( fileCopyName );

		TableHandle table1 = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		TableHandle table2 = (TableHandle) designHandle
				.findElement( "My table2" );//$NON-NLS-1$
		TableHandle table3 = (TableHandle) designHandle
				.findElement( "My table3" );//$NON-NLS-1$

		assertNotNull( table1 );
		assertEquals( 13, table1.getColumnCount( ) );

		// get first row in group header of group 0 .

		RowOperationParameters parameters1 = new RowOperationParameters( 0, 0,
				0 );

		// get second row in group header of group 0.

		RowOperationParameters parameters2 = new RowOperationParameters( 0, 0,
				1 );

		// get second row in table footer.

		RowOperationParameters parameters3 = new RowOperationParameters( 2, -1,
				1 );

		// get first row in table header.

		RowOperationParameters parameters4 = new RowOperationParameters( 0, -1,
				0 );

		// get second row in table detail which contain row span.

		RowOperationParameters parameters5 = new RowOperationParameters( 2, -1,
				1 );

		// get first row in group header of group 1.

		RowOperationParameters parameters6 = new RowOperationParameters( 0, 1,
				0 );

		// check canCopy

		assertTrue( table1.canCopyRow( parameters1 ) );

		// can't find such table row

		assertFalse( table1.canCopyRow( parameters2 ) );

		// contains rowspan , not a rectangle.

		assertFalse( table1.canCopyRow( parameters5 ) );

		// copy row in header of group 0.

		IDesignElement clonedData = table1.copyRow( parameters1 );
		TableRow clonedRow = (TableRow) clonedData.getHandle( design )
				.getElement( );

		Cell cell = (Cell) clonedRow.getContentsSlot( ).get( 0 );
		Object obj = cell.getSlot( 0 ).getContents( ).get( 0 );
		assertTrue( obj instanceof DataItem );
		assertEquals(
				"PHONE", ( (DataItem) obj ).getLocalProperty( null, "resultSetColumn" ) ); //$NON-NLS-1$//$NON-NLS-2$
		assertNull( clonedRow.getContainer( ) );

		// copy forbidden table row.

		try
		{
			table1.copyRow( parameters5 );
			fail( "fail to copy Row " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_ROW_COPY_FORBIDDEN, e
					.getErrorCode( ) );
		}

		// test canPaste method.

		assertTrue( table1.canPasteRow( clonedData, parameters1 ) );
		assertTrue( table1.canPasteRow( clonedData, parameters6 ) );

		// the different column count.

		assertFalse( table3.canPasteRow( clonedData, parameters4 ) );

		// slot id is out of range.

		assertFalse( table1.canPasteRow( clonedData, parameters3 ) );

		table1.pasteRow( clonedData, parameters6 );
		save( );
		assertTrue( compareFile( "TableRowCopy_golden_1.xml" ) ); //$NON-NLS-1$

		// paste a null table row

		try
		{
			table1.pasteRow( null, parameters6 );
			fail( "fail to paste Row because copied row is null" ); //$NON-NLS-1$
		}
		catch ( IllegalArgumentException e )
		{
			assertEquals( "empty row to paste.", e.getMessage( ) ); //$NON-NLS-1$
		}

		RowOperationParameters parameters7 = new RowOperationParameters( 0, 1,
				-1 );
		RowOperationParameters parameters9 = new RowOperationParameters( 2, -1,
				2 );

		// Test canInsertRow method.

		assertTrue( table2.canInsertRow( parameters4 ) );

		// row id is out of range.

		assertFalse( table1.canInsertRow( parameters7 ) );

		// no group in table2 , but should insert into group element.

		assertFalse( table2.canInsertRow( parameters1 ) );

		// there is row span in the table row.
		parameters5.setDestIndex( 2 );
		assertFalse( table1.canInsertRow( parameters5 ) );

		// insert out of group range.

		try
		{
			table2.insertRow( parameters1 );
			fail( "table2 insert error " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_ROW_INSERT_FORBIDDEN,
					e.getErrorCode( ) );
		}

		table2.insertRow( parameters4 );
		save( );
		assertTrue( compareFile( "TableRowCopy_golden_2.xml" ) ); //$NON-NLS-1$

		// can insert the same element twice.

		table2.insertRow( parameters4 );
		save( );
		assertTrue( compareFile( "TableRowCopy_golden_3.xml" ) ); //$NON-NLS-1$

		// source row is the same as destination row.

		clonedRow = (TableRow) clonedRow.clone( );
		parameters1.setSourceIndex( 0 );
		assertFalse( table1.canShiftRow( parameters1 ) );

		// only one row , but should shift to position 2.

		parameters9.setSourceIndex( 0 );
		assertFalse( table1.canShiftRow( parameters9 ) );

		parameters5.setSourceIndex( 0 );
		assertFalse( table1.canShiftRow( parameters5 ) );

		parameters4.setSourceIndex( 2 );
		assertTrue( table2.canShiftRow( parameters4 ) );

		try
		{
			table2.shiftRow( parameters9 );
			fail( "table2 shift error " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals( SemanticError.DESIGN_EXCEPTION_ROW_SHIFT_FORBIDDEN, e
					.getErrorCode( ) );
		}

		table2.shiftRow( parameters4 );
		save( );
		assertTrue( compareFile( "TableRowCopy_golden_4.xml" ) ); //$NON-NLS-1$

		clonedData = (IDesignElement) clonedData.clone( );

		// Test canInsertAndPaste method.

		assertTrue( table2.canInsertAndPasteRow( clonedData, parameters4 ) );

		// no group in table2 , but should insert into group element.

		assertFalse( table2.canInsertAndPasteRow( clonedData, parameters1 ) );

		// there is row span in table row.
		assertFalse( table1.canInsertAndPasteRow( clonedData, parameters5 ) );

		// insert forbidden table row.

		try
		{
			table2.insertAndPasteRow( clonedData, parameters1 );
			fail( "table2 inert and paste error " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_ROW_INSERTANDPASTE_FORBIDDEN,
					e.getErrorCode( ) );
		}

		table2.insertAndPasteRow( clonedData, parameters4 );
		save( );
		assertTrue( compareFile( "TableRowCopy_golden_5.xml" ) );//$NON-NLS-1$

	}

	/**
	 * Test copy a row with a nested table to the table header, exception should
	 * be reported.
	 * 
	 * @throws Exception
	 */
	public void testRowCopyPasteInNestedTable( ) throws Exception
	{
		openDesign( "TableItemHandleTest_nested.xml" ); //$NON-NLS-1$

		TableHandle table3 = (TableHandle) designHandle
				.findElement( "My table3" );//$NON-NLS-1$

		RowOperationParameters parameters1 = new RowOperationParameters(
				TableItem.HEADER_SLOT, -1, 0 );
		RowOperationParameters parameters2 = new RowOperationParameters(
				TableItem.DETAIL_SLOT, -1, 0 );

		assertTrue( table3.canCopyRow( parameters2 ) );

		IDesignElement clonedData = table3.copyRow( parameters2 );
		TableRow clonedRow = (TableRow) clonedData.getHandle( design )
				.getElement( );

		assertNotNull( clonedRow );
		assertNull( clonedRow.getContainer( ) );

		try
		{
			table3.pasteRow( clonedData, parameters1 );
			fail( "Paste Row error in header slot" ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					MessageConstants.CONTENT_EXCEPTION_INVALID_CONTEXT_CONTAINMENT,
					e.getErrorCode( ) );
		}
	}

	/**
	 * Test parser and properties of table element.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testGetColumnCount( ) throws Exception
	{
		openDesign( fileName );

		TableHandle table = (TableHandle) designHandle
				.findElement( "My table1" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table2" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table3" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table4" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table5" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table7" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		// one with semantic error.

		table = (TableHandle) designHandle.findElement( "My table8" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 5, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table9" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table10" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table11" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table12" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table13" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table14" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table15" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table16" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table17" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table18" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 1, table.getColumnCount( ) );

		// one with semantic error.

		table = (TableHandle) designHandle.findElement( "My table19" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 1, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table20" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table21" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 6, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table22" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 4, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table23" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table24" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table25" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table26" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table27" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 2, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table28" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 3, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table29" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 20, table.getColumnCount( ) );

		table = (TableHandle) designHandle.findElement( "My table30" ); //$NON-NLS-1$
		assertNotNull( table );
		assertEquals( 20, table.getColumnCount( ) );

		assertEquals( 0, designHandle.getErrorList( ).size( ) );
	}

	/**
	 * Inserts a new group into the table. This is one smoke test. If no
	 * exception is thrown, this operation is OK.
	 * 
	 * @throws Exception
	 * 
	 */

	public void testInsertGroup( ) throws Exception
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"testTable", 3, 1, 1, 1 ); //$NON-NLS-1$

		// normal transaction without filtering events.

		try
		{
			designHandle.getCommandStack( ).startTrans( null );

			TableGroupHandle group = newTableGroup( 1, 1, 3 );

			// the layout has been updated.

			table.getGroups( ).add( group );

			// set the color of the first column as blue

			table.getColumns( ).get( 0 ).setProperty( IStyleModel.COLOR_PROP,
					ColorPropertyType.BLUE );

			CellHandle cell = (CellHandle) ( (RowHandle) group.getHeader( )
					.get( 0 ) ).getCells( ).get( 0 );
			assertEquals( ColorPropertyType.BLUE, cell
					.getProperty( IStyleModel.COLOR_PROP ) );

			designHandle.getCommandStack( ).commit( );
		}
		catch ( Exception e )
		{
			assert false;
		}

	}

	/**
	 * Returns a newly created the table group with the given header row number,
	 * footer row number and the column number.
	 * 
	 * @param headerRowNum
	 *            header row number
	 * @param footerRowNum
	 *            footer row number
	 * @param columnNum
	 *            the column number
	 * @return a <code>TableGroupHandle</code>
	 * @throws SemanticException
	 */

	private TableGroupHandle newTableGroup( int headerRowNum, int footerRowNum,
			int columnNum ) throws SemanticException
	{
		assert headerRowNum > 0 && footerRowNum > 0 && columnNum > 0;
		TableGroupHandle group = designHandle.getElementFactory( )
				.newTableGroup( );

		for ( int i = 0; i < headerRowNum; i++ )
			group.getHeader( ).add(
					designHandle.getElementFactory( ).newTableRow( columnNum ) );

		for ( int i = 0; i < footerRowNum; i++ )
			group.getFooter( ).add(
					designHandle.getElementFactory( ).newTableRow( columnNum ) );

		return group;
	}

	/**
	 * Test cases:
	 * 
	 * test set page breaks (pageBreakAfter/pageBreakBefore) on row is not
	 * allowed now.
	 * 
	 * @throws SemanticException
	 * 
	 * @throws Exception
	 * 
	 */

	public void testSetPageBreakOnRow( ) throws SemanticException
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"testTable", 3, 1, 1, 1 ); //$NON-NLS-1$

		RowHandle tableRow = (RowHandle) table.getHeader( ).get( 0 );
		assertNotNull( tableRow.getDefn( ).getProperty(
				IStyleModel.PAGE_BREAK_AFTER_PROP ) );
		assertNotNull( tableRow.getDefn( ).getProperty(
				IStyleModel.PAGE_BREAK_BEFORE_PROP ) );
		assertNotNull( tableRow.getDefn( ).getProperty(
				IStyleModel.PAGE_BREAK_INSIDE_PROP ) );
	}

	/**
	 * Tests to rename a table group with duplicate names. The NameException
	 * should be thrown.
	 * 
	 * @throws SemanticException
	 */

	public void testRenameGroup( ) throws SemanticException
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"testTable", 3, 1, 1, 1 ); //$NON-NLS-1$

		// normal transaction without filtering events.

		TableGroupHandle group = newTableGroup( 1, 1, 3 );
		group.setName( "group1" ); //$NON-NLS-1$

		// the layout has been updated.

		table.getGroups( ).add( group );

		group = newTableGroup( 1, 1, 3 );
		group.setName( "group2" ); //$NON-NLS-1$

		// the layout has been updated.

		table.getGroups( ).add( group );

		try
		{
			group.setName( "group1" ); //$NON-NLS-1$
			fail( );
		}
		catch ( NameException e )
		{
			assertEquals( NameException.DESIGN_EXCEPTION_DUPLICATE, e
					.getErrorCode( ) );
		}
	}

	/**
	 * Tests to add a detail row into a summary table. The exception is expected
	 * 
	 * @throws NameException
	 * @throws ContentException
	 */
	public void testSummaryTable( ) throws Exception
	{
		createDesign( );

		TableHandle table = designHandle.getElementFactory( ).newTableItem(
				"testTable" ); //$NON-NLS-1$

		table.setIsSummaryTable( true );
		table.getHeader( ).add( table.getElementFactory( ).newTableRow( ) );
		table.getFooter( ).add( table.getElementFactory( ).newTableRow( ) );
		try
		{
			table.getDetail( ).add( table.getElementFactory( ).newTableRow( ) );
			fail( );
		}
		catch ( ContentException e )
		{
			assertEquals(
					ContentException.DESIGN_EXCEPTION_INVALID_CONTEXT_CONTAINMENT,
					e.getErrorCode( ) );
		}
		assertFalse( table.canContain( IListingElementModel.DETAIL_SLOT, table
				.getElementFactory( ).newTableRow( ) ) );
		assertFalse( table.canContain( IListingElementModel.DETAIL_SLOT,
				ReportDesignConstants.ROW_ELEMENT ) );
	}

	/**
	 * Tests for table width calculation.
	 */
	public void testTableWidthCalculation( ) throws Exception
	{
		openDesign( tableWidthTestFileName );

		checkWidthCalculation( "testTable1", "2in" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable2", "2in" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable3", "4cm" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable4", "6px" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable5" ); //$NON-NLS-1$
		checkWidthCalculation( "testTable6", "4in" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkCalculationException(
				"testTable7", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_INCONSISTENT_RELATIVE_UNIT );
		checkCalculationException(
				"testTable8", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_INCONSISTENT_UNIT_TYPE );
		checkCalculationException( "testTable9", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_ILLEGAL_PERCENTAGE );
		checkCalculationException( "testTable10", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_TABLE_COLUMN_WITH_NO_WIDTH );
		checkCalculationException( "testTable11", //$NON-NLS-1$
				SemanticError.DESIGN_EXCEPTION_TABLE_NO_COLUMN_FOUND );
		checkWidthCalculation( "testTable12", "4in" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable13", "4in" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable14", "8in" ); //$NON-NLS-1$ //$NON-NLS-2$
		
		designHandle.setImageDPI( 0 );
		checkWidthCalculation( "testTable12", "2.5in" ); //$NON-NLS-1$ //$NON-NLS-2$		
		checkWidthCalculation( "testTable13", "2.5in" ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable14", "5in" ); //$NON-NLS-1$ //$NON-NLS-2$
		
		checkWidthCalculation( "testTable12", "2in", 150 ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable13", "2in", 150 ); //$NON-NLS-1$ //$NON-NLS-2$
		checkWidthCalculation( "testTable14", "4in", 150 ); //$NON-NLS-1$ //$NON-NLS-2$
		
	}

	/**
	 * Checks the width of the table with given name before and after
	 * calculated. Expects no change after calculated.
	 * 
	 * @param tableName
	 *            the table name
	 */
	private void checkWidthCalculation( String tableName )
			throws SemanticException
	{
		checkWidthCalculation( tableName, null, -1 );
	}

	/**
	 * Checks the width of the table with given name before and after
	 * calculated.
	 * 
	 * @param tableName
	 *            the table name
	 * @param expectedWidth
	 *            the expected width after calculated.
	 * @throws SemanticException
	 */
	private void checkWidthCalculation( String tableName, String expectedWidth )
			throws SemanticException
	{
		checkWidthCalculation( tableName, expectedWidth, -1 );
	}

	/**
	 * Checks the width of the table with given name before and after
	 * calculated.
	 * 
	 * @param tableName
	 *            the table name
	 * @param expectedWidth
	 *            the expected width after calculated.
	 * @param dpi
	 *            the dpi value
	 */
	private void checkWidthCalculation( String tableName, String expectedWidth,
			int dpi ) throws SemanticException
	{
		TableHandle table = (TableHandle) designHandle.findElement( tableName );
		assertNotNull( table );
		if ( expectedWidth == null )
		{
			assertTrue( table.getWidth( ).getValue( ) instanceof DimensionValue );
			expectedWidth = table.getWidth( ).getValue( ).toString( );
		}
		if ( dpi == -1 )
		{
			table.setWidthToFitColumns( );
		}
		else
		{
			table.setWidthToFitColumns( dpi );
		}
		assertEquals( expectedWidth, table.getWidth( ).getValue( ).toString( ) );
	}

	/**
	 * Checks the width of the table with given name before and after
	 * calculated.
	 * 
	 * @param tableName
	 *            the table name
	 * @param isChanged
	 *            the flag indicates if the table's width is expected to be
	 *            changed after calculated.
	 * @param expectedWidth
	 *            the expected width after calculated
	 */
	private void checkCalculationException( String tableName,
			String expectedErrorCode )
	{
		TableHandle table = (TableHandle) designHandle.findElement( tableName );
		try
		{
			table.setWidthToFitColumns( );
			fail( expectedErrorCode + " expected!" ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals( expectedErrorCode, e.getErrorCode( ) );
			System.out.println( e.getLocalizedMessage( ) );
		}
	}
}
