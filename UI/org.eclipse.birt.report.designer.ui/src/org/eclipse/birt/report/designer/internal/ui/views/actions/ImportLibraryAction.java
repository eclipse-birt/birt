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
import java.net.URI;
import java.text.MessageFormat;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.dialogs.ImportLibraryDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

/**
 * The action used to add library to a report design
 */

public class ImportLibraryAction extends Action
{

	private static final String TEXT = Messages.getString( "ImportLibraryAction.Text" ); //$NON-NLS-1$
	private static final String MSG_DIALOG_TITLE = Messages.getString( "ImportLibraryAction.Title.ImportSuccessfully" ); //$NON-NLS-1$
	private static final String MSG_DIALOG_MSG = Messages.getString( "ImportLibraryAction.Message.ImportSuccessfully" ); //$NON-NLS-1$

	public ImportLibraryAction( )
	{
		setText( TEXT ); //$NON-NLS-1$
	}

	/* (non-Javadoc)
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
			String defaultName = new File( filename ).getName( ).split( "\\." )[0];
			ImportLibraryDialog importDialog = new ImportLibraryDialog( defaultName ); //$NON-NLS-1$
			if ( importDialog.open( ) == Dialog.OK )
			{
				ModuleHandle moduleHandle = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( );
				try
				{
					moduleHandle.includeLibrary( getRelativedPath(moduleHandle.getFileName(),filename),
									(String) importDialog.getResult( ) );
					ExceptionHandler.openMessageBox( MSG_DIALOG_TITLE,
							MessageFormat.format( MSG_DIALOG_MSG, new String[]{
								filename
							} ),
							SWT.ICON_INFORMATION );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		}
	}

	// copy from org.eclipse.birt.report.designer.ui.lib.explorer.action.ImportLibraryAction
	private static String getRelativedPath( String base, String child )
	{
		URI baseUri = new File( base ).getParentFile( ).toURI( );
		URI childUri = new File( child ).toURI( );
		return baseUri.relativize( childUri ).getPath();
	}
}
