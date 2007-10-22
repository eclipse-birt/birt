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

import java.net.URL;

import org.eclipse.birt.report.designer.ui.ReportPlugin;
import org.eclipse.birt.report.model.api.util.URIUtil;

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
		String path = url.getFile( );
		if ( url.getProtocol( ).equals( "file" ) ) //$NON-NLS-1$
		{
			// return new File( ReportPlugin.getDefault( ).getResourceFolder( )
			// ).toURI( )
			// .relativize( new File( url.getPath( ) ).toURI( ) )
			// .getPath( );
			return URIUtil.getRelativePath( ReportPlugin.getDefault( )
					.getResourceFolder( ), path );
		}
		return path;
	}
}
