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

import java.util.Iterator;

/**
 * Base Interface for methods common to elements and structures. This base
 * interface defines methods to work generically with these two kinds of
 * objects.
 */

public interface IObjectDefn {

	/**
	 * Gets the display name.
	 * 
	 * @return Returns the display name.
	 */

	public String getDisplayName();

	/**
	 * Gets the resource key for the display name.
	 * 
	 * @return The display name resource key.
	 */

	public Object getDisplayNameKey();

	/**
	 * Gets the internal name for the element.
	 * 
	 * @return Returns the name.
	 */

	public String getName();

	/**
	 * Gets a property definition given the property name.
	 * 
	 * @param propName the name of the property to get
	 * @return the property with that name, or null if the property cannot be found
	 */

	public IPropertyDefn findProperty(String propName);

	/**
	 * Returns an iterator over the property definitions. The
	 * <code>IPropertyDefn</code> s in the iterator will be sorted by there
	 * localized names.
	 * 
	 * @return an iterator over the property definitions.
	 */

	public Iterator<IPropertyDefn> getPropertyIterator();

	/**
	 * Returns an iterator over the property definitions. The
	 * <code>IPropertyDefn</code> s in the iterator are not sorted.
	 * 
	 * @return an iterator over the property definitions.
	 */

	public Iterator<IPropertyDefn> propertiesIterator();
}