/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/
package org.eclipse.birt.report.viewer.utilities;

/** A class used to get a workspace classpath
 * 
 */
public class WorkspaceClasspathManager
{

	private static IWorkspaceClasspathFinder finder;

	public static String getClassPath( )
	{
		if ( finder == null )
			return null;

		return finder.getClassPath( );
	}

	public static void registerClassPathFinder(
			IWorkspaceClasspathFinder pathfinder )
	{
		finder = pathfinder;
	}
}
