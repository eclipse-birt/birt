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

package org.eclipse.birt.report.model.elements.interfaces;

/**
 * The interface for constants of abstract design elements.
 */

public interface IDesignElementModel {

	/**
	 * Property name sufficed for any string property that can be localized. If the
	 * property name is "mumble", then the message ID for that property is
	 * "mumbleID".
	 */

	String ID_SUFFIX = "ID"; //$NON-NLS-1$

	/**
	 * Name of the property that holds custom XML for an element.
	 */

	String CUSTOM_XML_PROP = "customXml"; //$NON-NLS-1$

	/**
	 * Name of the property that holds comments about the element. Comments cannot
	 * be localized: they are for the use of the report developer.
	 */

	String COMMENTS_PROP = "comments"; //$NON-NLS-1$

	/**
	 * Display name of the element. Can be localized.
	 */

	String DISPLAY_NAME_PROP = "displayName"; //$NON-NLS-1$

	/**
	 * Message ID property for the display name.
	 */

	String DISPLAY_NAME_ID_PROP = "displayNameID"; //$NON-NLS-1$

	/**
	 * Element name property. The element name is <em>intrinsic</em>: it is
	 * available as a property, but is stored as a field.
	 */

	String NAME_PROP = "name"; //$NON-NLS-1$

	/**
	 * Name or reference to the element that this element extends. The extends
	 * property is <em>intrinsic</em>: it is available as a property, but is stored
	 * as a field.
	 */
	String EXTENDS_PROP = "extends"; //$NON-NLS-1$

	/**
	 * Name of the property that holds masks of BIRT/user defined properties for the
	 * element.
	 */

	String PROPERTY_MASKS_PROP = "propertyMasks"; //$NON-NLS-1$

	/**
	 * Name of the user property definition.
	 */

	String USER_PROPERTIES_PROP = "userProperties"; //$NON-NLS-1$

	/**
	 * Name of the event handler class.
	 */

	String EVENT_HANDLER_CLASS_PROP = "eventHandlerClass"; //$NON-NLS-1$

	/**
	 * Name of the new handler on each event. This property controls if the event
	 * handler should be created.
	 */
	String NEW_HANDLER_ON_EACH_EVENT_PROP = "newHandlerOnEachEvent"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the referred template parameter definition.
	 * Its value is instance of ElementRefValue.
	 *
	 * @see org.eclipse.birt.report.model.metadata.ElementRefValue
	 */

	String REF_TEMPLATE_PARAMETER_PROP = "refTemplateParameter"; //$NON-NLS-1$

	/**
	 * Name of the property that defines the view action in this element.
	 */

	String VIEW_ACTION_PROP = "viewAction"; //$NON-NLS-1$

	/**
	 * Marker to indicate that the element is not in a slot.
	 */

	int NO_SLOT = -1;

	/**
	 * Marker to indicate that at which level the user want to get the display label
	 * of this element. The display name or name of element.
	 */

	int USER_LABEL = 0;

	/**
	 * The display name, name or metadata name of element.
	 */

	int SHORT_LABEL = 1;

	/**
	 * The short label pluses additional information.
	 */

	int FULL_LABEL = 2;

}
