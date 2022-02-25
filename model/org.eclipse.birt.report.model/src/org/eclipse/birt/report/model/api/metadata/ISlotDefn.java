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

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.IContainerDefn;

/**
 * Interface for the definition of a slot within an element. It defines methods
 * to get Meta-data information about a slot within an element. Elements can act
 * as a <em>container</em>, that is one that can contain other elements. A
 * container has one or more <em>slots</em>. Many elements have just one slot,
 * but some (such as the design) have several.
 */

public interface ISlotDefn extends IContainerDefn {

	/**
	 * Returns the internal name.
	 *
	 * @return the name
	 */

	@Override
	String getName();

	/**
	 * Returns the slot cardinality.
	 *
	 * @return true if the cardinality is multiple, false if it is single
	 */

	boolean isMultipleCardinality();

	/**
	 * Returns the localized display name.
	 *
	 * @return the display name
	 */

	@Override
	String getDisplayName();

	/**
	 * Returns the message ID for the display name.
	 *
	 * @return the message ID for the display name
	 */

	String getDisplayNameID();

	/**
	 * Returns the internal slot identifier.
	 *
	 * @return the slot identifier
	 */

	int getSlotID();

	/**
	 * Returns the set of element types that can appear in the slot. Each object in
	 * the list is instance of {@link IElementDefn}.
	 *
	 * @return the list of content elements.
	 */

	List<IElementDefn> getContentElements();

	/**
	 * Returns the set of element types that can appear in the slot. Each object in
	 * the list is instance of {@link IElementDefn}. Extended elements are replaced
	 * by actual extension elements.
	 *
	 * @return the list of content elements.
	 */

	List<IElementDefn> getContentExtendedElements();

	/**
	 * Determines if this slot can contain an element of the given type.
	 *
	 * @param type the type to test
	 * @return true if the slot can contain the type, false otherwise
	 */

	@Override
	boolean canContain(IElementDefn type);

	/**
	 * Determines if an element can reside within this slot.
	 *
	 * @param content the design element to check
	 * @return true if the element can reside in the slot, false otherwise
	 */

	@Override
	boolean canContain(DesignElement content);

	/**
	 * Return the version in which the slot was introduced. Returns "reserved" if
	 * the slot is not yet supported.
	 *
	 * @return version in which the slot was introduced.
	 *
	 */

	String getSince();

	/**
	 * Return the XML element used to hold slot contents. If blank, then the slot is
	 * anonymous (its contents appear directly inside the container.)
	 *
	 * @return the XML element used to hold slot contents
	 */

	String getXmlName();

	/**
	 * Returns the selector associated with the slot. Some selectors end with -n. In
	 * this case, the n represents the number 1 though 9, depending on the slot
	 * location.
	 *
	 * @return the default style for this slot.
	 */

	String getSelector();
}
