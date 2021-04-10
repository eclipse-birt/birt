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
import org.eclipse.birt.report.model.api.metadata.IChoice;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ModelMessages;
import org.eclipse.birt.report.model.i18n.ThreadResources;

import com.ibm.icu.util.ULocale;

/**
 * Describes the choices for a property. The internal name of a choice property
 * is a string. The string maps to a display name shown to the user, and an XML
 * name used in the xml design file. The display name is localized, the XML name
 * is not.
 */

public class Choice implements Cloneable, IChoice, Comparable<Object> {

	/**
	 * Name of the choice name property.
	 */

	public final static String NAME_PROP = "name"; //$NON-NLS-1$

	/**
	 * Name of the display name id property.
	 */

	public final static String DISPLAY_NAME_ID_PROP = "displayNameID"; //$NON-NLS-1$

	protected IMessages messages;
	/**
	 * The resource key for the choice's display name.
	 */

	protected String displayNameKey;

	/**
	 * The choice name to appear in the xml design file.
	 */

	protected String name;

	/**
	 * Constructs a new Choice by the given name and id.
	 * 
	 * @param name the choice name
	 * @param id   the message ID for the display name
	 */

	public Choice(String name, String id) {
		this.name = name;
		displayNameKey = id;
	}

	/**
	 * Default constructor.
	 * 
	 */

	public Choice() {

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#clone()
	 */

	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}

	/**
	 * Returns the localized display name for the choice.
	 * 
	 * @return the localized display name for the choice.
	 */

	public String getDisplayName() {
		if (displayNameKey != null) {
			String displayName = null;
			if (messages == null) {
				displayName = ModelMessages.getMessage(this.displayNameKey);
			} else {
				ULocale locale = ThreadResources.getLocale();
				displayName = messages.getMessage(displayNameKey, locale);
			}
			if (displayName == null) {
				displayName = name;
			}
			return displayName;
		}
		return name;
	}

	/**
	 * Returns the localized display name for the choice.
	 * 
	 * @return the localized display name for the choice.
	 */

	public String getDisplayName(ULocale locale) {
		if (displayNameKey != null) {
			if (messages == null) {
				return ModelMessages.getMessage(this.displayNameKey, locale);
			}
			return messages.getMessage(displayNameKey, locale);
		}
		return name;
	}

	/**
	 * Returns the display name resource key for the choice.
	 * 
	 * @return the display name resource key
	 */

	public String getDisplayNameKey() {
		return displayNameKey;
	}

	/**
	 * Returns the choice name that appears in the XML design file.
	 * 
	 * @return the choice name used in the XML design file
	 */

	public String getName() {
		return name;
	}

	/**
	 * Sets the resource key for display name.
	 * 
	 * @param theDisplayNameKey the resource key for display name
	 */

	public void setDisplayNameKey(String theDisplayNameKey) {
		this.displayNameKey = theDisplayNameKey;
	}

	/**
	 * Sets the choice name.
	 * 
	 * @param theName the name to set
	 */

	public void setName(String theName) {
		this.name = theName;
	}

	/**
	 * Returns the value of the choice. The returned value equals to the internal
	 * name of the system choice.
	 * 
	 * @return the value of the choice
	 */

	public Object getValue() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */

	public int compareTo(Object o) {
		assert name != null;

		Choice choice = (Choice) o;
		return name.compareTo(choice.getName());
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.api.metadata.IChoice#copy()
	 */

	public IChoice copy() {
		try {
			return (IChoice) clone();
		} catch (CloneNotSupportedException e) {
			assert false;
			return null;
		}
	}

	public void setMessages(IMessages messages) {
		this.messages = messages;
	}

	public IMessages getMessages() {
		return messages;
	}
}