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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.core.model.views.outline.ReportElementModel;
import org.eclipse.birt.report.designer.core.util.mediator.request.ReportRequest;
import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.DesignElementHandle;
import org.eclipse.birt.report.model.api.SlotHandle;
import org.eclipse.gef.Request;

/**
 * Insert action
 */

public class InsertAction extends AbstractElementAction
{

	public final static String ID = "org.eclipse.birt.report.designer.ui.views.action.InsertAction";//$NON-NLS-1$	

	public final static String ABOVE = "above"; //$NON-NLS-1$

	public final static String BELOW = "below"; //$NON-NLS-1$

	public final static String CURRENT = "current"; //$NON-NLS-1$

	private final static String TEXT = Messages.getString( "InsertAction.text" ); //$NON-NLS-1$

	private SlotHandle slotHandle;

	private String position;

	private String type = null;

	/**
	 * Create a new insert action with given selection and text at specified
	 * position
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 *  
	 */
	public InsertAction( Object selectedObject )
	{
		this( selectedObject, TEXT );
	}

	/**
	 * Create a new insert action with given selection and text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 */
	public InsertAction( Object selectedObject, String text )
	{
		this( selectedObject, text, null );
	}

	/**
	 * Create a new insert action with given selection and text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 */
	public InsertAction( Object selectedObject, String text, String type )
	{
		this( selectedObject, text, type, CURRENT );
	}

	/**
	 * Create a new insert action with given selection and text at specified
	 * position
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 * @param type
	 *            the type of the element to insert
	 * @param pos
	 *            the insert position
	 */
	public InsertAction( Object selectedObject, String text, String type,
			String pos )
	{
		this( selectedObject, text, null, type, pos );
	}

	public InsertAction( Object selectedObject, String text,
			SlotHandle slotHandle, String type, String pos )
	{
		super( selectedObject, text );
		this.type = type;
		this.position = pos;
		if ( slotHandle != null )
		{
			this.slotHandle = slotHandle;
		}
		else
		{
			this.slotHandle = getDefaultSlotHandle( );
		}
	}

	/**
	 * Gets the default slot handle to insert
	 * 
	 * @return Returns the default slot handle to insert
	 */
	private SlotHandle getDefaultSlotHandle( )
	{
		Object obj = getSelection( );
		if ( obj instanceof ReportElementModel )
		{
			return ( (ReportElementModel) obj ).getSlotHandle( );
		}
		DesignElementHandle handle = (DesignElementHandle) obj;
		if ( position == CURRENT )
		{
			int slotId = DEUtil.getDefaultSlotID( handle );
			if ( slotId != -1 )
			{
				return handle.getSlot( slotId );
			}
		}
		return handle.getContainerSlotHandle( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#doAction()
	 */
	protected boolean doAction( ) throws Exception
	{
		Request request = new Request( IRequestConstants.REQUEST_TYPE_INSERT );
		Map extendsData = new HashMap( );
		extendsData.put( IRequestConstants.REQUEST_KEY_INSERT_SLOT, slotHandle );

		if ( type != null )
		{
			extendsData.put( IRequestConstants.REQUEST_KEY_INSERT_TYPE, type );
		}
		extendsData.put( IRequestConstants.REQUEST_KEY_INSERT_POSITION,
				position );
		request.setExtendedData( extendsData );
		boolean bool =  ProviderFactory.createProvider( getSelection( ) )
				.performRequest( getSelection( ), request );
		if (bool)
		{	
			List list = new ArrayList();

			list.add(request.getExtendedData( ).get( IRequestConstants.REQUEST_KEY_RESULT ));
			ReportRequest r = new ReportRequest();
			r.setType(ReportRequest.CREATE_ELEMENT);
			
			r.setSelectionObject(list);
			SessionHandleAdapter.getInstance().getMediator().notifyRequest(r);
			
		}
		return bool;
	}
}