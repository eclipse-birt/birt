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

import org.eclipse.birt.report.model.api.extension.IChoiceDefinition;
import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the choices defined by the extension element. There are two kinds
 * of choices:
 * 
 * <ul>
 * <li>The choice defined for extension property. Its name and resource key are
 * from plugin.xml.
 * <li>The choice defined for extension model property. Its name and resource
 * key are from <code>IChoiceDefinition</code>.
 * </ul>
 */

public class ExtensionChoice extends Choice {

	/**
	 * The choice from extension model property.
	 */

	IChoiceDefinition extChoice = null;

	/**
	 * The messages providing the localized messages.
	 */

	IMessages messages = null;

	/**
	 * The value of this choice.
	 */

	Object value = null;

	/**
	 * The default display name, which is used when the localized string is not
	 * found with I18N feature.
	 */

	String defaultDisplayName = null;

	/**
	 * Constructs an empty choice.
	 * 
	 * @param messages the messages provideing localized messages
	 */

	public ExtensionChoice(IMessages messages) {
		this.messages = messages;
	}

	/**
	 * Constructs the extension choice defined by extension elements.
	 * 
	 * @param extChoiceDefn the extension choice definition based
	 * @param messages      the messages providing localized messages
	 */

	public ExtensionChoice(IChoiceDefinition extChoiceDefn, IMessages messages) {
		assert extChoiceDefn != null;
		this.extChoice = extChoiceDefn;
		this.messages = messages;
	}

	/*
	 * Returns the localized display name, if non-empty string can be found with
	 * resource key and <code> IMessages </code> . Otherwise, return name of this
	 * choice.
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getDisplayName()
	 */

	public String getDisplayName() {
		String resourceKey = displayNameKey;
		String choiceName = name;

		if (extChoice != null) {
			resourceKey = extChoice.getDisplayNameID();
			choiceName = extChoice.getName();
		}

		if (resourceKey != null && messages != null) {
			String displayName = messages.getMessage(resourceKey, ThreadResources.getLocale());
			if (!StringUtil.isBlank(displayName))
				return displayName;
		}

		if (defaultDisplayName != null)
			return defaultDisplayName;

		return choiceName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getDisplayNameKey()
	 */

	public String getDisplayNameKey() {
		if (extChoice != null)
			return extChoice.getDisplayNameID();

		return displayNameKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getName()
	 */

	public String getName() {
		if (extChoice != null)
			return extChoice.getName();

		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#getValue()
	 */

	public Object getValue() {
		if (extChoice != null)
			return extChoice.getValue();

		return value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.metadata.Choice#setDisplayNameKey(java.lang.
	 * String)
	 */

	public void setDisplayNameKey(String theDisplayNameKey) {
		this.displayNameKey = theDisplayNameKey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.Choice#setName(java.lang.String)
	 */

	public void setName(String theName) {
		this.name = theName;
	}

	/**
	 * Sets the value for this choice.
	 * 
	 * @param value the value to set
	 */

	public void setValue(Object value) {
		this.value = value;
	}

	/**
	 * Sets the default display name.
	 * 
	 * @param defaultDisplayName the default display name to set
	 */

	public void setDefaultDisplayName(String defaultDisplayName) {
		this.defaultDisplayName = defaultDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	public int compareTo(Object o) {
		Choice choice = (Choice) o;

		String name = getName();
		assert name != null;

		return name.compareTo(choice.getName());
	}
}