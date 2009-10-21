/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.designer.internal.ui.ide.adapters;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.birt.report.designer.ui.IReportClasspathResolver;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;

/**
 * IDEReportClasspathResolver
 */
public class IDEReportClasspathResolver implements IReportClasspathResolver
{

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.designer.ui.IReportClasspathProvider#resolveClasspath
	 * (java.lang.Object)
	 */
	public String[] resolveClasspath( Object adaptable )
	{
		IProject project = adaptProject( adaptable );

		if ( project == null )
		{
			// TODO return global settings

			return null;
		}

		Set<String> paths = getProjectClasspath( project );

		return paths.toArray( new String[paths.size( )] );
	}

	private IProject adaptProject( Object adaptable )
	{
		// TODO support other adaptable types

		if ( adaptable instanceof IProject )
		{
			return (IProject) adaptable;
		}
		else if ( adaptable instanceof IResource )
		{
			return ( (IResource) adaptable ).getProject( );
		}
		else if ( adaptable instanceof URI )
		{
			// this should be the absolute report file path
			IFile[] files = ResourcesPlugin.getWorkspace( )
					.getRoot( )
					.findFilesForLocationURI( (URI) adaptable );

			if ( files != null && files.length > 0 )
			{
				return files[0].getProject( );
			}
		}
		else if ( adaptable instanceof IPath )
		{
			// this should be the absolute report file path
			IFile[] files = ResourcesPlugin.getWorkspace( )
					.getRoot( )
					.findFilesForLocation( (IPath) adaptable );

			if ( files != null && files.length > 0 )
			{
				return files[0].getProject( );
			}
		}
		else if ( adaptable instanceof String )
		{
			// this should be the absolute report file path
			IFile[] files = ResourcesPlugin.getWorkspace( )
					.getRoot( )
					.findFilesForLocation( Path.fromOSString( (String) adaptable ) );

			if ( files != null && files.length > 0 )
			{
				return files[0].getProject( );
			}
		}

		return null;
	}

	private Set<String> getProjectClasspath( IProject project )
	{
		if ( project == null )
		{
			return Collections.emptySet( );
		}

		Set<String> retValue = new HashSet<String>( );

		List<URL> paths = getProjectDependentClasspath( project );

		for ( int j = 0; j < paths.size( ); j++ )
		{
			URL url = paths.get( j );
			if ( url != null )
			{
				retValue.add( url.getPath( ) );
			}
		}

		String url = getProjectOutputClassPath( project );
		if ( url != null )
		{
			retValue.add( url );
		}

		// TODO read other project specific settings

		return retValue;
	}

	private String getProjectOutputClassPath( IProject project )
	{
		if ( !hasJavaNature( project ) )
		{
			return null;
		}

		IJavaProject fCurrJProject = JavaCore.create( project );
		IPath path = null;
		boolean projectExists = ( project.exists( ) && project.getFile( ".classpath" ).exists( ) ); //$NON-NLS-1$
		if ( projectExists )
		{
			if ( path == null )
			{
				path = fCurrJProject.readOutputLocation( );
				// String curPath = path.toOSString( );
				// String directPath = project.getLocation( ).toOSString( );
				// int index = directPath.lastIndexOf( File.separator );
				String absPath = getFullPath( path, project );

				return absPath;
			}
		}

		return null;
	}

	private List<URL> getProjectDependentClasspath( IProject project )
	{
		if ( !hasJavaNature( project ) )
		{
			return Collections.emptyList( );
		}

		List<URL> retValue = new ArrayList<URL>( );

		IJavaProject fCurrJProject = JavaCore.create( project );
		IClasspathEntry[] classpathEntries = null;

		boolean projectExists = ( project.exists( ) && project.getFile( ".classpath" ).exists( ) ); //$NON-NLS-1$

		if ( projectExists )
		{
			if ( classpathEntries == null )
			{
				classpathEntries = fCurrJProject.readRawClasspath( );
			}
		}

		if ( classpathEntries != null )
		{
			retValue = resolveClasspathEntries( classpathEntries );
		}

		return retValue;
	}

	private List<URL> resolveClasspathEntries(
			IClasspathEntry[] classpathEntries )
	{
		ArrayList<URL> newClassPath = new ArrayList<URL>( );

		for ( int i = 0; i < classpathEntries.length; i++ )
		{
			IClasspathEntry curr = classpathEntries[i];
			if ( curr.getEntryKind( ) == IClasspathEntry.CPE_LIBRARY )
			{
				try
				{
					boolean inWorkSpace = true;
					IWorkspace space = ResourcesPlugin.getWorkspace( );
					if ( space == null || space.getRoot( ) == null )
					{
						inWorkSpace = false;
					}

					IWorkspaceRoot root = ResourcesPlugin.getWorkspace( )
							.getRoot( );
					IPath path = curr.getPath( );
					if ( root.findMember( path ) == null )
					{
						inWorkSpace = false;
					}

					if ( inWorkSpace )
					{
						String absPath = getFullPath( path,
								root.findMember( path ).getProject( ) );

						URL url = new URL( "file:///" + absPath );//$NON-NLS-1$//file:/
						newClassPath.add( url );
					}
					else
					{
						newClassPath.add( curr.getPath( )
								.toFile( )
								.toURI( )
								.toURL( ) );
					}

				}
				catch ( MalformedURLException e )
				{
					// DO nothing
				}
			}
		}
		return newClassPath;
	}

	private String getFullPath( IPath path, IProject project )
	{
		// String curPath = path.toOSString( );
		// String directPath = project.getLocation( ).toOSString( );
		// int index = directPath.lastIndexOf( File.separator );
		// String absPath = directPath.substring( 0, index ) + curPath;
		// return absPath;

		String directPath;
		try
		{

			directPath = project.getDescription( )
					.getLocationURI( )
					.toURL( )
					.getPath( );
		}
		catch ( Exception e )
		{
			directPath = project.getLocation( ).toOSString( );
		}
		String curPath = path.toOSString( );
		int index = curPath.substring( 1 ).indexOf( File.separator );
		String absPath = directPath + curPath.substring( index + 1 );
		return absPath;
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
}
