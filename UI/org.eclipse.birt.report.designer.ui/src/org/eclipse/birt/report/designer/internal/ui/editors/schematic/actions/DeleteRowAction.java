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
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Action delete a row of a table or a grid.
 *  
 */
public class DeleteRowAction extends ContextSelectionAction
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
		return !getRowHandles( ).isEmpty( );
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
			part.deleteRow( getRowNumbers( ) );
			viewer.select( part );
		}
	}

	/**
	 * Gets row numbers of selected rows.
	 */
	private int[] getRowNumbers( )
	{
		List rowHandles = getRowHandles( );
		if ( rowHandles.isEmpty( ) )
		{
			return new int[0];
		}
		int[] rowNumbers = new int[rowHandles.size( )];
		for ( int i = 0; i < rowHandles.size( ); i++ )
		{
			rowNumbers[i] = getRowNumber( rowHandles.get( i ) );
		}
		return rowNumbers;
	}

	/**
	 * Gets row number.
	 * 
	 * @return The row number of the selected row object.
	 */
	public int getRowNumber( Object rowHandle )
	{
		return HandleAdapterFactory.getInstance( )
				.getRowHandleAdapter( rowHandle )
				.getRowNumber( );
	}

}