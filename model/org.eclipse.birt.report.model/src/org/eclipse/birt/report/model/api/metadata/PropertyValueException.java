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

package org.eclipse.birt.report.model.api.metadata;

import java.util.Objects;

import org.eclipse.birt.report.model.api.activity.SemanticException;
import org.eclipse.birt.report.model.api.core.IStructure;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.PropertyDefn;

/**
 * Indicates an invalid property value.
 */

public class PropertyValueException extends SemanticException
{

	/**
	 * Comment for <code>serialVersionUID</code>.
	 */

	private static final long serialVersionUID = 6453952392044174297L;

	/**
	 * Error code constant indicating that the property value is invalid.
	 */

	public static final String DESIGN_EXCEPTION_INVALID_VALUE = MessageConstants.PROPERTY_VALUE_EXCEPTION_INVALID_VALUE;

	/**
	 * Error code constant indicating that the property value can not be
	 * negative.
	 */

	public static final String DESIGN_EXCEPTION_NEGATIVE_VALUE = MessageConstants.PROPERTY_VALUE_EXCEPTION_NEGATIVE_VALUE;

	/**
	 * Error code constant indicating that the property value can not be
	 * negative or zero.
	 */

	public static final String DESIGN_EXCEPTION_NON_POSITIVE_VALUE = MessageConstants.PROPERTY_VALUE_EXCEPTION_NON_POSITIVE_VALUE;

	/**
	 * Error code constant indicating that the choice value is not found in the
	 * choice set.
	 */

	public static final String DESIGN_EXCEPTION_CHOICE_NOT_FOUND = MessageConstants.PROPERTY_VALUE_EXCEPTION_CHOICE_NOT_FOUND;

	/**
	 * Error code constant indicating that the property is not a list type.
	 */

	public static final String DESIGN_EXCEPTION_NOT_LIST_TYPE = MessageConstants.PROPERTY_VALUE_EXCEPTION_NOT_LIST_TYPE;

	/**
	 * Error code constant indicating that the item is not found in a list.
	 */

	public static final String DESIGN_EXCEPTION_ITEM_NOT_FOUND = MessageConstants.PROPERTY_VALUE_EXCEPTION_ITEM_NOT_FOUND;

	/**
	 * Error code constant indicating that the item is not type of structure
	 * list referred.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_ITEM_TYPE = MessageConstants.PROPERTY_VALUE_EXCEPTION_WRONG_ITEM_TYPE;

	/**
	 * Error code constant indicating that the elements are of different types.
	 */

	public static final String DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE = MessageConstants.PROPERTY_VALUE_EXCEPTION_WRONG_ELEMENT_TYPE;

	/**
	 * Error code constant indicating that the property value already exists.
	 */

	public static final String DESIGN_EXCEPTION_VALUE_EXISTS = MessageConstants.PROPERTY_VALUE_EXCEPTION_VALUE_EXISTS;

	/**
	 * Error code constant indicating that the property value is required.
	 */

	public static final String DESIGN_EXCEPTION_VALUE_REQUIRED = MessageConstants.PROPERTY_VALUE_EXCEPTION_VALUE_REQUIRED;

	/**
	 * Error code constant indicating that property has been locked in a base
	 * element, and the value of the property cannot be set in a derived
	 * element.
	 */

	public static final String DESIGN_EXCEPTION_VALUE_LOCKED = MessageConstants.PROPERTY_VALUE_EXCEPTION_VALUE_LOCKED;

	/**
	 * Error code constant indicating unit is not allowed for the dimension
	 * property.
	 */

	public static final String DESIGN_EXCEPTION_UNIT_NOT_ALLOWED = MessageConstants.PROPERTY_VALUE_EXCEPTION_UNIT_NOT_ALLOWED;

	/**
	 * Error code constant indicating the choice value is not allowed for a
	 * choice type property.
	 */

	public static final String DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED = MessageConstants.PROPERTY_VALUE_EXCEPTION_CHOICE_NOT_ALLOWED;

	/**
	 * The extension property of ExtendedItem is forbidden to be set by
	 * commands.
	 */

