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

package org.eclipse.birt.report.designer.ui.actions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.schematic.ListBandProxy;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.internal.ui.editors.parts.GraphicalEditorWithFlyoutPalette;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.LabelEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ListBandEditPart;
import org.eclipse.birt.report.designer.internal.ui.editors.schematic.editparts.ReportElementEditPart;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.internal.ui.views.actions.InsertAction;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.editors.ReportEditor;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CommandStack;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.ReportItemHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.Request;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;

/**
 * Abstract class for insert report element. Provides basic support for insert
 * actions.
 */

public abstract class BaseInsertMenuAction extends SelectionAction
{

	protected static final String STACK_MSG_INSERT_ELEMENT = Messages.getString( "BaseInsertMenuAction.stackMsg.insertElement" ); //$NON-NLS-1$

	private String insertType;

	protected SlotHandle slotHandle;

	private Object model;

	/**
	 * The constructor.
	 * 
	 * @param part
	 *            parent workbench part
	 * @param type
	 *            insert element type
	 */
	public BaseInsertMenuAction( IWorkbenchPart part, String type )
	{
		super( part );

		this.insertType = type;
	}

	private List getSupportList( SlotHandle handle )
	{
		return DEUtil.getElementSupportList( handle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.ui.actions.WorkbenchPartAction#calculateEnabled()
	 */
	protected boolean calculateEnabled( )
	{
		slotHandle = getDefaultSlotHandle( );

		if ( slotHandle == null )
		{
			return false;
		}

		List lst = getSupportList( slotHandle );

		return lst.contains( MetaDataDictionary.getInstance( )
				.getElement( insertType ) )
				&& DEUtil.handleValidateTargetCanContainMore( slotHandle, 0 );
	}

	/**
	 * Returns the container slotHandle.
	 * 
	 * @return
	 */
	private SlotHandle getDefaultSlotHandle( )
	{
		ISelection sels = getSelection( );

		if ( sels instanceof StructuredSelection )
		{
			StructuredSelection ssels = ( (StructuredSelection) sels );

			if ( ssels.size( ) > 0 )
			{
				Object obj = ssels.getFirstElement( );

				if ( obj instanceof ReportElementEditPart )
				{
					ReportElementEditPart part = (ReportElementEditPart) obj;

					if ( part instanceof ListBandEditPart )
					{
						ListBandProxy proxy = (ListBandProxy) part.getModel( );

						model = proxy;

						return proxy.getSlotHandle( );
					}
					model = part.getModel( );

					if ( model instanceof ReportElementModel )
					{
						return ( (ReportElementModel) model ).getSlotHandle( );
					}
					DesignElementHandle handle = (DesignElementHandle) model;

					int slotId = DEUtil.getDefaultSlotID( handle );
					if ( slotId != -1 )
					{
						return handle.getSlot( slotId );
					}

					return handle.getContainerSlotHandle( );
				}
			}
		}

		return null;
	}

	protected Request insertElement( ) throws Exception
	{
		Request request = new Request( IRequestConstants.REQUEST_TYPE_INSERT );
		Map extendsData = new HashMap( );
		extendsData.put( IRequestConstants.REQUEST_KEY_INSERT_SLOT, slotHandle );

		extendsData.put( IRequestConstants.REQUEST_KEY_INSERT_TYPE, insertType );

		extendsData.put( IRequestConstants.REQUEST_KEY_INSERT_POSITION,
				InsertAction.CURRENT );

		request.setExtendedData( extendsData );

		ProviderFactory.createProvider( model ).performRequest( model, request );

		return request;
	}

	protected void selectElement( final Object element, final boolean edit )
	{
		Display.getCurrent( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				if ( element instanceof ReportItemHandle )
				{
					IWorkbenchPart part = PlatformUI.getWorkbench( )
							.getActiveWorkbenchWindow( )
							.getPartService( )
							.getActivePart( );

					if ( part instanceof ReportEditor )
					{
						IEditorPart epart = ( (ReportEditor) part ).getActiveEditor( );

						if ( epart instanceof GraphicalEditorWithFlyoutPalette )
						{
							GraphicalViewer viewer = ( (GraphicalEditorWithFlyoutPalette) epart ).getGraphicalViewer( );
							Object cpart = viewer.getEditPartRegistry( )
									.get( element );

							if ( cpart instanceof EditPart )
							{
								viewer.flush( );
								viewer.select( (EditPart) cpart );
							}

							if ( edit && cpart instanceof LabelEditPart )
							{
								( (LabelEditPart) cpart ).performDirectEdit( );
							}
						}
					}
				}
			}
		} );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		CommandStack stack = SessionHandleAdapter.getInstance( )
				.getActivityStack( );
		stack.startTrans( STACK_MSG_INSERT_ELEMENT );

		try
		{
			final Request req = insertElement( );
			stack.commit( );

			selectElement( req.getExtendedData( )
					.get( IRequestConstants.REQUEST_KEY_RESULT ), true );
		}
		catch ( Exception e )
		{
			stack.rollbackAll( );
			ExceptionHandler.handle( e );
		}
	}
}