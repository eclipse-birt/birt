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

package org.eclipse.birt.report.model.library;

import org.eclipse.birt.report.model.api.ColumnBandData;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.RowOperationParameters;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.elements.SemanticError;
import org.eclipse.birt.report.model.elements.TableRow;
import org.eclipse.birt.report.model.util.BaseTestCase;

/**
 * The test case of the method 'canDoAction' of table row/column.
 * 
 * <p>
 * <table border="1" cellpadding="2" cellspacing="2" style="border-collapse:
 * collapse" bordercolor="#111111">
 * <th width="20%">Method</th>
 * <th width="40%">Test Case</th>
 * <th width="40%">Expected</th>
 * 
 * <tr>
 * <td>update table row/column operation.</td>
 * <td>Test canDo method which table has parent.</td>
 * <td>canDo method return false.</td>
 * </tr>
 * 
 * 
 * </table>
 * 
 */

public class LibraryWithTableTest extends BaseTestCase
{

	private String fileCanUpdate = "TableItemRowUpdateTest.xml";//$NON-NLS-1$

	/*
	 * @see TestCase#setUp()
	 */

	protected void setUp( ) throws Exception
	{
		super.setUp( );
	}

	/**
	 * Test update table row or table column.
	 * 
	 * @throws Exception
	 */
	public void testRowAndColumnUpdateAction( ) throws Exception
	{
		openDesign( fileCanUpdate );

		TableHandle tableHandle = (TableHandle) designHandle
				.findElement( "NewTable" ); //$NON-NLS-1$
		ColumnBandData data = tableHandle.copyColumn( 1 );
		assertFalse( tableHandle.canPasteColumn( data, 2, true ) );

		try
		{
			tableHandle.pasteColumn( data, 2, true );
			fail( "forbidden do action on column " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN, e
							.getErrorCode( ) );
		}
		assertFalse( tableHandle.canInsertAndPasteColumn( data, 1 ) );
		
		try
		{
			tableHandle.insertAndPasteColumn( data, 1 );
			fail( "forbidden do action on column " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN, e
							.getErrorCode( ) );
		}
		assertFalse( tableHandle.canShiftColumn( 1, 2 ) );
		try
		{
			tableHandle.shiftColumn( 1 , 2  );
			fail( "forbidden do action on column " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_COLUMN_PASTE_FORBIDDEN, e
							.getErrorCode( ) );
		}
		SlotHandle slotHandle = tableHandle.getSlot( 0 );
		RowHandle rowHandle = (RowHandle) slotHandle.get( 0 );

		TableRow row = (TableRow) rowHandle.copy( );
		RowOperationParameters parameters = new RowOperationParameters( 0, -1,
				0 );
		assertFalse( tableHandle.canPasteRow( row, parameters ) );
		try
		{
			tableHandle.pasteRow( row , parameters );
			fail( "forbidden do action on row " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_ROW_PASTE_FORBIDDEN, e
							.getErrorCode( ) );
		}
		assertFalse( tableHandle.canInsertRow( parameters ) );
		try
		{
			tableHandle.insertRow( parameters );
			fail( "forbidden do action on row " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_ROW_INSERT_FORBIDDEN, e
							.getErrorCode( ) );
		}
		assertFalse( tableHandle.canInsertAndPasteRow( row, parameters ) );
		try
		{
			tableHandle.insertAndPasteRow( row , parameters );
			fail( "forbidden do action on row " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_ROW_INSERTANDPASTE_FORBIDDEN, e
							.getErrorCode( ) );
		}
		parameters.setDestIndex( 2 );
		parameters.setSourceIndex( 0 );
		assertFalse( tableHandle.canShiftRow( parameters ) );
		try
		{
			tableHandle.shiftRow( parameters );
			fail( "forbidden do action on row " ); //$NON-NLS-1$
		}
		catch ( SemanticException e )
		{
			assertEquals(
					SemanticError.DESIGN_EXCEPTION_ROW_SHIFT_FORBIDDEN, e
							.getErrorCode( ) );
		}
	}

}
