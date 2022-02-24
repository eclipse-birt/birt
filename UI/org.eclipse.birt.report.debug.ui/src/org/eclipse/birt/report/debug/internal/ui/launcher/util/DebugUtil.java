///*******************************************************************************
// * Copyright (c) 2005 Actuate Corporation.
// * 
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License 2.0 which is available at
// * https://www.eclipse.org/legal/epl-2.0/.
// * 
// * SPDX-License-Identifier: EPL-2.0
// * 
// *
// * Contributors:
// *  Actuate Corporation  - initial API and implementation
// *******************************************************************************/
//
//package org.eclipse.birt.report.debug.internal.ui.launcher.util;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.util.HashMap;
//import java.util.MissingResourceException;
//import java.util.ResourceBundle;
//import java.util.TreeMap;
//
//import org.eclipse.core.internal.resources.ProjectDescriptionReader;
//import org.eclipse.core.resources.IProject;
//import org.eclipse.core.resources.IProjectDescription;
//import org.eclipse.core.resources.ResourcesPlugin;
//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.NullProgressMonitor;
//import org.eclipse.core.runtime.Path;
//import org.eclipse.core.runtime.Platform;
//import org.eclipse.pde.core.plugin.TargetPlatform;
//import org.eclipse.pde.internal.ui.PDEPlugin;
//import org.eclipse.pde.internal.ui.launcher.LaunchListener;
//
///**
// * DebugUtil
// * 
// * @deprecated
// */
//public class DebugUtil
//{
//
//	/**
//	 * Import exist project into current workspace.
//	 * 
//	 * @param prjFilePath
//	 *            .project file path.
//	 * @throws Exception
//	 */
//	public static void importProject( String prjFilePath ) throws Exception
//	{
//		IPath path = new Path( prjFilePath + File.separator + ".project" ); //$NON-NLS-1$
//		final IProjectDescription newDescription = loadProjectDescription( path );
//		final IProject project = ResourcesPlugin.getWorkspace( )
//				.getRoot( )
//				.getProject( newDescription.getName( ) );
//		if ( project == null )
//			throw new Exception( "DebugUtil.importProject.fail" ); //$NON-NLS-1$
//		NullProgressMonitor monitor = new NullProgressMonitor( );
//		project.create( newDescription, monitor );
//		project.open( monitor );
//	}
//
//	private static IProjectDescription loadProjectDescription( IPath path )
//			throws IOException
//	{
//		IProjectDescription result = null;
//		result = new ProjectDescriptionReader( ).read( path );
//		if ( result != null )
//		{
//			// check to see if we are using in the default area or not. use
//			// java.io.File for
//			// testing equality because it knows better w.r.t. drives and
//			// case sensitivity
//			IPath user = path.removeLastSegments( 1 );
//			IPath platform = Platform.getLocation( ).append( result.getName( ) );
//			if ( !user.toFile( ).equals( platform.toFile( ) ) )
//				result.setLocation( user );
//		}
//		return result;
//	}
//
//	/**
//	 * copy a special folder to current workspace folder.
//	 * 
//	 * @param folderPath
//	 * @return the result folder path.
//	 * @throws Exception
//	 */
//	public static String copyToWorkspace( String folderPath ) throws Exception
//	{
//		File folder = new File( folderPath );
//		if ( !folder.isDirectory( ) )
//			throw new Exception( );
//		String newFolderName = folder.getName( );
//		String workspaceRootPath = ResourcesPlugin.getWorkspace( )
//				.getRoot( )
//				.getLocation( )
//				.toOSString( );
//		String newFolderPath = workspaceRootPath
//				+ File.separator
//				+ newFolderName;
//		File newFolder = new File( newFolderPath );
//		if ( !newFolder.exists( ) )
//			newFolder.mkdir( );
//
//		copyFiles( folder, newFolder );
//
//		return newFolderPath;
//	}
//
//	private static void copyFiles( File folder, File newFolder )
//			throws IOException
//	{
//		File[] folderFiles = folder.listFiles( );
//		if ( folderFiles != null )
//			for ( int i = 0; i < folderFiles.length; i++ )
//			{
//				if ( folderFiles[i].isFile( ) )
//				{
//					File newFile = new File( newFolder,
//							folderFiles[i].getName( ) );
//					if ( !newFile.exists( ) )
//						newFile.createNewFile( );
//					copyFile( folderFiles[i], newFile );
//				}
//				else
//				{
//					File newSubFolder = new File( newFolder,
//							folderFiles[i].getName( ) );
//					if ( !newSubFolder.exists( ) )
//						newSubFolder.mkdir( );
//					copyFiles( folderFiles[i], newSubFolder );
//				}
//			}
//	}
//
//	private static void copyFile( File in, File out ) throws IOException
//	{
//		FileInputStream fis = new FileInputStream( in );
//		FileOutputStream fos = new FileOutputStream( out );
//		byte[] buf = new byte[1024];
//		int i = 0;
//		while ( ( i = fis.read( buf ) ) != -1 )
//		{
//			fos.write( buf, 0, i );
//		}
//		fis.close( );
//		fos.close( );
//	}
//
//	public static String getResourceString( String key )
//	{
//
//		ResourceBundle bundle = Platform.getResourceBundle( PDEPlugin.getDefault( )
//				.getBundleContext( )
//				.getBundle( ) );
//		if ( bundle != null )
//		{
//			try
//			{
//				String bundleString = bundle.getString( key );
//				// return "$"+bundleString;
//				return bundleString;
//			}
//			catch ( MissingResourceException e )
//			{
//				// default actions is to return key, which is OK
//			}
//		}
//		return key;
//	}
//
//	public static LaunchListener getPDEPluginLaunchListener( PDEPlugin in )
//	{
//		Method method = null;
//		try
//		{
//			method = PDEPlugin.class.getDeclaredMethod( "getLaunchesListener", //$NON-NLS-1$
//					new Class[]{} );
//			return (LaunchListener) ( method.invoke( in, new Object[]{} ) );
//		}
//		catch ( Exception e )
//		{
//			if ( method == null )
//			{
//				try
//				{
//					method = PDEPlugin.class.getDeclaredMethod( "getLaunchListener", new Class[]{} ); //$NON-NLS-1$
//					return (LaunchListener) ( method.invoke( in, new Object[]{} ) );
//				}
//				catch ( Exception e1 )
//				{
//
//				}
//
//			}
//
//		}
//
//		return null;
//	}
//
//	public static void runCreatePlatformConfigurationArea( TreeMap pluginMap,
//			File configDir, String primaryFeatureId, HashMap autoStartPlugins )
//	{
//		Method method = null;
//		try
//		{
//			method = TargetPlatform.class.getDeclaredMethod( "createPlatformConfigurationArea", new Class[]{ //$NON-NLS-1$
//							TreeMap.class,
//							File.class,
//							String.class,
//							HashMap.class
//					} );
//			method.invoke( TargetPlatform.class, new Object[]{
//					pluginMap, configDir, primaryFeatureId, autoStartPlugins
//			} );
//			return;
//		}
//		catch ( Exception e )
//		{
//			method = null;
//		}
//
//		if ( method == null )
//		{
//			try
//			{
//				method = TargetPlatform.class.getDeclaredMethod( "createPlatformConfigurationArea", new Class[]{ //$NON-NLS-1$
//								TreeMap.class, File.class, String.class
//						} );
//				method.invoke( TargetPlatform.class, new Object[]{
//						pluginMap, configDir, primaryFeatureId
//				} );
//			}
//			catch ( Exception e )
//			{
//			}
//		}
//	}
//
//	
//}
