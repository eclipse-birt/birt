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

import java.io.File;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.core.resources.IFile;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * Publish library to resouce folder, add this action to navigator This action
 * is enabled when library is selected.
 */

public class PublishLibraryNavigatorAction implements IViewActionDelegate
{

	protected ResourceNavigator navigator;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IViewActionDelegate#init(org.eclipse.ui.IViewPart)
	 */
	public void init( IViewPart view )
	{
		// TODO Auto-generated method stub
		if ( view instanceof ResourceNavigator )
		{
			navigator = (ResourceNavigator) view;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		// TODO Auto-generated method stub

		IFile file = getSelectedFile( );
		if ( file == null )
		{
			return;
		}

		String url = file.getLocation( ).toOSString( );
		ModuleHandle handle = null;
		try
		{
			handle = SessionHandleAdapter.getInstance( )
					.getSessionHandle( )
					.openLibrary( url );

			if ( !( handle instanceof LibraryHandle ) )
			{
				action.setEnabled( false );
				return;
			}

			String filePath = handle.getFileName( );
			String fileName = null;
			if ( filePath != null && filePath.length( ) != 0 )
			{
				fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );
			}

			PublishLibraryWizard publishLibrary = new PublishLibraryWizard( (LibraryHandle) handle,
					fileName,
					ReportPlugin.getDefault( ).getResourceFolder( ) );

			WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
					publishLibrary );

			dialog.setPageSize( 500, 250 );
			dialog.open( );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
			return;
		}
		finally
		{
			if ( handle != null )
			{
				handle.close( );
			}
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Get selected file.
	 * 
	 * @return IFile Selected file
	 */
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

}
