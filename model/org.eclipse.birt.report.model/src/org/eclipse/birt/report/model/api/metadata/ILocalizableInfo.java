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

	public String getDisplayNameKey();

	/**
	 * Returns the definition name.
	 * 
	 * @return the name of this definition
	 */

	public String getName();

	/**
	 * Returns the resource key for tool tip.
	 * 
	 * @return the resource key for tool tip
	 */

	public String getToolTipKey();

	/**
	 * Returns the display name if the resource key of display name is available.
	 * Otherwise, return empty string.
	 * 
	 * @return the display name
	 */

	public String getDisplayName();

	/**
	 * Returns the tool tip if the resource key of tool tip is available. Otherwise,
	 * return empty string.
	 * 
	 * @return the tool tip
	 */

	public String getToolTip();
}
