/*************************************************************************************
 * Copyright (c) 2004 Actuate Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.designer.ui.ide.wizards.SaveReportAsWizard;
import org.eclipse.birt.report.designer.ui.ide.wizards.SaveReportAsWizardDialog;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.window.Window;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IStorageEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * IDE ReportProvider This ReportProvider uses IFileEditorInput as report editor
 * input class.
 */
public class IDEFileReportProvider implements IReportProvider
{

	private ModuleHandle model = null;
	private static final String VERSION_MESSAGE = Messages
			.getString( "TextPropertyDescriptor.Message.Version" ); //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#connect(org.eclipse.birt.report.model.api.ModuleHandle)
	 */
	public void connect( ModuleHandle model )
	{
		this.model = model;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getReportModuleHandle(java.lang.Object)
	 */
	public ModuleHandle getReportModuleHandle( Object element )
	{
		return getReportModuleHandle( element, false );

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#saveReport(org.eclipse.birt.report.model.api.ModuleHandle,
	 *      java.lang.Object, org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void saveReport( ModuleHandle moduleHandle, Object element,
			IProgressMonitor monitor )
	{
		if ( element instanceof IFileEditorInput )
		{
			IFileEditorInput input = (IFileEditorInput) element;
			saveFile( moduleHandle, input.getFile( ), monitor );
		}

	}

	/**
	 * @param moduleHandle
	 * @param file
	 * @param monitor
	 */
	private void saveFile( final ModuleHandle moduleHandle, final IFile file,
			IProgressMonitor monitor )
	{
		// TODO
		IRunnableWithProgress op = new IRunnableWithProgress( ) {

			public synchronized final void run( IProgressMonitor monitor )
					throws InvocationTargetException, InterruptedException
			{
				try
				{
					IWorkspaceRunnable workspaceRunnable = new IWorkspaceRunnable( ) {

						public void run( IProgressMonitor pm )
								throws CoreException
						{
							try
							{
								execute( pm );
							}
							catch ( CoreException e )
							{
								throw e;
							}
							catch ( IOException e )
							{
								ExceptionHandler.handle( e );
							}
						}
					};

					ResourcesPlugin.getWorkspace( ).run( workspaceRunnable,
							ResourcesPlugin.getWorkspace( ).getRoot( ),
							IResource.NONE, monitor );
				}
				catch ( CoreException e )
				{
					throw new InvocationTargetException( e );
				}
				catch ( OperationCanceledException e )
				{
					throw new InterruptedException( e.getMessage( ) );
				}
			}

			public void execute( final IProgressMonitor monitor )
					throws CoreException, IOException
			{

				ByteArrayOutputStream out = new ByteArrayOutputStream( );
				moduleHandle.serialize( out );
				byte[] bytes = out.toByteArray( );
				out.close( );

				ByteArrayInputStream is = new ByteArrayInputStream( bytes );

				IContainer container = file.getParent( );
				if ( !container.exists( ) && container instanceof IFolder )
				{
					UIUtil.createFolder( (IFolder) container, monitor );
				}

				if ( file.exists( ) )
				{
					file.setContents( is, true, true, monitor );
				}
				else
				{
					// Save to new file.
					file.create( is, true, monitor );
				}
			}
		};

		try
		{
			new ProgressMonitorDialog( UIUtil.getDefaultShell( ) ).run( false,
					true, op );
		}

		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}

		try
		{
			file.refreshLocal( 0, monitor );
		}
		catch ( CoreException e )
		{
			ExceptionHandler.handle( e );
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getSaveAsPath(java.lang.Object)
	 */
	public IPath getSaveAsPath( Object element )
	{
		if ( element instanceof IFileEditorInput )
		{
			IFileEditorInput input = (IFileEditorInput) element;

			SaveReportAsWizardDialog dialog = new SaveReportAsWizardDialog(
					UIUtil.getDefaultShell( ), new SaveReportAsWizard(
							(ModuleHandle) model, input.getFile( ) ) );
			if ( dialog.open( ) == Window.OK )
			{
				return dialog.getResult( );
			}
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#createNewEditorInput(org.eclipse.core.runtime.IPath)
	 */
	public IEditorInput createNewEditorInput( IPath path )
	{
		return new FileEditorInput( ResourcesPlugin.getWorkspace( ).getRoot( )
				.getFile( path ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getInputPath(org.eclipse.ui.IEditorInput)
	 */
	public IPath getInputPath( IEditorInput input )
	{
		if ( input instanceof FileEditorInput )
		{
			return ( (FileEditorInput) input ).getPath( );
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getReportDocumentProvider(java.lang.Object)
	 */
	public IDocumentProvider getReportDocumentProvider( Object element )
	{
		return new ReportDocumentProvider( );
	}

	public ModuleHandle getReportModuleHandle( Object element, boolean reset )
	{
		if ( model == null || reset )
		{
			if ( element instanceof IStorageEditorInput )
			{
				IEditorInput input = (IEditorInput) element;
				String fileName = input.getName( );
				int blankIndex;
				if ( ( blankIndex = fileName.lastIndexOf( " " ) ) > 0 ) //$NON-NLS-1$
				{
					fileName = fileName.substring( 0, blankIndex );
				}

				if ( element instanceof IFileEditorInput )
				{
					fileName = ( (IFileEditorInput) element ).getFile( )
							.getLocation( ).toOSString( );
				}
				InputStream stream;
				try
				{
					stream = ( (IStorageEditorInput) element ).getStorage( )
							.getContents( );

					Map properties = new HashMap( );
					properties.put( IModuleModel.CREATED_BY_PROP, MessageFormat
							.format( VERSION_MESSAGE, new String[]{
									ReportPlugin.getVersion( ),
									ReportPlugin.getBuildInfo( )} ) );

					model = SessionHandleAdapter.getInstance( ).init( fileName,
							stream ,properties);
				}
				catch ( CoreException e )
				{
					// TODO throw exception
					// ExceptionHandler.handle( e );
				}
				catch ( DesignFileException e )
				{
					// ExceptionHandler.handle( e );
				}
			}
		}

		return model;
	}

}
