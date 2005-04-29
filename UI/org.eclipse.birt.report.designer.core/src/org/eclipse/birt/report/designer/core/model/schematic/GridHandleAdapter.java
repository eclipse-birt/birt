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
	 * 
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
			children.addAll( ( (RowHandle) it.next( ) ).getCells( )
					.getContents( ) );
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
		return getGridHandle( ).getColumns( ).getContents( );
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
	protected void buildRowInfo( )
	{
		insertRowInfo( getGridHandle( ).getRows( ),
				TableHandleAdapter.RowUIInfomation.GRID_ROW,
				TableHandleAdapter.RowUIInfomation.GRID_ROW );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter#canMerge(java.util.List)
	 */
	public boolean canMerge( List list )
	{
		return list != null && list.size( ) > 1;
	}

	/**
	 * return false for Grid Item for grid doesn't have slot
	 */
	public boolean hasSlotHandleRow( int id )
	{
		return false;
	}

}