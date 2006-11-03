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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.DesignFileException;
import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.elements.ExtendedItem;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.ModelUtil;
import org.eclipse.birt.report.model.util.VersionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.ext.LexicalHandler;

/**
 * Abstract handler for the XML module files. Holds the module being created.
 */

public abstract class ModuleParserHandler extends XMLParserHandler
{

	/**
	 * The design session that will own this module.
	 */

	protected DesignSession session = null;

	/**
	 * The module being created.
	 */

	protected Module module = null;

	/**
	 * Catched name of the module file.
	 */

	protected String fileName = null;

	/**
	 * Number value for the version string.
	 */

	int versionNumber = 0;

	/**
	 * Status identify whether the design file verion is the current supported
	 * version.
	 */

	boolean isCurrentVersion = false;

	/**
	 * Control flag identify whether need mark line number of the design
	 * element.
	 */

	protected boolean markLineNumber = true;

	/**
	 * Temporary variable to cache line number information of the design
	 * elements.
	 */

	protected HashMap tempLineNumbers = null;

	/**
	 * The temporary value for parser compatible.
	 */

	protected HashMap tempValue = new HashMap( );

	/**
	 * Cached element list whose id is not handle and added to the id map.
	 */

	protected List unhandleIDElements = new ArrayList( );

	/**
	 * Lists of those extended-item whose name is not allocated.
	 */

	private List unNameExtendedItems = new ArrayList( );

	/**
	 * Constructs the module parser handler with the design session.
	 * 
	 * @param theSession
	 *            the design session that is to own this module
	 * @param fileName
	 *            name of the module file
	 */

	protected ModuleParserHandler( DesignSession theSession, String fileName )
	{
		super( new ModuleParserErrorHandler( ) );
		this.session = theSession;
		this.fileName = fileName;
	}

	/**
	 * Returns the file name the handler is treating.
	 * 
	 * @return the file name the handler is treating.
	 */

	String getFileName( )
	{
		return this.fileName;
	}

	/**
	 * Returns <code>true</code> if the version of the module file this
	 * handler is parsing equals the given version.
	 * 
	 * @param toCompare
	 *            the version to compare
	 * @return <code>true</code> if the version of the module file this
	 *         handler is parsing equals <code>toCompare</code>.
	 */

	public boolean isVersion( int toCompare )
	{
		return versionNumber == toCompare;
	}

	/**
	 * Returns the module being created.
	 * 
	 * @return the module being created
	 */

	public Module getModule( )
	{
		return module;
	}

	/**
	 * Overrides the super method. This method first parses attributes of the
	 * current state, and then query whether to use a new state or the current
	 * one according to the attributes value.
	 * 
	 * @param namespaceURI
	 * @param localName
	 * 
	 * @see org.xml.sax.ContentHandler#startElement(java.lang.String,
	 *      java.lang.String, java.lang.String, org.xml.sax.Attributes)
	 */

