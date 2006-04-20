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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;


/**
 * 
 */

public class PublishLibraryAction implements
		IWorkbenchWindowActionDelegate
{

	public void dispose( )
	{
		// TODO Auto-generated method stub

	}

	public void init( IWorkbenchWindow window )
	{
		// TODO Auto-generated method stub

	}

	public void run( IAction action )
	{
		if(isEnable() == true)
		{
			publishiLibrary();
		}

	}

	private boolean publishiLibrary()
	{
		// copy to library folder
		String libraryFolderPath = ReportPlugin.getDefault( )
				.getResourcePreference( );

		String filePath = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getFileName( );
		String fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );
		File targetFolder = new File( libraryFolderPath );
		if ( !targetFolder.isDirectory( ) )
		{
			ExceptionHandler.openErrorMessageBox( Messages.getString( "PublishLibraryAction.wizard.errorTitle" ), //$NON-NLS-1$
					Messages.getString( "PublishLibraryAction.wizard.notvalidfolder" ) ); //$NON-NLS-1$
			return true;
		}
		if ( !targetFolder.exists( ) )
		{
			targetFolder.mkdirs( );
		}
		File targetFile = new File( targetFolder, fileName );
		if ( new File( filePath ).compareTo( targetFile ) == 0 )
		{
			ExceptionHandler.openErrorMessageBox( Messages.getString( "PublishLibraryAction.wizard.errorTitle" ), //$NON-NLS-1$
					Messages.getString( "PublishLibraryAction.wizard.message" ) ); //$NON-NLS-1$
			return true;
		}

		int overwrite = Window.OK;
		try
		{
			if ( targetFile.exists( ) )
			{
				String[] buttons = new String[]{
						IDialogConstants.YES_LABEL,
						IDialogConstants.NO_LABEL,
						IDialogConstants.CANCEL_LABEL
				};
				String question = Messages.getFormattedString( "SaveAsDialog.overwriteQuestion", //$NON-NLS-1$
						new Object[]{
							targetFile.getAbsolutePath( )
						} );
				MessageDialog d = new MessageDialog(UIUtil.getDefaultShell( ),
						Messages.getString( "SaveAsDialog.Question" ), //$NON-NLS-1$
						null,
						question,
						MessageDialog.QUESTION,
						buttons,
						0 );
				overwrite = d.open( );
			}
			if ( overwrite == Window.OK
					&& ( targetFile.exists( ) || ( !targetFile.exists( ) && targetFile.createNewFile( ) ) ) )
			{
				copyFile( filePath, targetFile );
			}
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}

		return overwrite != 1;	
	}
	
	public void selectionChanged( IAction action, ISelection selection )
	{
		action.setEnabled( isEnable( ) ); //$NON-NLS-1$

	}

	private boolean isEnable( )
	{
		IEditorPart editor = UIUtil.getActiveEditor( true );
		if ( editor != null )
		{
			return ( editor.getEditorInput( ).getName( ).endsWith( ".rptlibrary" ) ); //$NON-NLS-1$
		}
		return false;

	}
	
	private void copyFile( String in, File targetFile ) throws IOException
	{
		FileInputStream fis = new FileInputStream( in );
		FileOutputStream fos = new FileOutputStream( targetFile );
		byte[] buf = new byte[1024];
		int i = 0;
		while ( ( i = fis.read( buf ) ) != -1 )
		{
			fos.write( buf, 0, i );
		}
		fis.close( );
		fos.close( );
	}
	
}
