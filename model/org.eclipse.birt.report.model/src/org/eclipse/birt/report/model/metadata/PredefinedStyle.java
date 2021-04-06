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

import org.eclipse.birt.report.model.api.metadata.IPredefinedStyle;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * BIRT defines a fixed set of predefined styles. These style correspond to
 * certain report elements, or element slots. For example, there is a style for
 * chart, for list headers, for table footers and so on.
 * <p>
 * The set of predefined styles is part of the element design and is fixed by
 * the development team. Fixed properties of predefined styles includes the
 * internal name and display name. However, the property values for the style
 * are set in a design or template and are part of the design itself.
 * <p>
 * This class represents the invariant part of the predefined styles. It is used
 * to create a style element within a new design.
 * <p>
 * Note that predefined styles are identified with an internal name of element
 * name, slot name or their combination. For example, to select a list, use the
 * "list" style. To select a list header, use the "list-header" style. Styles
 * also have a display name id which is used to get localized display name, but
 * the internal name remains fixed across all locales. This ensures that a
 * design created in one locale can be used in another.
 * 
 */

public class PredefinedStyle implements IPredefinedStyle {

	/**
	 * The internal name is made up of element name, slot name or their
	 * combinationof. For example, "table" is for table, "table-header" for header
	 * slot of table element. We support up to 9 group levels. Hence a name like
	 * "table-group-header-1" is for <strong>TableGroup header</strong> for level
	 * <strong>1</strong>.
	 */

	private String name = null;

	/**
	 * The resource key ID for the display name. Allows the display name to be
	 * localized.
	 */

	private String displayNameKey = null;

	/**
	 * The string that specifies the type of this selector. Now it can be one of:
	 * Table, Grid and List. It must be name of the IElementDefn.
	 */
	private String type = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.IPredefinedStyle#getDisplayNameKey ()
	 */
	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Sets the display name ID. Done while creating the standard style.
	 * 
	 * @param id the display name message ID to set
	 */
	public void setDisplayNameKey(String id) {
		displayNameKey = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.IPredefinedStyle#getName()
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the internal name for this style. Must be done before adding the style
	 * to the data dictionary.
	 * 
	 * @param theName the name to set
	 */
	public void setName(String theName) {
		name = theName;
	}

	/**
	 * 
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		if (!StringUtil.isBlank(getName()))
			return getName();
		return super.toString();
	}
}
