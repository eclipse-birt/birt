/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.ui.lib.explorer.action;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Collection;

import org.eclipse.birt.report.designer.internal.ui.resourcelocator.ResourceEntry;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.lib.explorer.LibraryExplorerTreeViewPage;
import org.eclipse.birt.report.designer.ui.lib.explorer.dialog.MoveResourceDialog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.dialogs.SelectionDialog;

/**
 * The action class for moving resources in resource explorer.
 */
public class MoveResourceAction extends ResourceAction
{

	/**
	 * Constructs an action for moving resource.
	 * 
	 * @param page
	 *            the resource explorer page
	 */
	public MoveResourceAction( LibraryExplorerTreeViewPage page )
	{
		super( Messages.getString( "MoveLibraryAction.Text" ), page ); //$NON-NLS-1$
	}

	@Override
	public boolean isEnabled( )
	{
		return canModify( getSelectedResources( ) );
	}

	@Override
	public void run( )
	{
		Collection<File> files = null;

		try
		{
			files = getSelectedFiles( );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}

		if ( files == null || files.isEmpty( ) )
		{
			return;
		}

		SelectionDialog dialog = new MoveResourceDialog( );

		if ( dialog.open( ) == Window.OK )
		{
			Object[] selected = dialog.getResult( );

			if ( selected != null && selected.length == 1 )
			{
				ResourceEntry entry = (ResourceEntry) selected[0];
				IPath targetPath = null;

				try
				{
					targetPath = new Path( convertToFile( entry.getURL( ) ).getAbsolutePath( ) );
				}
				catch ( IOException e )
				{
					ExceptionHandler.handle( e );
				}

				for ( File file : files )
				{
					File srcFile = file;
					File targetFile = targetPath.append( file.getName( ) )
							.toFile( );

					if ( targetFile.exists( ) )
					{
						if ( !MessageDialog.openQuestion( getShell( ),
								Messages.getString( "MoveResourceAction.Dialog.Title" ), //$NON-NLS-1$
								Messages.getString( "MoveResourceAction.Dialog.Message" ) ) ) //$NON-NLS-1$
						{
							return;
						}
						
						try
						{
							new ProgressMonitorDialog( getShell( ) ).run( true,
									true,
									createDeleteRunnable( Arrays.asList( new File[]{
										targetFile
									} ) ) );
						}
						catch ( InvocationTargetException e )
						{
							ExceptionHandler.handle( e );
						}
						catch ( InterruptedException e )
						{
							ExceptionHandler.handle( e );
						}
					}
					if ( srcFile.renameTo( targetFile ) )
					{
						fireResourceChanged( targetFile.getAbsolutePath( ) );
					}
				}
			}
		}
	}
}
