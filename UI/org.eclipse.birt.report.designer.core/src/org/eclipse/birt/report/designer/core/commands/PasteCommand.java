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

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.command.ContentException;
import org.eclipse.birt.report.model.command.NameException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.gef.commands.Command;

/**
 * Paste Command
 *  
 */

public class PasteCommand extends Command
{

	/** Null permitted in instance. */
	private DesignElementHandle sourceHandle;

	private Object newContainer;

	private DesignElementHandle afterHandle;

	private DesignElement cloneElement;

	/** True: cut; False: copy */
	private boolean isCut = false;

	private int slotID = -1;

	private int position = -1;

	/**
	 * Constructor
	 * 
	 * @param sourceHandle
	 *            the source
	 * @param newContainer
	 *            the new container, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param afterHandle
	 *            the handle next to the source
	 * @param isCut
	 *            If true, delete source
	 */
	public PasteCommand( DesignElementHandle sourceHandle, Object newContainer,
			DesignElementHandle afterHandle, boolean isCut )
	{
		this.sourceHandle = sourceHandle;
		this.cloneElement = sourceHandle.copy( );
		this.newContainer = newContainer;
		this.afterHandle = afterHandle;
		this.isCut = isCut;
	}

	/**
	 * Constructor
	 * 
	 * @param sourceHandle
	 *            the source
	 * @param newContainer
	 *            the new container, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param position
	 *            the position will be added
	 * @param isCut
	 *            If true, delete source
	 */
	public PasteCommand( DesignElementHandle sourceHandle, Object newContainer,
			int position, boolean isCut )
	{
		this.sourceHandle = sourceHandle;
		this.cloneElement = sourceHandle.copy( );
		this.newContainer = newContainer;
		this.position = position;
		this.isCut = isCut;
	}

	/**
	 * Constructor
	 * 
	 * @param cloneElement
	 *            the copy of the source
	 * @param newContainer
	 *            the new container, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param afterHandle
	 *            the handle next to the source
	 */
	public PasteCommand( DesignElement cloneElement, Object newContainer,
			DesignElementHandle afterHandle )
	{
		this.cloneElement = cloneElement;
		this.newContainer = newContainer;
		this.afterHandle = afterHandle;
	}

	/**
	 * Constructor
	 * 
	 * @param cloneElement
	 *            the copy of the source
	 * @param newContainer
	 *            the new container, class type could be
	 *            <code>DesignElementHandle</code>,<code>SlotHandle</code>
	 *            or <code>ReportElementModel</code>
	 * @param position
	 *            the position will be added
	 */
	public PasteCommand( DesignElement cloneElement, Object newContainer,
			int position )
	{
		this.cloneElement = cloneElement;
		this.newContainer = newContainer;
		this.position = position;
	}

	/**
	 * Executes the Command.
	 */
	public void execute( )
	{
		try
		{
			//Drops old source handle if operation is cut
			dropSourceHandle( sourceHandle );

			//Gets new handle
			ReportDesignHandle currentDesignHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			DesignElementHandle newHandle = copyNewHandle( cloneElement,
					currentDesignHandle );

			//Adds new handle to report
			addHandleToReport( newHandle );
		}
		catch ( NameException e )
		{
			e.printStackTrace( );
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
	}

	private void addHandleToReport( DesignElementHandle newHandle )
			throws ContentException, NameException
	{
		if ( newContainer instanceof DesignElementHandle )
		{
			slotID = DEUtil.getDefaultSlotID( newContainer );
			if ( position == -1 && afterHandle != null )
			{
				position = DEUtil.findInsertPosition( (DesignElementHandle) newContainer,
						afterHandle );
			}
			( (DesignElementHandle) newContainer ).getSlot( slotID )
					.paste( newHandle, position );
		}
		else if ( newContainer instanceof SlotHandle )
		{
			slotID = ( (SlotHandle) newContainer ).getSlotID( );
			if ( position == -1 && afterHandle != null )
			{
				position = DEUtil.findInsertPosition( ( (SlotHandle) newContainer ).getElementHandle( ),
						afterHandle,
						slotID );
			}
			( (SlotHandle) newContainer ).paste( newHandle, position );
		}
		else if ( newContainer instanceof ReportElementModel )
		{
			slotID = ( (ReportElementModel) newContainer ).getSlotId( );
			if ( position == -1 && afterHandle != null )
			{
				position = DEUtil.findInsertPosition( ( (SlotHandle) newContainer ).getElementHandle( ),
						afterHandle,
						slotID );
			}
			( (ReportElementModel) newContainer ).getElementHandle( )
					.getSlot( slotID )
					.paste( newHandle, position );
		}
	}

	private void dropSourceHandle( DesignElementHandle oldHandle )
			throws SemanticException
	{
		if ( isCut && oldHandle != null )
		{
			if ( oldHandle.getContainer( ) != null )
			{
				oldHandle.drop( );
			}
		}
	}

	private DesignElementHandle copyNewHandle( DesignElement newElement,
			ReportDesignHandle currentDesignHandle )
	{
		currentDesignHandle.rename( newElement );
		return newElement.getHandle( currentDesignHandle.getDesign( ) );
	}

	/**
	 * @return <code>true</code> if the command can be executed
	 */
	public boolean canExecute( )
	{
		DesignElementHandle childHandle = sourceHandle;
		if ( childHandle == null )
		{
			childHandle = cloneElement.getHandle( SessionHandleAdapter.getInstance( )
					.getReportDesign( ) );
		}
		return DEUtil.handleValidateTargetCanContain( newContainer, childHandle )
				&& DEUtil.handleValidateTargetCanContainMore( newContainer, 1 );
	}
}