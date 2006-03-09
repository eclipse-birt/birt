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

package org.eclipse.birt.report.designer.internal.ui.editors;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.SaveReportAsWizard;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.SaveReportAsWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.ui.editors.IReportEditorContants;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
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
import org.eclipse.ui.IPathEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;

/**
 * Report provider for file system report input.
 */
public class FileReportProvider implements IReportProvider
{

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
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getReportModuleHandle(java.lang.Object,
	 *      boolean)
	 */
	public ModuleHandle getReportModuleHandle( Object element, boolean reset )
	{
		
		if ( element instanceof IPathEditorInput )
		{
			IPath path = ( (IPathEditorInput) element ).getPath( );

			if ( path != null )
			{
				String fileName = path.toOSString( );
				try
				{
					InputStream stream = new FileInputStream( path.toFile( ) );
					if ( fileName.endsWith( IReportEditorContants.LIBRARY_FILE_EXTENTION ) )
					{
						if ( reset )
						{
							SessionHandleAdapter.getInstance( ).init( fileName,
									stream,
									SessionHandleAdapter.LIBRARYFILE,
									true );
						}
						else
						{
							SessionHandleAdapter.getInstance( ).init( fileName,
									stream,
									SessionHandleAdapter.LIBRARYFILE );
						}
					}
					else
					{
						if ( reset )
						{
							SessionHandleAdapter.getInstance( ).init( fileName,
									stream,
									true );
						}
						else
						{
							SessionHandleAdapter.getInstance( ).init( fileName,
									stream );
						}
					}
					return SessionHandleAdapter.getInstance( )
							.getReportDesignHandle( );
				}
				catch ( DesignFileException e )
				{
					// ExceptionHandler.handle( e );
				}
				catch ( FileNotFoundException e )
				{
					e.printStackTrace();
				}
			}
		}
		return null;
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
		if ( element instanceof IPathEditorInput )
		{
			IPathEditorInput input = (IPathEditorInput) element;
			saveFile( moduleHandle, input.getPath( ).toFile( ), monitor );
		}

	}

	private void saveFile( final ModuleHandle moduleHandle, final File file,
			IProgressMonitor monitor )
	{
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
							IResource.NONE,
							monitor );
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
				if ( file.exists( ) || file.createNewFile( ) )
				{
					FileOutputStream out = new FileOutputStream( file );
					moduleHandle.serialize( out );
					out.close( );
				}
			}
		};

		try
		{
			new ProgressMonitorDialog( UIUtil.getDefaultShell( ) ).run( false,
					true,
					op );
		}

		catch ( Exception e )
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
		if ( element instanceof IPathEditorInput )
		{
			IEditorInput input = (IEditorInput) element;
			ModuleHandle handle = SessionHandleAdapter.getInstance( )
					.getReportDesignHandle( );
			SaveReportAsWizardDialog dialog = new SaveReportAsWizardDialog( UIUtil.getDefaultShell( ),
					new SaveReportAsWizard( (ReportDesignHandle) handle, input ) );
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
		File file = new File( path.toOSString( ) );
		try
		{
			if ( file.exists( ) || file.createNewFile( ) )
			{
				return new ReportEditorInput( file );
			}
		}
		catch ( IOException e )
		{
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#getInputPath(org.eclipse.ui.IEditorInput)
	 */
	public IPath getInputPath( IEditorInput input )
	{
		if ( input instanceof IPathEditorInput )
		{
			return ( (IPathEditorInput) input ).getPath( );
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
		return new FileReportDocumentProvider( );
	}

}
