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
import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.ReferenceableElement;
import org.eclipse.birt.report.model.elements.interfaces.IStyledElementModel;
import org.eclipse.birt.report.model.metadata.ElementRefValue;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;
import org.eclipse.birt.report.model.util.AbstractParseState;
import org.eclipse.birt.report.model.util.XMLParserHandler;
import org.xml.sax.SAXException;

/**
 * Parses the "property-list" tag. The tag may give the property value of the
 * element.
 */

public class SimplePropertyListState extends AbstractPropertyState
{

	/**
	 * The list to store the values.
	 */

	private List values = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#AbstractPropertyState(DesignParserHandler
	 *      theHandler, DesignElement element, )
	 */

	SimplePropertyListState( ModuleParserHandler theHandler,
			DesignElement element )
	{
		super( theHandler, element );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.parser.AbstractPropertyState#doSetProperty(org.eclipse.birt.report.model.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	protected void doSetProperty( PropertyDefn propDefn, Object valueToSet )
	{
		assert valueToSet != null;

		if ( propDefn.getTypeCode( ) != IPropertyType.LIST_TYPE )
		{
			DesignParserException e = new DesignParserException(
					DesignParserException.DESIGN_EXCEPTION_WRONG_SIMPLE_LIST_TYPE );
			handler.getErrorHandler( ).semanticError( e );
			valid = false;
			return;
		}

		// Validate the value.

		assert valueToSet instanceof List;
		List valueList = new ArrayList( );

		try
		{
			for ( int i = 0; i < ( (List) valueToSet ).size( ); i++ )
			{
				String item = (String) ( (List) valueToSet ).get( i );
				PropertyType type = propDefn.getSubType( );
				Object propValue = type.validateXml( handler.getModule( ),
						propDefn, item );
				if ( propValue != null )
					valueList.add( i, propValue );

			}
		}
		catch ( PropertyValueException ex )
		{
			ex.setElement( element );
			ex.setPropertyName( propDefn.getName( ) );
			handlePropertyValueException( ex );
			valid = false;
			return;
		}

		if ( !valueList.isEmpty( )
				&& !IStyledElementModel.STYLE_PROP.equals( propDefn.getName( ) ) )
			element.setProperty( propDefn, valueList );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
	 */
	
	public void end( ) throws SAXException
	{
		if ( values != null )
		{
			setProperty( name, values );	
			
			PropertyDefn defn = element.getPropertyDefn( name );
			if ( defn.getSubTypeCode( ) == IPropertyType.ELEMENT_REF_TYPE )
			{
				List propList = (List) element.getProperty( element.getRoot( ), name );
				if( propList != null )
				{
					for ( int i = 0; i < propList.size( ); i++ )
					{
						Object obj = propList.get(  i );
						if ( obj instanceof ElementRefValue )
						{
							ElementRefValue refValue = (ElementRefValue) obj;
							if ( refValue.isResolved( ) )
							{
								ReferenceableElement referred = refValue.getTargetElement( );
								referred.addClient( element, name );
							}
						}
					}
				}
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.util.AbstractParseState#startElement(java.lang.String)
	 */

	public AbstractParseState startElement( String tagName )
	{
		if ( DesignSchemaConstants.VALUE_TAG.equalsIgnoreCase( tagName ) )
			return new ValueState( );
		return super.startElement( tagName );
	}

	/**
	 * Convenience class for the inner classes used to parse parts of the Report
	 * tag.
	 */

	class InnerParseState extends AbstractParseState
	{

		public XMLParserHandler getHandler( )
		{
			return handler;
		}
	}

	class ValueState extends InnerParseState
	{

		/*
		 * (non-Javadoc)
		 * 
		 * @see org.eclipse.birt.report.model.util.AbstractParseState#end()
		 */

		public void end( ) throws SAXException
		{
			if ( values == null )
				values = new ArrayList( );
			values.add( text.toString( ) );
		}
	}

}
