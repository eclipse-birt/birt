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

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.metadata.IArgumentInfo;
import org.eclipse.birt.report.model.api.metadata.IClassInfo;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.scripts.IScriptableObjectClassInfo;
import org.eclipse.birt.report.model.api.scripts.ScriptableClassInfo;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Represents the definition of argument. The argument definition includes the
 * data type, internal name, and display name.
 */

public class ArgumentInfo implements IArgumentInfo {

	protected IMessages messages;

	/**
	 * The script type of this argument.
	 */

	private String type;

	protected IClassInfo classType;

	/**
	 * The internal (non-localized) name for the argument. This name is used in
	 * code.
	 */

	protected String name = null;

	/**
	 * The resource key for the argument display name.
	 */

	protected String displayNameKey = null;

	private IElementDefn elementDefn;

	/**
	 * Returns the internal name for the argument.
	 *
	 * @return the internal (non-localized) name for the argument
	 */

	@Override
	public String getName() {
		return name;
	}

	/**
	 * Returns the display name for the property if the resource key of display name
	 * is defined. Otherwise, return empty string.
	 *
	 * @return the user-visible, localized display name for the property
	 */

	@Override
	public String getDisplayName() {
		if (displayNameKey != null) {
			String displayName = null;
			if (messages == null) {
				displayName = ModelMessages.getMessage(this.displayNameKey);
			} else {
				ULocale locale = ThreadResources.getLocale();
				displayName = messages.getMessage(displayNameKey, locale);
			}
			if (displayName != null) {
				return displayName;
			}
		}
		return name;
	}

	/**
	 * Sets the internal name of the property.
	 *
	 * @param theName the internal property name
	 */

	public void setName(String theName) {
		name = theName;
	}

	/**
	 * Returns the resource key for the display name.
	 *
	 * @return The display name message ID.
	 */

	@Override
	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Sets the message ID for the display name.
	 *
	 * @param id message ID for the display name
	 */

	public void setDisplayNameKey(String id) {
		displayNameKey = id;
	}

	/**
	 * Returns the script type of this argument.
	 *
	 * @return the script type to set
	 */

	@Override
	public String getType() {
		return type;
	}

	/**
	 * Sets the element definition so that the scriptable factory can be retrieved.
	 * This method is only for peer extension elements.
	 *
	 * @param elementDefn the element definition
	 */

	void setElementDefn(IElementDefn elementDefn) {
		assert elementDefn instanceof PeerExtensionElementDefn;

		this.elementDefn = elementDefn;
	}

	public void setClassType(IClassInfo classType) {
		this.classType = classType;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.report.model.api.metadata.IArgumentInfo#getClassType()
	 */

	@Override
	public IClassInfo getClassType() {
		if (classType != null) {
			return classType;
		}

		IClassInfo tmpInfo = new ScriptableClassInfo().getClass(type);
		if (tmpInfo != null) {
			return tmpInfo;
		}

		if (elementDefn == null) {
			return null;
		}

		IScriptableObjectClassInfo factory = ((PeerExtensionElementDefn) elementDefn).getScriptableFactory();
		if (factory == null) {
			return null;
		}

		return factory.getScriptableClass(type);
	}

	/**
	 * Sets the script type of this argument.
	 *
	 * @param type the script type to set
	 */

	public void setType(String type) {
		this.type = type;
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

	public void setMessages(IMessages messages) {
		this.messages = messages;
	}

	public IMessages getMessages() {
		return messages;
	}
}
