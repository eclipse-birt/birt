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

import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Base class for the meta-data for property types. Every property has a
 * property type. The property type provides a display name, data validation
 * methods, an XML name, and more.
 * <p>
 * Note that the property type information is a partial description of a
 * property. Some types (such as choice) require further information specific to
 * the property, such as the actual list of choices.
 */

public interface IPropertyType
{

	/**
	 * Type code for the String property type.
	 */

	public static final int STRING_TYPE = PropertyType.STRING_TYPE;

	/**
	 * Type code for the Number property type.
	 */

	public static final int NUMBER_TYPE = PropertyType.NUMBER_TYPE;

	/**
	 * Type code for the Integer property type.
	 */

	public static final int INTEGER_TYPE = PropertyType.INTEGER_TYPE;

	/**
	 * Type code for the Dimension property type.
	 */

	public static final int DIMENSION_TYPE = PropertyType.DIMENSION_TYPE;

	/**
	 * Type code for the Color property type.
	 */

	public static final int COLOR_TYPE = PropertyType.COLOR_TYPE;

	/**
	 * Type code for the Choice property type.
	 */

	public static final int CHOICE_TYPE = PropertyType.CHOICE_TYPE;

	/**
	 * Type code for the Boolean property type.
	 */

	public static final int BOOLEAN_TYPE = PropertyType.BOOLEAN_TYPE;

	/**
	 * Type code for the Expression property type.
	 */

	public static final int EXPRESSION_TYPE = PropertyType.EXPRESSION_TYPE;

	/**
	 * Type code for the HTML property type.
	 */

	public static final int HTML_TYPE = PropertyType.HTML_TYPE;

	/**
	 * Type code for the resource key property type.
	 */

	public static final int RESOURCE_KEY_TYPE = PropertyType.RESOURCE_KEY_TYPE;

	/**
	 * Type code for the URI property type.
	 */

	public static final int URI_TYPE = PropertyType.URI_TYPE;

	/**
	 * Type code for the Date time property type.
	 */

	public static final int DATE_TIME_TYPE = PropertyType.DATE_TIME_TYPE;

	/**
	 * Type code for the XML property type.
	 */

	public static final int XML_TYPE = PropertyType.XML_TYPE;

	/**
	 * Type code for the Name property type.
	 */

	public static final int NAME_TYPE = PropertyType.NAME_TYPE;

	/**
	 * Type code for the Float property type.
	 */

	public static final int FLOAT_TYPE = PropertyType.FLOAT_TYPE;

	/**
	 * Type code for the Element reference property type.
	 */

	public static final int ELEMENT_REF_TYPE = PropertyType.ELEMENT_REF_TYPE;

	/**
	 * Type code for the Column reference property type.
	 */

	public static final int COLUMN_REF_TYPE = PropertyType.COLUMN_REF_TYPE;

	/**
	 * Type code for the Variant property type.
	 */

	public static final int VARIANT_TYPE = PropertyType.VARIANT_TYPE;

	/**
	 * Type code for the Structure property type.
	 */

	public static final int STRUCT_TYPE = PropertyType.STRUCT_TYPE;

	/**
	 * Type code for the Extends property type.
	 */

	public static final int EXTENDS_TYPE = PropertyType.EXTENDS_TYPE;

	/**
	 * Type code for the Script property type.
	 */

	public static final int SCRIPT_TYPE = PropertyType.SCRIPT_TYPE;

	/**
	 * Type code for the literal string property type.
	 */

	public static final int LITERAL_STRING_TYPE = PropertyType.LITERAL_STRING_TYPE;
	
	/**
	 * Number of types defined.
	 */

	public static final int TYPE_COUNT = 22;

	/**
	 * Name of the String property type.
	 */

	public static final String STRING_TYPE_NAME = PropertyType.STRING_TYPE_NAME;

	/**
	 * Name of the Number property type.
	 */

	public static final String NUMBER_TYPE_NAME = PropertyType.NUMBER_TYPE_NAME;

