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

import java.math.BigDecimal;

import org.eclipse.birt.report.model.elements.DesignChoiceConstants;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.i18n.ThreadResources;
import org.eclipse.birt.report.model.util.StringUtil;
import org.eclipse.birt.report.model.validators.ElementReferenceValidator;
import org.eclipse.birt.report.model.validators.StructureListValidator;

/**
 * Base class for both element property and structure member definitions.
 */

public abstract class PropertyDefn implements IPropertyDefn
{

	/**
	 * Type code for a system property.
	 */

	public static final int SYSTEM_PROPERTY = 0;

	/**
	 * Type code for a user property.
	 */

	public static final int USER_PROPERTY = 1;

	/**
	 * Type code for a structure member.
	 */

	public static final int STRUCT_PROPERTY = 2;

	/**
	 * Type code for an extension property.
	 */

	public static final int EXTENSION_PROPERTY = 3;

	/**
	 * The cached property type.
	 */

	protected PropertyType type = null;

	/**
	 * The internal (non-localized) name for the property. This name is used in
	 * code.
	 */

	protected String name = null;

	/**
	 * The message catalog ID for the property display name.
	 */

	protected String displayNameID = null;

	/**
	 * True if the property definition is extended, which is defined by the
	 * extended element, false if the property is the BIRT system-defined or
	 * user-defined.
	 */

	protected boolean isExtended = false;

	/**
	 * Optional detailed information for the property type. The type of this
	 * object depends on the property type:
	 * 
	 * <p>
	 * <dl>
	 * <dt><strong>Choice set </strong></dt>
	 * <dd>details holds an object of type <code>ChoiceSet</code> that holds
	 * the list of available choices.</dd>
	 * 
	 * <dt><strong>Extended Choice set </strong></dt>
	 * <dd>details holds an object of type <code>ChoiceSet</code> that holds
	 * the list of available extended choices.</dd>
	 * 
	 * <dt><strong>User Defined Choice set </strong></dt>
	 * <dd>details holds an object of type <code>ChoiceSet</code> that holds
	 * the list of user defined choices.</dd>
	 * 
	 * <dt><strong>Element Reference </strong></dt>
	 * <dd>details holds an object of type <code>ElementDefn</code> that
	 * identifies the type of element that is referenced.</dd>
	 * 
	 * <dt><strong>Structure definition </strong></dt>
	 * <dd>details holds an object of type <code>StructureDefn</code> that
	 * defines the structures in the list.</li>
	 * 
	 * <dt><strong>Argument List </strong></dt>
	 * <dd>details holds a list of argument <code>ArgumentDefn</code>.</li>
	 * </dl>
	 */

	protected Object details = null;

	/**
	 * Whether this is an intrinsic property.
	 */

	protected boolean intrinsic = false;

	/**
	 * The default value, if any, for this property.
	 */

	protected Object defaultValue = null;

	/**
	 * Choice sets containing an allowed choices for a choice type, or
	 * containing an allowed units set for a dimension type.
	 */

	protected ChoiceSet allowedChoices = null;

	/**
	 * Indicates if this whether this property is a list. This property is
	 * useful only when the property type is a structure type.
	 *  
	 */

	protected boolean isList = false;

	/**
	 * Reference to the name of value validator applied to this property.
	 */

	protected String valueValidator = null;

	/**
	 * The collection of semantic validatin triggers.
	 */

	private SemanticTriggerDefns triggers = null;

	/**
	 * Constructs a Property Definition.
	 */

	public PropertyDefn( )
	{
	}

	/**
	 * Returns the type of this value, the return can be one of the following
	 * constants:
	 * <p>
	 * <ul>
	 * <li>SYSTEM_PROPERTY</li>
	 * <li>USER_PROPERTY</li>
	 * <li>STRUCT_PROPERTY</li>
	 * </ul>
	 * 
	 * @return the type of this definition
	 */

	public abstract int getValueType( );

	/**
	 * Builds the semantic information for this property. Called once while
	 * loading the meta-data. The build must succeed, or a programming error has
	 * occurred.
	 * 
	 * @throws MetaDataException
	 *             if the property definition is inconsistent.
	 */

