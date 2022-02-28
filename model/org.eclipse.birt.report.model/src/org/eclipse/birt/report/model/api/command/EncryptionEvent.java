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

package org.eclipse.birt.report.model.api.command;

import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;

/**
 * Identifies that the encryption of an element property changed.
 *
 */

public class EncryptionEvent extends NotificationEvent {

	private String oldEncryption = null;

	private String newEncryption = null;

	private ElementPropertyDefn prop = null;

	/**
	 * Constructor.
	 *
	 * @param obj      the element that changed.
	 * @param propDefn
	 * @param oldName  the new name causing the event
	 * @param newName  the old name of the event
	 */

	public EncryptionEvent(DesignElement obj, ElementPropertyDefn propDefn, String oldName, String newName) {
		super(obj);
		this.prop = propDefn;
		this.oldEncryption = oldName;
		this.newEncryption = newName;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.report.model.design.activity.NotificationEvent#getEventType
	 * ()
	 */
	@Override
	public int getEventType() {
		return ENCRYPTION_EVENT;
	}

	/**
	 * Gets the new encryption causing the event.
	 *
	 * @return Returns the new encryption.
	 */

	public String getNewEncryption() {
		return newEncryption;
	}

	/**
	 * Gets the old encryption of the event.
	 *
	 * @return Returns the old encryption.
	 */

	public String getOldEncryption() {
		return oldEncryption;
	}

	/**
	 * Gets the property name that cause the encryption change.
	 *
	 * @return the property name.
	 */
	public String getPropertyName() {
		return prop.getName();
	}
}
