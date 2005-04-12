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

import org.eclipse.birt.report.designer.internal.ui.views.RenameInlineTool;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.GroupHandle;
import org.eclipse.birt.report.model.api.ReportElementHandle;
import org.eclipse.birt.report.model.api.metadata.MetaDataConstants;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;

/**
 * This class represents the rename action
 */

public class RenameAction extends AbstractViewerAction
{

	/**
	 * the default text
	 */
	public static final String TEXT = Messages.getString( "RenameAction.text" ); //$NON-NLS-1$

	private RenameInlineTool tool = null;

	/**
	 * Create a new rename action under the specific viewer
	 * 
	 * @param sourceViewer
	 *            the source viewer
	 *  
	 */
	public RenameAction( TreeViewer sourceViewer )
	{
		this( sourceViewer, TEXT );
	}

	/**
	 * Create a new rename action under the specific viewer with the given text
	 * 
	 * @param sourceViewer
	 *            the source viewer
	 * @param text
	 *            the text of the action
	 */
	public RenameAction( TreeViewer sourceViewer, String text )
	{
		super( sourceViewer, text );
		setAccelerator( SWT.F2 );
		if ( isEnabled( ) )
		{
			tool = new RenameInlineTool( getSelectedItems( )[0] );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#isEnabled()
	 */
	public boolean isEnabled( )
	{
		if ( getSelectedObjects( ).size( ) != 1 )
		{//multiple selection or no selection
			return false;
		}
		Object obj = super.getSelectedObjects( ).getFirstElement( );
		if ( obj instanceof ReportElementHandle )
		{
			if ( obj instanceof GroupHandle )
			{
				return true;
			}
			return ( (ReportElementHandle) obj ).getDefn( ).getNameOption( ) != MetaDataConstants.NO_NAME;
		}
		//No report element selected
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		if ( tool != null )
		{
			tool.doRename( );
		}
	}
}