/*************************************************************************
 * Copyright (c) 2011 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation - added support of relative file path
 *
 *************************************************************************
 */

package org.eclipse.birt.report.data.oda.excel.impl.util;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.birt.report.data.oda.excel.impl.i18n.Messages;
import org.eclipse.datatools.connectivity.oda.OdaException;
import org.eclipse.datatools.connectivity.oda.util.ResourceIdentifiers;


public class ResourceLocatorUtil
{
	private static Logger logger = Logger.getLogger(ResourceLocatorUtil.class.getName( ));

	public static URI resolvePath( Object resourceIdentifiers, String path ) throws OdaException
	{
		URI uri = null;
		File f = new File( path );
		if( f.isAbsolute( ) && f.exists( ) )
		{
			uri = f.toURI();
			logger.log( Level.FINER, "Excel source folder exists on local file system. Using path: " + uri );
			return uri;
		}

		logger.log( Level.FINER, "Try resolving URI and relative path: " + path );
		try
		{
			try
			{
				uri = new URI( path );
			}
			catch ( URISyntaxException ex )
			{
				uri = new URI( null, null, path, null );
			}

			logger.log( Level.FINER, "Resolved excel source URI: " + uri );

			if ( uri.isAbsolute() )
			{
				logger.log( Level.FINER, "Excel source folder URI is resolved as the absolute path: " + uri );
				return uri;
			}
			else if ( !uri.isAbsolute( ) && resourceIdentifiers != null )
			{
				uri = ResourceIdentifiers.resolveApplResource( resourceIdentifiers, uri );
				logger.log( Level.FINER, "Relative URI resolved as the absolute path: " + uri );
				return uri;
			}
			else
			{
				logger.log( Level.SEVERE,
						Messages.getString("Connection.InvalidRelativePath")
								+ uri );
				throw new OdaException( Messages.getString( "Connection.InvalidSource" ) );
			}
		}
		catch ( URISyntaxException e1 )
		{
			throw new OdaException( Messages.getString( "Connection.InvalidSource" ) ); //$NON-NLS-1$
		}
	}

}
