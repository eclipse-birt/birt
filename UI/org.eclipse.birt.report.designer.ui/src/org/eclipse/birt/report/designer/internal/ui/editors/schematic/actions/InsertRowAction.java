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

package org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions;

import java.util.List;

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.DummyEditpart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.ui.IWorkbenchPart;

/**
 * insert row action
 * 
 *  
 */
public class InsertRowAction extends SelectionAction
{

	private static final String ACTION_MSG_INSERT = Messages.getString( "InsertRowAction.actionMsg.insert" ); //$NON-NLS-1$

	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAction"; //$NON-NLS-1$

	/**
	 * Constructor
	 * 
	 * @param part
	 */
	public InsertRowAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_INSERT );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return getRowObject( ) != null;
	}

	public void run( )
	{
		TableEditPart part = getTableEditPart( );
		if ( part != null )
		{
			part.insertRow( getRowNumber( ) );
		}
	}

	protected TableEditPart getTableEditPart( )
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

	public Object getRowObject( )
	{
		if ( getSelectedObjects( ) == null || getSelectedObjects( ).isEmpty( ) )
			return null;
		Object obj = getSelectedObjects( ).get( 0 );

		if ( obj instanceof DummyEditpart )
		{
			if ( ( (DummyEditpart) obj ).getModel( ) instanceof RowHandle )
			{
				return ( (DummyEditpart) obj ).getModel( );
			}
		}
		return null;
	}

	public int getRowNumber( )
	{
		return HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( getRowObject( ) )
				.getRowNumber( );
	}

}