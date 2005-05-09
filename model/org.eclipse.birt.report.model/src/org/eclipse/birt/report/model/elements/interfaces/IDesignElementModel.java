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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for constants of abstract design elements.
 */

public interface IDesignElementModel
{

	/**
	 * Property name sufficed for any string property that can be localized. If
	 * the property name is "mumble", then the message ID for that property is
	 * "mumbleID".
	 */

	public static final String ID_SUFFIX = "ID"; //$NON-NLS-1$

	/**
	 * Name of the property that holds custom XML for an element.
	 */

	public static final String CUSTOM_XML_PROP = "customXml"; //$NON-NLS-1$

	/**
	 * Name of the property that holds comments about the element. Comments
	 * cannot be localized: they are for the use of the report developer.
	 */

	public static final String COMMENTS_PROP = "comments"; //$NON-NLS-1$

	/**
	 * Display name of the element. Can be localized.
	 */

	public static final String DISPLAY_NAME_PROP = "displayName"; //$NON-NLS-1$

	/**
	 * Message ID property for the display name.
	 */

	public static final String DISPLAY_NAME_ID_PROP = "displayNameID"; //$NON-NLS-1$

	/**
	 * Element name property. The element name is <em>intrinsic</em>: it is
	 * available as a property, but is stored as a field.
	 */

	public static final String NAME_PROP = "name"; //$NON-NLS-1$

	/**
	 * Name or reference to the element that this element extends. The extends
	 * property is <em>intrinsic</em>: it is available as a property, but is
	 * stored as a field.
	 */
	public static final String EXTENDS_PROP = "extends"; //$NON-NLS-1$

	/**
	 * Name of the property that holds masks of BIRT/user defined properties for
	 * the element.
	 */

	public static final String PROPERTY_MASKS_PROP = "propertyMask"; //$NON-NLS-1$

	/**
	 * Name of the user property definition.
	 */

	public static final String USER_PROPERTIES_PROP = "userProperties"; //$NON-NLS-1$

	/**
	 * Marker to indicate that the element is not in a slot.
	 */

	public static final int NO_SLOT = -1;

	/**
	 * Marker to indicate that at which level the user want to get the display
	 * label of this element. The display name or name of element.
	 */

	public static final int USER_LABEL = 0;

	/**
	 * The display name, name or metadata name of element.
	 */

	public static final int SHORT_LABEL = 1;

	/**
	 * The short label pluses additional information.
	 */

	public static final int FULL_LABEL = 2;

}
