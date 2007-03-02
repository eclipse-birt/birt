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

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.internal.ui.command.CommandUtils;
import org.eclipse.birt.report.designer.internal.ui.command.ICommandParameterNameContants;
import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.ParameterValueConversionException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.ui.IWorkbenchPart;

/**
 * Action of inserting one row above the current selected row.
 */

public class InsertRowAboveAction extends InsertRowAction
{

	private static final String ACTION_MSG_ROW_ABOVE = Messages
			.getString( "InsertRowAboveAction.actionMsg.rowAbove" ); //$NON-NLS-1$

	/** action ID */
	public static final String ID = "org.eclipse.birt.report.designer.internal.ui.editors.schematic.actions.InsertRowAboveAction"; //$NON-NLS-1$

	/**
	 * Constructs new instance.
	 * 
	 * @param part
	 *            current work bench part
	 */
	public InsertRowAboveAction( IWorkbenchPart part )
	{
		super( part );
		setId( ID );
		setText( ACTION_MSG_ROW_ABOVE );
	}

	/**
	 * Runs action.
	 * 
	 */
	public void run( )
	{
//		if ( Policy.TRACING_ACTIONS )
//		{
//			System.out.println( "Insert row above action >> Run ..." ); //$NON-NLS-1$
//		}
//		if ( getTableEditPart( ) != null && !getRowHandles( ).isEmpty( ) )
//		{
//			// has combined two behavior into one.
//			getTableEditPart( ).insertRows( -1, getRowNumbers( ) );
//		}
		
		
		CommandUtils.setVariable(ICommandParameterNameContants.INSERT_ROW_POSITION, new Integer(-1));
		
		try
		{
			CommandUtils.executeCommand( "org.eclipse.birt.report.designer.ui.command.insertRowCommand",null );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
		}

	//	return Boolean.TRUE.equals( returnVlaue );
	}
}