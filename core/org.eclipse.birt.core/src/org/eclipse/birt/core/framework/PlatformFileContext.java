/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation. All rights reserved. This program and
 * the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html Contributors: Actuate Corporation -
 * initial API and implementation
 ******************************************************************************/

package org.eclipse.birt.core.framework;

/**
 * An platform context that is based on file operations. Since in web
 * environment WAR deployment, absolute file path is not available. In this
 * case, user should NOT use this class. In this case, user should use
 * PlatformServletContext or develop his own PlatformContext to make sure
 * reousce operation are used.
 */
public class PlatformFileContext implements IPlatformContext
{

	protected String root;

	protected String[] arguments;

	/**
	 * PlatformFileContext Constructor
	 * 
	 */
	public PlatformFileContext( )
	{
		root = System.getProperty( "BIRT_HOME" );
		if ( root == null )
		{
			root = ".";
		}
		arguments = null;
	}

	/**
	 * PlatformFileContext Constructor( String , IPlatformConfig )
	 * 
	 * @param root
	 * @param platformConfig
	 */
	public PlatformFileContext( PlatformConfig config )
	{
		assert config != null;
		root = config.getBIRTHome( );;
		if ( root == null )
		{
			root = ".";
		}
		arguments = config.getOSGiArguments( );
	}

	public String getPlatform( )
	{
		return root;
	}

	public String[] getLaunchArguments( )
	{
		return arguments;
	}
}
