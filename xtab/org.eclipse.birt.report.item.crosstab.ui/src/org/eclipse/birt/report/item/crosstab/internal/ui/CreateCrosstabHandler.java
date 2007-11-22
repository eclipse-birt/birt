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

package org.eclipse.birt.report.item.crosstab.internal.ui;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.commands.CreateCommand;
import org.eclipse.birt.report.designer.core.model.LibraryHandleAdapter;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.HandleAdapterFactory;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.birt.report.item.crosstab.core.util.CrosstabExtendedItemFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.requests.CreateRequest;

/**
 * 
 */

public class CreateCrosstabHandler extends AbstractHandler
{

	private static String itemName = "Crosstab";

	public Object execute( ExecutionEvent event ) throws ExecutionException
	{
		SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getCommandStack( )
				.startTrans( Messages.getString( "InsertAction.text" ) ); //$NON-NLS-1$
		ExtendedItemHandle handle = null;

		try
		{
			handle = CrosstabExtendedItemFactory.createCrosstabReportItem( SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( ),
					null );
		}
		catch ( Exception e )
		{
			SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getCommandStack( )
					.rollback( );

			throw new ExecutionException( e.getLocalizedMessage( ), e );
		}

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext( );

		EditPart targetEditPart = (EditPart) context.getVariable( "targetEditPart" );
		if ( targetEditPart == null )
		{
			targetEditPart = UIUtil.getCurrentEditPart( );
		}

		Object parentModel = DNDUtil.unwrapToModel( targetEditPart.getModel( ) );

		CreateRequest request = (CreateRequest) context.getVariable( "request" );

		if ( request != null )
		{
			request.getExtendedData( ).put( DesignerConstants.KEY_NEWOBJECT,
					handle );

			try
			{
				targetEditPart.getCommand( request ).execute( );
				SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getCommandStack( )
						.commit( );
			}
			catch ( Exception e )
			{
				SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getCommandStack( )
						.rollback( );
			}
		}
		else
		{
			Map map = new HashMap( );
			map.put( DesignerConstants.KEY_NEWOBJECT, handle );
			CreateCommand command = new CreateCommand( map );

			try
			{
				if ( parentModel instanceof DesignElementHandle )
				{
					DesignElementHandle parentHandle = (DesignElementHandle) parentModel;
					if ( parentHandle.getDefn( ).isContainer( )
							&& ( parentHandle.canContain( DEUtil.getDefaultSlotID( parentHandle ),
									handle ) || parentHandle.canContain( DEUtil.getDefaultContentName( parentHandle ),
									handle ) ) )
					{
						command.setParent( parentHandle );
					}
					else
					{
						if ( parentHandle.getContainerSlotHandle( ) != null )
						{
							command.setAfter( parentHandle.getContainerSlotHandle( )
									.get( parentHandle.getIndex( ) + 1 ) );
						}
						else if ( parentHandle.getContainerPropertyHandle( ) != null )
						{
							command.setAfter( parentHandle.getContainerPropertyHandle( )
									.get( parentHandle.getIndex( ) + 1 ) );
						}
						command.setParent( parentHandle.getContainer( ) );
					}
				}
				else
				{
					command.setParent( SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( ) );
				}
				command.execute( );
				SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getCommandStack( )
						.commit( );
			}
			catch ( Exception e )
			{
				SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( )
						.getCommandStack( )
						.rollback( );
			}
			// SlotHandle slotHandle = getDefaultSlotHandle( itemName,
			// parentModel );
			// int pos = DNDUtil.calculateNextPosition( parentModel,
			// DNDUtil.handleValidateTargetCanContain( parentModel,
			// handle,
			// true ) );
			//
			// try
			// {
			// if ( pos == -1 )
			// {
			// slotHandle.add( handle );
			// }
			// else
			// {
			// slotHandle.add( handle, pos );
			// }
			// SessionHandleAdapter.getInstance( )
			// .getReportDesignHandle( )
			// .getCommandStack( )
			// .commit( );
			// }
			// catch ( Exception e )
			// {
			// SessionHandleAdapter.getInstance( )
			// .getReportDesignHandle( )
			// .getCommandStack( )
			// .rollback( );
			// }
		}
		// if ( parentModel instanceof DesignElementHandle )
		// {
		// DesignElementHandle parentHandle = (DesignElementHandle) parentModel;
		// if ( parentHandle.getDefn( ).isContainer( ) )
		// {
		// command.setParent( parentModel );
		// }
		// else
		// {
		// command.setParent( parentHandle.getContainer( ) );
		// }
		// }
		// else
		// {
		// command.setParent( SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( ) );
		// }
		//		
		// if ( request != null )
		// {
		// request.getExtendedData( ).put( DesignerConstants.KEY_NEWOBJECT,
		// handle );
		// command = (CreateCommand) targetEditPart.getCommand( request );
		// command.setParent( DNDUtil.unwrapToModel( targetEditPart.getModel( )
		// ) );
		// }
		// else
		// {
		//			
		// command = new CreateCommand( map );
		// Object parentModel = DNDUtil.unwrapToModel(
		// UIUtil.getCurrentEditPart( )
		// .getModel( ) );
		// if ( parentModel instanceof DesignElementHandle )
		// {
		// DesignElementHandle parentHandle = (DesignElementHandle) parentModel;
		// if ( parentHandle.getDefn( ).isContainer( ) )
		// {
		// command.setParent( parentModel );
		// }
		// else
		// {
		// command.setParent( SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( ) );
		// }
		// }
		// else
		// {
		// command.setParent( SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( ) );
		// }
		// }

		// try
		// {
		// command.execute( );
		// SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( )
		// .getCommandStack( )
		// .commit( );
		// }
		// catch ( Exception e )
		// {
		// SessionHandleAdapter.getInstance( )
		// .getReportDesignHandle( )
		// .getCommandStack( )
		// .rollback( );
		// }
		// if parent is library, select new object
		if ( parentModel instanceof LibraryHandle )
		{
			try
			{
				HandleAdapterFactory.getInstance( )
						.getLibraryHandleAdapter( )
						.setCurrentEditorModel( handle,
								LibraryHandleAdapter.CREATE_ELEMENT );
			}
			catch ( Exception e )
			{
			}
		}
		return handle;
	}

	// private SlotHandle getDefaultSlotHandle( String insertType, Object model
	// )
	// {
	// if ( model instanceof LibRootModel )
	// {
	// model = ( (LibRootModel) model ).getModel( );
	// }
	// if ( model instanceof SlotHandle )
	// {
	// return (SlotHandle) model;
	// }
	// else if ( model instanceof DesignElementHandle )
	// {
	// DesignElementHandle handle = (DesignElementHandle) model;
	//
	// if ( handle.getDefn( ).isContainer( ) )
	// {
	// int slotId = DEUtil.getDefaultSlotID( handle );
	// if ( handle.canContain( slotId, insertType ) )
	// {
	// return handle.getSlot( slotId );
	// }
	// }
	// return handle.getContainerSlotHandle( );
	// }
	// return null;
	// }
}
