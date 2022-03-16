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

import org.eclipse.birt.report.model.metadata.IContainerDefn;
import org.eclipse.birt.report.model.metadata.PropertyType;

/**
 * Base Interface for both element property, extension model property and
 * structure member definitions.
 */

public interface IPropertyDefn extends IContainerDefn {

	/**
	 * Type code for a system property.
	 */

	int SYSTEM_PROPERTY = 0;

	/**
	 * Type code for a user property.
	 */

	int USER_PROPERTY = 1;

	/**
	 * Type code for a property defined in XML file with the
	 * ReportItemExtensionPoint.
	 */

	int EXTENSION_PROPERTY = 2;

	/**
	 * Type code for a property defined by an extension implementation of
	 * ReportItemExtensionPoint.
	 */

	int EXTENSION_MODEL_PROPERTY = 3;

	/**
	 * Type code for a property defined by a ODA extension.
	 */

	int ODA_PROPERTY = 4;

	/**
	 * Type code for a property defined for commercial element implementation.
	 */
	int COMMERCIAL_PROPERTY = 5;

	/**
	 * Indicates whether this property is a list. It is useful only when the
	 * property type is a structure type.
	 *
	 * @return whether the property is a list or not.
	 */

	boolean isList();

	/**
	 * Returns the property type. See the list in {@link PropertyType}.
	 *
	 * @return he property type code
	 */

	int getTypeCode();

	/**
	 * Returns the message id for the display name.
	 *
	 * @return The display name message ID.
	 */

	String getDisplayNameID();

	/**
	 * Gets the list of choices for the property.
	 *
	 * @return the list of choices
	 */

	IChoiceSet getChoices();

	/**
	 * Checks if a property has a set of choices whatever choice is choice, extended
	 * choice or user defined choice.
	 *
	 * @return true if it has, otherwise false.
	 */

	boolean hasChoices();

	/**
	 * Returns the structure definition for this value.
	 *
	 * @return the structure definition, or null if this value is not a list of
	 *         structures
	 */

	IStructureDefn getStructDefn();

	/**
	 * Returns the default value for the property.
	 *
	 * @return The default value.
	 */

	Object getDefault();

	/**
	 * Return the element type associated with this property.
	 *
	 * @return the element type associated with the property
	 */

	IElementDefn getTargetElementType();

	/**
	 * Returns the allowed choices for this property. It contains allowed choices
	 * for a choice type.
	 * <p>
	 * If a property has not defined the restriction, then whole set will be
	 * returned.
	 *
	 * @return Returns the allowed choices of this property.
	 */

	IChoiceSet getAllowedChoices();

	/**
	 * Returns the allowed units for this property. It contains an allowed units set
	 * for a dimension type. Only the dimension type supports allowed units feature.
	 * <p>
	 * If a property has not defined the restriction, then whole set will be
	 * returned.
	 *
	 * @return Returns the allowed units of this property.
	 */

	IChoiceSet getAllowedUnits();

	/**
	 * Returns whether this property should be encrypted.
	 *
	 * @return <code>true</code> if this property should be encrypted.
	 */

	boolean isEncryptable();

	/**
	 * Returns the type of this value. The return can be one of the following
	 * constants:
	 * <p>
	 * <ul>
	 * <li>SYSTEM_PROPERTY</li>
	 * <li>USER_PROPERTY</li>
	 * <li>STRUCT_PROPERTY</li>
	 * <li>EXTENSION_PROPERTY</li>
	 * </ul>
	 *
	 * @return the type of this definition
	 */

	int getValueType();

	/**
	 * Return the context for a method or expression. If the property type is not
	 * method/expression, the return value is <code>null</code>.
	 *
	 * @return the expression or method context
	 */

	String getContext();

	/**
	 * Checks whether the expression can be the value of this property.
	 *
	 * @return <code>true</code> if the expression value is valid. Otherwise
	 *         <code>false</code>.
	 */

	boolean allowExpression();
}
