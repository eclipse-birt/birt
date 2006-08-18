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
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.views.navigator.ResourceNavigator;

/**
 * 
 */

public class PublishLibraryAction implements IWorkbenchWindowActionDelegate
{

	private ResourceNavigator navigator;

	public void dispose( )
	{
		// TODO Auto-generated method stub

	}

	public void init( IWorkbenchWindow window )
	{
		// TODO Auto-generated method stub
		IViewPart view = UIUtil.getView( "org.eclipse.ui.views.ResourceNavigator" );
		navigator = (ResourceNavigator) view;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run( IAction action )
	{
		String fileName = null;
		LibraryHandle libHandle = null;
		if ( isEnable( ) == false )
		{
			return;
		}

		if ( editLibrary( ) )
		{
			String filePath = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( )
					.getFileName( );
			fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );
			libHandle = (LibraryHandle) SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
		}
		else if ( selectLibrary( ) )
		{
			IFile file = getSelectedFile( navigator );
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
				if ( filePath != null && filePath.length( ) != 0 )
				{
					fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );
				}
				libHandle = (LibraryHandle) handle;
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

		PublishLibraryWizard publishLibrary = new PublishLibraryWizard( libHandle,
				fileName,
				ReportPlugin.getDefault( ).getResourcePreference( ) );

		WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
				publishLibrary );

		dialog.setPageSize( 500, 250 );
		dialog.open( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IActionDelegate#selectionChanged(org.eclipse.jface.action.IAction,
	 *      org.eclipse.jface.viewers.ISelection)
	 */
	public void selectionChanged( IAction action, ISelection selection )
	{
		action.setEnabled( isEnable( ) ); //$NON-NLS-1$

	}

	private boolean isEnable( )
	{
		return ( selectLibrary( ) || editLibrary( ) );

	}

	private boolean selectLibrary( )
	{
		IFile file = getSelectedFile( navigator );
		if ( file == null )
		{
			return false;
		}

		if ( file.getFileExtension( ).equals( "rptlibrary" ) )
			return true;
		return false;
	}

	private boolean editLibrary( )
	{
		IEditorPart editor = UIUtil.getActiveEditor( true );
		if ( editor != null )
		{
			return ( editor.getEditorInput( ).getName( ).endsWith( ".rptlibrary" ) ); //$NON-NLS-1$
		}
		return false;
	}

	/**
	 * Get selected file.
	 * 
	 * @return IFile Selected file
	 */
	private IFile getSelectedFile( ResourceNavigator navigator )
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
