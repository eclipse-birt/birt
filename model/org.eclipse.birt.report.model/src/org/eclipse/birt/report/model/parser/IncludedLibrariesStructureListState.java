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

package org.eclipse.birt.report.model.parser;

import java.net.URL;
import java.util.ArrayList;

import org.eclipse.birt.report.model.api.IResourceLocator;
import org.eclipse.birt.report.model.api.command.LibraryException;
import org.eclipse.birt.report.model.api.elements.structures.IncludedLibrary;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.xml.sax.SAXException;

/**
 * Parses the simple structure list for "includeLibraires" property, each of
 * which has only one member. So it also can be considered as String List.
 */

public class IncludedLibrariesStructureListState extends CompatibleListPropertyState
{

	private int lineNumber = 1;

	IncludedLibrariesStructureListState( ModuleParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */
	public AbstractParseState startElement( String tagName )
	{
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
			return new IncludedLibraryStructureState( handler, element,
					propDefn, list );

		return super.startElement( tagName );
	}

	class IncludedLibraryStructureState extends CompatibleStructureState
	{

		IncludedLibraryStructureState( ModuleParserHandler theHandler,
				DesignElement element, PropertyDefn propDefn, ArrayList theList )
		{
			super( theHandler, element, propDefn, theList );
			lineNumber = handler.getCurrentLineNo( );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			super.end( );

			IncludedLibrary includeLibrary = (IncludedLibrary) struct;

			if ( handler.markLineNumber )
				handler.module.addLineNo( struct, new Integer( lineNumber ) );

			// Use file name without path and suffix as default namespace.

			if ( StringUtil.isBlank( includeLibrary.getNamespace( ) ) )
			{
				String fileName = StringUtil.extractFileName( includeLibrary
						.getFileName( ) );
				includeLibrary.setNamespace( fileName );
			}

			String namespace = includeLibrary.getNamespace( );
			if ( handler.getModule( ).isDuplicateNamespace( namespace ) )
			{
				LibraryException ex = new LibraryException(
						handler.module,
						new String[]{namespace},
						LibraryException.DESIGN_EXCEPTION_DUPLICATE_LIBRARY_NAMESPACE );
				handler.getErrorHandler( ).semanticError( ex );
				return;
			}

			// the library has already been included.

			URL url = handler.module.findResource(
					includeLibrary.getFileName( ), IResourceLocator.LIBRARY );
			if ( url != null
					&& handler.module.getLibraryByLocation( url.toString( ) ) != null )
			{
				LibraryException ex = new LibraryException(
						handler.module,
						new String[]{url.toString( )},
						LibraryException.DESIGN_EXCEPTION_LIBRARY_ALREADY_INCLUDED );
				handler.getErrorHandler( ).semanticWarning( ex );
				return;
			}

			if ( handler.module instanceof Library )
			{
				Library library = (Library) handler.module;

				if ( url != null
						&& library.isRecursiveFile( url.toString( ) )
						|| library.isRecursiveNamespace( includeLibrary
								.getNamespace( ) ) )
				{
					LibraryException ex = new LibraryException(
							handler.module,
							new String[]{namespace},
							LibraryException.DESIGN_EXCEPTION_LIBRARY_INCLUDED_RECURSIVELY );
					handler.getErrorHandler( ).semanticError( ex );
					return;
				}
			}

			handler.module.loadLibrarySilently( includeLibrary );
		}
	}
}
