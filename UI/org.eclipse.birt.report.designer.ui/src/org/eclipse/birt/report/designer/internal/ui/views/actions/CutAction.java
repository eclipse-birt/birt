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

import org.eclipse.birt.report.designer.internal.ui.dnd.DNDUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.gef.dnd.TemplateTransfer;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Cut action
 */
public class CutAction extends AbstractElementAction
{

	private static final String TEXT = Messages.getString( "CutAction.text" ); //$NON-NLS-1$

	/**
	 * Create a new cut action with given selection and default text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 *  
	 */
	public CutAction( Object selectedObject )
	{
		this( selectedObject, TEXT );
	}

	/**
	 * Create a new cut action with given selection and text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 */
	public CutAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
		ISharedImages shareImages = PlatformUI.getWorkbench( )
				.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_CUT ) );
		setDisabledImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_CUT_DISABLED ) );
		setAccelerator( SWT.CTRL | 'X' );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		if ( DNDUtil.handleValidateDragInOutline( getSelection( ) ) )
			return super.isEnabled( );
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.AbstractElementAction#doAction()
	 */
	protected boolean doAction( ) throws Exception
	{
		Object cloneElements = DNDUtil.cloneSource( getSelection( ) );
		TemplateTransfer.getInstance( ).setTemplate( cloneElements );
		DNDUtil.dropSource( getSelection( ) );
		return true;
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
			return Messages.getString( "CutAction.trans" ); //$NON-NLS-1$
		}
		return TEXT + " " + DEUtil.getDisplayLabel( getSelection( ) ); //$NON-NLS-1$
	}
}