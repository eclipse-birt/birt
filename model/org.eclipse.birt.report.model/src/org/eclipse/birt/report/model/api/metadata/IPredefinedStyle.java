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

public interface IPredefinedStyle {

	/**
	 * Returns the message ID for the display name.
	 * 
	 * @return the display name message ID
	 */
	public String getDisplayNameKey();

	/**
	 * Gets the internal style name.
	 * 
	 * @return the name
	 */
	public String getName();

	/**
	 * 
	 * @return
	 */
	public String getType();

}
