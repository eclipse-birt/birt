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

package org.eclipse.birt.report.designer.core.commands;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.command.ContentException;
import org.eclipse.gef.commands.Command;

/**
 * This command moves a child inside a SlotHandle
 * 
 * 
 */

public class FlowMoveChildCommand extends Command
{

	private static final String TRANS_LABEL_MOVE_ELEMENT = Messages.getString( "FlowMoveChildCommand.transLabel.moveElement" ); //$NON-NLS-1$

	private Object child = null;

	private Object after = null;

	private Object container = null;

	/**
	 * Constructor
	 * 
	 * @param container
	 * @param model
	 * @param model2
	 */
	public FlowMoveChildCommand( Object child, Object after, Object container )
	{
		this.child = child;
		this.after = after;
		this.container = container;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is
	 * not executable.
	 */

	public void execute( )
	{
		if ( DesignerConstants.TRACING_COMMANDS )
		{
			System.out.println( "FlowMoveChildCommand >> Starts ... " ); //$NON-NLS-1$
		}
		try
		{

			DesignElementHandle containerHandle = null;

			int slotID = -1, pos = -1;

			// for virtual model that contains a slot handle
			if ( container instanceof ListBandProxy )
			{
				containerHandle = ( (ListBandProxy) container ).getSlotHandle( )
						.getElementHandle( );
				slotID = ( (ListBandProxy) container ).getSlotHandle( )
						.getSlotID( );
				pos = DEUtil.findInsertPosition( containerHandle,
						(DesignElementHandle) after,
						slotID );
			}

			// for real node that contains design element handle
			else if ( container instanceof DesignElementHandle )
			{
				containerHandle = (DesignElementHandle) container;
				slotID = DEUtil.getSlotID( containerHandle, after );
				pos = DEUtil.findInsertPosition( containerHandle,
						(DesignElementHandle) after );
			}
			else if ( container instanceof ReportElementModel )
			{
				containerHandle = ( (ReportElementModel) container ).getElementHandle( );
				slotID = ( (ReportElementModel) container ).getSlotId( );
				pos = DEUtil.findInsertPosition( containerHandle,
						(DesignElementHandle) after,
						slotID );
			}

			DesignElementHandle handle = (DesignElementHandle) child;

			CommandStack stack = SessionHandleAdapter.getInstance( )
					.getCommandStack( );

			stack.startTrans( TRANS_LABEL_MOVE_ELEMENT );

			handle.moveTo( containerHandle, slotID );
			containerHandle.getSlot( slotID ).shift( handle, pos );

			stack.commit( );
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "FlowMoveChildCommand >> Finished. Moved " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel( handle )
						+ " to the slot " //$NON-NLS-1$
						+ slotID
						+ " of " //$NON-NLS-1$
						+ DEUtil.getDisplayLabel( containerHandle )
						+ ",Position: " //$NON-NLS-1$
						+ pos );
			}
		}
		catch ( ContentException e )
		{
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "FlowMoveChildCommand >> Failed" ); //$NON-NLS-1$
			}
			e.printStackTrace( );
		}
	}
}