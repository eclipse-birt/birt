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

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of deleting a column
 *  
 */
public class DeleteColumnAction extends SelectionAction
{

	private static final String ACTION_MSG_DELETE = Messages.getString( "DeleteColumnAction.actionMsg.delete" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "DeleteGroupAction.DeleteColumnAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 * 
	 * @param part
	 *            current work bench part
	 */
	public DeleteColumnAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_DELETE );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return getColumnObject( ) != null;
	}

	/**
	 * Runs action.
	 *  
	 */
	public void run( )
	{
		TableEditPart part = getTableEditPart( );
		if ( part != null )
		{
			part.deleteColumn( new int[]{
				getRowNumber( )
			} );
		}
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return the table edit part
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
	 * Gets the current selected column object.
	 * 
	 * @return the current column object
	 */
	public Object getColumnObject( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		Object obj = getSelectedObjects( ).get( 0 );

		if ( obj instanceof DummyEditpart )
		{
			if ( ( (DummyEditpart) obj ).getModel( ) instanceof ColumnHandle )
			{
				return ( (DummyEditpart) obj ).getModel( );
			}
		}
		return null;
	}

	/**
	 * Gets row number.
	 * 
	 * @return the row number
	 */
	public int getRowNumber( )
	{
		return HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( getColumnObject( ) )
				.getColumnNumber( );
	}
}