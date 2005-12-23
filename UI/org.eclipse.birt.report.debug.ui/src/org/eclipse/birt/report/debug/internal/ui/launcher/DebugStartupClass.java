/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation .
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.report.debug.internal.ui.launcher;

import java.util.StringTokenizer;

import org.eclipse.birt.report.debug.internal.ui.launcher.util.DebugUtil;
import org.eclipse.ui.IStartup;
import org.eclipse.birt.report.viewer.utilities.IWorkspaceClasspathFinder;
import org.eclipse.birt.report.viewer.utilities.WorkspaceClasspathManager;
import org.eclipse.birt.report.debug.internal.ui.launcher.util.WorkspaceClassPathFinder;

/**
 * Copy the seletion of the project in the debug lauch.The key name is
 * user.projectname.
 * 
 */
public class DebugStartupClass implements IStartup
{

	private static final String WORKSPACE_CLASSPATH_KEY = "workspace.projectclasspath";

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup( )
	{
		WorkspaceClassPathFinder finder = new WorkspaceClassPathFinder( );
		// Register a classpath finder class to the viewer
		WorkspaceClasspathManager.registerClassPathFinder( finder );

		// Set the classpath property (used in Java scripting)
		String projectClassPaths = finder.getClassPath( );

		// HashTable doesn't accept null value
		if ( projectClassPaths == null )
		{
			projectClassPaths = ""; //$NON-NLS-1$
		}
		System.setProperty( WORKSPACE_CLASSPATH_KEY, projectClassPaths );

		String value = System.getProperty( "user.projectname" ); //$NON-NLS-1$
		if ( value == null || value.length( ) == 0 )
		{
			return;
		}
		StringTokenizer token = new StringTokenizer( value, ";" ); //$NON-NLS-1$
		while ( token.hasMoreTokens( ) )
		{
			String str = token.nextToken( );
			try
			{
				DebugUtil.importProject( str );
			} catch ( Exception e1 )
			{

				e1.printStackTrace( );
			}
		}
	}
}