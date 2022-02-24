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

package org.eclipse.birt.report.model.command;

import org.eclipse.birt.report.model.activity.SimpleRecord;
import org.eclipse.birt.report.model.api.activity.NotificationEvent;
import org.eclipse.birt.report.model.api.command.EncryptionEvent;
import org.eclipse.birt.report.model.core.DesignElement;
import org.eclipse.birt.report.model.core.Module;
import org.eclipse.birt.report.model.i18n.MessageConstants;
import org.eclipse.birt.report.model.metadata.ElementPropertyDefn;
import org.eclipse.birt.report.model.util.CommandLabelFactory;
import org.eclipse.birt.report.model.util.EncryptionUtil;

/**
 * Records a change to the encryption of an element.
 * 
 */

public class EncryptionRecord extends SimpleRecord {

	/**
	 * The element to change.
	 */

	protected DesignElement element = null;

	/**
	 * 
	 */
	protected ElementPropertyDefn prop = null;

	/**
	 * The new encryption. Can be null.
	 */

	protected String newEncryption = null;

	/**
	 * The old encryption. Can be null.
	 */

	protected String oldEncryption = null;

	/**
	 * 
	 */
	protected Object oldLocalValue = null;

	/**
	 * 
	 */
	protected Object oldValue = null;

	/**
	 * Constructor.
	 * 
	 * @param module
	 * 
	 * @param obj        the element to change.
	 * @param propDefn
	 * @param encryption
	 */

	public EncryptionRecord(Module module, DesignElement obj, ElementPropertyDefn propDefn, String encryption) {
		element = obj;
		prop = propDefn;
		newEncryption = encryption;
		oldEncryption = element.getLocalEncryptionID(propDefn);

		// get value and local value
		oldValue = element.getProperty(module, prop);
		oldLocalValue = element.getLocalProperty(module, prop);

		label = CommandLabelFactory.getCommandLabel(MessageConstants.CHANGE_PROPERTY_ENCRYPTION_MESSAGE,
				new String[] { propDefn.getDisplayName() });
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.SimpleRecord#perform(boolean)
	 */

	protected void perform(boolean undo) {
		String encryptionID = undo ? oldEncryption : newEncryption;

		// if old value is null, it means the element and all its ancestor do
		// not set value, so need do nothing about property value
		if (oldValue == null) {
			element.setEncryptionHelper(prop, encryptionID);
		} else {
			// old value is not null
			if (oldLocalValue != null) {
				// if has local value, it must have encryption too.
				if (encryptionID == null)
					encryptionID = element.getEncryptionID(prop);

				Object newValue = EncryptionUtil.encrypt(prop, encryptionID, oldLocalValue);
				element.setProperty(prop, newValue);
				element.setEncryptionHelper(prop, encryptionID);
			} else {
				// if not undo(do or redo) and want to set the local value, it
				// must have encryption too.
				if (!undo && encryptionID == null)
					encryptionID = element.getEncryptionID(prop);

				// if do then get value and encrypt it and set; if undo, then
				// set null
				Object newValue = undo ? null : EncryptionUtil.encrypt(prop, encryptionID, oldValue);
				element.setProperty(prop, newValue);
				element.setEncryptionHelper(prop, encryptionID);
			}

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.report.model.design.core.AbstractElementRecord#getTarget ()
	 */

	public DesignElement getTarget() {
		return element;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.report.model.design.core.AbstractElementRecord#getEvent
	 * ()
	 */

	public NotificationEvent getEvent() {
		return new EncryptionEvent(element, prop, oldEncryption, newEncryption);
	}

}