	public void startElement( String namespaceURI, String localName,
			String qName, Attributes atts ) throws SAXException
	{
		errorHandler.setCurrentElement( qName );
		AbstractParseState newState = topState( ).startElement( qName );
		newState.parseAttrs( atts );
		AbstractParseState jumpToState = newState.jumpTo( );
		if ( jumpToState != null )
		{
			pushState( jumpToState );
			return;
		}

		newState.setElementName( qName );
		pushState( newState );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.xml.sax.ContentHandler#endDocument()
	 */

	public void endDocument( ) throws SAXException
	{
		super.endDocument( );

		this.tempValue = null;

		// add all the exceptions to the module

		module.getAllExceptions( ).addAll( getErrorHandler( ).getErrors( ) );

		// Check whether duplicate library namespace exists.

		List libraries = module.getAllLibraries( );
		{
			Iterator iter = libraries.iterator( );
			while ( iter.hasNext( ) )
			{
				Library library = (Library) iter.next( );

				if ( !library.isValid( ) )
				{
					// Forward the fatal error to top level.

					Exception fatalException = ModelUtil
							.getFirstFatalException( library.getAllExceptions( ) );
					if ( fatalException != null )
					{
						XMLParserException exception = errorHandler
								.semanticError( fatalException );
						module.getAllExceptions( ).add( exception );
					}
				}
			}
		}

		// Skip the semantic check if we've already found errors.
		// Doing the semantic check would just uncover bogus errors
		// due to the ones we've already seen.

		if ( !module.getAllErrors( ).isEmpty( )
				|| module.getFatalException( ) != null )
		{
			// The most errors which are found during parsing cannot be
			// recovered.

			module.setValid( false );
			List allExceptions = new ArrayList( );
			allExceptions.addAll( module.getAllExceptions( ) );
			allExceptions.addAll( errorHandler.getWarnings( ) );

			DesignFileException exception = new DesignFileException( module
					.getFileName( ), allExceptions );

			throw new SAXException( exception );
		}

		// the module is ok, then allocate the id for it and its contents

		if ( !unhandleIDElements.isEmpty( ) )
		{
			handleID( );
			unhandleIDElements = null;
		}

		// add un-named extended items to name-space
		if ( !unNameExtendedItems.isEmpty( )
				&& versionNumber < VersionUtil.VERSION_3_2_8 )
			handleUnnamedExtendedItems( );

		// build the line number information of design elements if needed.

		if ( markLineNumber && tempLineNumbers != null )
		{
			Iterator iter = tempLineNumbers.entrySet( ).iterator( );
			while ( iter.hasNext( ) )
			{
				Map.Entry entry = (Map.Entry) iter.next( );
				module.addElementLineNo( ( (DesignElement) entry.getKey( ) )
						.getID( ), ( (Integer) entry.getValue( ) ).intValue( ) );
			}
			tempLineNumbers = null;
		}

		// if module options not set the parser-semantic check options or set it
		// to true, then perform semantic check. Semantic error is recoverable.

		ModuleOption options = module.getOptions( );
		if ( options == null || options.useSemanticCheck( ) )
			module.semanticCheck( module );

		// translates warnings during parsing design files to ErrorDetail.

		if ( errorHandler.getWarnings( ) != null )
		{
			module.getAllExceptions( ).addAll( errorHandler.getWarnings( ) );
		}
	}

	/**
	 * @param versionNumber
	 *            the versionNumber to set
	 */
	public void setVersionNumber( int versionNumber )
	{
		this.versionNumber = versionNumber;
	}

	/**
	 * Allocates a unique id for all the unhandle elements.
	 */

	private void handleID( )
	{
		for ( int i = 0; i < unhandleIDElements.size( ); i++ )
		{
			DesignElement element = (DesignElement) unhandleIDElements.get( i );

			if ( element.getExtendsElement( ) == null )
			{
				if ( element.getRoot( ) == module )
				{
					assert element.getID( ) == DesignElement.NO_ID;
					element.setID( module.getNextID( ) );
					module.addElementID( element );
				}
			}
			else
				// TODO only need for the compound element
				module.manageId( element, true );
		}
	}

	/**
	 * Adds unnamed extended items to name-space.
	 * 
	 */

	private void handleUnnamedExtendedItems( )
	{
		for ( int i = 0; i < unNameExtendedItems.size( ); i++ )
		{
			DesignElement element = (DesignElement) unNameExtendedItems.get( i );
			ModelUtil.addElement2NameSpace( module, element );
		}
	}

	/**
	 * Initializes line number mark flag if needed.
	 * 
	 * @param options
	 *            the options set for this module
	 */

	final protected void initLineNumberMarker( ModuleOption options )
	{
		assert module != null;
		if ( options == null || options.markLineNumber( ) )
		{
			// lazy initialization.
			markLineNumber = true;
			module.initLineNoMap( );
			tempLineNumbers = new HashMap( );
		}
		else
			markLineNumber = false;
	}

	/**
	 * Adds a unNamed extended-item to the list.
	 * 
	 * @param element
	 *            the element to add
	 */

	final void addUnnamedExtendedItem( ExtendedItem element )
	{
		if ( !unNameExtendedItems.contains( element ) )
			unNameExtendedItems.add( element );
	}

	static class ModuleLexicalHandler implements LexicalHandler
	{

		ModuleParserHandler handler = null;

		/**
		 * 
		 */

		ModuleLexicalHandler( ModuleParserHandler handler )
		{
			this.handler = handler;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#comment(char[], int, int)
		 */
		public void comment( char[] ch, int start, int length )
				throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#endCDATA()
		 */
		public void endCDATA( ) throws SAXException
		{
			AbstractParseState tmpState = handler.topState( );
			tmpState.setIsCDataSection( true );
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#endDTD()
		 */
		public void endDTD( ) throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#endEntity(java.lang.String)
		 */
		public void endEntity( String name ) throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#startCDATA()
		 */

		public void startCDATA( ) throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#startDTD(java.lang.String,
		 *      java.lang.String, java.lang.String)
		 */
		public void startDTD( String name, String publicId, String systemId )
				throws SAXException
		{
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.xml.sax.ext.LexicalHandler#startEntity(java.lang.String)
		 */
		public void startEntity( String name ) throws SAXException
		{
		}
	}
}
