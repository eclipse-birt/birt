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

package org.eclipse.birt.report.designer.ui.samples.ide.action;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Enumeration;

import org.eclipse.birt.report.designer.internal.ui.util.ExceptionHandler;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.birt.report.designer.ui.samplesview.action.IOpenSampleReportAction;
import org.eclipse.birt.report.designer.ui.samplesview.sampleslocator.SampleIncludedSourceEntry;
import org.eclipse.birt.report.designer.ui.samplesview.util.PlaceResources;
import org.eclipse.birt.report.designer.ui.samplesview.view.ReportExamples;
import org.eclipse.birt.report.model.api.ReportDesignHandle;
import org.eclipse.core.resources.ICommand;
import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceStatus;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

public class IDEOpenSampleReportAction extends Action implements
		IOpenSampleReportAction
{

	private static final String ACTION_TEXT = Messages.getString( "SampleReportsView.Action.openSampleReport" );

	private static final String SCRIPTING_CATEGORY = "Scripted Data Source";

	private static final String EXTENDING_CATEGORY = "Extending BIRT";

	private static final String DRILL_TO_DETAILS_CATEGORY = "Drill to Details";

	private static final String LIBRARIES_CATEGORY = "Libraries";

	private static final String XML_DATA_SOURCE_CATEGORY = "XML Data Source";

	private static final String[] EXTENDING_PLUGIN_PATTERN = new String[]{
		"*.zip" //$NON-NLS-1$
	};

	private ReportExamples composite;

	private IProject reportProject;

	public IDEOpenSampleReportAction( )
	{
		super( ACTION_TEXT );
		setToolTipText( Messages.getString( "SampleReportsView.Action.openSampleReport.toolTipText.ide" ) );
	}

	public void setMainComposite( ReportExamples composite )
	{
		this.composite = composite;
	}

	public void run( )
	{
		TreeItem item = (TreeItem) composite.getSelectedElement( );
		Object selectedElement = item.getData( );
		if ( selectedElement == null
				|| !( selectedElement instanceof ReportDesignHandle ) )
		{
			return;
		}

		/*
		 * 1.Create a report project
		 */
		reportProject = createProject( item.getText( ).substring( 0,
				item.getText( ).lastIndexOf( "." ) ), false );
		/*
		 * 2.Place the sample report into project folder
		 */
		if ( reportProject != null )
		{
			PlaceResources.copy( composite.getShell( ),
					reportProject.getLocation( ).toOSString( ),
					item.getText( ),
					( (ReportDesignHandle) selectedElement ).getFileName( ) );
		}

		if ( item.getParentItem( )
				.getText( )
				.equals( DRILL_TO_DETAILS_CATEGORY ) )
		{
			PlaceResources.copyDrillThroughReport( composite.getShell( ),
					reportProject.getLocation( ).toOSString( ),
					item.getText( ) );
		}

		/*
		 * Copy the inluded libraries if selecting sample report demostrate XML
		 * Data Source
		 */
		if ( item.getParentItem( ).getText( ).equals( XML_DATA_SOURCE_CATEGORY ) )
		{
			PlaceResources.copyIncludedPng( composite.getShell( ),
					reportProject.getLocation( ).toOSString( ) );
		}

		/*
		 * Copy the inluded png image if selecting sample report demostrate XML
		 * report library feature
		 */
		if ( item.getParentItem( ).getText( ).equals( LIBRARIES_CATEGORY ) )
		{
			PlaceResources.copyIncludedLibraries( composite.getShell( ),
					reportProject.getLocation( ).toOSString( ) );
		}

		/*
		 * Create a Eclipse Java project if selecting scripted data source
		 * sample
		 */
		if ( item.getParentItem( ).getText( ).equals( SCRIPTING_CATEGORY ) )
		{
			IProject javaProject = createProject( SCRIPTING_CATEGORY, true );
			if ( javaProject != null )
			{
				createSourceAndOutputFolder( javaProject );
				try
				{
					setClasspath( javaProject );
				}
				catch ( JavaModelException e )
				{
					ExceptionHandler.handle( e );
				}
				catch ( CoreException e )
				{
					ExceptionHandler.handle( e );
				}
			}

			Enumeration enumeration = SampleIncludedSourceEntry.getJavaObjects( );
			while ( enumeration.hasMoreElements( ) )
			{
				URL javaObjectURL = (URL) enumeration.nextElement( );
				String filename = javaObjectURL.getFile( );
				String desFileName = filename.substring( filename.lastIndexOf( '/' ) + 1 );

				PlaceResources.copy( composite.getShell( ),
						javaProject.getFolder( "src" )
								.getLocation( )
								.toOSString( ),
						desFileName,
						javaObjectURL );
			}
			refreshReportProject( javaProject );
		}

		/*
		 * 3.Refresh the report project
		 */
		refreshReportProject( reportProject );

		/*
		 * Copy the plug-in zip if selecting extending BIRT sample
		 */
		if ( item.getParentItem( ).getParentItem( ) != null
				&& item.getParentItem( )
						.getParentItem( )
						.getText( )
						.equals( EXTENDING_CATEGORY ) )
		{
			PlaceExtendedPlugin( item.getParentItem( ).getText( ) );
		}
	}

	private void PlaceExtendedPlugin( String categoryName )
	{
		Enumeration enumeration = SampleIncludedSourceEntry.getExtendedPlugin( categoryName );
		URL pluginURL = (URL) enumeration.nextElement( );
		String filename = pluginURL.getFile( );
		String pluginName = filename.substring( filename.lastIndexOf( '/' ) + 1 );

		final FileDialog saveDialog = new FileDialog( composite.getShell( ),
				SWT.SAVE );
		saveDialog.setFilterExtensions( EXTENDING_PLUGIN_PATTERN ); //$NON-NLS-1$
		saveDialog.setFileName( pluginName );
		if ( saveDialog.open( ) == null )
			return;

		PlaceResources.copy( composite.getShell( ),
				saveDialog.getFilterPath( ),
				saveDialog.getFileName( ),
				pluginURL );
	}

	private IProject createProject( String projectName, boolean isJavaProject )
	{
		final IProject projectHandle = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getProject( projectName );

		if ( projectHandle.exists( ) )
		{
			return projectHandle;
		}

		final IProjectDescription description = ResourcesPlugin.getWorkspace( )
				.newProjectDescription( projectHandle.getName( ) );

		if ( isJavaProject == true )
		{
			String[] natures = new String[]{
				JavaCore.NATURE_ID
			};
			description.setNatureIds( natures );
			addJavaBuildSpec( description );
		}
		else
		{
			String[] natures = new String[]{
				"org.eclipse.birt.report.designer.ui.reportprojectnature"
			};
			description.setNatureIds( natures );
		}

		// create the new project operation
		WorkspaceModifyOperation op = new WorkspaceModifyOperation( ) {

			protected void execute( IProgressMonitor monitor )
					throws CoreException
			{
				create( description, projectHandle, monitor );
			}
		};

		try
		{
			new ProgressMonitorDialog( composite.getShell( ) ).run( false,
					true,
					op );
		}
		catch ( InterruptedException e )
		{
			ExceptionHandler.handle( e );
			return null;
		}
		catch ( InvocationTargetException e )
		{
			// ie.- one of the steps resulted in a core exception
			Throwable t = e.getTargetException( );
			if ( t instanceof CoreException )
			{
				if ( ( (CoreException) t ).getStatus( ).getCode( ) == IResourceStatus.CASE_VARIANT_EXISTS )
				{
					MessageDialog.openError( composite.getShell( ),
							Messages.getString( "NewReportProjectWizard.errorMessage" ), //$NON-NLS-1$
							Messages.getFormattedString( "NewReportProjectWizard.caseVariantExistsError", new String[]{projectHandle.getName( )} ) //$NON-NLS-1$,
					);
				}
				else
				{
					ErrorDialog.openError( composite.getShell( ),
							Messages.getString( "NewReportProjectWizard.errorMessage" ), //$NON-NLS-1$
							null, // no special message
							( (CoreException) t ).getStatus( ) );
				}
			}
			else
			{
				// CoreExceptions are handled above, but unexpected runtime
				// exceptions and errors may still occur.
				ExceptionHandler.handle( e );

				MessageDialog.openError( composite.getShell( ),
						Messages.getString( "NewReportProjectWizard.errorMessage" ), //$NON-NLS-1$
						Messages.getFormattedString( "NewReportProjectWizard.internalError", new Object[]{t.getMessage( )} ) ); //$NON-NLS-1$
			}
			return null;
		}

		return projectHandle;
	}

	private void create( IProjectDescription description,
			IProject projectHandle, IProgressMonitor monitor )
			throws CoreException, OperationCanceledException
	{
		try
		{
			monitor.beginTask( "", 2000 );//$NON-NLS-1$
			projectHandle.create( description, new SubProgressMonitor( monitor,
					1000 ) );
			if ( monitor.isCanceled( ) )
				throw new OperationCanceledException( );
			projectHandle.open( new SubProgressMonitor( monitor, 1000 ) );

		}
		finally
		{
			monitor.done( );
		}
	}

	private void refreshReportProject( final IProject project )
	{
		WorkspaceModifyOperation op = new WorkspaceModifyOperation( ) {

			protected void execute( IProgressMonitor monitor )
					throws CoreException
			{
				project.refreshLocal( IResource.DEPTH_INFINITE, monitor );
			}
		};

		try
		{
			new ProgressMonitorDialog( composite.getShell( ) ).run( false,
					true,
					op );
		}
		catch ( Exception e )
		{
			ExceptionHandler.handle( e );
		}
	}

	private void addJavaBuildSpec( IProjectDescription description )
	{
		ICommand command = description.newCommand( );
		command.setBuilderName( JavaCore.BUILDER_ID );
		description.setBuildSpec( new ICommand[]{
			command
		} );
	}

	private void createSourceAndOutputFolder( IProject project )
	{

		IFolder srcFolder = project.getFolder( "src" );
		if ( !srcFolder.exists( ) )
			try
			{
				createFolder( srcFolder );
			}
			catch ( CoreException e )
			{
				ExceptionHandler.handle( e );
			}

		IFolder outputFolder = project.getFolder( "bin" );
		if ( !outputFolder.exists( ) )
			try
			{
				createFolder( outputFolder );
			}
			catch ( CoreException e )
			{
				ExceptionHandler.handle( e );
			}

	}

	private void createFolder( IFolder folder ) throws CoreException
	{
		if ( !folder.exists( ) )
		{
			IContainer parent = folder.getParent( );
			if ( parent instanceof IFolder )
			{
				createFolder( (IFolder) parent );
			}
			folder.create( true, true, null );
		}
	}

	private void setClasspath( IProject project ) throws JavaModelException,
			CoreException
	{
		IJavaProject javaProject = JavaCore.create( project );

		IPath path = project.getFullPath( ).append( "bin" );
		javaProject.setOutputLocation( path, null );

		IClasspathEntry[] entries = getClassPathEntries( project );
		javaProject.setRawClasspath( entries, null );
	}

	private IClasspathEntry[] getClassPathEntries( IProject project )
	{
		IClasspathEntry[] internalClassPathEntries = getInternalClassPathEntries( project );
		IClasspathEntry[] entries = new IClasspathEntry[internalClassPathEntries.length + 1];
		System.arraycopy( internalClassPathEntries,
				0,
				entries,
				0,
				internalClassPathEntries.length );
		entries[entries.length - 1] = JavaCore.newContainerEntry( new Path( "org.eclipse.jdt.launching.JRE_CONTAINER" ) );
		return entries;
	}

	protected IClasspathEntry[] getInternalClassPathEntries( IProject project )
	{
		IClasspathEntry[] entries = new IClasspathEntry[1];
		IPath path = project.getFullPath( ).append( "src" );
		entries[0] = JavaCore.newSourceEntry( path );
		return entries;
	}
}
