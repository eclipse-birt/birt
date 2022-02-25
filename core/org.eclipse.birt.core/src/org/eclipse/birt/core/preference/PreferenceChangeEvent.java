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

package org.eclipse.birt.core.preference;

import java.io.NotSerializableException;
import java.util.prefs.Preferences;

import org.eclipse.birt.core.i18n.CoreMessages;
import org.eclipse.birt.core.i18n.ResourceConstants;

public class PreferenceChangeEvent extends java.util.EventObject {

	/**
	 *
	 */
	private static final long serialVersionUID = -899164778720093431L;

	public static final String SPECIALTODEFAULT = "special to default";

	/**
	 * Key of the preference that changed.
	 *
	 * @serial
	 */
	private String key;

	/**
	 * New value for preference, or <tt>null</tt> if it was removed.
	 *
	 * @serial
	 */
	private Object newValue;

	private Object oldValue;

	/**
	 * Constructs a new <code>PreferenceChangeEvent</code> instance.
	 *
	 * @param node     The Preferences node that emitted the event.
	 * @param key      The key of the preference that was changed.
	 * @param newValue The new value of the preference, or <tt>null</tt> if the
	 *                 preference is being removed.
	 */
	public PreferenceChangeEvent(IPreferences node, String key, Object oldValue, Object newValue) {
		super(node);
		this.key = key;
		this.newValue = newValue;
		this.oldValue = oldValue;
	}

	/**
	 * Returns the preference node that emitted the event.
	 *
	 * @return The preference node that emitted the event.
	 */
	public Preferences getNode() {
		return (Preferences) getSource();
	}

	/**
	 * Returns the key of the preference that was changed.
	 *
	 * @return The key of the preference that was changed.
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Returns the new value for the preference.
	 *
	 * @return The new value for the preference, or <tt>null</tt> if the preference
	 *         was removed.
	 */
	public Object getNewValue() {
		return newValue;
	}

	public Object getOldValue() {
		return oldValue;
	}

	/**
	 * Throws NotSerializableException, since NodeChangeEvent objects are not
	 * intended to be serializable.
	 */
	private void writeObject(java.io.ObjectOutputStream out) throws NotSerializableException {
		throw new NotSerializableException(CoreMessages.getString(ResourceConstants.NOT_SERIALIZABLE));
	}

	/**
	 * Throws NotSerializableException, since PreferenceChangeEvent objects are not
	 * intended to be serializable.
	 */
	private void readObject(java.io.ObjectInputStream in) throws NotSerializableException {
		throw new NotSerializableException(CoreMessages.getString(ResourceConstants.NOT_SERIALIZABLE));
	}
}
