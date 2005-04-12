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

package org.eclipse.birt.report.designer.core.model.schematic;

import java.util.Iterator;

import org.eclipse.birt.report.designer.core.model.DesignElementHandleAdapter;
import org.eclipse.birt.report.designer.core.model.IModelAdapterHelper;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.TableGroupHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.birt.report.model.api.command.NameException;

/**
 * Adapter class to extend TableHandleAdapter. Deals with the table group in the
 * table.
 *  
 */

public class TableGroupHandleAdapter extends DesignElementHandleAdapter
{

	private static final String TRANS_LABEL_NOT_INCLUDE = Messages.getString( "TableHandleAdapter.transLabel.notInclude" ); //$NON-NLS-1$
	private static final String TRANS_LABEL_INCLUDE = Messages.getString( "TableHandleAdapter.transLabel.include" ); //$NON-NLS-1$

	/**
	 * @param table
	 * @param mark
	 */
	public TableGroupHandleAdapter( TableGroupHandle table,
			IModelAdapterHelper mark )
	{
		super( table, mark );
	}

	protected TableGroupHandle getTableGroupHandle( )
	{
		return (TableGroupHandle) getHandle( );
	}

	/**
	 * Check if the slot handle contains specified id.
	 * 
	 * @param slotId
	 */
	public boolean hasSlotHandleRow( int slotId )
	{
		SlotHandle slot = getTableGroupHandle( ).getSlot( slotId );
		return slot.getCount( ) > 0;
	}

	public void insertRowInSlotHandle( int slotId ) throws ContentException,
			NameException
	{
		transStar( TRANS_LABEL_INCLUDE
				+ TableHandleAdapter.getOperationName( slotId ) );
		RowHandle rowHandle = getTableGroupHandle( ).getElementFactory( )
				.newTableRow( );
		getTableHandleAdapter( ).addCell( rowHandle );
		getTableGroupHandle( ).getSlot( slotId ).add( rowHandle );
		transEnd( );
	}

	public void deleteRowInSlotHandle( int slotId ) throws SemanticException
	{
		transStar( TRANS_LABEL_NOT_INCLUDE
				+ TableHandleAdapter.getOperationName( slotId ) );
		deleteRows( getTableGroupHandle( ).getSlot( slotId ) );
		transEnd( );
	}

	private void deleteRows( SlotHandle rowSlot ) throws SemanticException
	{
		int[] rows = new int[0];
		Iterator itor = rowSlot.iterator( );
		while ( itor.hasNext( ) )
		{
			Object obj = itor.next( );
			RowHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getRowHandleAdapter( obj );
			int lenegth = rows.length;
			int[] temp = new int[lenegth + 1];

			System.arraycopy( rows, 0, temp, 0, lenegth );
			temp[lenegth] = adapt.getRowNumber( );
			rows = temp;
		}
		getTableHandleAdapter( ).deleteRow( rows );
	}

	protected TableHandleAdapter getTableHandleAdapter( )
	{
		return HandleAdapterFactory.getInstance( )
				.getTableHandleAdapter( getTableGroupHandle( ).getContainer( ) );
	}
}