	void build( ) throws MetaDataException
	{
		// Ensure we can find the property type.

		if ( getType( ) == null )
			throw new MetaDataException( new String[]{name},
					MetaDataException.DESIGN_EXCEPTION_PROP_TYPE_ERROR );

		displayNameID = StringUtil.trimString( displayNameID );

		// Perform type-specific initialization.

		switch ( type.getTypeCode( ) )
		{
			case PropertyType.CHOICE_TYPE :

				// Build the set of choices. The list is required if this
				// property
				// is a choice property, and is not allowed otherwise.

				if ( getChoices( ) == null )
					throw new MetaDataException(
							new String[]{name},
							MetaDataException.DESIGN_EXCEPTION_MISSING_PROP_CHOICES );
				break;

			case PropertyType.STRUCT_TYPE :

				// A structure definition must be provided.

				if ( getStructDefn( ) == null )
					throw new MetaDataException(
							new String[]{name},
							MetaDataException.DESIGN_EXCEPTION_MISSING_STRUCT_DEFN );

				if ( isList( ) )
				{
					StructureListValidator validator = StructureListValidator
							.getInstance( );
					SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(
							StructureListValidator.NAME );
					triggerDefn.setPropertyName( getName( ) );
					triggerDefn.setValidator( validator );
					getTriggers( ).addSemanticValidationDefn( triggerDefn );
				}
				break;

			case PropertyType.ELEMENT_REF_TYPE :
				if ( details == null )
					throw new MetaDataException(
							new String[]{name},
							MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE );

				// Look up a string name reference.

				if ( details instanceof String )
				{
					MetaDataDictionary dd = MetaDataDictionary.getInstance( );
					ElementDefn elementDefn = (ElementDefn) dd
							.getElement( StringUtil
									.trimString( (String) details ) );
					if ( elementDefn == null )
						throw new MetaDataException(
								new String[]{(String) details, name},
								MetaDataException.DESIGN_EXCEPTION_UNDEFINED_ELEMENT_TYPE );
					if ( elementDefn.getNameSpaceID( ) == MetaDataConstants.NO_NAME_SPACE )
						throw new MetaDataException(
								new String[]{(String) details, name},
								MetaDataException.DESIGN_EXCEPTION_UNNAMED_ELEMENT_TYPE );
					details = elementDefn;
				}

				// Otherwise, an element definition must be provided.

				else if ( getTargetElementType( ) == null )
					throw new MetaDataException(
							new String[]{name},
							MetaDataException.DESIGN_EXCEPTION_MISSING_ELEMENT_TYPE );

				SemanticTriggerDefn triggerDefn = new SemanticTriggerDefn(
						ElementReferenceValidator.NAME );
				triggerDefn.setPropertyName( getName( ) );
				triggerDefn.setValidator( ElementReferenceValidator
						.getInstance( ) );
				getTriggers( ).addSemanticValidationDefn( triggerDefn );
				break;
		}

		if ( getTypeCode( ) != PropertyType.STRUCT_TYPE && isList == true )
		{
			// only support list of structures.

			throw new MetaDataException( new String[]{getType( ).getName( )},
					MetaDataException.DESIGN_EXCEPTION_INVALID_LIST_TYPE );
		}

		// if the property has a defalut value, validate it again. At this time,
		// it will be validated against the allowed choices.

		if ( defaultValue != null )
		{
			try
			{
				validateXml( null, defaultValue.toString( ) );
			}
			catch ( PropertyValueException e )
			{
				throw new MetaDataException(
						new String[]{name, defaultValue.toString( )},
						MetaDataException.DESIGN_EXCEPTION_INVALID_DEFAULT_VALUE );
			}
		}

		// ensure that the validator is defined in the dictionary.

		if ( valueValidator != null )
		{
			MetaDataDictionary dict = MetaDataDictionary.getInstance( );
			if ( dict.getValueValidator( valueValidator ) == null )
				throw new MetaDataException(
						new String[]{valueValidator, name},
						MetaDataException.DESIGN_EXCEPTION_VALIDATOR_NOT_FOUND );
		}

		getTriggers( ).build( );

	}

	/**
	 * Determines whether this is a system-defined property. Must be overridden
	 * by derived classes.
	 * 
	 * @return true if a system-defined property, otherwise false
	 */

