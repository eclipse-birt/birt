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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.CellHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Splits cell action
 *  
 */
public class SplitAction extends SelectionAction
{

	private static final String ACTION_MSG_SPLIT_CELLS = Messages.getString( "SplitAction.actionMsg.splitCells" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.SplitAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance
	 * 
	 * @param part
	 *            current work bench part
	 */
	public SplitAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_SPLIT_CELLS );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return false;
		List list = getSelectedObjects( );
		int size = list.size( );
		if ( size != 1 )
		{
			return false;
		}
		Object obj = list.get( 0 );
		if ( obj instanceof TableCellEditPart )
		{
			CellHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getCellHandleAdapter( ( (TableCellEditPart) obj ).getModel( ) );
			if ( adapt.getRowSpan( ) != 1 || adapt.getColumnSpan( ) != 1 )
			{
				return true;
			}
		}
		return false;
	}

	/**
	 * Runs action.
	 *  
	 */
	public void run( )
	{
		getTableEditPart( ).splitCell( getCellEditPart( ) );
	}

	/**
	 * Gets current table edit part.
	 * 
	 * @return the current table edit part
	 */
	private TableEditPart getTableEditPart( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		List list = getSelectedObjects( );
		int size = list.size( );
		TableEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = getSelectedObjects( ).get( i );

			if ( obj instanceof TableEditPart )
			{
				part = (TableEditPart) obj;
			}
			else if ( obj instanceof TableCellEditPart )
			{
				part = (TableEditPart) ( (TableCellEditPart) obj ).getParent( );
			}
		}
		return part;
	}

	/**
	 * Gets cell edit part.
	 * 
	 * @return current table cell edit part
	 */
	private TableCellEditPart getCellEditPart( )
	{
		return (TableCellEditPart) getSelectedObjects( ).get( 0 );
	}
}