/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.GridHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;

/**
 * Adapter class to adapt model handle. This adapter provides convenience.
 * methods to GUI requirement GridHandleAdapter responds to model GridHandle
 *  
 */

public class GridHandleAdapter extends TableHandleAdapter
{

	/**
	 * Constructor
	 * @param table
	 * @param mark
	 */
	public GridHandleAdapter( GridHandle grid, IModelAdapterHelper mark )
	{
		super( grid, mark );
	}

	/**
	 * Gets the Children iterator. This children relationship is determined by
	 * GUI requirement. This is not the model children relationship.
	 * 
	 * @return Children iterator
	 */
	public List getChildren( )
	{
		List children = new ArrayList( );

		SlotHandle rows = getGridHandle( ).getRows( );

		for ( Iterator it = rows.iterator( ); it.hasNext( ); )
		{
			insertIteratorToList( ( (RowHandle) it.next( ) ).getCells( )
					.iterator( ), children );
		}
		removePhantomCells( children );
		return children;
	}

	/**
	 * Gets the all columns list
	 * 
	 * @return
	 */
	public List getColumns( )
	{
		List list = new ArrayList( );
		insertIteratorToList( getGridHandle( ).getColumns( ).iterator( ), list );
		return list;
	}

	private GridHandle getGridHandle( )
	{
		return (GridHandle) getHandle( );
	}

	/**
	 * Gets all rows list.
	 * 
	 * @return The rows list.
	 */
	public List initRowsInfo( )
	{
		clearBuffer( );
		// shoudl return all cells
		SlotHandle gridRows = getGridHandle( ).getRows( );

		insertIteratorToList( gridRows.iterator( ),
				rows,
				TableHandleAdapter.RowUIInfomation.GRID_ROW,
				TableHandleAdapter.RowUIInfomation.GRID_ROW );
		caleRowInfo( rows );
		return rows;

	}

	/**
	 * return false for Grid Item for grid doesn't have slot
	 */
	public boolean hasSlotHandleRow( int id )
	{
		return false;
	}

	/**
	 * Provides delete row function.
	 */
	public void deleteRow( int[] rows ) throws SemanticException
	{
		if ( getRowCount( ) == 1 )
		{
			getGridHandle( ).drop( );
			return;
		}
		super.deleteRow( rows );
	}

	/**
	 * Provides delete row function.
	 */
	public void deleteColumn( int[] columns ) throws SemanticException
	{
		if ( getColumnCount( ) == 1 )
		{
			getGridHandle( ).drop( );
			return;
		}
		super.deleteColumn( columns );
	}
}