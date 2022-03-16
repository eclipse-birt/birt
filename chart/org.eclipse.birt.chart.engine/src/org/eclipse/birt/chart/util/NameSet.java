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

package org.eclipse.birt.chart.util;

import org.eclipse.birt.chart.engine.i18n.Messages;

/**
 * This class provides basic implementaion to hold a name and display name.
 */
public class NameSet {

	private String prefix;
	private String suffix;

	private String[] nameArray;
	private String[] displayNameArray;

	/**
	 * The constructor.
	 *
	 * @param prefix
	 * @param suffix
	 * @param name
	 */
	public NameSet(String prefix, String suffix, String[] name) {
		this.prefix = prefix;
		this.suffix = suffix;
		nameArray = name;
		initDisplayNameArray();
	}

	/**
	 * Returns a new NameSet object joined with current one, use current prefix and
	 * suffix.
	 *
	 * @param ns
	 * @return
	 */
	public NameSet join(NameSet ns) {
		String[] nss = ns.getNames();
		String[] newNames = new String[nameArray.length + nss.length];

		System.arraycopy(nameArray, 0, newNames, 0, nameArray.length);
		System.arraycopy(nss, 0, newNames, nameArray.length, nss.length);

		return new NameSet(prefix, suffix, newNames);
	}

	private void initDisplayNameArray() {
		if (nameArray != null) {
			displayNameArray = new String[nameArray.length];
			for (int i = 0; i < displayNameArray.length; i++) {
				displayNameArray[i] = Messages.getString(prefix + nameArray[i] + suffix);
			}
		}
	}

	/**
	 * Returns the original name by the display name.
	 *
	 * @param displayName
	 * @return
	 */
	public String getNameByDisplayName(String displayName) {
		for (int i = 0; i < displayNameArray.length; i++) {
			if (displayName != null && displayName.equals(displayNameArray[i])) {
				return nameArray[i];
			}
		}

		return null;
	}

	/**
	 * Returns the display name by the original name.
	 *
	 * @param displayName
	 * @return
	 */
	public String getDisplayNameByName(String name) {
		for (int i = 0; i < nameArray.length; i++) {
			if (name != null && name.equals(nameArray[i])) {
				return displayNameArray[i];
			}
		}

		return null;
	}

	/**
	 * Returns the display name by the original name.
	 *
	 * @param displayName
	 * @return
	 * @since 2.3.1
	 */
	public String getDisplayNameByName(String name, String defaultValue) {
		for (int i = 0; i < nameArray.length; i++) {
			if (name != null && name.equals(nameArray[i])) {
				return displayNameArray[i];
			}
		}

		return defaultValue;
	}

	/**
	 * Returns an index by given name, if name not found, returns 0.
	 *
	 * @param name
	 * @return
	 */
	public int getSafeNameIndex(String name) {
		for (int i = 0; i < nameArray.length; i++) {
			if (name != null && name.equals(nameArray[i])) {
				return i;
			}
		}

		return 0;
	}

	/**
	 * Returns an index by given name, if name not found, returns -1.
	 *
	 * @param name
	 * @return
	 */
	public int getNameIndex(String name) {
		for (int i = 0; i < nameArray.length; i++) {
			if (name != null && name.equals(nameArray[i])) {
				return i;
			}
		}

		return -1;
	}

	/**
	 * Returns the display name array.
	 *
	 * @return
	 */
	public String[] getDisplayNames() {
		return displayNameArray;
	}

	/**
	 * Returns the original name array.
	 *
	 * @return
	 */
	public String[] getNames() {
		return nameArray;
	}

}