	public boolean isSystemProperty( )
	{
		return getValueType( ) == SYSTEM_PROPERTY;
	}

	/**
	 * Determines whether this is an extension-defined property. Must be
	 * overridden by derived classes.
	 * 
	 * @return true if it is an extension-defined property, otherwise false
	 */

	public boolean isExtensionProperty( )
	{
		return getValueType( ) == EXTENSION_PROPERTY;
	}

	/**
	 * Determines whether this is a user-defined property.
	 * 
	 * @return True if a user-defined property
	 */

	public boolean isUserProperty( )
	{
		return getValueType( ) == USER_PROPERTY;
	}

	/**
	 * Determines whether this is a structure member.
	 * 
	 * @return true if a structure member
	 */

	public boolean isStructureMember( )
	{
		return getValueType( ) == STRUCT_PROPERTY;
	}

	/**
	 * Returns the internal name for the property.
	 * 
	 * @return the internal (non-localized) name for the property
	 */

	public String getName( )
	{
		return name;
	}

	/**
	 * Returns the property type. See the list in MetaDataConstants.
	 * 
	 * @return he property type code
	 */

	public int getTypeCode( )
	{
		return type.getTypeCode( );
	}

	/**
	 * Gets the property type object for this property.
	 * 
	 * @return the property type object
	 */

	public PropertyType getType( )
	{
		return type;
	}

	/**
	 * Checks whether <code>value</code> exists in the choice set for an
	 * extended choice property type. If <code>value</code> exists in the
	 * choice set, return this value. Otherwise, return null.
	 * 
	 * @param value
	 *            the candidate value
	 * @return the internal choice name if found. Otherwise, return
	 *         <code>null</code>.
	 */

	private String validateExtendedChoicesByName( Object value )
	{
		if ( value == null || hasChoices( ) == false )
			return null;

		ChoiceSet choiceSet = getChoices( );
		Choice choice = choiceSet.findChoice( value.toString( ) );

		if ( choice != null )
			return choice.getName( );

		return null;
	}

	/**
	 * Checks whether <code>displayName</code> matches any items in the choice
	 * set for an extended choice property type. If <code>displayName</code>
	 * exists in the choice set, return the name of this choice. Otherwise,
	 * return <code>null</code>.
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

		ChoiceSet choiceSet = getChoices( );
		Choice choice = choiceSet.findChoiceByDisplayName( displayName );

		if ( choice != null )
			return choice.getName( );

		return null;
	}

	/**
	 * Validates a value to be stored for this value definition. This method
	 * checks names of choice properties first. Then, checks display names of
	 * choice properties. Then uses type to validate value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the candidate value
	 * @return the translated value to be stored
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	public Object validateValue( ReportDesign design, Object value )
			throws PropertyValueException
	{

		Object retValue = null;

		// Validates from extended choices.

		if ( hasChoices( ) && type.getTypeCode( ) != PropertyType.CHOICE_TYPE )
		{
			retValue = validateExtendedChoicesByName( value );

			if ( retValue == null && value != null )
				retValue = validateExtendedChoicesByDisplayName( design, value
						.toString( ) );

			if ( retValue != null )
				return retValue;
		}

		// Property type validation

		retValue = type.validateValue( design, this, value );

		// Per-property validations using a specific validator.

		if ( valueValidator != null )
			MetaDataDictionary.getInstance( )
					.getValueValidator( valueValidator ).validate( design,
							this, retValue );

		return retValue;
	}

	/**
	 * Validates an XML value to be stored for this value definition. This
	 * method checks names of predefined choice properties first. Then uses type
	 * to validate value. If the property definition has a validator, uses this
	 * validator to validate the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the candidate value
	 * @return the translated value to be stored
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	public Object validateXml( ReportDesign design, String value )
			throws PropertyValueException
	{
		Object retValue = null;

		// Validates from extended choices.

		if ( hasChoices( ) && type.getTypeCode( ) != PropertyType.CHOICE_TYPE )
		{
			retValue = validateExtendedChoicesByName( value );

			if ( retValue != null )
				return retValue;
		}

		// Property type validation

		retValue = getType( ).validateXml( design, this, value );

		// Per-property validations using a specific validator.

		if ( valueValidator != null )
			MetaDataDictionary.getInstance( )
					.getValueValidator( valueValidator ).validate( design,
							this, retValue );

		return retValue;
	}

	/**
	 * Returns the display name for the property.
	 * 
	 * @return the user-visible, localized display name for the property
	 */