	public static final String DESIGN_EXCEPTION_EXTENSION_SETTING_FORBIDDEN = MessageConstants.PROPERTY_VALUE_EXTENSION_SETTING_FORBIDDEN;

	/**
	 * Within child element, properties that can cause structure change are not
	 * allowed to set.
	 */

	public static final String DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN = MessageConstants.PROPERTY_VALUE_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN;

	/**
	 * The character "." is forbidden to NamePropertyType.
	 * 
	 * @deprecated
	 */

	public static final String DESIGN_EXCEPTION_DOT_FORBIDDEN = MessageConstants.PROPERTY_VALUE_EXCEPTION_DOT_FORBIDDEN;

	/**
	 * Error codes that indicates that the report item theme type is not
	 * supported yet.
	 */
	public static final String DESIGN_EXCEPTION_NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE = MessageConstants.PROPERTY_VALUE_EXCEPTION_NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE;

	/**
	 * The invalid value.
	 */

	protected Object invalidValue = null;

	/**
	 * The name of the property being set.
	 */

	protected String propertyName = null;

	/**
	 * The name of the member being set.
	 */

	protected String memberName = null;

	/**
	 * Name of the type of the property.
	 */

	protected String propertyTypeName = null;

	/**
	 * The display name of the property being set.
	 */
	protected String propertyDisplayName = null;

	/**
	 * Constructs an exception given an invalid value, error code and the
	 * property type constants.
	 * 
	 * @param value
	 *            The invalid value.
	 * @param errCode
	 *            description of the problem
	 * @param type
	 *            the parameter data type
	 */

	public PropertyValueException( Object value, String errCode, int type )
	{
		super( null, errCode );
		this.invalidValue = value;
		this.propertyTypeName = MetaDataDictionary.getInstance( )
				.getPropertyType( type ).getName( );
	}

	/**
	 * Constructs an exception given an design element, an element property
	 * name, an invalid value and the error code. Using this constructor when
	 * the property is an element property.
	 * 
	 * @param obj
	 *            design element on which the property was being set
	 * @param propName
	 *            name of the property or the method being set
	 * @param value
	 *            the invalid value
	 * @param errCode
	 *            description of the problem
	 */

	public PropertyValueException( DesignElement obj, String propName,
			Object value, String errCode )
	{
		super( obj, errCode );
		this.propertyName = propName;
		this.invalidValue = value;
		PropertyDefn propDefn = element.getPropertyDefn( propertyName );
		Objects.requireNonNull(propDefn, String.format("Unknown property %s for design object %s", propName, obj));
		this.propertyTypeName = propDefn.getType( ).getName( );
		this.propertyDisplayName = propDefn.getDisplayName( );

	}

	/**
	 * Constructs an exception given the definition of the property, an invalid
	 * value and its error code. Using this constructor when the definition of
	 * the property is available
	 * 
	 * @param obj
	 *            design element on which the property was being set
	 * @param propDefn
	 *            definition of the property.
	 * @param value
	 *            invalid value of the property.
	 * @param errCode
	 *            error code.
	 */

	public PropertyValueException( DesignElement obj, IPropertyDefn propDefn,
			Object value, String errCode )
	{
		super( obj, errCode );
		assert propDefn != null;

		this.invalidValue = value;
		this.propertyName = propDefn.getName( );
		this.propertyTypeName = ( (PropertyDefn) propDefn ).getType( )
				.getName( );
		this.propertyDisplayName = propDefn.getDisplayName( );
	}

	/**
	 * Constructs an exception given the definition of the property, the
	 * structure member definition, an invalid value and its error code. Using
	 * this constructor when the definition of the structure member is
	 * available.
	 * 
	 * @param obj
	 *            design element on which the property was being set
	 * @param propDefn
	 *            definition of the property.
	 * @param memberDefn
	 *            definition of the structure member
	 * @param value
	 *            invalid value of the property.
	 * @param errCode
	 *            error code.
	 */

