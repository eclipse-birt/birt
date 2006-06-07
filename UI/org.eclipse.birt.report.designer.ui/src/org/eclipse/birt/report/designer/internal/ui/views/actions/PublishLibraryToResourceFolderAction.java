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
import org.eclipse.birt.report.designer.internal.ui.wizards.PublishLibraryWizard;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.LibraryHandle;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.ui.IEditorPart;

public class PublishLibraryToResourceFolderAction extends AbstractViewAction
{

	public static final String ACTION_TEXT = Messages.getString( "PublishLibraryToResourceFolderAction.Action.Text" ); //$NON-NLS-1$

	private String filePath;
	private String fileName;
	private String folderName;

	/**
	 * @param selectedObject
	 */
	public PublishLibraryToResourceFolderAction( Object selectedObject )
	{
		super( selectedObject, ACTION_TEXT );
	}

	/**
	 * @param selectedObject
	 * @param text
	 */
	public PublishLibraryToResourceFolderAction( Object selectedObject,
			String text )
	{
		super( selectedObject, text );
	}

	public boolean isEnable( )
	{
		Object selectObj = getSelection();
		if( selectObj instanceof LibraryHandle)
		{
			return true;
		}else
		{
			return false;
		}
	}

	public void run( )
	{

		if ( isEnable( ) == false )
		{
			return;
		}

		String filePath = SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( )
				.getFileName( );
		String fileName = filePath.substring( filePath.lastIndexOf( File.separator ) + 1 );

		PublishLibraryWizard publishLibrary = new PublishLibraryWizard( (LibraryHandle) SessionHandleAdapter.getInstance( )
				.getReportDesignHandle( ),
				fileName,
				ReportPlugin.getDefault( ).getResourcePreference( ) );

		WizardDialog dialog = new WizardDialog( UIUtil.getDefaultShell( ),
				publishLibrary );

		dialog.setPageSize( 500, 250 );
		dialog.open( );

	}


}
