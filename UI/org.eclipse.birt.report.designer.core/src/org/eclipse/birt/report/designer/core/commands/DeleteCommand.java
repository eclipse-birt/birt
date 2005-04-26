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

import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.ColumnHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.EmbeddedImageHandle;
import org.eclipse.birt.report.model.api.ListHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.activity.SemanticException;
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
			dropSourceElementHandle( (DesignElementHandle) source );
		}
		else if ( source instanceof SlotHandle )
		{
			dropSourceSlotHandle( (SlotHandle) source );
		}
		else if ( source instanceof ReportElementModel )
		{
			dropSourceSlotHandle( ( (ReportElementModel) source ).getSlotHandle( ) );
		}
		else if ( source instanceof ListBandProxy )
		{
			dropSourceSlotHandle( ( (ListBandProxy) source ).getSlotHandle( ) );
		}
		else if ( source instanceof EmbeddedImageHandle )
		{
			dropEmbeddedImageHandle( (EmbeddedImageHandle) ( source ) );
		}

	}

	/**
	 * @param object
	 */
	private void dropEmbeddedImageHandle( EmbeddedImageHandle embeddedImage )
	{
		try
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.dropImage( embeddedImage.getName( ) );
		}
		catch ( SemanticException e )
		{
			e.printStackTrace( );
		}

	}

	protected void dropSourceElementHandle( DesignElementHandle handle )
			throws SemanticException
	{
		if ( handle.getContainer( ) != null )
		{
			if ( handle instanceof CellHandle )
			{
				dropSourceSlotHandle( ( (CellHandle) handle ).getContent( ) );
			}
			else if ( handle instanceof RowHandle )
			{
				new DeleteRowCommand( handle ).execute( );
			}
			else if ( handle instanceof ColumnHandle )
			{
				new DeleteColumnCommand( handle ).execute( );
			}
			else
			{
				handle.drop( );
			}
		}
	}

	protected void dropSourceSlotHandle( SlotHandle slot )
			throws SemanticException
	{
		List list = slot.getContents( );
		for ( int i = 0; i < list.size( ); i++ )
		{
			dropSourceElementHandle( (DesignElementHandle) list.get( i ) );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.commands.Command#canExecute()
	 */
	public boolean canExecute( )
	{
		return canDrop( model );
	}

	protected boolean canDrop( Object source )
	{
		if ( source == null )
		{
			return false;
		}
		else if ( source instanceof Object[] )
		{
			return canDrop( new StructuredSelection( (Object[]) source ) );
		}
		else if ( source instanceof StructuredSelection )
		{
			StructuredSelection selection = (StructuredSelection) source;
			if ( selection.isEmpty( ) )
			{
				return false;
			}
			Iterator iterator = selection.iterator( );
			while ( iterator.hasNext( ) )
			{
				if ( !canDrop( iterator.next( ) ) )
				{
					return false;
				}
			}
			return true;
		}
		else if ( source instanceof ReportElementModel )
		{
			return canDrop( ( (ReportElementModel) source ).getSlotHandle( ) );
		}
		else if ( source instanceof ListBandProxy )
		{
			return canDrop( ( (ListBandProxy) source ).getSlotHandle( ) );
		}
		else if ( source instanceof SlotHandle )
		{
			SlotHandle slot = (SlotHandle) source;
			return slot.getElementHandle( ) instanceof ListHandle;
		}
		else if ( source instanceof EmbeddedImageHandle )
		{
			return true;
		}
		return source instanceof ReportElementHandle
				&& !( source instanceof MasterPageHandle );

	}
}