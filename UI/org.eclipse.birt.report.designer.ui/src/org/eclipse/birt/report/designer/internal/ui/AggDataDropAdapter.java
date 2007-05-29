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

package org.eclipse.birt.report.designer.internal.ui;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.DataColumnBindingDialog;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDLocation;
import org.eclipse.birt.report.designer.internal.ui.dnd.DNDService;
import org.eclipse.birt.report.designer.internal.ui.dnd.IDropAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.TableCellEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.newelement.DesignElementFactory;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.DataItemHandle;
import org.eclipse.birt.report.model.api.TableHandle;
import org.eclipse.jface.window.Window;

/**
 * 
 */

public class AggDataDropAdapter implements IDropAdapter
{

	public static final String TEMPLATE = "DATA_AGG"; //$NON-NLS-1$
	public static final String TRANS_NAME = Messages.getString( "AggDataDropAdapter.Trans.Name" ); //$NON-NLS-1$

	public int canDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( transfer instanceof Object[] )
		{

		}
		if ( transfer.equals( TEMPLATE ) && target instanceof TableCellEditPart )
		{
			CellHandle cellHandle = (CellHandle) ( (TableCellEditPart) target ).getModel( );
			int slotId = cellHandle.getContainer( )
					.getContainerSlotHandle( )
					.getSlotID( );
			if ( slotId == TableHandle.HEADER_SLOT
					|| slotId == TableHandle.FOOTER_SLOT
					|| slotId == TableHandle.GROUP_SLOT )
			{
				return DNDService.LOGIC_TRUE;
			}
			else
			{
				return DNDService.LOGIC_FALSE;
			}
		}
		return DNDService.LOGIC_UNKNOW;
	}

	public boolean performDrop( Object transfer, Object target, int operation,
			DNDLocation location )
	{
		if ( transfer instanceof Object[] )
		{

		}
		if ( transfer.equals( TEMPLATE ) && target instanceof TableCellEditPart )
		{
			//create data item, and pass it to AggregationDataBindingDialog
			//start transaction
			SessionHandleAdapter.getInstance( )
					.getCommandStack( )
					.startTrans( TRANS_NAME );

			DataItemHandle dataHandle = DesignElementFactory.getInstance( )
					.newDataItem( null );

			CellHandle cellHandle = (CellHandle) ( (TableCellEditPart) target ).getModel( );
			try
			{
				cellHandle.addElement( dataHandle, CellHandle.CONTENT_SLOT );

				DataColumnBindingDialog dialog = new DataColumnBindingDialog( true );
				dialog.setInput( dataHandle );
				dialog.setAggreate( true );

				if ( dialog.open( ) == Window.OK )
				{
					dataHandle.setResultSetColumn( dialog.getBindingColumn( )
							.getName( ) );
					SessionHandleAdapter.getInstance( )
							.getCommandStack( )
							.commit( );
				}
				else
				{
					SessionHandleAdapter.getInstance( )
							.getCommandStack( )
							.rollback( );
				}
			}
			catch ( Exception e )
			{
				SessionHandleAdapter.getInstance( )
						.getCommandStack( )
						.rollback( );
				ExceptionHandler.handle( e );
			}

		}
		return true;
	}

}
