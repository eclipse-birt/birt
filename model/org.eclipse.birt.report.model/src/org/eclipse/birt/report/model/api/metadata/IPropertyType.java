/*******************************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 *
 * SPDX-License-Identifier: EPL-2.0
 *
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

	int STRING_TYPE = 0;

	/**
	 * Type code for the Number property type.
	 */

	int NUMBER_TYPE = 1;

	/**
	 * Type code for the Integer property type.
	 */

	int INTEGER_TYPE = 2;

	/**
	 * Type code for the Dimension property type.
	 */

	int DIMENSION_TYPE = 3;

	/**
	 * Type code for the Color property type.
	 */

	int COLOR_TYPE = 4;

	/**
	 * Type code for the Choice property type.
	 */

	int CHOICE_TYPE = 5;

	/**
	 * Type code for the Boolean property type.
	 */

	int BOOLEAN_TYPE = 6;

	/**
	 * Type code for the Expression property type.
	 */

	int EXPRESSION_TYPE = 7;

	/**
	 * Type code for the HTML property type.
	 */

	int HTML_TYPE = 8;

	/**
	 * Type code for the resource key property type.
	 */

	int RESOURCE_KEY_TYPE = 9;

	/**
	 * Type code for the URI property type.
	 */

	int URI_TYPE = 10;

	/**
	 * Type code for the Date time property type.
	 */

	int DATE_TIME_TYPE = 11;

	/**
	 * Type code for the XML property type.
	 */

	int XML_TYPE = 12;

	/**
	 * Type code for the Name property type.
	 */

	int NAME_TYPE = 13;

	/**
	 * Type code for the Float property type.
	 */

	int FLOAT_TYPE = 14;

	/**
	 * Type code for the Element reference property type.
	 */

	int ELEMENT_REF_TYPE = 15;

	/**
	 * Type code for the Structure property type.
	 */

	int STRUCT_TYPE = 16;

	/**
	 * Type code for the Extends property type.
	 */

	int EXTENDS_TYPE = 17;

	/**
	 * Type code for the Script property type.
	 */

	int SCRIPT_TYPE = 18;

	/**
	 * Type code for the structure reference property type.
	 */

	int STRUCT_REF_TYPE = 19;

	/**
	 * Type code for the list property type.
	 */

	int LIST_TYPE = 20;

	/**
	 * Type code for the Literal String property type.
	 */

	int LITERAL_STRING_TYPE = 21;

	/**
	 * Type code for the key property type of a structure member.
	 */

	int MEMBER_KEY_TYPE = 22;

	/**
	 * Type code for element property type.
	 */
	int ELEMENT_TYPE = 23;

	/**
	 * Type code for element attribute type. This is different from ELEMENT_TYPE.
	 * Its behavior like STRUCT_TYPE and isList=true.
	 */

	int CONTENT_ELEMENT_TYPE = 24;

	/**
	 * Type code for locale property type.
	 */
	int LOCALE_TYPE = 25;

	/**
	 * Number of types defined.
	 */

	int TYPE_COUNT = 26;

	/**
	 * Name of the String property type.
	 */

	String STRING_TYPE_NAME = "string"; //$NON-NLS-1$

	/**
	 * Name of the Number property type.
	 */

	String NUMBER_TYPE_NAME = "number"; //$NON-NLS-1$

	/**
	 * Name of the Integer property type.
	 */

	String INTEGER_TYPE_NAME = "integer"; //$NON-NLS-1$

	/**
	 * Name of the Dimension property type.
	 */

	String DIMENSION_TYPE_NAME = "dimension"; //$NON-NLS-1$

	/**
	 * Name of the Color property type.
	 */

	String COLOR_TYPE_NAME = "color"; //$NON-NLS-1$

	/**
	 * Name of the Choice property type.
	 */

	String CHOICE_TYPE_NAME = "choice"; //$NON-NLS-1$

	/**
	 * Name of the Boolean property type.
	 */

	String BOOLEAN_TYPE_NAME = "boolean"; //$NON-NLS-1$

	/**
	 * Name of the Expression property type.
	 */

	String EXPRESSION_TYPE_NAME = "expression"; //$NON-NLS-1$

	/**
	 * Name of the HTML property type.
	 */

	String HTML_TYPE_NAME = "html"; //$NON-NLS-1$

	/**
	 * Name of the Resource Key property type.
	 */

	String RESOURCE_KEY_TYPE_NAME = "resourceKey"; //$NON-NLS-1$

	/**
	 * Name of the Points property type.
	 */

	String POINTS_TYPE_NAME = "points"; //$NON-NLS-1$

	/**
	 * Name of the URI property type.
	 */

	String URI_TYPE_NAME = "uri"; //$NON-NLS-1$

	/**
	 * Name of the Date Time property type.
	 */

	String DATE_TIME_TYPE_NAME = "dateTime"; //$NON-NLS-1$

	/**
	 * Name of the XML property type.
	 */

	String XML_TYPE_NAME = "xml"; //$NON-NLS-1$

	/**
	 * Name of the Name property type.
	 */

	String NAME_TYPE_NAME = "name"; //$NON-NLS-1$

	/**
	 * Name of the Float property type.
	 */

	String FLOAT_TYPE_NAME = "float"; //$NON-NLS-1$

	/**
	 * Name of the Element reference property type.
	 */

	String ELEMENT_REF_NAME = "elementRef"; //$NON-NLS-1$

	/**
	 * Name of the Structure property type.
	 */

	String STRUCT_TYPE_NAME = "structure"; //$NON-NLS-1$

	/**
	 * Name of the Extends property type.
	 */

	String EXTENDS_TYPE_NAME = "extends"; //$NON-NLS-1$

	/**
	 * Name of the Script property type.
	 */

	String SCRIPT_TYPE_NAME = "script"; //$NON-NLS-1$

	/**
	 * Name of the structure reference property type.
	 */

	String STRUCT_REF_TYPE_NAME = "structRef"; //$NON-NLS-1$

	/**
	 * Name of the list property type.
	 */

	String LIST_TYPE_NAME = "list"; //$NON-NLS-1$

	/**
	 * Name of the Literal String property type.
	 */

	String LITERAL_STRING_TYPE_NAME = "literalString"; //$NON-NLS-1$

	/**
	 * Name the key property type of a structure member.
	 */

	String MEMBER_KEY_NAME = "memberKey"; //$NON-NLS-1$

	/**
	 * Name of the element property type.
	 */
	String ELEMENT_TYPE_NAME = "element"; //$NON-NLS-1$

	/**
	 * Type code for element attribute type. This is different from ELEMENT_TYPE.
	 * Its behavior like STRUCT_TYPE and isList=true.
	 */

	String CONTENT_ELEMENT_TYPE_NAME = "contentElement"; //$NON-NLS-1$

	/**
	 * Name of the locale property type.
	 */
	String LOCALE_TYPE_NAME = "locale"; //$NON-NLS-1$

	/**
	 * Returns the localized display name.
	 *
	 * @return the localized display name
	 */

	String getDisplayName();

	/**
	 * Returns the numeric code for this type.
	 *
	 * @return the internal type code
	 */

	int getTypeCode();

	/**
	 * Returns the name to use in the XML design and XML metadata files.
	 *
	 * @return the type name used in the XML design file
	 */

	String getName();

	/**
	 * Gets the set of choices for this type.
	 *
	 * @return the set of choices, or null if no choices are available
	 */

	IChoiceSet getChoices();

	/**
	 * Gets the display name resource key.
	 *
	 * @return the display name message key
	 */

	String getDisplayNameKey();

}
