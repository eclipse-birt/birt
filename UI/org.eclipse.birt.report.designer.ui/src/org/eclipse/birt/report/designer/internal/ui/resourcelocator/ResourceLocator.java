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

package org.eclipse.birt.report.designer.internal.ui.resourcelocator;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.birt.report.designer.ui.ReportPlugin;

/**
 * 
 */

public class ResourceLocator
{

	public static ResourceEntry[] getRootEntries( )
	{
		return new ResourceEntry[]{
				new FragmentResourceEntry( ), new PathResourceEntry( )
		};
	}

	public static ResourceEntry[] getRootEntries( String[] fileNamePattern )
	{
		return new ResourceEntry[]{
				new FragmentResourceEntry( fileNamePattern ),
				new PathResourceEntry( fileNamePattern )
		};
	}

	public static ResourceEntry[] getResourceFolder( String[] fileNamePattern )
	{
		return new ResourceEntry[]{
			new PathResourceEntry( fileNamePattern )
		};
	}

	public static String relativize( URL url )
	{
		if ( url.getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
		{
			try
			{
				return new File( ReportPlugin.getDefault( ).getResourceFolder( ) ).toURI( )
						.relativize( url.toURI( ) )
						.getPath( );
			}
			catch ( URISyntaxException e )
			{
			}
		}
		return url.getPath( );
	}
}
