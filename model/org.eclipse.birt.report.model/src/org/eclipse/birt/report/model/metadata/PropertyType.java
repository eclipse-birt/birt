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

import org.eclipse.birt.report.model.api.metadata.IChoiceSet;
import org.eclipse.birt.report.model.api.metadata.IPropertyType;
import org.eclipse.birt.report.model.api.metadata.PropertyValueException;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.elements.ReportDesign;
import org.eclipse.birt.report.model.i18n.ModelMessages;

/**
 * Base class for the meta-data for property types. Every property has a
 * property type. The property type provides a display name, data validation
 * methods, an XML name, and more.
 * <p>
 * Note that the property type information is a partial description of a
 * property. Some types (such as choice) require further information specific to
 * the property, such as the actual list of choices.
 * <p>
 * The conversion and validation methods require a handle to the report design.
 * The design provides additional information for those conversions that require
 * it. For example, dimensions require knowledge of the default units for the
 * design. Colors require access to the custom colors defined on the design.
 */

public abstract class PropertyType implements IPropertyType
{

	/**
	 * The resource key for the localized property display name.
	 */

	private String displayNameKey = null;

	/**
	 * Type code for the String property type.
	 */

	public static final int STRING_TYPE = 0;

	/**
	 * Type code for the Number property type.
	 */

	public static final int NUMBER_TYPE = 1;

	/**
	 * Type code for the Integer property type.
	 */

	public static final int INTEGER_TYPE = 2;

	/**
	 * Type code for the Dimension property type.
	 */

	public static final int DIMENSION_TYPE = 3;

	/**
	 * Type code for the Color property type.
	 */

	public static final int COLOR_TYPE = 4;

	/**
	 * Type code for the Choice property type.
	 */

	public static final int CHOICE_TYPE = 5;

	/**
	 * Type code for the Boolean property type.
	 */

	public static final int BOOLEAN_TYPE = 6;

	/**
	 * Type code for the Expression property type.
	 */

	public static final int EXPRESSION_TYPE = 7;

	/**
	 * Type code for the HTML property type.
	 */

	public static final int HTML_TYPE = 8;

	/**
	 * Type code for the resource key property type.
	 */

	public static final int RESOURCE_KEY_TYPE = 9;

	/**
	 * Type code for the URI property type.
	 */

	public static final int URI_TYPE = 10;

	/**
	 * Type code for the Date time property type.
	 */

	public static final int DATE_TIME_TYPE = 11;

	/**
	 * Type code for the XML property type.
	 */

	public static final int XML_TYPE = 12;

	/**
	 * Type code for the Name property type.
	 */

	public static final int NAME_TYPE = 13;

	/**
	 * Type code for the Float property type.
	 */

	public static final int FLOAT_TYPE = 14;

	/**
	 * Type code for the Element reference property type.
	 */

	public static final int ELEMENT_REF_TYPE = 15;

	/**
	 * Type code for the Column reference property type.
	 */

	public static final int COLUMN_REF_TYPE = 16;

	/**
	 * Type code for the Variant property type.
	 */

	public static final int VARIANT_TYPE = 17;

	/**
	 * Type code for the Structure property type.
	 */

	public static final int STRUCT_TYPE = 18;

	/**
	 * Type code for the Extends property type.
	 */

	public static final int EXTENDS_TYPE = 19;

	/**
	 * Type code for the Script property type.
	 */

	public static final int SCRIPT_TYPE = 20;

	/**
	 * Type code for the literal string property type.
	 */

	public static final int LITERAL_STRING_TYPE = 21;

	/**
	 * Number of types defined.
	 */

	public static final int TYPE_COUNT = 22;

	/**
	 * Name of the String property type.
	 */

	public static final String STRING_TYPE_NAME = "string"; //$NON-NLS-1$

	/**
	 * Name of the Number property type.
	 */

	public static final String NUMBER_TYPE_NAME = "number"; //$NON-NLS-1$

	/**
	 * Name of the Integer property type.
	 */

	public static final String INTEGER_TYPE_NAME = "integer"; //$NON-NLS-1$

	/**
	 * Name of the Dimension property type.
	 */

	public static final String DIMENSION_TYPE_NAME = "dimension"; //$NON-NLS-1$

	/**
	 * Name of the Color property type.
	 */

	public static final String COLOR_TYPE_NAME = "color"; //$NON-NLS-1$

	/**
	 * Name of the Choice property type.
	 */

	public static final String CHOICE_TYPE_NAME = "choice"; //$NON-NLS-1$

	/**
	 * Name of the Boolean property type.
	 */

	public static final String BOOLEAN_TYPE_NAME = "boolean"; //$NON-NLS-1$

