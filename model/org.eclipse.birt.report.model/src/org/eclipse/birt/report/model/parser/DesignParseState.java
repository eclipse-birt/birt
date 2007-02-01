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
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.PROPERTY_TAG ) )
			return new PropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.LIST_PROPERTY_TAG ) )
			return new ListPropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EXPRESSION_TAG ) )
			return new ExpressionState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.XML_PROPERTY_TAG ) )
			return new XmlPropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
			return new StructureState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.METHOD_TAG ) )
			return new PropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_PROPERTY_TAG ) )
			return new TextPropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HTML_PROPERTY_TAG ) )
			return new TextPropertyState( handler, getElement( ) );
		if ( tagName
				.equalsIgnoreCase( DesignSchemaConstants.ENCRYPTED_PROPERTY_TAG ) )
			return new EncryptedPropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.SIMPLE_PROPERTY_LIST_TAG ) )
			return new SimplePropertyListState( handler, getElement( ) );

		return super.startElement( tagName );
	}

	
}