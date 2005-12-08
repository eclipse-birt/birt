/*
 * Created on 2005-11-11
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

package org.eclipse.birt.report.debug.internal.ui.launcher.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TreeMap;

import org.eclipse.core.internal.resources.ProjectDescriptionReader;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.ProjectScope;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.pde.core.plugin.IPluginModelBase;
import org.eclipse.pde.internal.core.PDECore;
import org.eclipse.pde.internal.core.TargetPlatform;
import org.eclipse.pde.internal.core.WorkspaceModelManager;
import org.eclipse.pde.internal.ui.PDEPlugin;
import org.eclipse.pde.internal.ui.launcher.LaunchListener;

/**
 * @author Administrator
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class DebugUtil
{

	/**
	 * Import exist project into current workspace.
	 * 
	 * @param prjFilePath
	 *            .project file path.
	 * @throws Exception
	 */
	public static void importProject( String prjFilePath ) throws Exception
	{
		IPath path = new Path( prjFilePath + File.separator + ".project" ); //$NON-NLS-1$
		final IProjectDescription newDescription = loadProjectDescription( path );
		final IProject project = ResourcesPlugin.getWorkspace( ).getRoot( )
				.getProject( newDescription.getName( ) );
		if ( project == null )
			throw new Exception( "DebugUtil.importProject.fail" ); //$NON-NLS-1$
		NullProgressMonitor monitor = new NullProgressMonitor( );
		project.create( newDescription, monitor );
		project.open( monitor );
	}

	private static IProjectDescription loadProjectDescription( IPath path )
			throws IOException
	{
		IProjectDescription result = null;
		result = new ProjectDescriptionReader( ).read( path );
		if ( result != null )
		{
			// check to see if we are using in the default area or not. use
			// java.io.File for
			// testing equality because it knows better w.r.t. drives and
			// case sensitivity
			IPath user = path.removeLastSegments( 1 );
			IPath platform = Platform.getLocation( ).append( result.getName( ) );
			if ( !user.toFile( ).equals( platform.toFile( ) ) )
				result.setLocation( user );
		}
		return result;
	}

	/**
	 * copy a special folder to current workspace folder.
	 * 
	 * @param folderPath
	 * @return the result folder path.
	 * @throws Exception
	 */
	public static String copyToWorkspace( String folderPath ) throws Exception
	{
		File folder = new File( folderPath );
		if ( !folder.isDirectory( ) )
			throw new Exception( );
		String newFolderName = folder.getName( );
		String workspaceRootPath = ResourcesPlugin.getWorkspace( ).getRoot( )
				.getLocation( ).toOSString( );
		String newFolderPath = workspaceRootPath + File.separator
				+ newFolderName;
		File newFolder = new File( newFolderPath );
		if ( !newFolder.exists( ) )
			newFolder.mkdir( );

		copyFiles( folder, newFolder );

		return newFolderPath;
	}

	private static void copyFiles( File folder, File newFolder )
			throws IOException
	{
		File[] folderFiles = folder.listFiles( );
		if ( folderFiles != null )
			for ( int i = 0; i < folderFiles.length; i++ )
			{
				if ( folderFiles[i].isFile( ) )
				{
					File newFile = new File( newFolder, folderFiles[i]
							.getName( ) );
					if ( !newFile.exists( ) )
						newFile.createNewFile( );
					copyFile( folderFiles[i], newFile );
				}
				else
				{
					File newSubFolder = new File( newFolder, folderFiles[i]
							.getName( ) );
					if ( !newSubFolder.exists( ) )
						newSubFolder.mkdir( );
					copyFiles( folderFiles[i], newSubFolder );
				}
			}
	}

	private static void copyFile( File in, File out ) throws IOException
	{
		FileInputStream fis = new FileInputStream( in );
		FileOutputStream fos = new FileOutputStream( out );
		byte[] buf = new byte[1024];
		int i = 0;
		while ( ( i = fis.read( buf ) ) != -1 )
		{
			fos.write( buf, 0, i );
		}
		fis.close( );
		fos.close( );
	}

	public static String getResourceString( String key )
	{

		ResourceBundle bundle = Platform.getResourceBundle( PDEPlugin
				.getDefault( ).getBundleContext( ).getBundle( ) );
		if ( bundle != null )
		{
			try
			{
				String bundleString = bundle.getString( key );
				//return "$"+bundleString;
				return bundleString;
			}
			catch ( MissingResourceException e )
			{
				// default actions is to return key, which is OK
			}
		}
		return key;
	}

	public static LaunchListener getPDEPluginLaunchListener( PDEPlugin in )
	{
		Method method = null;
		try
		{
			method = PDEPlugin.class.getDeclaredMethod( "getLaunchesListener", //$NON-NLS-1$
					new Class[]{} );
			return (LaunchListener) ( method.invoke( in, new Object[]{} ) );
		}
		catch ( Exception e )
		{
			if ( method == null )
			{
				try
				{
					method = PDEPlugin.class.getDeclaredMethod(
							"getLaunchListener", new Class[]{} ); //$NON-NLS-1$
					return (LaunchListener) ( method
							.invoke( in, new Object[]{} ) );
				}
				catch ( Exception e1 )
				{

				}

			}

		}

		return null;
	}

	public static void runCreatePlatformConfigurationArea( TreeMap pluginMap,
			File configDir, String primaryFeatureId, HashMap autoStartPlugins )
	{
		Method method = null;
		try
		{
			method = TargetPlatform.class.getDeclaredMethod(
					"createPlatformConfigurationArea", new Class[]{ //$NON-NLS-1$
							TreeMap.class, File.class, String.class,
							HashMap.class} );
			method.invoke( TargetPlatform.class, new Object[]{pluginMap,
					configDir, primaryFeatureId, autoStartPlugins} );
			return;
		}
		catch ( Exception e )
		{
			method = null;
		}

		if ( method == null )
		{
			try
			{
				method = TargetPlatform.class.getDeclaredMethod(
						"createPlatformConfigurationArea", new Class[]{ //$NON-NLS-1$
								TreeMap.class, File.class, String.class} );
				method.invoke( TargetPlatform.class, new Object[]{pluginMap,
						configDir, primaryFeatureId} );
			}
			catch ( Exception e )
			{
			}
		}
	}

	public static String getDevEntriesProperties( String fileName,
			boolean checkExcluded )
	{
		File file = new File( fileName );
		if ( !file.exists( ) )
		{
			File directory = file.getParentFile( );
			if ( directory != null
					&& ( !directory.exists( ) || directory.isFile( ) ) )
			{
				directory.mkdirs( );
			}
		}
		Properties properties = new Properties( );
		WorkspaceModelManager manager = PDECore.getDefault( )
				.getWorkspaceModelManager( );
		IPluginModelBase[] models = getAllModels(manager );
		for ( int i = 0; i < models.length; i++ )
		{
			String id = models[i].getPluginBase( ).getId( );
			if ( id == null )
				continue;
			String entry = writeEntry( getOutputFolders( models[i],
					checkExcluded ) );
			if ( entry.length( ) > 0 )
				properties.put( id, entry );
		}

		try
		{
			FileOutputStream stream = new FileOutputStream( fileName );
			properties.store( stream, "" ); //$NON-NLS-1$
			stream.flush( );
			stream.close( );
			return new URL( "file:" + fileName ).toString( ); //$NON-NLS-1$
		}
		catch ( IOException e )
		{
			PDECore.logException( e );
		}
		return getDevEntries( checkExcluded );
	}
	private static IPluginModelBase[] getAllModels(WorkspaceModelManager manager)
	{
		try
		{
			Method method = WorkspaceModelManager.class.getDeclaredMethod("getAllModels", new Class[]{}); //$NON-NLS-1$
			method.setAccessible(true);
			return (IPluginModelBase[])method.invoke(manager, new Object[]{});
		}
		catch ( Exception e )
		{
		}
		return null;
	}
	public static String getDevEntries( boolean checkExcluded )
	{
		WorkspaceModelManager manager = PDECore.getDefault( )
				.getWorkspaceModelManager( );
		IPluginModelBase[] models = getAllModels(manager );
		ArrayList list = new ArrayList( );
		for ( int i = 0; i < models.length; i++ )
		{
			String id = models[i].getPluginBase( ).getId( );
			if ( id == null || id.trim( ).length( ) == 0 )
				continue;
			IPath[] paths = getOutputFolders( models[i], checkExcluded );
			for ( int j = 0; j < paths.length; j++ )
			{
				list.add( paths[j] );
			}
		}
		String entry = writeEntry( (IPath[]) list.toArray( new IPath[list
				.size( )] ) );
		return entry.length( ) > 0 ? entry : "bin"; //$NON-NLS-1$
	}

	private static IPath[] getOutputFolders( IPluginModelBase model,
			boolean checkExcluded )
	{
		ArrayList result = new ArrayList( );
		IProject project = model.getUnderlyingResource( ).getProject( );
		try
		{
			if ( project.hasNature( JavaCore.NATURE_ID ) )
			{
				IJavaProject jProject = JavaCore.create( project );

				List excluded = getFoldersToExclude( project, checkExcluded );
				IPath path = jProject.getOutputLocation( );
				if ( path != null && !excluded.contains( path ) )
					addPath( result, project, path );

				IClasspathEntry[] entries = jProject.getRawClasspath( );
				for ( int i = 0; i < entries.length; i++ )
				{
					if ( entries[i].getContentKind( ) == IPackageFragmentRoot.K_SOURCE
							&& entries[i].getEntryKind( ) == IClasspathEntry.CPE_SOURCE )
					{
						path = entries[i].getOutputLocation( );
						if ( path != null && !excluded.contains( path ) )
							addPath( result, project, path );
					}
				}
			}
		}
		catch ( JavaModelException e )
		{
		}
		catch ( CoreException e )
		{
		}
		return (IPath[]) result.toArray( new IPath[result.size( )] );
	}

	private static List getFoldersToExclude( IProject project,
			boolean checkExcluded )
	{
		ArrayList list = new ArrayList( );
		if ( checkExcluded )
		{
			IEclipsePreferences pref = new ProjectScope( project )
					.getNode( PDECore.PLUGIN_ID );
			if ( pref != null )
			{
				String binExcludes = pref.get(
						PDECore.SELFHOSTING_BIN_EXLCUDES, "" ); //$NON-NLS-1$
				StringTokenizer tokenizer = new StringTokenizer( binExcludes,
						"," ); //$NON-NLS-1$
				while ( tokenizer.hasMoreTokens( ) )
				{
					list.add( new Path( tokenizer.nextToken( ).trim( ) ) );
				}
			}
		}
		return list;
	}

	private static String writeEntry( IPath[] paths )
	{
		StringBuffer buffer = new StringBuffer( );
		for ( int i = 0; i < paths.length; i++ )
		{
			buffer.append( paths[i].toString( ) );
			if ( i < paths.length - 1 )
				buffer.append( "," ); //$NON-NLS-1$
		}
		return buffer.toString( );
	}

	private static void addPath( ArrayList result, IProject project, IPath path )
	{
		if ( path.getDevice( ) == null )
		{
			if ( path.segmentCount( ) >= 1 )
			{
				if ( path.segment( 0 ).equals( project.getName( ) ) )
				{
					path = path.removeFirstSegments( 1 );
					if ( path.segmentCount( ) == 0 )
						path = new Path( "." ); //$NON-NLS-1$
				}
			}
		}

		if ( !result.contains( path ) )
			result.add( path );
	}
	
	
}