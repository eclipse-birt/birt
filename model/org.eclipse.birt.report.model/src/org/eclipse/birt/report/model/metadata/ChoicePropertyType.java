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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.util.StringUtil;

/**
 * Type for a property defined by a list of choices. The actual list of choices
 * is defined by the specific choice property. A choice name is stored as
 * <code>java.lang.String</code> internally.
 * 
 * @see ElementPropertyDefn
 */

public class ChoicePropertyType extends PropertyType
{

	/**
	 * Display name key.
	 */

	private static final String DISPLAY_NAME_KEY = "Property.choice"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */

	public ChoicePropertyType( )
	{
		super( DISPLAY_NAME_KEY );
	}

	/**
	 * Validates a choice property. The choice property can be a string with one
	 * of the valid choices, or it can be a string that contains one of the
	 * localized names for the choices.
	 * 
	 * @return object of type String, Returns <code>null</code> if the
	 *         <code>value</code> parameter is null.
	 * @throws PropertyValueException
	 *             if <code>value</code> is not found in the predefined
	 *             choices, or it is not a valid localized choice name.
	 */

	public Object validateValue( ReportDesign design, PropertyDefn defn,
			Object value ) throws PropertyValueException
	{
		if ( value == null )
			return null;

		if ( value instanceof String )
		{
			Object data = validateInputString( design, defn, (String) value );
			if ( data != null )
				return data;
		}

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_INVALID_VALUE, getTypeCode( ) );
	}

	/**
	 * Validates an XML value within a choice set. This method throws an
	 * exception if choice properties cannot be validated in the predefined
	 * choice list. Otherwise return the validated value.
	 * 
	 * @return the choice name if it is contained in the predefined choice list;
	 *         Return <code>null/code> the value is null;
	 * @throws PropertyValueException
	 *             if this value is not found in the predefined choice list.
	 */

	public Object validateXml( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		value = StringUtil.trimString( value );
		if ( value == null )
			return null;

		// if the property doesn't define the restrictions, the whole choice set
		// will be returned.

		ChoiceSet allowedChoices = defn.getAllowedChoices( );
		assert allowedChoices != null;

		// Internal name of a choice.

		Choice choice = allowedChoices.findChoice( value );
		if ( choice != null )
			return choice.getName( );

		ChoiceSet propChoices = defn.getChoices( );
		if ( propChoices.contains( value ) )
		{
			// The is in the whole choice set, but not in the allowed list.

			throw new PropertyValueException( value,
					PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED, getTypeCode( ) );
		}

		throw new PropertyValueException( value,
				PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, getTypeCode( ) );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getTypeCode()
	 */

	public int getTypeCode( )
	{
		return CHOICE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#getName()
	 */

	public String getName( )
	{
		return CHOICE_TYPE_NAME;
	}

	/**
	 * Converts the choice property value to a locale-independent string. The
	 * <code>value</code> should be in the predefined choice list. If
	 * <code>value</code> is <code>null</code>, the return will be null, if
	 * the <code>value</code> is in the predefined choice list, the value will
	 * be returned as a String.
	 * 
	 * @return the value as a string if it is in the predefined choice list,
	 *         return null if the value is null;
	 */

	public String toString( ReportDesign design, PropertyDefn defn, Object value )
	{
		ChoiceSet propChoices = defn.getChoices( );
		assert propChoices != null;

		if ( value == null )
			return null;

		Choice choice = propChoices.findChoice( (String) value );
		if ( choice != null )
			return choice.getName( );

		// should never be here.

		assert false;
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.metadata.PropertyType#toXml(org.eclipse.birt.report.model.design.elements.ReportDesign,
	 *      org.eclipse.birt.report.model.design.metadata.PropertyDefn,
	 *      java.lang.Object)
	 */

	public String toXml( ReportDesign design, PropertyDefn defn, Object value )
	{
		return toString( design, defn, value );
	}

	/**
	 * Returns a localized choice display name according to its internal name.
	 * 
	 * @return the display string for its internal value.
	 */

	public String toDisplayString( ReportDesign design, PropertyDefn defn,
			Object name )
	{
		if ( name == null )
			return null;

		ChoiceSet propChoices = defn.getChoices( );
		assert propChoices != null;

		Choice choice = propChoices.findChoice( name.toString( ) );
		if ( choice != null )
		{
			// Return localized choice name.

			return choice.getDisplayName( );
		}

		// assert false for other cases.

		assert false;
		return null;
	}

	/**
	 * Validates a string according to predefined choice properties in
	 * locale-dependent way, the <code>name</code> can be either an internal
	 * choice name or it can be a localized choice name.
	 * 
	 * @return the internal choice name, if the <code>name</code> is an
	 *         internal choice name or a localized choice name, return
	 *         <code>null</code> if <code>name</code> is null.
	 * @throws PropertyValueException
	 *             if the <code>name</code> is not valid.
	 */

	public Object validateInputString( ReportDesign design, PropertyDefn defn,
			String name ) throws PropertyValueException
	{
		name = StringUtil.trimString( name );
		if ( name == null )
			return null;

		// if the property doesn't define the restrictions, the whole choice set
		// will be returned.

		ChoiceSet allowedChoices = defn.getAllowedChoices( );
		assert allowedChoices != null;

		// 1. Internal name of a choice.

		Choice choice = allowedChoices.findChoice( name );
		if ( choice != null )
			return choice.getName( );

		// 2. localized display name of a choice.
		// Convert the localized choice name into internal name.

		choice = null;
		if ( !allowedChoices.isUserDefined( ) )
			choice = allowedChoices.findChoiceByDisplayName( name );
		else
			choice = allowedChoices.findUserChoiceByDisplayName( design, name );

		if ( choice != null )
			return choice.getName( );

		ChoiceSet propChoices = defn.getChoices( );
		if ( propChoices.contains( name ) )
		{
			// The is in the whole choice set, but not in the allowed list.

			throw new PropertyValueException( name,
					PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_ALLOWED, getTypeCode( ) );
		}

		throw new PropertyValueException( name,
				PropertyValueException.DESIGN_EXCEPTION_CHOICE_NOT_FOUND, getTypeCode( ) );

	}
}