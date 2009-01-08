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

import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;

/**
 * Parses the "ex-property" tag. We use the "ex-property" tag if the element
 * property or structure member is an extended property mostly for use with ODA
 * property.
 * 
 */

public class ExtendedPropertyState extends StructureState
{

	/**
	 * 
	 * @param theHandler
	 * @param element
	 * @param propDefn
	 */
	ExtendedPropertyState( ModuleParserHandler theHandler,
			DesignElement element, PropertyDefn propDefn )
	{
		super( theHandler, element, propDefn );

		// till now, there is structure ODAProperty can be written as
		// ex-property.

		if ( IOdaDataSourceModel.PRIVATE_DRIVER_PROPERTIES_PROP
				.equalsIgnoreCase( propDefn.getName( ) ) )
			struct = new ExtendedProperty( );
		else
			handler
					.getErrorHandler( )
					.semanticError(
							new DesignParserException(
									DesignParserException.DESIGN_EXCEPTION_WRONG_EXTENDED_PROPERTY_TYPE ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#startElement(java
	 * .lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		assert struct instanceof ExtendedProperty;

		int tagValue = tagName.toLowerCase( ).hashCode( );
		if ( ParserSchemaConstants.NAME_ATTRIB == tagValue )
			return new TextState( handler, struct, ExtendedProperty.NAME_MEMBER );
		if ( ParserSchemaConstants.VALUE_TAG == tagValue )
			return new TextState( handler, struct,
					ExtendedProperty.VALUE_MEMBER );

		return super.startElement( tagName );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.util.AbstractParseState#parseAttrs(org.
	 * xml.sax.Attributes)
	 */

	public void parseAttrs( Attributes attrs ) throws XMLParserException
	{
		super.parseAttrs( attrs );
		assert struct instanceof ExtendedProperty;
	}

}