	/**
	 * Name of the Expression property type.
	 */

	public static final String EXPRESSION_TYPE_NAME = "expression"; //$NON-NLS-1$

	/**
	 * Name of the HTML property type.
	 */

	public static final String HTML_TYPE_NAME = "html"; //$NON-NLS-1$

	/**
	 * Name of the Resource Key property type.
	 */

	public static final String RESOURCE_KEY_TYPE_NAME = "resourceKey"; //$NON-NLS-1$

	/**
	 * Name of the Points property type.
	 */

	public static final String POINTS_TYPE_NAME = "points"; //$NON-NLS-1$

	/**
	 * Name of the URI property type.
	 */

	public static final String URI_TYPE_NAME = "uri"; //$NON-NLS-1$

	/**
	 * Name of the Date Time property type.
	 */

	public static final String DATE_TIME_TYPE_NAME = "dateTime"; //$NON-NLS-1$

	/**
	 * Name of the XML property type.
	 */

	public static final String XML_TYPE_NAME = "xml"; //$NON-NLS-1$

	/**
	 * Name of the Name property type.
	 */

	public static final String NAME_TYPE_NAME = "name"; //$NON-NLS-1$

	/**
	 * Name of the Float property type.
	 */

	public static final String FLOAT_TYPE_NAME = "float"; //$NON-NLS-1$

	/**
	 * Name of the Element reference property type.
	 */

	public static final String ELEMENT_REF_NAME = "element"; //$NON-NLS-1$

	/**
	 * Name of the Column reference property type.
	 */

	public static final String COLUMN_REF_NAME = "column"; //$NON-NLS-1$

	/**
	 * Name of the Variant property type.
	 */

	public static final String VARIANT_NAME = "variant"; //$NON-NLS-1$

	/**
	 * Name of the Structure property type.
	 */

	public static final String STRUCT_TYPE_NAME = "structure"; //$NON-NLS-1$

	/**
	 * Name of the Extends property type.
	 */

	public static final String EXTENDS_TYPE_NAME = "extends"; //$NON-NLS-1$

	/**
	 * Name of the Script property type.
	 */

	public static final String SCRIPT_TYPE_NAME = "script"; //$NON-NLS-1$

	/**
	 * Name of the literal string property type.
	 */

	public static final String LITERAL_STRING_TYPE_NAME = "literalString"; //$NON-NLS-1$

	/**
	 * Constructs a property type given its display name id.
	 * <p>
	 * Instances of this class should be created only when creating the
	 * meta-data dictionary.
	 * 
	 * @param displayNameID
	 *            the resource key for this property type's display name
	 */

	protected PropertyType( String displayNameID )
	{
		setDisplayNameKey( displayNameID );
	}

	/**
	 * Returns the localized display name.
	 * 
	 * @return the localized display name
	 */

	public String getDisplayName( )
	{
		assert displayNameKey != null;
		return ModelMessages.getMessage( displayNameKey );

	}

	/**
	 * Returns the numeric code for this type.
	 * 
	 * @return the internal type code
	 */

	public abstract int getTypeCode( );

	/**
	 * Returns the name to use in the XML design and XML metadata files.
	 * 
	 * @return the type name used in the XML design file
	 */

	public abstract String getName( );

	/**
	 * Internal method to build the property type. Should be called only from
	 * <code>MetaDataDictionary</code>.
	 */

	protected void build( )
	{
	}

	/**
	 * Sets the display name resource key.
	 * 
	 * @param key
	 *            message key to set
	 */

	void setDisplayNameKey( String key )
	{
		displayNameKey = key;
	}

	/**
	 * Sets the set of choices.
	 * 
	 * @param theChoices
	 *            the choices to set
	 */

	void setChoices( ChoiceSet theChoices )
	{
		if ( theChoices == null )
			return;

		// Default implementation does nothing. Must be overridden
		// by derived types that support choices.

		assert false;
	}

	/**
	 * Gets the set of choices for this type.
	 * 
	 * @return the set of choices, or null if no choices are available
	 */

	public IChoiceSet getChoices( )
	{
		return null;
	}

	/**
	 * Gets the display name resource key.
	 * 
	 * @return the display name message key
	 */

	public String getDisplayNameKey( )
	{
		return displayNameKey;
	}

