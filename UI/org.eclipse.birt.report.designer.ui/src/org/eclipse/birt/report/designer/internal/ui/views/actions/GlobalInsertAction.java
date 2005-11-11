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

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.processor.ElementProcessorFactory;
import org.eclipse.birt.report.designer.internal.ui.processor.IElementProcessor;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.ui.actions.GeneralInsertMenuAction;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.DesignEngine;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.RowHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.api.elements.ReportDesignConstants;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.StructuredSelection;

/**
 * 
 */

public class GlobalInsertAction extends AbstractGlobalSelectionAction
{

	private String dataType;

	/**
	 * @param provider
	 * @param id
	 */
	protected GlobalInsertAction( ISelectionProvider provider, String id,
			String dataType )
	{
		super( provider, id );
		this.dataType = dataType;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		if ( libraryInsertConfict( ) )
		{
			return false;
		}

		SlotHandle container = getContainer( );
		if ( container != null )
		{
			return container.getElementHandle( )
					.canContain( container.getSlotID( ), dataType );
		}

		return false;
	}

	private boolean libraryInsertConfict( )
	{
		int index = -1;
		if ( SessionHandleAdapter.getInstance( ).getReportDesignHandle( ) instanceof ReportDesignHandle
				&& ( (StructuredSelection) getSelection( ) ).toArray( ).length > 0 )
		{
			for ( int i = 0; i < ( (StructuredSelection) getSelection( ) ).toArray( ).length; i++ )
			{
				if ( ( (StructuredSelection) getSelection( ) ).toArray( )[i] instanceof ReportElementModel )
				{
					index = i;
					break;
				}
			}

		}
		if ( index >= 0 )
		{
			if ( ( (ReportElementModel) ( ( (StructuredSelection) getSelection( ) ).toArray( )[index] ) ).getElementHandle( ) instanceof LibraryHandle )
			{
				return true;
			}
		}

		return false;
	}

	private SlotHandle getContainer( )
	{
		SlotHandle container = null;
		if ( getSelectedObjects( ).size( ) == 1 )
		{
			Object selected = getSelectedObjects( ).get( 0 );

			if ( selected instanceof SlotHandle )
			{
				container = (SlotHandle) selected;
			}
			else if ( selected instanceof ReportElementModel )
			{
				container = ( (ReportElementModel) selected ).getSlotHandle( );
			}
			else if ( selected instanceof DesignElementHandle )
			{
				int slotId = DEUtil.getDefaultSlotID( selected );
				if ( slotId != -1 )
				{
					container = ( (DesignElementHandle) selected ).getSlot( slotId );
				}
			}
		}
		return container;
	}

	public void run( )
	{
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getCommandStack( );
		IElementProcessor processor = ElementProcessorFactory.createProcessor( dataType );
		stack.startTrans( processor.getCreateTransactionLabel( ) );
		DesignElementHandle handle = processor.createElement( null );
		if ( handle == null )
		{
			stack.rollback( );
		}
		else
		{
			try
			{
				getContainer( ).add( handle );
			}
			catch ( Exception e )
			{
				stack.rollback( );
				ExceptionHandler.handle( e );
			}
			stack.commit( );
			synWithMediator( handle );
		}
		super.run( );
	}

	private void synWithMediator( DesignElementHandle handle )
	{
		List list = new ArrayList( );

		list.add( handle );
		ReportRequest r = new ReportRequest( );
		r.setType( ReportRequest.CREATE_ELEMENT );

		r.setSelectionObject( list );
		SessionHandleAdapter.getInstance( ).getMediator( ).notifyRequest( r );
	}
}
