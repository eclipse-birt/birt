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

package org.eclipse.birt.report.designer.ui.ide.navigator;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishTemplateWizard;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * 
 */

public class PublishTemplateNavigatorAction implements IViewActionDelegate
{

	protected ResourceNavigator navigator;

	/**
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init( IViewPart view )
	{
		if ( view instanceof ResourceNavigator )
		{
			navigator = (ResourceNavigator) view;
		}
	}

	/**
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		IFile file = getSelectedFile( );
		if ( file != null )
		{
			String url = file.getLocation( ).toOSString( );
			try
			{
				ModuleHandle handle = SessionHandleAdapter.getInstance( )
						.getSessionHandle( )
						.openDesign( url );

				if ( !( handle instanceof ReportDesignHandle ) )
				{
					action.setEnabled( false );
					return;
				}

				WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
						new PublishTemplateWizard( (ReportDesignHandle) handle ) );
				dialog.setPageSize( 500, 250 );
				dialog.open( );

				handle.close( );
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
				return;
			}
		}
		else
		{
			action.setEnabled( false );
		}
	}

	protected IFile getSelectedFile( )
	{
		if ( navigator != null )
		{
			IStructuredSelection selection = (IStructuredSelection) navigator.getTreeViewer( )
					.getSelection( );
			if ( selection.size( ) == 1
					&& selection.getFirstElement( ) instanceof IFile )
			{
				return (IFile) selection.getFirstElement( );
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{

	}

}