	/**
	 * Validates a value for this property. The value is one that comes from the
	 * user or program. A string value is assumed to be in the user's locale.
	 * Many properties accept values of several types; see the specific property
	 * type for details.
	 * <p>
	 * This method also does any necessary conversions. For example, it converts
	 * a string representation of a number into the standard internal type;
	 * converts string dimension values into the internal dimension object, etc.
	 * The return value is what should be stored in the property list when
	 * setting a property.
	 * 
	 * @param design
	 *            the design used to resolve expressions, names, unit
	 *            conversions, custom colors, etc.
	 * @param defn
	 *            optional property definition that provides additional
	 *            information such as a choice list
	 * @param value
	 *            the value to be validated
	 * @return the validated value if the value is valid
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	abstract public Object validateValue( ReportDesign design,
			PropertyDefn defn, Object value ) throws PropertyValueException;

	/**
	 * Validate a user input value for this property, the value is one that
	 * comes from the user input. The string value user entered is assumed to be
	 * in the user's locale. This method convert the locale-depedent user input
	 * into a standard internal type. The return value is what the specific type
	 * should be stored internally.
	 * 
	 * @param design
	 *            the design used to resolve expressions, names, unit
	 *            conversions, custom colors, etc.
	 * @param defn
	 *            optional property definition that provides additional
	 *            information such as a choice list
	 * @param value
	 *            the input string value to be validated
	 * @return the validated value if the input value is valid.
	 * @throws PropertyValueException
	 */
	public Object validateInputString( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		return validateValue( design, defn, value );
	}

	/**
	 * Validates the XML representation of the value of a property of this
	 * property type. The XML value is given as a string read from an XML file.
	 * <p>
	 * The default implementation does the same validation as for user input.
	 * Derived type classes override this when XML-specific behavior is
	 * required, especially for dates and numbers.
	 * 
	 * @param design
	 *            the design used to resolve expressions, names, unit
	 *            conversions, custom colors, etc.
	 * @param defn
	 *            optional property definition that provides additional
	 *            information such as a choice list
	 * @param value
	 *            the value read from the XML file
	 * @return the object to store for the property value
	 * @throws PropertyValueException
	 *             if the value is not valid
	 */

	public Object validateXml( ReportDesign design, PropertyDefn defn,
			String value ) throws PropertyValueException
	{
		return validateValue( design, defn, StringUtil.trimString( value ) );
	}

	/**
	 * Converts a property of this type to a double value. If the value does not
	 * convert to a double, return 0.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the value of a parameter of this type
	 * @return the value as a double, or 0 if the value does not convert to a
	 *         double.
	 */

	public double toDouble( ReportDesign design, Object value )
	{
		return toInteger( design, value );
	}

	/**
	 * Converts a property of this type to a integer value. If the value does
	 * not convert to a integer, return 0.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            The value of a parameter of this type.
	 * @return the value as an integer, or 0 if the value does not convert to an
	 *         integer
	 */

	public int toInteger( ReportDesign design, Object value )
	{
		// Default implementation is for types that cannot
		// be converted to integer.

		return 0;
	}

	/**
	 * Converts the property value to an XML string.
	 * <p>
	 * Use this form when providing an additional property-specific choice set
	 * to use in the conversion.
	 * 
	 * @param design
	 *            the report design
	 * @param defn
	 *            optional property definition that provides additional
	 *            information such as a choice list
	 * @param value
	 *            the property value. Must be valid.
	 * @return the XML representation of the property.
	 */

	public String toXml( ReportDesign design, PropertyDefn defn, Object value )
	{
		return toString( design, defn, value );
	}

	/**
	 * Converts the property value to a locale-independent string.
	 * <p>
	 * Use this form when providing an additional property-specific choice set
	 * to use in the conversion.
	 * 
	 * @param design
	 *            the report design
	 * @param defn
	 *            optional property definition that provides additional
	 *            information such as a choice list
	 * @param value
	 *            the property value. Must be valid.
	 * @return the XML representation of the property.
	 */

	public abstract String toString( ReportDesign design, PropertyDefn defn,
			Object value );

	/**
	 * Returns the localized string value of a property.
	 * 
	 * @param design
	 *            the report design
	 * @param defn
	 *            optional property definition that provides additional
	 *            information such as a choice list
	 * @param value
	 *            the internal value
	 * @return the property as a localized string
	 */

	public String toDisplayString( ReportDesign design, PropertyDefn defn,
			Object value )
	{
		return toString( design, defn, value );
	}

	/**
	 * Converts the property value to a number (<code>BigDecimal</code>).
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the property value. Must be valid.
	 * @return the value of the property as a <code>BigDecimal</code>
	 */

	public BigDecimal toNumber( ReportDesign design, Object value )
	{
		return new BigDecimal( toDouble( design, value ) );
	}

	/**
	 * Converts the property value to a Boolean.
	 * 
	 * @param design
	 *            the report design
	 * @param value
	 *            the property value. Must be valid.
	 * @return the value of the property as a Boolean
	 */

	public boolean toBoolean( ReportDesign design, Object value )
	{
		return toInteger( design, value ) != 0;
	}

}