	public PropertyValueException( DesignElement obj, IPropertyDefn propDefn,
			IPropertyDefn memberDefn, Object value, String errCode )
	{
		super( obj, errCode );
		assert propDefn != null;

		this.invalidValue = value;
		this.propertyName = propDefn.getName( );
		this.memberName = memberDefn.getName( );
		this.propertyTypeName = ( (PropertyDefn) propDefn ).getType( )
				.getName( );
		this.propertyDisplayName = propDefn.getDisplayName( );
	}

	/**
	 * Constructs an exception given an invalid value, error code.
	 * 
	 * @param value
	 *            The invalid value.
	 * @param errCode
	 *            description of the problem
	 */

	public PropertyValueException( Object value, String errCode )
	{
		super( null, errCode );
		this.invalidValue = value;
	}

	/**
	 * Sets the element, if it is known.
	 * 
	 * @param obj
	 *            The element on which the property was being set.
	 */

	public void setElement( DesignElement obj )
	{
		element = obj;
	}

	/**
	 * Sets the name of the property being set, if it is known.
	 * 
	 * @param propName
	 *            The name of the property being set.
	 */

	public void setPropertyName( String propName )
	{
		propertyName = propName;
	}

	/**
	 * Returns the invalid value.
	 * 
	 * @return the invalid value
	 */

	public Object getInvalidValue( )
	{
		return invalidValue;
	}

	/**
	 * Returns the name of the property being set.
	 * 
	 * @return the property name, or null if not known
	 */

	public String getPropertyName( )
	{
		return propertyName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Throwable#getLocalizedMessage()
	 */

	public String getLocalizedMessage( )
	{
		String value = ""; //$NON-NLS-1$

		if ( invalidValue != null )
			value = invalidValue.toString( );

		if ( sResourceKey == DESIGN_EXCEPTION_INVALID_VALUE )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{value,
					this.propertyTypeName} );
		}
		if ( sResourceKey == DESIGN_EXCEPTION_NEGATIVE_VALUE
				|| sResourceKey == DESIGN_EXCEPTION_NON_POSITIVE_VALUE )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{value,
					this.propertyDisplayName} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_CHOICE_NOT_FOUND
				|| sResourceKey == DESIGN_EXCEPTION_VALUE_EXISTS
				|| sResourceKey == DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED
				|| sResourceKey == DESIGN_EXCEPTION_WRONG_ELEMENT_TYPE
				|| sResourceKey == DESIGN_EXCEPTION_NOT_SUPPORTED_REPORT_ITEM_THEME_TYPE )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{value} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_VALUE_REQUIRED
				|| sResourceKey == DESIGN_EXCEPTION_VALUE_LOCKED
				|| sResourceKey == DESIGN_EXCEPTION_NOT_LIST_TYPE
				|| sResourceKey == DESIGN_EXCEPTION_ITEM_NOT_FOUND )
		{
			return ModelMessages.getMessage( sResourceKey,
					new String[]{propertyDisplayName} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_WRONG_ITEM_TYPE )
		{
			PropertyDefn propDefn = element.getPropertyDefn( propertyName );

			if ( memberName != null )
			{
				propDefn = (PropertyDefn) propDefn.getStructDefn( ).getMember(
						memberName );
			}

			assert invalidValue instanceof IStructure;
			assert propDefn != null;
			assert propDefn.getTypeCode( ) == IPropertyType.STRUCT_TYPE;

			return ModelMessages.getMessage( sResourceKey, new String[]{
					( (IStructure) invalidValue ).getStructName( ),
					propDefn.getStructDefn( ).getName( )} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_UNIT_NOT_ALLOWED )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{value,
					propertyDisplayName} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_EXTENSION_SETTING_FORBIDDEN
				|| sResourceKey == DESIGN_EXCEPTION_PROPERTY_CHANGE_FORBIDDEN )
		{
			return ModelMessages.getMessage( sResourceKey, new String[]{
					propertyDisplayName, element.getFullName( )} );
		}
		else if ( sResourceKey == DESIGN_EXCEPTION_DOT_FORBIDDEN )
		{
			return ModelMessages.getMessage( sResourceKey,
					new String[]{(String) invalidValue} );
		}
		return ModelMessages.getMessage( sResourceKey );
	}
}