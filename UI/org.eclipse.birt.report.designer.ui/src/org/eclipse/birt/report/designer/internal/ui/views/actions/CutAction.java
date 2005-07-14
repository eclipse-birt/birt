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

import org.eclipse.birt.report.designer.internal.ui.util.Policy;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.util.DEUtil;
import org.eclipse.birt.report.designer.util.DNDUtil;
import org.eclipse.gef.ui.actions.Clipboard;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

/**
 * Cut action
 */
public class CutAction extends AbstractViewAction
{

	private static final String DEFAULT_TEXT = Messages.getString( "CutAction.text" ); //$NON-NLS-1$

	/**
	 * Create a new cut action with given selection and default text
	 * 
	 * @param selectedObject
	 *            the selected object,which cannot be null
	 * 
	 */
	public CutAction( Object selectedObject )
	{
		this( selectedObject, DEFAULT_TEXT );
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
		return DNDUtil.handleValidateDragInOutline( getSelection( ) )
				&& createDeleteAction( getSelection( ) ).isEnabled( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#run()
	 */
	public void run( )
	{
		if ( Policy.TRACING_ACTIONS )
		{
			System.out.println( "Cut action >> Cut " + getSelection( ) ); //$NON-NLS-1$
		}
		Object cloneElements = DNDUtil.cloneSource( getSelection( ) );
		DeleteAction action = createDeleteAction( getSelection( ) );
		action.run( );
		if ( action.hasExecuted( ) )
		{
			Clipboard.getDefault( ).setContents( cloneElements );
		}
	}

	protected DeleteAction createDeleteAction( final Object objects )
	{
		return new DeleteAction( objects ) {

			/*
			 * (non-Javadoc)
			 * 
			 * @see org.eclipse.birt.report.designer.internal.ui.views.actions.DeleteAction#getTransactionLabel()
			 */
			protected String getTransactionLabel( )
			{
				if ( objects instanceof IStructuredSelection )
				{
					return Messages.getString( "CutAction.trans" ); //$NON-NLS-1$
				}
				return DEFAULT_TEXT + " " + DEUtil.getDisplayLabel( objects ); //$NON-NLS-1$
			}
		};
	}
}