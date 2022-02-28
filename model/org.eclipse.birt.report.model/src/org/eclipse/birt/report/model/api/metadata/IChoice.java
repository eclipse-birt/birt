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

	String getDisplayName();

	/**
	 * Returns the localized display name for the choice.
	 *
	 * @return the localized display name for the choice.
	 */

	String getDisplayName(ULocale locale);

	/**
	 * Returns the display name resource key for the choice.
	 *
	 * @return the display name resource key
	 */

	String getDisplayNameKey();

	/**
	 * Returns the choice name that appears in the XML design file.
	 *
	 * @return the choice name used in the XML design file
	 */

	String getName();

	/**
	 * Returns the value of the choice. The returned value equals to the internal
	 * name of the system choice.
	 *
	 * @return the value of the choice
	 */

	Object getValue();

	/**
	 * Creates a deep copy of this choice.
	 *
	 * @return a copy of this choice.
	 */

	IChoice copy();
}
