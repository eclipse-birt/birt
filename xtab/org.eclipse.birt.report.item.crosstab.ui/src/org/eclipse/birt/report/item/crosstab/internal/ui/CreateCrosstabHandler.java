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
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.expressions.IEvaluationContext;
import org.eclipse.gef.EditPart;

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
				.startTrans( "Create " + itemName ); //$NON-NLS-1$
		ExtendedItemHandle handle = null;
		//		InsertCubeDialog insertCubeDialog = new InsertCubeDialog( );
		//		if ( insertCubeDialog.open( ) == Window.OK )
		//		{
		handle = DesignElementFactory.getInstance( ).newExtendedItem( null,
				itemName );
		//			if ( insertCubeDialog.getResult( ) != null )
		//				try
		//				{
		//					handle.setProperty( ICrosstabReportItemConstants.CUBE_PROP,
		//							insertCubeDialog.getResult( ) );
		//				}
		//				catch ( SemanticException e )
		//				{
		//					// TODO Auto-generated catch block
		//					e.printStackTrace( );
		//				}
		//		List list = new ArrayList( );
		//
		//		list.add( handle );
		//		ReportRequest r = new ReportRequest( );
		//		r.setType( ReportRequest.CREATE_ELEMENT );
		Map map = new HashMap( );
		map.put( DesignerConstants.KEY_NEWOBJECT, handle );
		CreateCommand command = new CreateCommand( map );
		//		SessionHandleAdapter.getInstance( )
		//				.getReportDesignHandle( )
		//				.getCommandStack( )
		//				.execute( command );

		IEvaluationContext context = (IEvaluationContext) event.getApplicationContext( );
		EditPart targetEditPart = (EditPart) context.getVariable( "targetEditPart" );

		if ( targetEditPart != null )
		{
			command.setParent( targetEditPart.getModel( ) );
		}
		else
		{
			Object parentModel = UIUtil.getCurrentEditPart( ).getModel( );
			if ( parentModel instanceof DesignElementHandle )
			{
				DesignElementHandle parentHandle = (DesignElementHandle) parentModel;
				if ( parentHandle.getDefn( ).isContainer( ) )
				{
					command.setParent( parentModel );
				}
				else
				{
					command.setParent( SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( ) );
				}
			}
			else
			{
				command.setParent( SessionHandleAdapter.getInstance( )
						.getReportDesignHandle( ) );
			}
		}

		try
		{
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
		//if parent is library, select new object
		if ( command.getParent( ) instanceof LibraryHandle )
		{
			HandleAdapterFactory.getInstance( )
					.getLibraryHandleAdapter( )
					.setCurrentEditorModel( handle,
							LibraryHandleAdapter.CREATE_ELEMENT );
		}
		//		}
		//		else
		//		{
		//			SessionHandleAdapter.getInstance( )
		//					.getReportDesignHandle( )
		//					.getCommandStack( )
		//					.rollback( );
		//		}
		return handle;
	}

}
