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
 * Represents the abstract definition which can be localized. This definition
 * includes name, display name ID and tool tip ID.
 */

public interface ILocalizableInfo {
	/**
	 * Returns the resource key for display name.
	 *
	 * @return the resource key for display name
	 */

	String getDisplayNameKey();

	/**
	 * Returns the definition name.
	 *
	 * @return the name of this definition
	 */

	String getName();

	/**
	 * Returns the resource key for tool tip.
	 *
	 * @return the resource key for tool tip
	 */

	String getToolTipKey();

	/**
	 * Returns the display name if the resource key of display name is available.
	 * Otherwise, return empty string.
	 *
	 * @return the display name
	 */

	String getDisplayName();

	/**
	 * Returns the tool tip if the resource key of tool tip is available. Otherwise,
	 * return empty string.
	 *
	 * @return the tool tip
	 */

	String getToolTip();
}
