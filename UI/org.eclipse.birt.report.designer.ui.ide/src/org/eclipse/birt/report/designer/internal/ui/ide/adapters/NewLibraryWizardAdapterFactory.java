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

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URL;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.IHelpContextIds;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.wizards.INewLibraryCreationPage;
import org.eclipse.birt.report.designer.ui.wizards.NewLibraryWizard;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.command.LibraryChangeEvent;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdapterFactory;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.WizardNewFileCreationPage;
import org.eclipse.ui.ide.IDE;

/**
 * Add INewLibraryCreationPage adaptable to NewLibraryWizard.
 */

public class NewLibraryWizardAdapterFactory implements IAdapterFactory
{

	public Object getAdapter( Object adaptableObject, Class adapterType )
	{
		NewLibraryWizard wizard = (NewLibraryWizard) adaptableObject;

		return new NewLibraryCreationPage( "", wizard.getSelection( ) ); //$NON-NLS-1$
	}

	public Class[] getAdapterList( )
	{
		return new Class[]{INewLibraryCreationPage.class};
	}

}

class NewLibraryCreationPage extends WizardNewFileCreationPage
		implements
			INewLibraryCreationPage
{

	private static final String OPENING_FILE_FOR_EDITING = Messages
			.getString( "NewLibraryWizard.text.OpenFileForEditing" ); //$NON-NLS-1$
	private static final String CREATING = Messages
			.getString( "NewLibraryWizard.text.Creating" ); //$NON-NLS-1$

	// private static final String NEW_REPORT_FILE_NAME_PREFIX =
	// Messages.getString(
	// "NewLibraryWizard.displayName.NewReportFileNamePrefix" ); //$NON-NLS-1$
	// private static final String NEW_REPORT_FILE_EXTENSION =
	// Messages.getString( "NewLibraryWizard.displayName.NewReportFileExtension"
	// ); //$NON-NLS-1$
	// private static final String NEW_REPORT_FILE_NAME =
	// NEW_REPORT_FILE_NAME_PREFIX
	// + NEW_REPORT_FILE_EXTENSION;
	// private static final String CREATE_A_NEW_REPORT = Messages.getString(
	// "NewLibraryWizard.text.CreateReport" ); //$NON-NLS-1$
	// private static final String REPORT = Messages.getString(
	// "NewLibraryWizard.title.Report" ); //$NON-NLS-1$
	// private static final String WIZARDPAGE = Messages.getString(
	// "NewLibraryWizard.title.WizardPage" ); //$NON-NLS-1$
	// private static final String NEW = Messages.getString(
	// "NewLibraryWizard.title.New" ); //$NON-NLS-1$

	/**
	 * (non-Javadoc) Method declared on IDialogPage.
	 */
	public void createControl( Composite parent )
	{
		super.createControl( parent );
		UIUtil.bindHelp( getControl( ), IHelpContextIds.NEW_LIBRARY_WIZARD_ID );
	}

	public NewLibraryCreationPage( String pageName,
			IStructuredSelection selection )
	{
		super( pageName, selection );
	}

	public boolean performFinish( )
	{
		final IPath containerName = getContainerFullPath( );
		String fn = getFileName( );
		final String fileName;
		if ( !fn.endsWith( ".rptlibrary" ) ) //$NON-NLS-1$
		{
			fileName = fn + ".rptlibrary"; //$NON-NLS-1$
		}
		else
		{
			fileName = fn;
		}
		InputStream streamFromPage = null;
		URL url = Platform.find( Platform.getBundle( ReportPlugin.REPORT_UI ),
				new Path( "/templates/blank_library.rptlibrary" ) );
		if ( url != null )
		{
			try
			{
				streamFromPage = url.openStream( );
			}
			catch ( IOException e1 )
			{
				// ignore.
			}
		}
		final InputStream stream = streamFromPage;
		IRunnableWithProgress op = new IRunnableWithProgress( ) {

			public void run( IProgressMonitor monitor )
					throws InvocationTargetException
			{
				try
				{
					doFinish( containerName, fileName, stream, monitor );
				}
				catch ( CoreException e )
				{
					throw new InvocationTargetException( e );
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

	private void doFinish( IPath containerName, String fileName,
			InputStream stream, IProgressMonitor monitor ) throws CoreException
	{
		// create a sample file
		monitor.beginTask( CREATING + fileName, 2 );
		IResource resource = (IContainer) ResourcesPlugin.getWorkspace( )
				.getRoot( ).findMember( containerName );
		IContainer container = null;
		if ( resource == null || !resource.exists( )
				|| !( resource instanceof IContainer ) )
		{
			// create folder if not exist
			IFolder folder = createFolderHandle( containerName );
			UIUtil.createFolder( folder, monitor );
			container = folder;
		}
		else
		{
			container = (IContainer) resource;
		}
		final IFile file = container.getFile( new Path( fileName ) );
		try
		{
			if ( file.exists( ) )
			{
				file.setContents( stream, true, true, monitor );
			}
			else
			{
				file.create( stream, true, monitor );
			}
			stream.close( );
		}
		catch ( Exception e )
		{
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
					IEditorPart editorPart = IDE.openEditor( page, file, true );
					ModuleHandle model = SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( );
					if ( ReportPlugin.getDefault( )
							.getEnableCommentPreference( ) )
					{
						model.setStringProperty( ModuleHandle.COMMENTS_PROP,
								ReportPlugin.getDefault( )
										.getCommentPreference( ) );
						model.save( );
						editorPart.doSave( null );
					}
					// page.openEditor( new FileEditorInput( file ),
					// LibraryReportEditor.EDITOR_ID,
					// true );
				}
				catch ( Exception e )
				{
					ExceptionHandler.handle( e );
				}
			}
		} );

		monitor.worked( 1 );

		fireLibraryChanged( fileName );
	}

	private void fireLibraryChanged( String fileName )
	{
		SessionHandleAdapter.getInstance( ).getSessionHandle( )
				.fireResourceChange( new LibraryChangeEvent( fileName ) );
	}

	protected IFolder createFolderHandle( IPath folderPath )
	{
		IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace( )
				.getRoot( );
		return workspaceRoot.getFolder( folderPath );
	}
}
