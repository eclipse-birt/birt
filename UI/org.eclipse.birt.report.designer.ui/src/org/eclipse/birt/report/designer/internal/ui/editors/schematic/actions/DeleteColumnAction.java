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
 * Action to delete a column of a table or a grid.
 *  
 */
public class DeleteColumnAction extends ContextSelectionAction
{

	/** action text */
	private static final String ACTION_MSG_DELETE = Messages.getString( "DeleteColumnAction.actionMsg.delete" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "DeleteGroupAction.DeleteColumnAction"; //$NON-NLS-1$

	/**
	 * Constructs a new instance of this acion.
	 * 
	 * @param part
	 *            The current work bench part
	 */
	public DeleteColumnAction( IWorkbenchPart part )
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
		return !getColumnHandles( ).isEmpty( );
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
			part.deleteColumn( getColumnNumbers( ) );
			viewer.select( part );
		}
	}

	/**
	 * Gets column numbers of selected columns.
	 */
	public int[] getColumnNumbers( )
	{
		List columnHandles = getColumnHandles( );
		if ( columnHandles.isEmpty( ) )
		{
			return new int[0];
		}
		int[] colNumbers = new int[columnHandles.size( )];
		for ( int i = 0; i < columnHandles.size( ); i++ )
		{
			colNumbers[i] = getColumnNumber( columnHandles.get( i ) );
		}
		return colNumbers;
	}

	/**
	 * Gets column number of the selected column object.
	 * 
	 * @return the column number
	 */
	public int getColumnNumber( Object columnHandle )
	{
		return HandleAdapterFactory.getInstance( )
				.getColumnHandleAdapter( columnHandle )
				.getColumnNumber( );
	}
}