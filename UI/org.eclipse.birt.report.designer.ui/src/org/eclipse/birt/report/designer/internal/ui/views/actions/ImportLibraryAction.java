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

import java.io.File;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.internal.ui.views.ILibraryProvider;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.IEditorPart;

/**
 * The action used to add library to a report design
 */

public class ImportLibraryAction extends Action
{

	public static final String ID = "UseLibraryAction"; //$NON-NLS-1$
	public static final String ACTION_TEXT = Messages.getString( "UseLibraryAction.Text" ); //$NON-NLS-1$

	public ImportLibraryAction( )
	{
		setText( ACTION_TEXT ); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.Action#isEnabled()
	 */
	public boolean isEnabled( )
	{
		// TODO Auto-generated method stub
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.action.IAction#run()
	 */
	public void run( )
	{
		FileDialog dialog = new FileDialog( UIUtil.getDefaultShell( ), SWT.OPEN );
		dialog.setFilterExtensions( new String[]{
			"*.rptlibrary" //$NON-NLS-1$
			} );
		String filename;
		try
		{
			filename = dialog.open( );
		}
		catch ( Throwable e )
		{
			return;
		}
		if ( filename != null )
		{
			if( !(new File(filename).exists()))
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "AddLibraryAction.Error.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( "AddLibraryAction.Error.FileNotFound", new String[]{filename} ) ); //$NON-NLS-1$
				return;
			}
			if ( !(filename.endsWith( ".rptlibrary") ))
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "AddLibraryAction.Error.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( "AddLibraryAction.Error.FileIsNotLibrary", new String[]{filename,".rptlibrary"} ) ); //$NON-NLS-1$
				return;
			}	
			if ( !isInExplorer( filename ) )
			{
				addToPreference( filename );
			}

			ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			try
			{
				UIUtil.includeLibrary( moduleHandle, filename );
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}

	private boolean isInExplorer( String fileName )
	{
		IEditorPart editor = UIUtil.getActiveEditor( true );
		ILibraryProvider provider = (ILibraryProvider) editor.getAdapter( ILibraryProvider.class );
		if ( provider != null )
		{
			LibraryHandle[] libraries = provider.getLibraries( );
			for ( int i = 0; i < libraries.length; i++ )
			{
				if ( libraries[i].getFileName( ).equals( fileName ) )
					return true;
			}
		}
		return false;
	}

	private void addToPreference( String fileName )
	{
		String[] libraries = ReportPlugin.getDefault( ).getLibraryPreference( );
		String[] newLibraries = new String[libraries.length + 1];
		System.arraycopy( libraries, 0, newLibraries, 0, libraries.length );
		newLibraries[libraries.length] = fileName;
		ReportPlugin.getDefault( ).setLibraryPreference( newLibraries );
	}

	// // copy from
	// //
	// org.eclipse.birt.report.designer.ui.lib.explorer.action.ImportLibraryAction
	// private static String getRelativedPath( String base, String child )
	// {
	// URI baseUri = new File( base ).getParentFile( ).toURI( );
	// URI childUri = new File( child ).toURI( );
	// return baseUri.relativize( childUri ).getPath( );
	// }
}