	public String getDisplayName( )
	{
		assert displayNameID != null;
		return ThreadResources.getMessage( this.displayNameID );
	}

	/**
	 * Sets the internal name of the property.
	 * 
	 * @param theName
	 *            the internal property name
	 */

	public void setName( String theName )
	{
		name = theName;
	}

	/**
	 * Gets the list of choices for the property.
	 * 
	 * @return the list of choices
	 */

	public ChoiceSet getChoices( )
	{
		if ( details instanceof ChoiceSet )
			return (ChoiceSet) details;
		return null;
	}

	/**
	 * Checks if a property has a set of choices whatever choice is choice,
	 * extended choice or user defined choice.
	 * 
	 * @return true if it has, otherwise false.
	 */

	public boolean hasChoices( )
	{
		return getChoices( ) != null;
	}

	/**
	 * Returns the message id for the display name.
	 * 
	 * @return The display name message ID.
	 */

	public String getDisplayNameID( )
	{
		return displayNameID;
	}

	/**
	 * Sets the detailed information for the property.
	 * <p>
	 * <ul>
	 * <li>Choice: details holds an object of type <code>ChoiceSet</code>
	 * that holds the list of available choices.</li>
	 * <li>Element Ref: details holds an object of type
	 * <code>ElementDefn</code> that identifies the type of element that can
	 * be referenced.</li>
	 * <li>Structure List: details holds an object of type
	 * <code>StructureDefn</code> that defines the structures in the list.
	 * </li>
	 * </ul>
	 * 
	 * @param obj
	 *            the details object to set
	 */

	public void setDetails( Object obj )
	{
		details = obj;
	}

	/**
	 * Sets the message ID for the display name.
	 * 
	 * @param id
	 *            message ID for the display name
	 */

	public void setDisplayNameID( String id )
	{
		displayNameID = id;
	}

	/**
	 * Gets the XML value for a value of this type.
	 * 
	 * This method checks the predefined choice properties first. If has not,
	 * then uses type to return the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the XML value string
	 */

	public String getXmlValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return null;

