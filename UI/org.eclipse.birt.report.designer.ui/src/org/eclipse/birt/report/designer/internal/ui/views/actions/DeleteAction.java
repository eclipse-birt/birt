/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.views.actions;

import java.util.Iterator;

import org.eclipse.birt.report.designer.internal.ui.views.IRequestConstants;
import org.eclipse.birt.report.designer.internal.ui.views.ProviderFactory;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.model.api.CellHandle;
import org.eclipse.birt.report.model.api.MasterPageHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.gef.Request;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * This class implements the delete action in the outline view
 * 
 *  
 */
public class DeleteAction extends AbstractElementAction
{

	/**
	 * the default text
	 */
	private static final String TEXT = Messages.getString( "DeleteAction.text" ); //$NON-NLS-1$

	/**
	 * Create a new delete action with given selection and default text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 *  
	 */
	public DeleteAction( Object selectedObject )
	{
		this( selectedObject, TEXT );
	}

	/**
	 * Create a new delete action with given selection and text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 */
	public DeleteAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
		ISharedImages shareImages = PlatformUI.getWorkbench( )
				.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE ) );
		setDisabledImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_DELETE_DISABLED ) );
		setAccelerator( SWT.DEL );
	}

	protected boolean doAction( ) throws Exception
	{
		if ( isEnabled( ) )
		{
			return ProviderFactory.createProvider( getSelection( ) )
					.performRequest( getSelection( ),
							new Request( IRequestConstants.REQUEST_TYPE_DELETE ) );
		}
		return false;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		Object obj = getSelection( );
		if ( obj instanceof IStructuredSelection )
		{
			for ( Iterator itor = ( (IStructuredSelection) obj ).iterator( ); itor.hasNext( ); )
			{
				if ( !canDelete( itor.next( ) ) )
				{
					return false;
				}
			}
			return true;
		}
		return canDelete( obj );
	}

	private boolean canDelete( Object obj )
	{
		return obj instanceof ReportElementHandle
				&& !( obj instanceof CellHandle )
				&& !( obj instanceof MasterPageHandle );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#getTransactionLabel()
	 */

	protected String getTransactionLabel( )
	{
		if ( getSelection( ) instanceof IStructuredSelection )
		{
			return Messages.getString( "DeleteAction.trans" ); //$NON-NLS-1$
		}
		return TEXT + " " + DEUtil.getDisplayLabel( getSelection( ) ); //$NON-NLS-1$
	}

}