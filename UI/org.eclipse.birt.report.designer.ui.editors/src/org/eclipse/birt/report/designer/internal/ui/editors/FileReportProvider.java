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
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.designer.core.model.SessionHandleAdapter;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.SaveReportAsWizard;
import org.eclipse.birt.report.designer.internal.ui.editors.wizards.SaveReportAsWizardDialog;
import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.internal.ui.util.UIUtil;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.designer.ui.editors.IReportProvider;
import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.IModuleOption;
import org.eclipse.birt.report.model.api.ModuleHandle;
import org.eclipse.birt.report.model.api.core.IModuleModel;
import org.eclipse.core.resources.IFile;
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
	protected static Logger logger = Logger.getLogger( FileReportProvider.class.getName( ) );

	private ModuleHandle model;
	private static final String VERSION_MESSAGE = Messages.getString( "TextPropertyDescriptor.Message.Version" ); //$NON-NLS-1$

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

		if ( ( model == null || reset ) && element instanceof IPathEditorInput )
		{
			IPath path = ( (IPathEditorInput) element ).getPath( );

			if ( path != null )
			{
				String fileName = path.toOSString( );
				try
				{
					InputStream stream = new FileInputStream( path.toFile( ) );

					Map properties = new HashMap( );

					String designerVersion = MessageFormat.format( VERSION_MESSAGE,
							new String[]{
									ReportPlugin.getVersion( ),
									ReportPlugin.getBuildInfo( )
							} );
					properties.put( IModuleModel.CREATED_BY_PROP,
							designerVersion );
					properties.put( IModuleOption.CREATED_BY_KEY,
							designerVersion );
					String projectFolder = getProjectFolder( (IPathEditorInput) element );
					if ( projectFolder != null )
					{
						properties.put( IModuleOption.RESOURCE_FOLDER_KEY,
								projectFolder );
					}
					model = SessionHandleAdapter.getInstance( ).init( fileName,
							stream,
							properties );
				}
				catch ( DesignFileException e )
				{
					logger.log(Level.SEVERE, e.getMessage(),e);
				}
				catch ( FileNotFoundException e )
				{
					logger.log(Level.SEVERE, e.getMessage(),e);
				}
			}
		}
		return model;
	}

	private String getProjectFolder( IEditorInput input )
	{
		Object fileAdapter = input.getAdapter( IFile.class );
		IFile file = null;
		if ( fileAdapter != null )
			file = (IFile) fileAdapter;
		if ( file != null && file.getProject( ) != null )
		{
			return file.getProject( ).getLocation( ).toOSString( );
		}
		if ( input instanceof IPathEditorInput )
		{
			File fileSystemFile = ( (IPathEditorInput) input ).getPath( )
					.toFile( );
			return fileSystemFile.getParent( );
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

			SaveReportAsWizardDialog dialog = new SaveReportAsWizardDialog( UIUtil.getDefaultShell( ),
					new SaveReportAsWizard( (ModuleHandle) model, input ) );
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.designer.ui.editors.IReportProvider#connect(org.eclipse.birt.report.model.api.ModuleHandle)
	 */
	public void connect( ModuleHandle handle )
	{
		model = handle;
	}

}
