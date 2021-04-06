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

import java.util.Iterator;

import org.eclipse.birt.report.model.api.extension.IMessages;
import org.eclipse.birt.report.model.api.metadata.IElementDefn;
import org.eclipse.birt.report.model.api.util.StringUtil;
import org.eclipse.birt.report.model.i18n.ThreadResources;

/**
 * Represents the definition of extension slot.
 */

public class ExtensionSlotDefn extends SlotDefn {

	/**
	 * Implementation for the localization of message.
	 */
	protected IMessages messages = null;

	/**
	 * Display name for the default set.
	 */
	protected String defaultDisplayName = null;

	/**
	 * Constructs the extension slot definition with <code>IMessages</code>.
	 * 
	 * @param messages the message interface to do the I18n work for extension
	 */

	public ExtensionSlotDefn(IMessages messages) {
		this.messages = messages;
	}

	/**
	 * @return the defaultDisplayName
	 */
	public String getDefaultDisplayName() {
		return defaultDisplayName;
	}

	/**
	 * @param defaultDisplayName the defaultDisplayName to set
	 */
	public void setDefaultDisplayName(String defaultDisplayName) {
		this.defaultDisplayName = defaultDisplayName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.SlotDefn#getDisplayName()
	 */

	public String getDisplayName() {
		if (displayNameID != null && messages != null) {
			String displayName = messages.getMessage(displayNameID, ThreadResources.getLocale());
			if (!StringUtil.isBlank(displayName))
				return displayName;
		}

		if (defaultDisplayName != null)
			return defaultDisplayName;

		return getName();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.metadata.SlotDefn#build()
	 */

	protected void build() throws MetaDataException {
		if (contentTypes.isEmpty())
			throw new MetaDataException(new String[] { this.name },
					MetaDataException.DESIGN_EXCEPTION_MISSING_SLOT_TYPE);

		// Translate the type names into element types.

		MetaDataDictionary dd = MetaDataDictionary.getInstance();
		contentElements.clear();
		Iterator<String> iter = contentTypes.iterator();
		while (iter.hasNext()) {
			String name = iter.next();
			IElementDefn type = dd.getElement(name);
			if (type == null)
				type = dd.getExtension(name);
			if (type == null)
				throw new MetaDataException(new String[] { name, this.name },
						MetaDataException.DESIGN_EXCEPTION_INVALID_SLOT_TYPE);
			contentElements.add(type);
		}
	}
}
