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

import com.ibm.icu.util.ULocale;

/**
 * Describes the options for a property value. A choice has a display name and
 * an internal name (XML name). The display name is localized, the XML name is
 * not.
 */

public interface IChoice {

	/**
	 * Returns the localized display name for the choice.
	 * 
	 * @return the localized display name for the choice.
	 */

	public String getDisplayName();

	/**
	 * Returns the localized display name for the choice.
	 * 
	 * @return the localized display name for the choice.
	 */

	public String getDisplayName(ULocale locale);

	/**
	 * Returns the display name resource key for the choice.
	 * 
	 * @return the display name resource key
	 */

	public String getDisplayNameKey();

	/**
	 * Returns the choice name that appears in the XML design file.
	 * 
	 * @return the choice name used in the XML design file
	 */

	public String getName();

	/**
	 * Returns the value of the choice. The returned value equals to the internal
	 * name of the system choice.
	 * 
	 * @return the value of the choice
	 */

	public Object getValue();

	/**
	 * Creates a deep copy of this choice.
	 * 
	 * @return a copy of this choice.
	 */

	public IChoice copy();
}
