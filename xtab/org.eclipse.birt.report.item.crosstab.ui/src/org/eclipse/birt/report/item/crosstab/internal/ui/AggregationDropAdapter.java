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

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabCellConstants;
import org.eclipse.birt.report.item.crosstab.core.de.CrosstabCellHandle;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.ICrosstabCellAdapterFactory;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.window.Window;

/**
 * AggregationDropAdapter
 */
public class AggregationDropAdapter implements IDropAdapter
{

	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( transfer.equals( "DATA_AGG" ) //$NON-NLS-1$
				&& target instanceof CrosstabCellEditPart )
		{
			CrosstabCellAdapter adapter = (CrosstabCellAdapter) ( (CrosstabCellEditPart) target ).getModel( );
			if ( adapter.getCrosstabCellHandle( ) != null
					&& DEUtil.isReferenceElement( adapter.getCrosstabCellHandle( )
							.getCrosstabHandle( ) ) )
				return DNDService.LOGIC_FALSE;

			String posType = adapter.getPositionType( );

			if ( ICrosstabCellAdapterFactory.CELL_MEASURE_AGGREGATION.equals( posType )
					|| ICrosstabCellAdapterFactory.CELL_MEASURE.equals( posType ) )
			{
				return DNDService.LOGIC_TRUE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		
		if(target instanceof EditPart)
		{
			EditPart editPart = (EditPart)target;
		
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getCommandStack( );
		stack.startTrans( "Add Aggregation" ); //$NON-NLS-1$

		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
				.newDataItem( null );

		CrosstabCellHandle cellHandle = ( (CrosstabCellAdapter) ( (CrosstabCellEditPart) target ).getModel( ) ).getCrosstabCellHandle( );
		try
		{
			cellHandle.addContent( dataHandle, CellHandle.CONTENT_SLOT );

			DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
			dialog.setInput( dataHandle, null, cellHandle );
			dialog.setAggreate( true );

			if ( dialog.open( ) == Window.OK )
			{
				cellHandle.getModelHandle( ).getPropertyHandle( ICrosstabCellConstants.CONTENT_PROP ).removeItem( dataHandle );
				CreateRequest request = new CreateRequest( );

				request.getExtendedData( )
						.put( DesignerConstants.KEY_NEWOBJECT, dataHandle );
				request.setLocation( location.getPoint( ) );				
				
				Command command = editPart.getCommand( request );
				if ( command != null && command.canExecute( ) )
				{
					dataHandle.setResultSetColumn( dialog.getBindingColumn( )
							.getName( ) );		
					
					editPart.getViewer( )
					.getEditDomain( )
					.getCommandStack( )
					.execute( command );
					
					stack.commit( );
				}else
				{
					stack.rollback( );
				}
		
			}
			else
			{
				stack.rollback( );
			}
		}
		catch ( Exception e )
		{
			stack.rollback( );
			ExceptionHandler.handle( e );
		}
		}
		return true;
	}

//	public boolean performDrop( Object transfer, Object target, int operation,
//			DNDLocation location )
//	{
//		
//		if(target instanceof EditPart)
//		{
//			EditPart editPart = (EditPart)target;
//		
//		CommandStack stack = SessionHandleAdapter.getInstance( )
//				.getCommandStack( );
//		stack.startTrans( "Add Aggregation" ); //$NON-NLS-1$
//
//		DataItemHandle dataHandle = DesignElementFactory.getInstance( )
//				.newDataItem( null );
//
//		CrosstabCellHandle cellHandle = ( (CrosstabCellAdapter) ( (CrosstabCellEditPart) target ).getModel( ) ).getCrosstabCellHandle( );
//		try
//		{
//			cellHandle.addContent( dataHandle, CellHandle.CONTENT_SLOT );
//
//			DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
//			dialog.setInput( dataHandle, null, cellHandle );
//			dialog.setAggreate( true );
//
//			if ( dialog.open( ) == Window.OK )
//			{
//				cellHandle.getModelHandle( ).getPropertyHandle( ICrosstabCellConstants.CONTENT_PROP ).removeItem( dataHandle );
//				CreateRequest request = new CreateRequest( );
//
//				request.getExtendedData( )
//						.put( DesignerConstants.KEY_NEWOBJECT, dataHandle );
//				request.setLocation( location.getPoint( ) );				
//				
//				Command command = editPart.getCommand( request );
//				if ( command != null && command.canExecute( ) )
//				{
//					dataHandle.setResultSetColumn( dialog.getBindingColumn( )
//							.getName( ) );		
//					
//					editPart.getViewer( )
//					.getEditDomain( )
//					.getCommandStack( )
//					.execute( command );
//					
//					stack.commit( );
//				}else
//				{
//					stack.rollback( );
//				}
//		
//			}
//			else
//			{
//				stack.rollback( );
//			}
//		}
//		catch ( Exception e )
//		{
//			stack.rollback( );
//			ExceptionHandler.handle( e );
//		}
//		}
//		return true;
//	}
	
}
