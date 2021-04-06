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

package org.eclipse.birt.report.model.metadata;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.metadata.ILocalizableInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Represents the abstract definition which can be localized. This definition
 * includes name, display name ID and tool tip ID.
 */

public abstract class LocalizableInfo implements ILocalizableInfo {

	protected IMessages messages;

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

	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param displayNameKey the resource key to set
	 */

	public void setDisplayNameKey(String displayNameKey) {
		this.displayNameKey = displayNameKey;
	}

	/**
	 * Returns the definition name.
	 * 
	 * @return the name of this definition
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the definition name.
	 * 
	 * @param name the name to set
	 */

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the resource key for tool tip.
	 * 
	 * @return the resource key for tool tip
	 */

	public String getToolTipKey() {
		return toolTipKey;
	}

	/**
	 * Sets the resource key for tool tip.
	 * 
	 * @param toolTipKey the resource key to set
	 */

	public void setToolTipKey(String toolTipKey) {
		this.toolTipKey = toolTipKey;
	}

	/**
	 * Returns the display name if the resource key of display name is available.
	 * Otherwise, return empty string.
	 * 
	 * @return the display name
	 */

	public String getDisplayName() {
		if (displayNameKey != null) {
			String displayName = null;
			if (messages == null) {
				displayName = ModelMessages.getMessage(displayNameKey);
			} else {
				ULocale locale = ThreadResources.getLocale();
				displayName = messages.getMessage(displayNameKey, locale);
			}
			if (displayName != null) {
				return displayName;
			}
		}

		return name; // $NON-NLS-1$
	}

	/**
	 * Returns the tool tip if the resource key of tool tip is available. Otherwise,
	 * return empty string.
	 * 
	 * @return the tool tip
	 */

	public String getToolTip() {
		if (toolTipKey != null) {
			if (messages == null) {
				return ModelMessages.getMessage(toolTipKey);
			}
			ULocale locale = ThreadResources.getLocale();
			return messages.getMessage(toolTipKey, locale);
		}

		return ""; //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */

	public String toString() {
		if (!StringUtil.isBlank(getName()))
			return getName();
		return super.toString();
	}

	public void setMessages(IMessages messages) {
		this.messages = messages;
	}

	public IMessages getMessages() {
		return messages;
	}
}