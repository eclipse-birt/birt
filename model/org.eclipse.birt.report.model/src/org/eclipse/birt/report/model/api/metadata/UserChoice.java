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

import org.eclipse.birt.report.model.metadata.Choice;

/**
 * Describes the user defined choices for a user defined property. The internal
 * name of a choice property is a string. The string maps to a display name
 * shown to the user, and an XML name used in the xml design file. The display
 * name is localized, the XML name is not.
 */

public final class UserChoice extends Choice {

	/**
	 * Name of the choice value property.
	 */

	public final static String VALUE_PROP = "value"; //$NON-NLS-1$

	/**
	 * Name of the display name property.
	 */

	public final static String DISPLAY_NAME_PROP = "displayName"; //$NON-NLS-1$

	/**
	 * The choice's display name.
	 */

	protected String displayName;

	/**
	 * The user choice's value, it is required.
	 */

	protected Object value;

	/**
	 * Constructs a new User Choice by the given name and id.
	 * 
	 * @param name the choice name
	 * @param id   the message ID for the display name
	 */

	public UserChoice(String name, String id) {
		super(name, id);
	}

	/**
	 * Returns the property value of "displayName" for the choice.
	 * 
	 * @return the display name for the choice.
	 */

	public String getDisplayName() {
		return displayName;
	}

	/**
	 * Returns the display name for the choice.
	 * 
	 * @param name the display name
	 */

	public void setDisplayName(String name) {
		this.displayName = name;
	}

	/**
	 * Sets the value of the user choice.
	 * 
	 * @param theValue the value of the user choice to set
	 */

	public void setValue(Object theValue) {
		this.value = theValue;
	}

	/**
	 * Gets the value of the user choice.
	 * 
	 * @return the value of the user choice
	 */

	public Object getValue() {
		return this.value;
	}
}
