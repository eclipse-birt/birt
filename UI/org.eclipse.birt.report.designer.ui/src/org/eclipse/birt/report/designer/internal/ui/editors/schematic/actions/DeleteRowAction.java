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
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Action delete a row of a table or a grid.
 *  
 */
public class DeleteRowAction extends SelectionAction
{

	/** action text */
	private static final String ACTION_MSG_DELETE = Messages.getString( "DeleteRowAction.actionMsg.delete" ); //$NON-NLS-1$

	/** action ID for registry */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.DeleteRowAction"; //$NON-NLS-1$

	/**
	 * Constructs a new instance of this action.
	 * 
	 * @param part
	 *            The current work bench part
	 */
	public DeleteRowAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_DELETE );
		ISharedImages shareImages = PlatformUI.getWorkbench( )
				.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE ) );
		setDisabledImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE_DISABLED ) );
		setAccelerator( SWT.DEL );
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

	/**
	 * Runs this action.
	 *  
	 */
	public void run( )
	{
		TableEditPart part = getTableEditPart( );
		if ( part != null )
		{
			EditPartViewer viewer = part.getViewer( );
			part.deleteRow( new int[]{
				getRowNumber( )
			} );
			// set selection to the table(grid) part after deleting
			viewer.select( part );
		}
	}

	/**
	 * Gets table edit part.
	 * 
	 * @return the table edit part
	 */
	private TableEditPart getTableEditPart( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;

		int size = list.size( );
		TableEditPart part = null;
		for ( int i = 0; i < size; i++ )
		{
			Object obj = list.get( i );

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
	 * Gets the current selected row object.
	 * 
	 * @return The current selected row object.
	 */
	public Object getRowObject( )
	{
		List list = getSelectedObjects( );
		if ( list == null || list.isEmpty( ) )
			return null;

		Object obj = list.get( 0 );

		if ( obj instanceof DummyEditpart )
		{
			if ( ( (DummyEditpart) obj ).getModel( ) instanceof RowHandle )
			{
				return ( (DummyEditpart) obj ).getModel( );
			}
		}
		return null;
	}

	/**
	 * Gets row number.
	 * 
	 * @return The row number of the selected row object.
	 */
	public int getRowNumber( )
	{
		return HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( getRowObject( ) )
				.getRowNumber( );
	}

}