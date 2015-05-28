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

import java.util.List;

import org.eclipse.birt.report.model.api.elements.structures.ExtendedProperty;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.metadata.IElementPropertyDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.interfaces.IOdaDataSourceModel;
import org.eclipse.birt.report.model.metadata.ODAExtensionElementDefn;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.EncryptionUtil;
import org.eclipse.birt.report.model.util.XMLParserException;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

/**
 * Parses the "ex-property" tag. We use the "ex-property" tag if the element
 * property or structure member is an extended property mostly for use with ODA
 * property.
 * 
 */

public class ExtendedPropertyState extends StructureState
{

	protected String encryptionID = null;
	
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
		encryptionID = attrs
				.getValue( DesignSchemaConstants.ENCRYPTION_ID_ATTRIB );
		((ExtendedProperty)struct).setEncryptionID( encryptionID );
		assert struct instanceof ExtendedProperty;
	}
	
	public void end( ) throws SAXException
	{
		super.end( );
		String encryptionID = ( (ExtendedProperty) struct ).getEncryptionID( );
		String value = ( (ExtendedProperty) struct ).getValue( );

		if ( !StringUtil.isBlank( encryptionID ) && !StringUtil.isBlank( value ) )
		{
			String name = ( (ExtendedProperty) struct ).getName( );
			boolean isEncryptable = false;
			List<IElementPropertyDefn> hidePrivatePropsList = null;
            IElementDefn tmpElementDefn = element.getDefn( );
            if ( tmpElementDefn instanceof ODAExtensionElementDefn )
                hidePrivatePropsList = ( (ODAExtensionElementDefn) tmpElementDefn )
                        .getHidePrivateProps( );
			IElementPropertyDefn oadPropertyDefn = null;
			if ( hidePrivatePropsList != null
					&& hidePrivatePropsList.size( ) > 0 )
			{
				for ( IElementPropertyDefn defn : hidePrivatePropsList )
				{
					if ( name.equals( defn.getName( ) ) && defn.isEncryptable( ) )
					{
						isEncryptable = true;
						oadPropertyDefn = defn;
						break;
					}
				}
			}

			if ( isEncryptable )
			{
				String valueToSet = StringUtil.trimString( value );
				valueToSet = (String) EncryptionUtil.decrypt( (PropertyDefn) oadPropertyDefn,
						encryptionID,valueToSet );
				PropertyDefn pd = (PropertyDefn) ( struct.getDefn( ).findProperty( ExtendedProperty.VALUE_MEMBER ) );
				struct.setProperty( pd, valueToSet );
			}
		}
	}

}