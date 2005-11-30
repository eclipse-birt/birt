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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IStartup#earlyStartup()
	 */
	public void earlyStartup( )
	{
		// Register a classpath finder class to the viewer
		WorkspaceClasspathManager
				.registerClassPathFinder( new WorkspaceClassPathFinder( ) );

		String value = System.getProperty( "user.projectname" );
		if ( value == null || value.length( ) == 0 )
		{
			return;
		}
		StringTokenizer token = new StringTokenizer( value, ";" );
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