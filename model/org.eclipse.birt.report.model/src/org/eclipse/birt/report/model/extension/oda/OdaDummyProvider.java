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

package org.eclipse.birt.report.model.extension.oda;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.birt.report.model.api.command.ExtendsException;
import org.eclipse.birt.report.model.api.metadata.IPropertyDefn;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ExtensionElementDefn;
import org.eclipse.birt.report.model.parser.DesignSchemaConstants;

/**
 * The dummmy provider to save property values if the ODA extension cannnot be
 * found.
 * 
 */

public class OdaDummyProvider implements ODAProvider
{

	/**
	 * ID of the extension which is used to extend the extendable element.
	 */

	private String extensionID = null;

	/**
	 * Values of ODA datasource/datasets.
	 */

	private Map values = new LinkedHashMap( );

	/**
	 * The default constructor.
	 * 
	 * @param extensionID
	 *            the extension id
	 */

	public OdaDummyProvider( String extensionID )
	{
		this.extensionID = extensionID;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#checkExtends(org.eclipse.birt.report.model.core.DesignElement)
	 */
	public void checkExtends( DesignElement parent ) throws ExtendsException
	{

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getExtDefn()
	 */
	public ExtensionElementDefn getExtDefn( )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getPropertyDefn(java.lang.String)
	 */
	public IPropertyDefn getPropertyDefn( String propName )
	{
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#getPropertyDefns()
	 */
	public List getPropertyDefns( )
	{
		return Collections.EMPTY_LIST;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidODADataSetExtensionID(java.lang.String)
	 */
	public boolean isValidODADataSetExtensionID( String extensionID )
	{
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.extension.oda.ODAProvider#isValidODADataSourceExtensionID(java.lang.String)
	 */

	public boolean isValidODADataSourceExtensionID( String extensionID )
	{
		return false;
	}

	/**
	 * Sets one property to the provider.
	 * 
	 * @param name
	 *            the property name
	 * @param value
	 *            the property value
	 * @param tagName
	 *            the tag name in the design file for this property
	 */

	public void saveValue( String name, String value, String tagName )
	{
		assert tagName != null;

		if ( name == null || value == null )
			return;

		values.put( name, new OdaProperty( name, value, tagName ) );
	}

	/**
	 * Returns the value with the given property value.
	 * 
	 * @param name
	 *            the property name
	 * @return the property value
	 */

	public String getValue( String name )
	{
		if ( name == null )
			return null;

		OdaProperty prop = (OdaProperty) values.get( name );
		if ( prop == null )
			return null;

		return prop.getValue( );
	}

	/**
	 * Returns the type with the given property value.
	 * 
	 * @param name
	 *            the property name
	 * @return the property type
	 */

	public int getPropertyTypeCode( String name )
	{
		if ( name == null )
			return -1;

		OdaProperty prop = (OdaProperty) values.get( name );
		if ( prop == null )
			return -1;

		return prop.getPropertyTypeCode( );
	}

	/**
	 * Returns the tag name with the given property value.
	 * 
	 * @param name
	 *            the property name
	 * @return the tag name
	 */

	public String getTagName( String name )
	{
		if ( name == null )
			return null;

		OdaProperty prop = (OdaProperty) values.get( name );
		if ( prop == null )
			return null;

		return prop.getTagName( );
	}

	/**
	 * Returns property names. Each item in the return list is a property name
	 * in string.
	 * 
	 * @return a list containing property names
	 */

	public List getPropertyNames( )
	{
		List retList = new ArrayList( );
		retList.addAll( values.keySet( ) );
		return retList;
	}

	/**
	 * Represents a oda defined property.
	 * 
	 */

	static class OdaProperty
	{

		private String name;
		private String tagName;
		private String value;

		OdaProperty( String name, String value, String tagName )
		{
			this.name = name;
			this.value = value;
			this.tagName = tagName;
		}

		/**
		 * Returns the property type code.
		 * 
		 * @return the property type code
		 */

		public int getPropertyTypeCode( )
		{
			if ( DesignSchemaConstants.PROPERTY_TAG.equalsIgnoreCase( tagName ) )
				return IPropertyType.LITERAL_STRING_TYPE;
			else if ( DesignSchemaConstants.EXPRESSION_TAG
					.equalsIgnoreCase( tagName ) )
				return IPropertyType.EXPRESSION_TYPE;
			else if ( DesignSchemaConstants.METHOD_TAG
					.equalsIgnoreCase( tagName ) )
				return IPropertyType.SCRIPT_TYPE;
			else if ( DesignSchemaConstants.XML_PROPERTY_TAG
					.equalsIgnoreCase( tagName ) )
				return IPropertyType.XML_TYPE;

			assert false;
			return -1;
		}

		/**
		 * Returns the property value.
		 * 
		 * @return the value
		 */

		public String getValue( )
		{
			return value;
		}

		/**
		 * Returns the tag name in the design file.
		 * 
		 * @return the tagName
		 */
		public String getTagName( )
		{
			return tagName;
		}

	}
}
