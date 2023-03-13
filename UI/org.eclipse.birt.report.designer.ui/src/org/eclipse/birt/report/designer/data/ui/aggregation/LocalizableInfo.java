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

package org.eclipse.birt.report.designer.data.ui.aggregation;

import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;

/**
 * Represents the abstract definition which can be localized. This definition
 * includes name, display name ID and tool tip ID.
 */

public abstract class LocalizableInfo implements ILocalizableInfo {

	/**
	 * The name of the definition.
	 */

	protected String name;

	/**
	 * The resource key for display name.
	 */

	protected String displayNameKey;

	/**
	 * The resource key for tool tip.
	 */

	protected String toolTipKey;

	/**
	 * Returns the resource key for display name.
	 *
	 * @return the resource key for display name
	 */

	@Override
	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Sets the resource key for display name.
	 *
	 * @param displayNameKey the resource key to set
	 */

	void setDisplayNameKey(String displayNameKey) {
		this.displayNameKey = displayNameKey;
	}

	/**
	 * Returns the definition name.
	 *
	 * @return the name of this definition
	 */

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Sets the definition name.
	 *
	 * @param name the name to set
	 */

	void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the resource key for tool tip.
	 *
	 * @return the resource key for tool tip
	 */

	@Override
	public String getToolTipKey() {
		return toolTipKey;
	}

	/**
	 * Sets the resource key for tool tip.
	 *
	 * @param toolTipKey the resource key to set
	 */

	void setToolTipKey(String toolTipKey) {
		this.toolTipKey = toolTipKey;
	}

	/**
	 * Returns the display name if the resource key of display name is available.
	 * Otherwise, return empty string.
	 *
	 * @return the display name
	 */

	@Override
	public String getDisplayName() {
		return displayNameKey != null ? displayNameKey : ""; //$NON-NLS-1$
	}

	/**
	 * Returns the tool tip if the resource key of tool tip is available. Otherwise,
	 * return empty string.
	 *
	 * @return the tool tip
	 */

	@Override
	public String getToolTip() {
		return toolTipKey != null ? toolTipKey : ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see java.lang.Object#toString()
	 */

	@Override
	public String toString() {
		if (!StringUtil.isBlank(getName())) {
			return getName();
		}
		return super.toString();
	}

}
