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

import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableEditPart;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action to insert one row below the selected row.
 */
public class InsertRowBelowAction extends InsertRowAction
{

	private static final String ACTION_MSG_ROW_BELOW = Messages.getString( "InsertRowBelowAction.actionMsg.rowBelow" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowBelowAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 * 
	 * @param part
	 *            current work bench part
	 */
	public InsertRowBelowAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_ROW_BELOW );
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
			// insert row below the current selected row.
			part.insertRow( 1, getRowNumber( ) );
		}
	}
}