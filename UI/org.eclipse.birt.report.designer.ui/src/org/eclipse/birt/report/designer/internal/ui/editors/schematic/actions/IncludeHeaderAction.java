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

import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.core.model.schematic.TableHandleAdapter;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of whether or not including footer of table
 */
public class IncludeHeaderAction extends InsertRowAction
{

	private static final String ACTION_MSG_INCLUDE_HEADER = Messages.getString( "IncludeHeaderAction.actionMsg.includeHeader" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.IncludeHeaderAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 * 
	 * @param part
	 *            current work bench part
	 */
	public IncludeHeaderAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setChecked( true );
		setText( ACTION_MSG_INCLUDE_HEADER );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.SelectionAction#update()
	 */
	public void update( )
	{
		super.update( );
		if ( getTableEditPart( ) != null )
		{
			TableHandleAdapter adapt = HandleAdapterFactory.getInstance( )
					.getTableHandleAdapter( getTableEditPart( ).getModel( ) );
			setChecked( adapt.hasSlotHandleRow( TableHandleAdapter.HEADER ) );
		}
	}

	/**
	 * Runs action.
	 */
	public void run( )
	{
		getTableEditPart( ).includeSlotHandle( isChecked( ),
				TableHandleAdapter.HEADER );
	}
}