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

package org.eclipse.birt.report.model.api.core;

import org.eclipse.birt.report.model.api.command.UserPropertyException;
import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IObjectDefn;
import org.eclipse.birt.report.model.api.metadata.IStructureDefn;
import org.eclipse.birt.report.model.api.metadata.UserChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.metadata.ChoiceSet;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.metadata.MetaDataDictionary;
import org.eclipse.birt.report.model.metadata.MetaDataException;
import org.eclipse.birt.report.model.metadata.MethodInfo;
import org.eclipse.birt.report.model.metadata.PropertyDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Represents a user-defined property. User-defined properties are created by
 * the user and reside on elements. If element E has a user-defined property,
 * and element C extends E, then element C also has all the user-defined
 * properties defined on element E.
 * <p>
 * The user property definition implements the <code>IStructure</code>
 * interface so that it can be accessed generically, and changes can be done
 * though the command mechanism to allow undo/redo of style changes.
 *  
 */

public final class UserPropertyDefn extends ElementPropertyDefn
		implements
			IStructure
{

	/**
	 * Display name for the property.
	 */

	private String displayName = null;

	/**
	 * Name of the type member.
	 */

	public static final String TYPE_MEMBER = "type"; //$NON-NLS-1$

	/**
	 * Name of the name member.
	 */

	public static final String NAME_MEMBER = "name"; //$NON-NLS-1$

	/**
	 * Name of the display name member.
	 */

	public static final String DISPLAY_NAME_MEMBER = "displayName"; //$NON-NLS-1$

	/**
	 * Name of the display name ID member.
	 */

	public static final String DISPLAY_NAME_ID_MEMBER = "displayNameID"; //$NON-NLS-1$

	/**
	 * Name of the structure itself. This is the name used to identify the
	 * structure in the meta-data dictionary.
	 */

	public static final String STRUCTURE_NAME = "UserProperty"; //$NON-NLS-1$

	/**
	 * Name of the choices member.
	 */

	public static final String CHOICES_MEMBER = "choices"; //$NON-NLS-1$

	/**
	 * Default constructor.
	 */

	public UserPropertyDefn( )
	{
		PropertyType typeDefn = MetaDataDictionary.getInstance( )
				.getPropertyType( PropertyType.STRING_TYPE_NAME );
		setType( typeDefn );
	}

	/**
	 * Gets the value of property by the given property definition.
	 * 
	 * @param design
	 *            the report design
	 * @param prop
	 *            definition of the property to get
	 * 
	 * @return value of the property.
	 */

	public Object getProperty( ReportDesign design, PropertyDefn prop )
	{
		return getLocalProperty( design, prop );
	}

	/**
	 * Sets the value for the given property definition.
	 * 
	 * @param prop
	 *            definition of the property to set
	 * @param value
	 *            value to set
	 */

	public void setProperty( PropertyDefn prop, Object value )
	{
		assert prop != null;
		String memberName = prop.getName( );
		if ( memberName.equals( TYPE_MEMBER ) )
		{
			type = MetaDataDictionary.getInstance( ).getPropertyType(
					(String) value );
		}
		else if ( memberName.equals( NAME_MEMBER ) )
		{
			if ( value == null )
				name = null;
			else
				name = value.toString( );
		}
		else if ( memberName.equals( DISPLAY_NAME_MEMBER ) )
		{
			if ( value == null )
				displayName = null;
			else
				displayName = value.toString( ); 
		}
		else if ( memberName.equals( DISPLAY_NAME_ID_MEMBER ) )
		{
			if ( value == null )
				displayNameID = null;
			else
				displayNameID = value.toString( );
		}
	}

	/**
	 * Gets the name predefined for this structure.
	 * 
	 * @return structure name "UserProperty".
	 *  
	 */

	public String getStructName( )
	{
		return STRUCTURE_NAME;
	}

	/**
	 * Gets the property type.
	 * 
	 * @return integer represented user property type.
	 *  
	 */

	public int getValueType( )
	{
		return USER_PROPERTY;
	}

	/**
	 * Makes a copy of this user property definition.
	 * 
	 * @return IStructure of this property definition, or null if this property
	 *         definition can not be cloned.
	 */

	public IStructure copy( )
	{
		try
		{
			UserPropertyDefn uDefn = (UserPropertyDefn) clone( );
			if ( details instanceof ChoiceSet )
			{
				uDefn.details = ( (ChoiceSet) details ).clone( );
			}
			return uDefn;
		}
		catch ( CloneNotSupportedException e )
		{
			assert false;
		}
		return null;
	}

	/**
	 * Gets the definition of the structure which represents the user property
	 * definition.
	 * 
	 * @return structure definition.
	 */

	public IStructureDefn getDefn( )
	{
		return MetaDataDictionary.getInstance( ).getStructure( STRUCTURE_NAME );
	}

	/**
	 * Gets the object definition of the user property definition.
	 * 
	 * @return object definition.
	 *  
	 */

	public IObjectDefn getObjectDefn( )
	{
		return MetaDataDictionary.getInstance( ).getStructure( STRUCTURE_NAME );
	}

	/**
	 * Gets the display name of this user property definition. The search will
	 * check the translation dictionary firstly, then look at the instance
	 * itself. If no display name defined, the XML name will be returned.
	 * 
	 * @return display name of this user property.
	 *  
	 */
	public String getDisplayName( )
	{
		// TODO: 1. lookup the displayName in translation dictionary.

		if ( !StringUtil.isBlank( displayName ) )
		{
			// 2. return displayName set on the instance.
			return displayName;
		}

		// 3. return XML name instead.
		return name;
	}

	/**
	 * Sets the display name of the property. Use this only for testing; you
	 * should normally set the display name message ID so that the name can be
	 * retrieved from a message catalog and localized.
	 * 
	 * @param theName
	 *            the display name to set
	 */

	public void setDisplayName( String theName )
	{
		displayName = theName;
	}

	/**
	 * Sets the (anonymous) set of choices for a property. The choices are
	 * stored here directly, they are not named and stored in the data
	 * dictionary as are choices for system properties.
	 * 
	 * @param choiceArray
	 *            choice array to be set.
	 */

	public void setChoices( UserChoice[] choiceArray )
	{
		if ( choiceArray == null )
		{
			details = null;
			return;
		}

		// Create an anonymous extended choice set to hold the choices.

		ChoiceSet choices = new ChoiceSet( null );
		choices.setChoices( choiceArray );
		details = choices;
	}

	/**
	 * Checks whether <code>displayName</code> matches any items in the choice
	 * set for an extended choice property type on a user defined choice set. If
	 * <code>displayName</code> exists in the choice set, return the name of
	 * this choice. Otherwise, return <code>null</code>.
	 * 
	 * @param design
	 *            the report design
	 * @param displayName
	 *            the candidate display name
	 * @return the choice name if found. Otherwise, return <code>null</code>.
	 */

	protected String validateExtendedChoicesByDisplayName( ReportDesign design,
			String displayName )
	{
		if ( displayName == null || hasChoices( ) == false )
			return null;

		IChoiceSet choiceSet = getChoices( );
		UserChoice choice = choiceSet.findUserChoiceByDisplayName( design,
				displayName );

		if ( choice != null )
			return choice.getName( );

		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.PropertyDefn#getStructDefn()
	 */

	public IStructureDefn getStructDefn( )
	{
		return MetaDataDictionary.getInstance( ).getStructure( STRUCTURE_NAME );
	}

	/**
	 * User-defined methods are not supported.
	 * 
	 * @return <code>null</code>
	 */

	public MethodInfo getMethodInfo( )
	{
		assert false;
		return null;
	}

	/**
	 * Sets the property type.
	 * 
	 * @param typeDefn
	 *            the property type
	 */

	public void setType( PropertyType typeDefn )
	{
		type = typeDefn;
	}

	/**
	 * Checks whether the element can take the given user property definition
	 * and the definition is valid.
	 * 
	 * @param element
	 *            the design element that holds the user-defined property
	 * @throws UserPropertyException
	 *             if the element is not allowed to have user property or the
	 *             user property definition is invalid.
	 * @throws MetaDataException
	 *             if the user property definition is inconsistent.
	 */

	public void checkUserPropertyDefn( DesignElement element )
			throws UserPropertyException, MetaDataException
	{
		// Does the element allow user properties?

		String name = getName( );
		if ( !element.getDefn( ).allowsUserProperties( ) )
			throw new UserPropertyException( element, name,
					UserPropertyException.DESIGN_EXCEPTION_USER_PROP_DISALLOWED );

		// Validate the name.

		if ( StringUtil.isBlank( name ) )
			throw new UserPropertyException( element, name,
					UserPropertyException.DESIGN_EXCEPTION_NAME_REQUIRED );

		if ( element.getPropertyDefn( name ) != null )
			throw new UserPropertyException( element, name,
					UserPropertyException.DESIGN_EXCEPTION_DUPLICATE_NAME );

		// Validate the property type is provided and not structure or element
		// reference.

		MetaDataDictionary dd = MetaDataDictionary.getInstance( );
		if ( dd.getPropertyType( getTypeCode( ) ) == null
				|| getTypeCode( ) == PropertyType.ELEMENT_REF_TYPE
				|| getTypeCode( ) == PropertyType.STRUCT_TYPE )
			throw new UserPropertyException( element, name,
					UserPropertyException.DESIGN_EXCEPTION_INVALID_TYPE );

		// Ensure choices exist if this is a choice typeCode.

		if ( getTypeCode( ) == PropertyType.CHOICE_TYPE )
		{
			IChoiceSet choices = getChoices( );
			if ( choices == null || choices.getChoices( ).length == 0 )
				throw new UserPropertyException( element, name,
						UserPropertyException.DESIGN_EXCEPTION_MISSING_CHOICES );
		}

		// Build the cached semantic data.

		this.build( );
	}

	/* (non-Javadoc)
	 * @see org.eclipse.birt.report.model.core.IStructure#getLocalProperty(org.eclipse.birt.report.model.elements.ReportDesign, org.eclipse.birt.report.model.metadata.PropertyDefn)
	 */
	
	public Object getLocalProperty( ReportDesign design, PropertyDefn propDefn )
	{
		assert propDefn != null;
		String memberName = propDefn.getName( );
		if ( memberName.equals( TYPE_MEMBER ) )
			return type.getName( );
		if ( memberName.equals( NAME_MEMBER ) )
			return name;
		if ( memberName.equals( DISPLAY_NAME_MEMBER ) )
			return displayName;
		if ( memberName.equals( DISPLAY_NAME_ID_MEMBER ) )
			return displayNameID;
		return null;
	}

}