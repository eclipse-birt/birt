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
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Copy action
 */
public class CopyAction extends AbstractViewAction
{

	private static final String DEFAULT_TEXT = Messages.getString( "CopyAction.text" ); //$NON-NLS-1$

	/**
	 * Create a new copy action with given selection and default text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 *  
	 */
	public CopyAction( Object selectedObject )
	{
		this( selectedObject, DEFAULT_TEXT );
	}

	/**
	 * Create a new copy action with given selection and text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * @param text
	 *            the text of the action
	 */
	public CopyAction( Object selectedObject, String text )
	{
		super( selectedObject, text );
		ISharedImages shareImages = PlatformUI.getWorkbench( )
				.getSharedImages( );
		setImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_COPY ) );
		setDisabledImageDescriptor( shareImages.getImageDescriptor( ISharedImages.IMG_TOOL_COPY_DISABLED ) );
		setAccelerator( SWT.CTRL | 'C' );
	}

	/**
	 * Runs this action. Copies the content. Each action implementation must
	 * define the steps needed to carry out this action. The default
	 * implementation of this method in <code>Action</code> does nothing.
	 */
	public void run( )
	{
		Object cloneElements = DNDUtil.cloneSource( getSelection( ) );
		Clipboard.getDefault( ).setContents( cloneElements );
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

}