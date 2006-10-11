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

import org.eclipse.birt.report.model.api.ModuleOption;
import org.eclipse.birt.report.model.core.DesignSession;
import org.eclipse.birt.report.model.elements.Library;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.AbstractParseState;

/**
 * Generic module parser handler, used to parse a design file or a library file.
 * 
 */

public class GenericModuleParserHandler extends ModuleParserHandler
{

	/**
	 * Catched system ID.
	 */

	private URL systemID = null;

	/**
	 * Options set for this module.
	 */

	private ModuleOption options = null;

	GenericModuleParserHandler( DesignSession theSession, URL systemID,
			String fileName, ModuleOption options )
	{
		super( theSession, fileName );
		this.systemID = systemID;
		this.fileName = fileName;
		this.options = options;
	}

	public AbstractParseState createStartState( )
	{
		return new StartState( );
	}

	/**
	 * Recognizes the top-level tags: Report or Library
	 */

	class StartState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
		 */

		public AbstractParseState startElement( String tagName )
		{
			if ( DesignSchemaConstants.REPORT_TAG.equalsIgnoreCase( tagName ) )
			{
				module = new ReportDesign( session );
				module.setSystemId( systemID );
				module.setFileName( fileName );
				module.setOptions( options );
				initLineNumberMarker( options );
				if ( markLineNumber )
					tempLineNumbers.put( module, new Integer( locator
							.getLineNumber( ) ) );
				return new ReportState( GenericModuleParserHandler.this );
			}
			else if ( DesignSchemaConstants.LIBRARY_TAG
					.equalsIgnoreCase( tagName ) )
			{
				module = new Library( session );
				module.setSystemId( systemID );
				module.setFileName( fileName );
				module.setOptions( options );
				initLineNumberMarker( options );
				if ( markLineNumber )
					tempLineNumbers.put( module, new Integer( locator
							.getLineNumber( ) ) );
				return new LibraryState( GenericModuleParserHandler.this );
			}

			return super.startElement( tagName );
		}
	}

}
