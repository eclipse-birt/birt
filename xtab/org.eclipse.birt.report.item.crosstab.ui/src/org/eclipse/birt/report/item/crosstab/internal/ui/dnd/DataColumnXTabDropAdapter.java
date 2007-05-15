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

package org.eclipse.birt.report.item.crosstab.internal.ui.dnd;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.DesignerConstants;
import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.cubebuilder.page.SimpleCubeBuilder;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.item.crosstab.core.ICrosstabReportItemConstants;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabCellEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.editparts.CrosstabTableEditPart;
import org.eclipse.birt.report.item.crosstab.internal.ui.editors.model.CrosstabCellAdapter;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DataSetHandle;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ExtendedItemHandle;
import org.eclipse.birt.report.model.api.PropertyHandle;
import org.eclipse.birt.report.model.api.ResultSetColumnHandle;
import org.eclipse.birt.report.model.api.olap.TabularCubeHandle;
import org.eclipse.birt.report.model.elements.interfaces.IReportItemModel;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.commands.Command;
import org.eclipse.gef.requests.CreateRequest;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.PlatformUI;

/**
 * 
 */

public class DataColumnXTabDropAdapter implements IDropAdapter
{

	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( !isDataColumn( transfer ) )
			return DNDService.LOGIC_UNKNOW;
		DesignElementHandle handle = getExtendedItemHandle( target );
		if ( handle != null )
		{
			//when xtab has not bind with Cube, data item can drop on everywhere in xtab.
			if ( handle.getProperty( IReportItemModel.CUBE_PROP ) == null
					&& ( target instanceof CrosstabTableEditPart || target instanceof CrosstabCellEditPart ) )
			{
				return DNDService.LOGIC_TRUE;
			}
			else if ( handle.getProperty( IReportItemModel.CUBE_PROP ) != null )
			{
				Object model = ( (EditPart) target ).getModel( );

				if ( model instanceof CrosstabCellAdapter )
				{
					PropertyHandle propertyHandle = ( (CrosstabCellAdapter) model ).getPropertyHandle( );
					if ( propertyHandle != null
							&& propertyHandle.canContain( "Data" ) )
					{
						return DNDService.LOGIC_TRUE;
					}
				}
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	private boolean isDataColumn( Object transfer )
	{
		if ( transfer instanceof Object[] )
		{
			Object[] transfers = (Object[]) transfer;
			for ( int i = 0; i < transfers.length; i++ )
			{
				if ( !isDataColumn( transfers[i] ) )
					return false;
			}
			return true;
		}
		return transfer instanceof ResultSetColumnHandle;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		DesignElementHandle handle = getExtendedItemHandle( target );
		if ( handle != null )
		{
			if ( handle.getProperty( IReportItemModel.CUBE_PROP ) != null )
			{
				EditPart editPart = (EditPart) target;

				if ( editPart != null )
				{
					CreateRequest request = new CreateRequest( );

					request.getExtendedData( )
							.put( DesignerConstants.KEY_NEWOBJECT, transfer );
					request.setLocation( location.getPoint( ) );
					Command command = editPart.getCommand( request );
					if ( command != null && command.canExecute( ) )
					{
						editPart.getViewer( )
								.getEditDomain( )
								.getCommandStack( )
								.execute( command );
						return true;
					}
					else
						return false;
				}
				return false;
			}
			else
			{
				CommandStack stack = getActionStack( );
				stack.startTrans( "Create a cube for binding the crossTab" ); //$NON-NLS-1$
				try
				{
					ResultSetColumnHandle columnHandle = getColumnHandle( transfer );
					if ( columnHandle != null )
					{
						DataSetHandle dataSetHandle = (DataSetHandle) columnHandle.getElementHandle( );

						TabularCubeHandle newCube = DesignElementFactory.getInstance( )
								.newTabularCube( "Customer Cube" );

						SessionHandleAdapter.getInstance( )
								.getReportDesignHandle( )
								.getCubes( )
								.add( newCube );

						SimpleCubeBuilder builder = new SimpleCubeBuilder( PlatformUI.getWorkbench( )
								.getDisplay( )
								.getActiveShell( ) );
						builder.setInput( newCube, dataSetHandle );

						if ( builder.open( ) == Window.OK )
						{
							if ( handle != null )
							{
								handle.setProperty( IReportItemModel.CUBE_PROP,
										newCube );
							}
							stack.commit( );

							ReportRequest request = new ReportRequest( ReportRequest.CREATE_ELEMENT );
							List selectionObjects = new ArrayList( );
							selectionObjects.add( handle );
							request.setSelectionObject( selectionObjects );
							SessionHandleAdapter.getInstance( )
									.getMediator( )
									.notifyRequest( request );
						}
						else
						{
							stack.rollback( );
						}
					}
					return true;
				}
				catch ( Exception e )
				{
					stack.rollback( );
					ExceptionHandler.handle( e );
				}
			}
		}
		return false;
	}

	private DesignElementHandle getExtendedItemHandle( Object target )
	{
		if ( target instanceof CrosstabTableEditPart )
			return (DesignElementHandle) ( (CrosstabTableEditPart) target ).getModel( );
		if ( target instanceof EditPart )
		{
			EditPart part = (EditPart) target;
			if ( target instanceof IAdaptable )
			{
				DesignElementHandle handle = (DesignElementHandle) ( (IAdaptable) target ).getAdapter( DesignElementHandle.class );
				if ( handle == null && part.getParent( ) != null )
					return getExtendedItemHandle( part.getParent( ) );
			}
		}
		return null;
	}

	private ResultSetColumnHandle getColumnHandle( Object transfer )
	{
		if ( transfer instanceof Object[] )
		{
			Object[] transfers = (Object[]) transfer;
			for ( int i = 0; i < transfers.length; i++ )
			{
				if ( transfers[i] instanceof ResultSetColumnHandle )
					return (ResultSetColumnHandle) transfers[i];
			}
			return null;
		}
		if ( transfer instanceof ResultSetColumnHandle )
			return (ResultSetColumnHandle) transfer;
		return null;
	}

	private CommandStack getActionStack( )
	{
		return SessionHandleAdapter.getInstance( ).getCommandStack( );
	}
}
