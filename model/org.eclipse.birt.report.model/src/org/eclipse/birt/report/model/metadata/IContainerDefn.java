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

package org.eclipse.birt.report.model.metadata;

import java.util.List;

import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.core.DesignElement;

/**
 *
 */
public interface IContainerDefn {

	/**
	 * Returns the internal name for the property.
	 *
	 * @return the internal (non-localized) name for the property
	 */

	String getName();

	/**
	 * Returns the display name for the property.
	 *
	 * @return the user-visible, localized display name for the property
	 */

	String getDisplayName();

	/**
	 * Determines if this property can contain an element of the given type.
	 *
	 * @param type the type to test
	 * @return true if the property can contain the type, false otherwise
	 */

	boolean canContain(IElementDefn type);

	/**
	 * Determines if an element can reside within this property value.
	 *
	 * @param content the design element to check
	 * @return true if the element can reside in the property value, false otherwise
	 */

	boolean canContain(DesignElement content);

	/**
	 * Returns list of allowed element types if property is of element-type. Each
	 * item in the list is instance of <code>IElementDefn</code>. It gives the same
	 * result as <code>getAllowedElements(true)</code>;
	 *
	 * @return list of allowed element definitions if property is of element-type,
	 *         otherwise return empty list
	 */
	List<IElementDefn> getAllowedElements();

	/**
	 * Returns list of allowed element types if property is of element-type. Each
	 * item in the list is instance of <code>IElementDefn</code>. If
	 * "extractExtensions" is true, then the return list will contains the real
	 * extension definitions(for example, <code>Chart</code>) rather than
	 * <code>ExtendedItem</code>; Otherwise return what are set in the property
	 * definition itself.
	 *
	 * @param extractExtensions
	 *
	 * @return list of allowed element definitions if property is of element-type,
	 *         otherwise return empty list
	 */
	List<IElementDefn> getAllowedElements(boolean extractExtensions);

	NameConfig getNameConfig();
}
