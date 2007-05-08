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

import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserHandler;

/**
 * Base class for report element parse states.
 * 
 */

public abstract class DesignParseState extends AbstractParseState
{

	/**
	 * Pointer to the design file parser handler.
	 */

	protected ModuleParserHandler handler = null;

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            SAX handler for the design file parser
	 */

	public DesignParseState( ModuleParserHandler theHandler )
	{
		handler = theHandler;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#getHandler()
	 */

	public XMLParserHandler getHandler( )
	{
		return handler;
	}

	/**
	 * Returns the element being created.
	 * 
	 * @return the report element being created
	 */

	public abstract DesignElement getElement( );

	/**
	 * Sets the value of a property with a string parsed from the XML file.
	 * Performs any required semantic checks.
	 * 
	 * @param propName
	 *            property name
	 * @param value
	 *            value string from the XML file
	 */

	protected void setProperty( String propName, String value )
	{
		// Ensure that the property is defined.

		DesignElement element = getElement( );
		ElementPropertyDefn prop = element.getPropertyDefn( propName );
		assert prop != null;
		if ( prop == null )
			return;

		// Validate the value.

		Object propValue = null;
		try
		{
			propValue = prop.validateXml( handler.getModule( ), value );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propName );
			handler.getErrorHandler( ).semanticError( ex );
			return;
		}
		element.setProperty( propName, propValue );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		int tagValue = tagName.toLowerCase( ).hashCode( );
		if (  ParserSchemaConstants.PROPERTY_TAG == tagValue )
			return new PropertyState( handler, getElement( ) );
		if (  ParserSchemaConstants.LIST_PROPERTY_TAG == tagValue )
			return new ListPropertyState( handler, getElement( ) );
		if (  ParserSchemaConstants.EXPRESSION_TAG == tagValue )
			return new ExpressionState( handler, getElement( ) );
		if (  ParserSchemaConstants.XML_PROPERTY_TAG == tagValue )
			return new XmlPropertyState( handler, getElement( ) );
		if (  ParserSchemaConstants.STRUCTURE_TAG == tagValue )
			return new StructureState( handler, getElement( ) );
		if (  ParserSchemaConstants.METHOD_TAG == tagValue )
			return new PropertyState( handler, getElement( ) );
		if (  ParserSchemaConstants.TEXT_PROPERTY_TAG == tagValue )
			return new TextPropertyState( handler, getElement( ) );
		if (  ParserSchemaConstants.HTML_PROPERTY_TAG == tagValue )
			return new TextPropertyState( handler, getElement( ) );
		if ( ParserSchemaConstants.ENCRYPTED_PROPERTY_TAG == tagValue )
			return new EncryptedPropertyState( handler, getElement( ) );
		if (  ParserSchemaConstants.SIMPLE_PROPERTY_LIST_TAG == tagValue )
			return new SimplePropertyListState( handler, getElement( ) );

		return super.startElement( tagName );
	}

	
}