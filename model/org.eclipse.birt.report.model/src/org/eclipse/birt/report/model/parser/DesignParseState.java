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

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.IStructure;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyValueException;
import org.eclipse.birt.report.model.metadata.StructPropertyDefn;
import org.eclipse.birt.report.model.metadata.StructureDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.Attributes;

/**
 * Base class for report element parse states.
 *  
 */

public abstract class DesignParseState extends AbstractParseState
{

	/**
	 * Pointer to the design file parser handler.
	 */

	protected DesignParserHandler handler = null;

	/**
	 * Constructs the design parse state with the design file parser handler.
	 * 
	 * @param theHandler
	 *            SAX handler for the design file parser
	 */

	public DesignParseState( DesignParserHandler theHandler )
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
	 * Sets the value of a property based on an XML attribute. Performs semantic
	 * checks required for the property. The property is assumed to be on the
	 * element that this state is building.
	 * 
	 * @param propName
	 *            property name
	 * @param attrs
	 *            the SAX attributes object
	 * @param attrName
	 *            the XML attribute name for the property
	 */

	protected void setProperty( String propName, Attributes attrs,
			String attrName )
	{
		setProperty( propName, getAttrib( attrs, attrName ) );
	}

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
		if ( StringUtil.isBlank( value ) )
			return;

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
			propValue = prop.validateXml( handler.getDesign( ), value.trim( ) );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propName );
			handler.semanticError( ex );
			return;
		}
		element.setProperty( propName, propValue );
	}

	/**
	 * Sets the member of a structure.
	 * 
	 * @param struct
	 *            the structure that contains the member to set
	 * @param propName
	 *            the property in which the structure appears
	 * @param member
	 *            the structure member name
	 * @param value
	 *            the value parsed from the XML file
	 */

	void setMember( IStructure struct, String propName, String member,
			String value )
	{
		if ( StringUtil.isBlank( value ) )
			return;

		// Ensure that the member is defined.

		StructureDefn structDefn = struct.getDefn( );
		assert structDefn != null;
		if ( structDefn == null )
			return;

		StructPropertyDefn memberDefn = structDefn.getMember( member );
		assert memberDefn != null;
		if ( memberDefn == null )
			return;

		// Validate the value.

		Object propValue = null;
		try
		{
			propValue = memberDefn.validateXml( handler.getDesign( ), value
					.trim( ) );
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( getElement( ) );
			ex.setPropertyName( propName + "." + member ); //$NON-NLS-1$
			handler.semanticError( ex );
			return;
		}
		struct.setProperty( memberDefn, propValue );
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
			return new PropertyListState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.EXPRESSION_TAG ) )
			return new ExpressionState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.XML_PROPERTY_TAG ) )
			return new XmlPropertyState( handler, getElement( ) );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.STRUCTURE_TAG ) )
			return new StructureState( handler, getElement() );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.METHOD_TAG ) )
			return new PropertyState( handler, getElement() );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.TEXT_PROPERTY_TAG ) )
			return new TextPropertyState( handler, getElement() );
		if ( tagName.equalsIgnoreCase( DesignSchemaConstants.HTML_PROPERTY_TAG ) )
			return new TextPropertyState( handler, getElement() );

		return super.startElement( tagName );
	}

}