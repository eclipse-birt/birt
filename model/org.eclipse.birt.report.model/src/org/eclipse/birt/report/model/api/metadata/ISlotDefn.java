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

import java.util.List;

import org.eclipse.birt.report.model.core.DesignElement;

/**
 * Interface for the definition of a slot within an element. It defines methods
 * to get Meta-data information about a slot within an element. Elements can act
 * as a <em>container</em>, that is one that can contain other elements. A
 * container has one or more <em>slots</em>. Many elements have just one
 * slot, but some (such as the design) have several.
 */

public interface ISlotDefn
{
	/**
	 * Returns the internal name.
	 * 
	 * @return the name
	 */

	public String getName( );
	
	/**
	 * Returns the slot cardinality.
	 * 
	 * @return true if the cardinality is multiple, false if it is single
	 */

	public boolean isMultipleCardinality( );

	/**
	 * Returns the localized display name.
	 * 
	 * @return the display name
	 */

	public String getDisplayName( );
	
	/**
	 * Returns the message ID for the display name.
	 * 
	 * @return the message ID for the display name
	 */

	public String getDisplayNameID( );

	/**
	 * Returns the internal slot identifier.
	 * 
	 * @return the slot identifier
	 */

	public int getSlotID( );

	/**
	 * Returns the set of element types that can appear in the slot. Each object
	 * in the list is instance of {@link IElementDefn}.
	 * 
	 * @return the list of content elements.
	 */

	public List getContentElements( );
	
	/**
	 * Determines if this slot can contain an element of the given type.
	 * 
	 * @param type
	 *            the type to test
	 * @return true if the slot can contain the type, false otherwise
	 */

	public boolean canContain( IElementDefn type );
	
	/**
	 * Determines if an element can reside within this slot.
	 * 
	 * @param content
	 *            the design element to check
	 * @return true if the element can reside in the slot, false otherwise
	 */

	public boolean canContain( DesignElement content );
}