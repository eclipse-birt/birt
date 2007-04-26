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

package org.eclipse.birt.report.item.crosstab.internal.ui.editors.commands;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IDesignElement;
import org.eclipse.gef.commands.Command;

/**
 * Paster the count to the crosscell handle
 */

public class CrosstabPasterCommand extends Command
{

	private DesignElementHandle sourceHandle;
	private IDesignElement cloneElement;

	private DesignElementHandle newContainer;

	private DesignElementHandle afterHandle;

	/**
	 * Insert position
	 */
	private int position = -1;

	/**
	 * designelement content property name
	 */
	private String contentName = null;

	/**
	 * Constructor
	 * 
	 * @param sourceHandle
	 * @param newContainer
	 * @param afterHandle
	 */
	public CrosstabPasterCommand( DesignElementHandle sourceHandle,
			DesignElementHandle newContainer, DesignElementHandle afterHandle )
	{
		this.sourceHandle = sourceHandle;
		this.cloneElement = sourceHandle.copy( );
		this.newContainer = newContainer;
		this.afterHandle = afterHandle;
	}

	/**
	 * @return <code>true</code> if the command can be executed
	 */
	public boolean canExecute( )
	{
		DesignElementHandle childHandle = sourceHandle;

		return DNDUtil.handleValidateTargetCanContain( newContainer,
				childHandle )
				&& DNDUtil.handleValidateTargetCanContainMore( newContainer, 1 );
	}

	/**
	 * Executes the Command.
	 */
	public void execute( )
	{
		try
		{
			calculatePositionAndSlotId( );
			ModuleHandle currentDesignHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			DesignElementHandle newHandle = copyNewHandle( cloneElement,
					currentDesignHandle );

			// Adds new handle to report
			addHandleToReport( newHandle );
		}
		catch ( Exception e )
		{
			if ( DesignerConstants.TRACING_COMMANDS )
			{
				System.out.println( "PasteCommand >> Failed." ); //$NON-NLS-1$
			}
			ExceptionHandler.handle( e );
		}
	}

	/**
	 * Caculate the paste position
	 */
	private void calculatePositionAndSlotId( )
	{
		if ( contentName == null )
		{
			contentName = DEUtil.getDefaultContentName( newContainer );
		}

		position = DEUtil.findInsertPosition( newContainer,
				afterHandle,
				getContentName( ) );

	}

	/**
	 * Add this design element to report.
	 * 
	 * @param newHandle
	 *            The design element to add
	 * @throws SemanticException
	 */
	private void addHandleToReport( DesignElementHandle newHandle )
			throws SemanticException
	{
		newContainer.paste( getContentName( ), newHandle, position );
	}

	private DesignElementHandle copyNewHandle( IDesignElement element,
			ModuleHandle currentDesignHandle )
			throws CloneNotSupportedException
	{
		IDesignElement newElement = element;
		DesignElementHandle handle = newElement.getHandle( currentDesignHandle.getModule( ) );
		currentDesignHandle.rename( handle );
		return handle;
	}

	/**
	 * Gets the content name
	 * 
	 * @return
	 */
	public String getContentName( )
	{
		return contentName;
	}

	/**
	 * Sets the conten name
	 * 
	 * @param contentName
	 */
	public void setContentName( String contentName )
	{
		this.contentName = contentName;
	}
}
