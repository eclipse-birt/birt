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

package org.eclipse.birt.report.debug.ui.launcher;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.eclipse.birt.report.debug.internal.ui.launcher.IReportLauncherSettings;
import org.eclipse.birt.report.debug.internal.ui.launcher.util.DebugUtil;
import org.eclipse.birt.report.debug.internal.ui.launcher.util.ReportLauncherUtils;
import org.eclipse.birt.report.debug.internal.ui.launcher.util.WorkspaceClassPathFinder;
import org.eclipse.birt.report.designer.nls.Messages;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.LaunchConfigurationDelegate;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.internal.launching.JavaSourceLookupDirector;
import org.eclipse.jdt.launching.ExecutionArguments;
import org.eclipse.jdt.launching.IVMInstall;
import org.eclipse.jdt.launching.VMRunnerConfiguration;
import org.eclipse.jdt.launching.sourcelookup.containers.JavaProjectSourceContainer;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.ExternalModelManager;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.launcher.LauncherUtils;

/**
 * add comment here
 * 
 */

public class ReportLaunchConfigurationDelegate extends
		LaunchConfigurationDelegate implements IReportLauncherSettings
{

	/**
	 * It is roperty key.
	 */
	private static final String PROJECT_NAMES_KEY = "user.projectname"; //$NON-NLS-1$

	private static final String PROJECT_CLASSPATH_KEY = "user.projectclasspath"; //$NON-NLS-1$

	private static WorkspaceClassPathFinder finder = new WorkspaceClassPathFinder( );

	public ReportLaunchConfigurationDelegate( )
	{
		fConfigDir = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.debug.core.ILaunch,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void launch( ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor ) throws CoreException
	{
		try
		{
			fConfigDir = null;
			monitor.beginTask( "", 5 ); //$NON-NLS-1$
			String workspace = configuration.getAttribute( "location0", //$NON-NLS-1$
					LauncherUtils.getDefaultPath( )
							.append( WORKESPACENAME )
							.toOSString( ) );
			if ( !LauncherUtils.clearWorkspace( configuration,
					workspace,
					new SubProgressMonitor( monitor, 1 ) ) )
			{
				monitor.setCanceled( true );
				return;
			}
			if ( configuration.getAttribute( "clearConfig", false ) ) //$NON-NLS-1$
				LauncherUtils.clearConfigArea( getConfigDir( configuration ),
						new SubProgressMonitor( monitor, 1 ) );
			launch.setAttribute( "configLocation", getConfigDir( configuration ) //$NON-NLS-1$
					.toString( ) );
			IVMInstall launcher = LauncherUtils.createLauncher( configuration );
			monitor.worked( 1 );
			VMRunnerConfiguration runnerConfig = createVMRunner( configuration );
			cofigLaunch( launch, configuration );
			if ( runnerConfig == null )
			{
				monitor.setCanceled( true );
				return;
			}
			monitor.worked( 1 );
			LauncherUtils.setDefaultSourceLocator( configuration, launch );
			DebugUtil.getPDEPluginLaunchListener( PDEPlugin.getDefault( ) )
					.manage( launch );
			launcher.getVMRunner( mode ).run( runnerConfig, launch, monitor );
			monitor.worked( 1 );

		}
		catch ( CoreException e )
		{
			monitor.setCanceled( true );
			throw e;
		}
	}

	/**
	 * @param launch
	 */
	private void cofigLaunch( ILaunch launch, ILaunchConfiguration configuration )
	{
		if ( launch.getSourceLocator( ) instanceof JavaSourceLookupDirector )
		{
			JavaSourceLookupDirector director = (JavaSourceLookupDirector) launch.getSourceLocator( );
			ISourceContainer[] contains = director.getSourceContainers( );
			List list = new ArrayList( );

			if ( contains != null && contains.length != 0 )
			{
				for ( int i = 0; i < contains.length; i++ )
				{
					list.add( contains[i] );
				}
			}

			try
			{
				List sourcePaths = getAllProjectSourcePaths( configuration.getAttribute( IMPORTPROJECTNAMES,
						"" ) ); //$NON-NLS-1$
				for ( int i = 0; i < sourcePaths.size( ); i++ )
				{
					// String source = ( String ) sourcePaths.get( i );
					// ISourceContainer temp = new DirectorySourceContainer(
					// new Path( source ), true );
					// list.add( temp );

					IJavaProject source = (IJavaProject) sourcePaths.get( i );
					ISourceContainer temp = new JavaProjectSourceContainer( source );
					list.add( i, temp );
				}
			}
			catch ( CoreException e )
			{

			}

			ISourceContainer[] retValue = new ISourceContainer[list.size( )];
			retValue = (ISourceContainer[]) list.toArray( retValue );
			director.setSourceContainers( retValue );
		}
	}

	/**
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
	private VMRunnerConfiguration createVMRunner(
			ILaunchConfiguration configuration ) throws CoreException
	{
		String classpath[] = LauncherUtils.constructClasspath( configuration );
		if ( classpath == null )
		{
			String message = Messages.getString( "ReportLaunchConfigurationDelegate.noStartup" ); //$NON-NLS-1$
			throw new CoreException( LauncherUtils.createErrorStatus( message ) );
		}
		String programArgs[] = getProgramArguments( configuration );
		if ( programArgs == null )
		{
			return null;
		}
		else
		{
			String envp[] = DebugPlugin.getDefault( )
					.getLaunchManager( )
					.getEnvironment( configuration );
			VMRunnerConfiguration runnerConfig = new VMRunnerConfiguration( "org.eclipse.core.launcher.Main", classpath ); //$NON-NLS-1$
			runnerConfig.setVMArguments( getVMArguments( configuration ) );
			runnerConfig.setProgramArguments( programArgs );
			runnerConfig.setEnvironment( envp );
			return runnerConfig;
		}
	}

	private String[] getProgramArguments( ILaunchConfiguration configuration )
			throws CoreException
	{
		ArrayList programArgs = new ArrayList( );

		// If a product is specified, then add it to the program args
		if ( configuration.getAttribute( USE_PRODUCT, false ) )
		{
			programArgs.add( "-product" ); //$NON-NLS-1$
			programArgs.add( configuration.getAttribute( PRODUCT, "" ) ); //$NON-NLS-1$
		}
		else
		{
			// specify the application to launch
			programArgs.add( "-application" ); //$NON-NLS-1$
			programArgs.add( configuration.getAttribute( APPLICATION,
					LauncherUtils.getDefaultApplicationName( ) ) );
		}

		// specify the workspace location for the runtime workbench
		String targetWorkspace = configuration.getAttribute( LOCATION + "0", LauncherUtils.getDefaultPath( ).append( WORKESPACENAME ).toOSString( ) ); //$NON-NLS-1$ //$NON-NLS-2$
		programArgs.add( "-data" ); //$NON-NLS-1$
		programArgs.add( targetWorkspace );

		boolean isOSGI = PDECore.getDefault( )
				.getModelManager( )
				.isOSGiRuntime( );
		if ( configuration.getAttribute( USEFEATURES, false ) )
		{
			validateFeatures( );
			IPath installPath = PDEPlugin.getWorkspace( )
					.getRoot( )
					.getLocation( );
			programArgs.add( "-install" ); //$NON-NLS-1$
			programArgs.add( "file:" + installPath.removeLastSegments( 1 ).addTrailingSeparator( ).toString( ) ); //$NON-NLS-1$
			programArgs.add( "-update" ); //$NON-NLS-1$
		}
		else
		{
			TreeMap pluginMap = LauncherUtils.getPluginsToRun( configuration );
			if ( pluginMap == null )
				return null;

			String primaryFeatureId = ReportLauncherUtils.getPrimaryFeatureId( );
			DebugUtil.runCreatePlatformConfigurationArea( pluginMap,
					getConfigDir( configuration ),
					primaryFeatureId,
					ReportLauncherUtils.getAutoStartPlugins( configuration ) );
			programArgs.add( "-configuration" ); //$NON-NLS-1$
			if ( isOSGI )
				programArgs.add( "file:" + new Path( getConfigDir( configuration ).getPath( ) ).addTrailingSeparator( ).toString( ) ); //$NON-NLS-1$
			else
				programArgs.add( "file:" + new Path( getConfigDir( configuration ).getPath( ) ).append( "platform.cfg" ).toString( ) ); //$NON-NLS-1$ //$NON-NLS-2$

			if ( !isOSGI )
			{
				if ( primaryFeatureId != null )
				{
					programArgs.add( "-feature" ); //$NON-NLS-1$
					programArgs.add( primaryFeatureId );
				}
				IPluginModelBase bootModel = (IPluginModelBase) pluginMap.get( "org.eclipse.core.boot" ); //$NON-NLS-1$
				String bootPath = LauncherUtils.getBootPath( bootModel );
				if ( bootPath != null && !bootPath.endsWith( ".jar" ) ) { //$NON-NLS-1$
					programArgs.add( "-boot" ); //$NON-NLS-1$
					programArgs.add( "file:" + bootPath ); //$NON-NLS-1$
				}
			}
		}

		// add the output folder names
		programArgs.add( "-dev" ); //$NON-NLS-1$
		if ( PDECore.getDefault( ).getModelManager( ).isOSGiRuntime( ) )
			programArgs.add( DebugUtil.getDevEntriesProperties( getConfigDir( configuration ).toString( )
					+ "/dev.properties", true ) ); //$NON-NLS-1$
		else
			programArgs.add( DebugUtil.getDevEntries( true ) );

		// necessary for PDE to know how to load plugins when target platform =
		// host platform
		// see PluginPathFinder.getPluginPaths()
		programArgs.add( "-pdelaunch" ); //$NON-NLS-1$

		// add tracing, if turned on
		if ( configuration.getAttribute( TRACING, false )
				&& !TRACING_NONE.equals( configuration.getAttribute( TRACING_CHECKED,
						(String) null ) ) )
		{
			programArgs.add( "-debug" ); //$NON-NLS-1$
			programArgs.add( LauncherUtils.getTracingFileArgument( configuration,
					getConfigDir( configuration ).toString( )
							+ Path.SEPARATOR
							+ ".options" ) ); //$NON-NLS-1$
		}

		// add the program args specified by the user
		StringTokenizer tokenizer = new StringTokenizer( configuration.getAttribute( PROGARGS,
				"" ) ); //$NON-NLS-1$
		while ( tokenizer.hasMoreTokens( ) )
		{
			programArgs.add( tokenizer.nextToken( ) );
		}

		// show splash only if we are launching the default application
		boolean showSplash = true;
		int index = programArgs.indexOf( "-application" ); //$NON-NLS-1$
		if ( index != -1 && index <= programArgs.size( ) - 2 )
		{
			if ( !programArgs.get( index + 1 )
					.equals( LauncherUtils.getDefaultApplicationName( ) ) )
			{
				showSplash = false;
			}
		}
		if ( showSplash && !programArgs.contains( "-nosplash" ) ) { //$NON-NLS-1$
			programArgs.add( 0, "-showsplash" ); //$NON-NLS-1$
			programArgs.add( 1, computeShowsplashArgument( ) );
		}
		return (String[]) programArgs.toArray( new String[programArgs.size( )] );
	}

	/**
	 * @param configuration
	 * @return
	 * @throws CoreException
	 */
	private String[] getVMArguments( ILaunchConfiguration configuration )
			throws CoreException
	{
		String temp[] = ( new ExecutionArguments( configuration.getAttribute( "vmargs", "" ), "" ) ).getVMArgumentsArray( ); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		String path = configuration.getAttribute( IMPORTPROJECT, "" ); //$NON-NLS-1$

		String append = "-D" + PROJECT_NAMES_KEY + "=" + path; //$NON-NLS-1$ //$NON-NLS-2$

		String projectClassPaths = finder.getClassPath( configuration.getAttribute( IMPORTPROJECTNAMES,
				"" ) ); //$NON-NLS-1$

		String classPath = ""; //$NON-NLS-1$
		// String sourcePath = "";
		if ( projectClassPaths != null && projectClassPaths.length( ) != 0 )
		{
			classPath = "-D" + PROJECT_CLASSPATH_KEY + "=" + projectClassPaths; //$NON-NLS-1$ //$NON-NLS-2$
		}

		if ( temp == null )
		{
			temp = ( new String[]{
					append, classPath
			} );
		}
		else
		{
			List list = new ArrayList( );
			int size = temp.length;
			for ( int i = 0; i < size; i++ )
				list.add( temp[i] );

			list.add( append );
			list.add( classPath );
			temp = (String[]) list.toArray( temp );
		}
		return temp;
	}

	/**
	 * @param path
	 *            no use now
	 * @return
	 */
	private List getAllProjectSourcePaths( String path )
	{
		List retValue = new ArrayList( );
		IProject[] projects = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getProjects( );
		if ( projects == null || projects.length == 0 )
		{
			return retValue;
		}
		for ( int i = 0; i < projects.length; i++ )
		{
			IProject project = projects[i];
			if ( project != null && hasJavaNature( project ) )
			{
				retValue.add( JavaCore.create( project ) );
			}
		}
		return retValue;
	}

	/**
	 * @param projectName
	 * @return
	 */
	private List getProjectSourcePaths( String projectName )
	{
		List retValue = new ArrayList( );

		IProject project = ResourcesPlugin.getWorkspace( )
				.getRoot( )
				.getProject( projectName );

		if ( projectName == null )
		{
			return retValue;
		}

		List list = getProjectSourcePath( project );
		for ( int j = 0; j < list.size( ); j++ )
		{
			String url = (String) list.get( j );
			if ( url != null )
			{
				retValue.add( url );
			}
		}
		return retValue;
	}

	private List getProjectSourcePath( IProject project )
	{
		List retValue = new ArrayList( );
		if ( !hasJavaNature( project ) )
		{
			return null;
		}

		// IJavaProject fCurrJProject = JavaCore.create( project );

		//
		// IClasspathEntry[] paths = fCurrJProject.readRawClasspath( );
		// if (paths == null || paths.length == 0)
		// {
		// return retValue;
		// }
		// int len = paths.length;
		// for (int i=0; i<len; i++)
		// {
		// IPath temp = paths[i].getPath();
		//			
		// String curPath = temp.toOSString( );
		// String directPath = project.getLocation( ).toOSString( );
		// int index = directPath.lastIndexOf( File.separator );
		// String absPath = directPath.substring( 0, index ) + curPath;
		//				
		// retValue.add(directPath);
		// }
		retValue.add( project.getLocation( ).toOSString( ) );
		return retValue;
	}

	/**
	 * Returns true if the given project is accessible and it has a java nature,
	 * otherwise false.
	 * 
	 * @param project
	 *            IProject
	 * @return boolean
	 */
	private boolean hasJavaNature( IProject project )
	{
		try
		{
			return project.hasNature( JavaCore.NATURE_ID );
		}
		catch ( CoreException e )
		{
			// project does not exist or is not open
		}
		return false;
	}

	private void validateFeatures( ) throws CoreException
	{
		IPath installPath = PDEPlugin.getWorkspace( ).getRoot( ).getLocation( );
		String lastSegment = installPath.lastSegment( );
		boolean badStructure = lastSegment == null;
		if ( !badStructure )
		{
			IPath featuresPath = installPath.removeLastSegments( 1 )
					.append( "features" ); //$NON-NLS-1$
			badStructure = !lastSegment.equalsIgnoreCase( "plugins" ) //$NON-NLS-1$
					|| !featuresPath.toFile( ).exists( );
		}
		if ( badStructure )
		{
			throw new CoreException( LauncherUtils.createErrorStatus( Messages.getString( "ReportLaunchConfigurationDelegate.badFeatureSetup" ) ) ); //$NON-NLS-1$
		}
		else
		{
			ensureProductFilesExist( getProductPath( ) );
			return;
		}
	}

	private IPath getInstallPath( )
	{
		return PDEPlugin.getWorkspace( ).getRoot( ).getLocation( );
	}

	private IPath getProductPath( )
	{
		return getInstallPath( ).removeLastSegments( 1 );
	}

	private String computeShowsplashArgument( )
	{
		IPath eclipseHome = ExternalModelManager.getEclipseHome( );
		IPath fullPath = eclipseHome.append( "eclipse" ); //$NON-NLS-1$
		return fullPath.toOSString( ) + " -showsplash 600"; //$NON-NLS-1$
	}

	private void ensureProductFilesExist( IPath productArea )
	{
		File productDir = productArea.toFile( );
		File marker = new File( productDir, ".eclipseproduct" ); //$NON-NLS-1$
		IPath eclipsePath = ExternalModelManager.getEclipseHome( );
		if ( !marker.exists( ) )
			copyFile( eclipsePath, ".eclipseproduct", marker ); //$NON-NLS-1$
		if ( PDECore.getDefault( ).getModelManager( ).isOSGiRuntime( ) )
		{
			File configDir = new File( productDir, "configuration" ); //$NON-NLS-1$
			if ( !configDir.exists( ) )
				configDir.mkdirs( );
			File ini = new File( configDir, "config.ini" ); //$NON-NLS-1$
			if ( !ini.exists( ) )
				copyFile( eclipsePath.append( "configuration" ), "config.ini", //$NON-NLS-1$ //$NON-NLS-2$
						ini );
		}
		else
		{
			File ini = new File( productDir, "install.ini" ); //$NON-NLS-1$
			if ( !ini.exists( ) )
				copyFile( eclipsePath, "install.ini", ini ); //$NON-NLS-1$
		}
	}

	private void copyFile( IPath eclipsePath, String name, File target )
	{
		File source = new File( eclipsePath.toFile( ), name );
		if ( !source.exists( ) )
			return;
		FileInputStream is = null;
		FileOutputStream os = null;
		try
		{
			is = new FileInputStream( source );
			os = new FileOutputStream( target );
			byte buf[] = new byte[1024];
			long currentLen = 0L;
			for ( int len = is.read( buf ); len != -1; len = is.read( buf ) )
			{
				currentLen += len;
				os.write( buf, 0, len );
			}

		}
		catch ( IOException _ex )
		{
		}
		finally
		{
			try
			{
				if ( is != null )
					is.close( );
				if ( os != null )
					os.close( );
			}
			catch ( IOException _ex )
			{
			}
		}
		return;
	}

	protected IProject[] getBuildOrder( ILaunchConfiguration configuration,
			String mode ) throws CoreException
	{
		return computeBuildOrder( LauncherUtils.getAffectedProjects( configuration ) );
	}

	protected IProject[] getProjectsForProblemSearch(
			ILaunchConfiguration configuration, String mode )
			throws CoreException
	{
		return LauncherUtils.getAffectedProjects( configuration );
	}

	private File getConfigDir( ILaunchConfiguration config )
	{
		if ( fConfigDir == null )
			try
			{
				if ( config.getAttribute( "usefeatures", false ) ) //$NON-NLS-1$
				{
					String root = getProductPath( ).toString( );
					if ( PDECore.getDefault( )
							.getModelManager( )
							.isOSGiRuntime( ) )
						root = root + "/configuration"; //$NON-NLS-1$
					fConfigDir = new File( root );
				}
				else
				{
					fConfigDir = ReportLauncherUtils.createConfigArea( config.getName( ) );
				}
			}
			catch ( CoreException _ex )
			{
				fConfigDir = ReportLauncherUtils.createConfigArea( config.getName( ) );
			}
		if ( !fConfigDir.exists( ) )
			fConfigDir.mkdirs( );
		return fConfigDir;
	}

	private static final String KEY_BAD_FEATURE_SETUP = Messages.getString( "ReportLaunchConfigurationDelegate.badFeatureSetup" ); //$NON-NLS-1$

	private static final String KEY_NO_STARTUP = Messages.getString( "ReportLaunchConfigurationDelegate.noStartup" ); //$NON-NLS-1$

	private File fConfigDir;
}