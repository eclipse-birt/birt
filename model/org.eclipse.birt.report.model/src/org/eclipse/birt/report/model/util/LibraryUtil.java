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

package org.eclipse.birt.report.model.util;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.core.IAccessControl;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.Library;

/**
 * The utility class for the library related operation.
 * 
 */

public class LibraryUtil
{

	/**
	 * Checks whether the library with given name space/URL can be included in
	 * the given module.
	 * 
	 * @param module
	 *            the module to include library
	 * @param namespace
	 *            the library name space
	 * @param url
	 *            the URL of the library file
	 * @param outermostModule
	 *            the root of the module, its host must be null
	 * @return the matched library
	 * 
	 * @throws LibraryException
	 */

	public static Library checkIncludeLibrary( Module module, String namespace,
			URL url, Module outermostModule ) throws LibraryException
	{

		Library foundLib = outermostModule.getLibraryWithNamespace( namespace );

		if ( url != null )
		{
			if ( foundLib != null )
			{
				String tmpPath = foundLib.getLocation( );
				String foundPath = url.toExternalForm( );

				// the case: the same name spaces but different library files.

				if ( !foundPath.equalsIgnoreCase( tmpPath ) )
				{
					throw new LibraryException(
							module,
							new String[]{namespace},
							LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE );
				}

				// the library has already been included.

				if ( module.getLibraryWithNamespace( namespace,
						IAccessControl.DIRECTLY_INCLUDED_LEVEL ) != null )
				{
					throw new LibraryException(
							module,
							new String[]{namespace},
							LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE );
				}
			}
			else
			{
				foundLib = outermostModule.getLibraryByLocation( url
						.toExternalForm( ) );

				// the case: if the name space is different and the file is the
				// same, must throw exception.

				if ( foundLib != null
						&& !namespace
								.equalsIgnoreCase( foundLib.getNamespace( ) ) )
				{
					throw new LibraryException(
							module,
							new String[]{namespace},
							LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED );
				}
			}

			if ( module.getLibraryByLocation( url.toExternalForm( ),
					IAccessControl.DIRECTLY_INCLUDED_LEVEL ) != null )
			{
				throw new LibraryException(
						module,
						new String[]{url.toExternalForm( )},
						LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED );
			}
		}

		// check the recursive libraries from top to bottom

		if ( module instanceof Library )
		{
			Library library = (Library) module;

			if ( url != null && library.isRecursiveFile( url.toExternalForm( ) )
					|| library.isRecursiveNamespace( namespace ) )
			{
				throw new LibraryException(
						module,
						new String[]{namespace},
						LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY );
			}
		}

		return foundLib;
	}

	/**
	 * Inserts load libraries into the given map.
	 * 
	 * @param reloadLibs
	 *            the map contains reload libraries, the name space is key and
	 *            the library instance is the value
	 * @param library
	 *            the given library
	 */

	public static void insertReloadLibs( Map<String, Library> reloadLibs,
			Library library )
	{
		if ( reloadLibs == null || reloadLibs == Collections.EMPTY_MAP )
			return;

		Set<String> namespaces = reloadLibs.keySet( );

		List<Library> tmpLibs = library.getAllLibraries( );
		String namespace = library.getNamespace( );

		if ( !namespaces.contains( namespace ) )
			reloadLibs.put( namespace, library );

		for ( int i = 0; i < tmpLibs.size( ); i++ )
		{
			Library tmpLib = tmpLibs.get( i );
			namespace = tmpLib.getNamespace( );

			if ( !namespaces.contains( namespace ) )
				reloadLibs.put( namespace, library );

			reloadLibs.put( namespace, tmpLib );
		}
	}

}
