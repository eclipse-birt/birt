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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

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
			if ( !( new File( filename ).exists( ) ) )
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "AddLibraryAction.Error.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( "AddLibraryAction.Error.FileNotFound", new String[]{filename} ) ); //$NON-NLS-1$
				return;
			}
			if ( !( filename.endsWith( ".rptlibrary" ) ) )
			{
				ExceptionHandler.openErrorMessageBox( Messages.getString( "AddLibraryAction.Error.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( "AddLibraryAction.Error.FileIsNotLibrary", new String[]{filename, ".rptlibrary"} ) ); //$NON-NLS-1$
				return;
			}

			try
			{
				filename = copyToResourceFolder( filename );
				if ( filename != null )
				{
					ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( );
					UIUtil.includeLibrary( moduleHandle, filename );
				}
			}
			catch ( Exception e )
			{
				ExceptionHandler.handle( e );
			}
		}
	}

	private String copyToResourceFolder( String filename ) throws IOException
	{
		File orgFile = new File( filename );
		File resourceFolder = new File( ReportPlugin.getDefault( )
				.getResourceFolder( ) );
		if ( resourceFolder.exists( ) )
		{
			File targetFile = new File( resourceFolder, orgFile.getName( ) );
			if ( targetFile.exists( ) )
			{
				if(targetFile.getAbsolutePath( ).equals( orgFile.getAbsolutePath( ) ))
				{
					return orgFile.getAbsolutePath( );
				}
				if ( MessageDialog.openConfirm( UIUtil.getDefaultShell( ),
						Messages.getString( "UseLibraryAction.Error.Title" ), //$NON-NLS-1$
						Messages.getFormattedString( "UseLibraryAction.Error.Message", //$NON-NLS-1$
								new String[]{
									targetFile.getName( )
								} ) ) )
					coypFile( orgFile, targetFile );
			}else{
				coypFile( orgFile, targetFile );
			}
			return targetFile.getAbsolutePath( );
		}

		return null;
	}

	private void coypFile( File org, File dest ) throws IOException
	{
		if ( dest.exists( ) || dest.createNewFile( ) )
		{
			FileInputStream in = new FileInputStream( org );
			FileOutputStream out = new FileOutputStream( dest );
			byte[] bytes = new byte[64];
			int length = 0;
			while ( ( length = in.read( bytes ) ) != -1 )
			{
				out.write( bytes, 0, length );
			}
			in.close( );
			out.close( );
		}
	}

}
