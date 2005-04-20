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
 * Base Interface for both element property, extension model property and
 * structure member definitions.
 */

public interface IPropertyDefn
{

	/**
	 * Indicates whether this property is a list. It is useful only when the
	 * property type is a structure type.
	 * 
	 * @return whether the property is a list or not.
	 */

	public boolean isList( );

	/**
	 * Returns the internal name for the property.
	 * 
	 * @return the internal (non-localized) name for the property
	 */

	public String getName( );

	/**
	 * Returns the property type. See the list in {@link PropertyType}.
	 * 
	 * @return he property type code
	 */

	public int getTypeCode( );

	/**
	 * Returns the display name for the property.
	 * 
	 * @return the user-visible, localized display name for the property
	 */

	public String getDisplayName( );

	/**
	 * Returns the message id for the display name.
	 * 
	 * @return The display name message ID.
	 */

	public String getDisplayNameID( );

	/**
	 * Gets the list of choices for the property.
	 * 
	 * @return the list of choices
	 */

	public IChoiceSet getChoices( );

	/**
	 * Checks if a property has a set of choices whatever choice is choice,
	 * extended choice or user defined choice.
	 * 
	 * @return true if it has, otherwise false.
	 */

	public boolean hasChoices( );

	/**
	 * Returns the structure definition for this value.
	 * 
	 * @return the structure definition, or null if this value is not a list of
	 *         structures
	 */

	public IStructureDefn getStructDefn( );

	/**
	 * Returns the default value for the property.
	 * 
	 * @return The default value.
	 */

	public Object getDefault( );

	/**
	 * Return the element type associated with this property.
	 * 
	 * @return the element type associated with the property
	 */

	public IElementDefn getTargetElementType( );

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

	public IChoiceSet getAllowedChoices( );

	/**
	 * Returns whether this property should be encrypted.
	 * 
	 * @return <code>true</code> if this property should be encrypted.
	 */

	public boolean isEncrypted( );
}