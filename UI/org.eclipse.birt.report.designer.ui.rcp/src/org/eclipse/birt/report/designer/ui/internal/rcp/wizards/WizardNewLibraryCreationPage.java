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

package org.eclipse.birt.report.designer.ui.internal.rcp.wizards;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.editors.MultiPageReportEditor;
import org.eclipse.birt.report.designer.ui.editors.ReportEditorInput;
import org.eclipse.birt.report.designer.ui.wizards.INewLibraryCreationPage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

/**
 * Wizard Page for RCP New Library Wizard.
 */

public class WizardNewLibraryCreationPage extends WizardPage implements
		INewLibraryCreationPage
{

	private static final String MSG_DUPLICATE_FILE_NAME = Messages.getString( "WizardNewReportCreationPage.msg.duplicate.fileName" ); //$NON-NLS-1$

	private static final String MSG_EMPTY_FILE_LOCATION_DIRECTORY = Messages.getString( "WizardNewReportCreationPage.msg.empty.file.locationDirectory" ); //$NON-NLS-1$

	private static final String MSG_EMPTY_FILE_NAME = Messages.getString( "WizardNewReportCreationPage.msg.empty.file.name" ); //$NON-NLS-1$

	private static final String CREATING = Messages.getString( "NewReportWizard.text.Creating" ); //$NON-NLS-1$

	private static final String OPENING_FILE_FOR_EDITING = Messages.getString( "NewReportWizard.text.OpenFileForEditing" ); //$NON-NLS-1$

	private static final String SUFFIX = ".rptlibrary"; //$NON-NLS-1$

	private static final String TEMPLATE_FILE = "/templates/blank_library.rptlibrary"; //$NON-NLS-1$

	private Listener locationModifyListener = new Listener( ) {

		public void handleEvent( Event e )
		{
			setPageComplete( validatePage( ) );
		}
	};

	NewReportPageSupport pageSupport = null;

	/**
	 * The Constructor.
	 * 
	 * @param pageName
	 */
	public WizardNewLibraryCreationPage( String pageName )
	{
		super( pageName );
		pageSupport = new NewReportPageSupport( );
	}

	public void createControl( Composite parent )
	{
		initializeDialogUnits( parent );
		setControl( pageSupport.createComposite( parent ) );

		pageSupport.getFileNameField( ).addListener( SWT.Modify,
				locationModifyListener );
		pageSupport.getLocationPathField( ).addListener( SWT.Modify,
				locationModifyListener );

		setPageComplete( validatePage( ) );
		setErrorMessage( null );
		setMessage( null );
	}

	public void setVisible( boolean visible )
	{
		getControl( ).setVisible( visible );
		if ( visible )
		{
			pageSupport.getFileNameField( ).setFocus( );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.dialogs.WizardNewFileCreationPage#validatePage()
	 */
	public boolean validatePage( )
	{
		if ( getFileName( ).equals( "" ) )//$NON-NLS-1$
		{
			setErrorMessage( null );
			setMessage( MSG_EMPTY_FILE_NAME );
			return false;
		}

		String location = getContainerFullPath( ).toOSString( );

		if ( location.equals( "" ) ) //$NON-NLS-1$
		{
			setErrorMessage( null );
			setMessage( MSG_EMPTY_FILE_LOCATION_DIRECTORY );
			return false;
		}

		IPath path;

		if ( !getFileName( ).endsWith( IReportEditorContants.LIBRARY_FILE_EXTENTION ) )
		{
			path = getContainerFullPath( ).append( getFileName( )
					+ IReportEditorContants.LIBRARY_FILE_EXTENTION );
		}
		else
		{
			path = getContainerFullPath( ).append( getFileName( ) );
		}

		if ( path.toFile( ).exists( ) )
		{
			setErrorMessage( MSG_DUPLICATE_FILE_NAME );
			return false;
		}

		setErrorMessage( null );
		setMessage( null );
		return true;
	}

	public void setContainerFullPath( IPath initPath )
	{
		pageSupport.setInitialFileLocation( initPath.toOSString( ) );
	}

	public void setFileName( String initFileName )
	{
		pageSupport.setInitialFileName( initFileName );
	}

	public String getFileName( )
	{
		return pageSupport.getFileName( );
	}

	public IPath getContainerFullPath( )
	{
		return pageSupport.getFileLocationFullPath( );
	}

	public boolean performFinish( )
	{
		final IPath locPath = getContainerFullPath( );
		String fn = getFileName( );

		final String fileName;
		if ( !fn.endsWith( SUFFIX ) ) //$NON-NLS-1$
		{
			fileName = fn + SUFFIX; //$NON-NLS-1$
		}
		else
		{
			fileName = fn;
		}
		InputStream inputData = null;

		URL url = Platform.find( Platform.getBundle( ReportPlugin.REPORT_UI ),
				new Path( TEMPLATE_FILE ) );
		if ( url != null )
		{
			try
			{
				inputData = url.openStream( );
			}
			catch ( IOException e1 )
			{
				// ignore.
			}
		}

		final InputStream stream = inputData;
		IRunnableWithProgress op = new IRunnableWithProgress( ) {

			public void run( IProgressMonitor monitor )
			{
				try
				{
					doFinish( locPath, fileName, stream, monitor );
				}
				finally
				{
					monitor.done( );
				}
			}
		};
		try
		{
			getContainer( ).run( true, false, op );
		}
		catch ( InterruptedException e )
		{
			return false;
		}
		catch ( InvocationTargetException e )
		{
			Throwable realException = e.getTargetException( );
			ExceptionHandler.handle( realException );
			return false;
		}
		return true;
	}

	private void doFinish( IPath locationPath, String fileName,
			InputStream stream, IProgressMonitor monitor )
	{
		// create a sample file
		monitor.beginTask( CREATING + fileName, 2 );

		final File file = new File( locationPath.toString( ), fileName );
		try
		{
			File container = new File( locationPath.toString( ) );
			if ( !container.exists( ) )
			{
				container.mkdirs( );
			}
			FileOutputStream out = new FileOutputStream( file );
			byte[] buff = new byte[stream.available( )];
			stream.read( buff );
			out.write( buff );
			out.close( );
			stream.close( );
		}
		catch ( IOException e )
		{
			ExceptionHandler.handle( e );
		}

		monitor.worked( 1 );
		monitor.setTaskName( OPENING_FILE_FOR_EDITING );
		getShell( ).getDisplay( ).asyncExec( new Runnable( ) {

			public void run( )
			{
				IWorkbench workbench = PlatformUI.getWorkbench( );
				IWorkbenchWindow window = workbench.getActiveWorkbenchWindow( );

				IWorkbenchPage page = window.getActivePage( );
				try
				{
					page.openEditor( new ReportEditorInput( file ),
							IReportEditorContants.LIBRARY_EDITOR_ID,
							true );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		} );

		monitor.worked( 1 );

	}

}