	/**
	 * Name of the Integer property type.
	 */

	public static final String INTEGER_TYPE_NAME = PropertyType.INTEGER_TYPE_NAME;

	/**
	 * Name of the Dimension property type.
	 */

	public static final String DIMENSION_TYPE_NAME = PropertyType.DIMENSION_TYPE_NAME;

	/**
	 * Name of the Color property type.
	 */

	public static final String COLOR_TYPE_NAME = PropertyType.COLOR_TYPE_NAME;

	/**
	 * Name of the Choice property type.
	 */

	public static final String CHOICE_TYPE_NAME = PropertyType.CHOICE_TYPE_NAME;

	/**
	 * Name of the Boolean property type.
	 */

	public static final String BOOLEAN_TYPE_NAME = PropertyType.BOOLEAN_TYPE_NAME;

	/**
	 * Name of the Expression property type.
	 */

	public static final String EXPRESSION_TYPE_NAME = PropertyType.EXPRESSION_TYPE_NAME;

	/**
	 * Name of the HTML property type.
	 */

	public static final String HTML_TYPE_NAME = PropertyType.HTML_TYPE_NAME;

	/**
	 * Name of the Resource Key property type.
	 */

	public static final String RESOURCE_KEY_TYPE_NAME = PropertyType.RESOURCE_KEY_TYPE_NAME;

	/**
	 * Name of the URI property type.
	 */

	public static final String URI_TYPE_NAME = PropertyType.URI_TYPE_NAME;

	/**
	 * Name of the Date Time property type.
	 */

	public static final String DATE_TIME_TYPE_NAME = PropertyType.DATE_TIME_TYPE_NAME;

	/**
	 * Name of the XML property type.
	 */

	public static final String XML_TYPE_NAME = PropertyType.XML_TYPE_NAME;

	/**
	 * Name of the Name property type.
	 */

	public static final String NAME_TYPE_NAME = PropertyType.NAME_TYPE_NAME;

	/**
	 * Name of the Float property type.
	 */

	public static final String FLOAT_TYPE_NAME = PropertyType.FLOAT_TYPE_NAME;

	/**
	 * Name of the Element reference property type.
	 */

	public static final String ELEMENT_REF_NAME = PropertyType.ELEMENT_REF_NAME;

	/**
	 * Name of the Column reference property type.
	 */

	public static final String COLUMN_REF_NAME = PropertyType.COLUMN_REF_NAME;

	/**
	 * Name of the Variant property type.
	 */

	public static final String VARIANT_NAME = PropertyType.VARIANT_NAME;

	/**
	 * Name of the Structure property type.
	 */

	public static final String STRUCT_TYPE_NAME = PropertyType.STRUCT_TYPE_NAME;

	/**
	 * Name of the Extends property type.
	 */

	public static final String EXTENDS_TYPE_NAME = PropertyType.EXTENDS_TYPE_NAME;

	/**
	 * Name of the Script property type.
	 */

	public static final String SCRIPT_TYPE_NAME = PropertyType.SCRIPT_TYPE_NAME;

	/**
	 * Name of the literal string property type.
	 */

	public static final String LITERAL_STRING_TYPE_NAME = PropertyType.LITERAL_STRING_TYPE_NAME;
	
	/**
	 * Returns the localized display name.
	 * 
	 * @return the localized display name
	 */

	public String getDisplayName( );

	/**
	 * Returns the numeric code for this type.
	 * 
	 * @return the internal type code
	 */

	public int getTypeCode( );

	/**
	 * Returns the name to use in the XML design and XML metadata files.
	 * 
	 * @return the type name used in the XML design file
	 */

	public String getName( );

	/**
	 * Gets the set of choices for this type.
	 * 
	 * @return the set of choices, or null if no choices are available
	 */

	public IChoiceSet getChoices( );

	/**
	 * Gets the display name resource key.
	 * 
	 * @return the display name message key
	 */

	public String getDisplayNameKey( );

}
