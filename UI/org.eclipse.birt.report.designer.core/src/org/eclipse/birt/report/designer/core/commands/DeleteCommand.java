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

import java.util.List;

import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.model.activity.SemanticException;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.commands.Command;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * Deletes an object or multiple objects or do nothing.
 * 
 *  
 */

public class DeleteCommand extends Command
{

	private Object model = null;

	/**
	 * Deletes the command
	 * 
	 * @param model
	 *            the model
	 */

	public DeleteCommand( Object model )
	{
		this.model = model;
	}

	/**
	 * Executes the Command. This method should not be called if the Command is
	 * not executable.
	 */

	public void execute( )
	{
		try
		{
			dropSource( model );
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}
	}

	protected void dropSource( Object source ) throws SemanticException
	{
		if ( source instanceof Object[] )
		{
			Object[] array = (Object[]) source;
			for ( int i = 0; i < array.length; i++ )
			{
				dropSource( array[i] );
			}
		}
		else if ( source instanceof StructuredSelection )
		{
			dropSource( ( (StructuredSelection) source ).toArray( ) );
		}
		else if ( source instanceof DesignElementHandle )
		{
			if ( ( (DesignElementHandle) source ).getContainer( ) != null )
			{
				( (DesignElementHandle) source ).drop( );
			}
		}
		else if ( source instanceof SlotHandle )
		{
			dropSourceSlotHandle( (SlotHandle) source );
		}
		else if ( source instanceof ReportElementModel )
		{
			dropSourceSlotHandle( ( (ReportElementModel) source ).getSlotHandle( ) );
		}
	}

	protected void dropSourceSlotHandle( SlotHandle slot )
			throws SemanticException
	{
		List list = slot.getContents( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			( (DesignElementHandle) list.get( i ) ).drop( );
		}
	}
}