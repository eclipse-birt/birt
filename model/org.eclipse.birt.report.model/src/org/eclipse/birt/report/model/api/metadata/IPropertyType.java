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

/**
 * Base class for the meta-data for property types. Every property has a
 * property type. The property type provides a display name, data validation
 * methods, an XML name, and more.
 * <p>
 * Note that the property type information is a partial description of a
 * property. Some types (such as choice) require further information specific to
 * the property, such as the actual list of choices.
 */

public interface IPropertyType {

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
	 * Type code for the Structure property type.
	 */

	public static final int STRUCT_TYPE = 16;

	/**
	 * Type code for the Extends property type.
	 */

	public static final int EXTENDS_TYPE = 17;

	/**
	 * Type code for the Script property type.
	 */

	public static final int SCRIPT_TYPE = 18;

	/**
	 * Type code for the structure reference property type.
	 */

	public static final int STRUCT_REF_TYPE = 19;

	/**
	 * Type code for the list property type.
	 */

	public static final int LIST_TYPE = 20;

	/**
	 * Type code for the Literal String property type.
	 */

	public static final int LITERAL_STRING_TYPE = 21;

	/**
	 * Type code for the key property type of a structure member.
	 */

	public static final int MEMBER_KEY_TYPE = 22;

	/**
	 * Type code for element property type.
	 */
	public static final int ELEMENT_TYPE = 23;

	/**
	 * Type code for element attribute type. This is different from ELEMENT_TYPE.
	 * Its behavior like STRUCT_TYPE and isList=true.
	 */

	public static final int CONTENT_ELEMENT_TYPE = 24;

	/**
	 * Type code for locale property type.
	 */
	public static final int LOCALE_TYPE = 25;

	/**
	 * Number of types defined.
	 */

	public static final int TYPE_COUNT = 26;

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

	public static final String ELEMENT_REF_NAME = "elementRef"; //$NON-NLS-1$

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
	 * Name of the structure reference property type.
	 */

	public static final String STRUCT_REF_TYPE_NAME = "structRef"; //$NON-NLS-1$

	/**
	 * Name of the list property type.
	 */

	public static final String LIST_TYPE_NAME = "list"; //$NON-NLS-1$

	/**
	 * Name of the Literal String property type.
	 */

	public static final String LITERAL_STRING_TYPE_NAME = "literalString"; //$NON-NLS-1$

	/**
	 * Name the key property type of a structure member.
	 */

	public static final String MEMBER_KEY_NAME = "memberKey"; //$NON-NLS-1$

	/**
	 * Name of the element property type.
	 */
	public static final String ELEMENT_TYPE_NAME = "element"; //$NON-NLS-1$

	/**
	 * Type code for element attribute type. This is different from ELEMENT_TYPE.
	 * Its behavior like STRUCT_TYPE and isList=true.
	 */

	public static final String CONTENT_ELEMENT_TYPE_NAME = "contentElement"; //$NON-NLS-1$

	/**
	 * Name of the locale property type.
	 */
	public static final String LOCALE_TYPE_NAME = "locale"; //$NON-NLS-1$

	/**
	 * Returns the localized display name.
	 * 
	 * @return the localized display name
	 */

	public String getDisplayName();

	/**
	 * Returns the numeric code for this type.
	 * 
	 * @return the internal type code
	 */

	public int getTypeCode();

	/**
	 * Returns the name to use in the XML design and XML metadata files.
	 * 
	 * @return the type name used in the XML design file
	 */

	public String getName();

	/**
	 * Gets the set of choices for this type.
	 * 
	 * @return the set of choices, or null if no choices are available
	 */

	public IChoiceSet getChoices();

	/**
	 * Gets the display name resource key.
	 * 
	 * @return the display name message key
	 */

	public String getDisplayNameKey();

}