		String retValue = validateExtendedChoicesByName( value );
		return retValue == null ? type.toXml( design, this, value ) : retValue;
	}

	/**
	 * Returns a value as a locale independent string.
	 * 
	 * This method checks the predefined choice properties first. If has not,
	 * then uses type to return the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the XML value string
	 */

	public String getStringValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return null;

		String retValue = validateExtendedChoicesByName( value );
		return retValue == null
				? type.toString( design, this, value )
				: retValue;
	}

	/**
	 * Returns a value as a <code>double</code>.
	 * 
	 * This method checks the predefined choice properties first. If has not,
	 * then uses type to return the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the value as <code>double</code>
	 */

	public double getFloatValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return 0.0;

		String retValue = validateExtendedChoicesByName( value );
		return retValue == null ? type.toDouble( design, value ) : 0.0d;
	}

	/**
	 * Returns a value as a <code>int</code>.
	 * 
	 * This method checks the predefined choice properties first. If has not,
	 * then uses type to return the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the value as <code>int</code>
	 */

	public int getIntValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return 0;

		String retValue = validateExtendedChoicesByName( value );
		return retValue == null ? type.toInteger( design, value ) : 0;
	}

	/**
	 * Returns a value as a <code>BigDecimal</code>.
	 * 
	 * This method checks the predefined choice properties first. If has not,
	 * then uses type to return the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the value as <code>BigDecimal</code>
	 */

	public BigDecimal getNumberValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return null;

		String retValue = validateExtendedChoicesByName( value );
		return retValue == null ? type.toNumber( design, value ) : null;
	}

	/**
	 * Returns a value as a <code>boolean</code>.
	 * 
	 * This method checks the predefined choice properties first. If has not,
	 * then uses type to return the value.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the value as <code>boolean</code>
	 */

	public boolean getBooleanValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return false;

		String retValue = validateExtendedChoicesByName( value );
		return retValue == null ? type.toBoolean( design, value ) : false;
	}

	/**
	 * Returns the localized string value of a property.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the internal value
	 * @return the property as a localized string
	 */

	public String getDisplayValue( ReportDesign design, Object value )
	{
		if ( value == null )
			return null;

		String retValue = validateExtendedChoicesByName( value );

		if ( retValue == null )
			return type.toDisplayString( design, this, value );

		return getChoices( ).findChoice( value.toString( ) ).getDisplayName( );

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
	 * Returns the structure definition for this value.
	 * 
	 * @return the structure definition, or null if this value is not a list of
	 *         structures
	 */

	public IStructureDefn getStructDefn( )
	{
		if ( details instanceof StructureDefn )
			return (StructureDefn) details;
		return null;
	}

	/**
	 * Returns the default value for the property.
	 * 
	 * @return The default value.
	 */

	public Object getDefault( )
	{
		return defaultValue;
	}

	/**
	 * Sets the default value for the property.
	 * 
	 * @param value
	 *            The default value to set.
	 */

	protected void setDefault( Object value )
	{
		defaultValue = value;
	}

	/**
	 * Indicates whether the property is intrinsic or not. An intrinsic property
	 * is a system one represented by a member variable.
	 * 
	 * @return true if the property is intrinsic, false if it is a "normal"
	 *         property
	 */

	public boolean isIntrinsic( )
	{
		return intrinsic;
	}

	/**
	 * Sets the property as intrinsic.
	 * 
	 * @param flag
	 *            true if the property is intrinsic, false otherwise
	 */

	void setIntrinsic( boolean flag )
	{
		intrinsic = flag;
	}

	/**
	 * Return the element type associated with this property.
	 * 
	 * @return the element type associated with the property
	 */

	public IElementDefn getTargetElementType( )
	{
		if ( details instanceof ElementDefn )
			return (ElementDefn) details;
		return null;
	}

	/**
	 * Returns the allowed choices for this property. It contains allowed
	 * choices for a choice type, or containing an allowed units set for a
	 * dimension type.
	 * <p>
	 * If a property has not defined the restriction, then whole set will be
	 * returned.
	 * 
	 * @return Returns the allowed choices of this property.
	 */

	public ChoiceSet getAllowedChoices( )
	{
		if ( allowedChoices != null )
			return allowedChoices;

		if ( getTypeCode( ) == PropertyType.DIMENSION_TYPE )
			return MetaDataDictionary.getInstance( ).getChoiceSet(
					DesignChoiceConstants.CHOICE_UNITS );

		return getChoices( );
	}

	/**
	 * Sets the allowed choices for this property
	 * 
	 * @param allowedChoices
	 *            The allowed choices to set.
	 */

	void setAllowedChoices( ChoiceSet allowedChoices )
	{
		this.allowedChoices = allowedChoices;
	}

	/**
	 * Set a validator.
	 * 
	 * @param validator
	 */

	void setValueValidator( String validator )
	{
		this.valueValidator = validator;
	}

	/**
	 * Indicates whether the property is defined by the extended element.
	 * 
	 * @return true if the property is defined by the extended element, false if
	 *         the property is BIRT system-defined or user-defined
	 */

	public boolean isExtended( )
	{
		return this.isExtended;
	}

	/**
	 * Sets the property as extended.
	 * 
	 * @param isExtended
	 *            true if the property is defined by the extended element, false
	 *            if the property is BIRT system-defined or user-defined
	 */

	void setExtended( boolean isExtended )
	{
		this.isExtended = isExtended;
	}

	/**
	 * Indicates whether this property is a list. It is useful only when the
	 * property type is a structure type.
	 * 
	 * @return whether the property is a list or not.
	 */

	public boolean isList( )
	{
		return isList;
	}

	/**
	 * Set if the property is a list.
	 * 
	 * @param isList
	 *            whether the property is a list or not.
	 */

	protected void setIsList( boolean isList )
	{
		this.isList = isList;
	}

	/**
	 * Returns the semantic validation trigger collection.
	 * 
	 * @return the semantic validation triggers
	 */

	public SemanticTriggerDefns getTriggers( )
	{
		if ( triggers == null )
			triggers = new SemanticTriggerDefns( );

		return triggers;
	}

}