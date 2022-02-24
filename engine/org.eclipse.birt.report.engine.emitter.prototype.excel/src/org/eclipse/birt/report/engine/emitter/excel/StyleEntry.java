/*******************************************************************************
 * Copyright (c) 2004, 2008Actuate Corporation.
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

package org.eclipse.birt.report.engine.emitter.excel;

public class StyleEntry implements StyleConstant {
	private boolean isHyperlink = false;

	private Object[] props = null;
	private int hashCode;

	public StyleEntry(StyleEntry entry) {
		this();
		if (entry == null) {
			return;
		}
		for (int i = 0; i < props.length; i++) {
			props[i] = entry.props[i];
		}
		hashCode = entry.hashCode;
	}

	public StyleEntry() {
		props = new Object[StyleConstant.COUNT];
	}

	public void setProperty(int id, Object value) {
		props[id] = value;
		int tmpCode = (value == null ? 0 : value.hashCode()) << (id % 31);
		hashCode = hashCode + tmpCode;
	}

	public Object getProperty(int id) {
		return props[id];
	}

	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}

		if (!(obj instanceof StyleEntry)) {
			return false;
		}

		StyleEntry tar = (StyleEntry) obj;

		for (int i = 0; i < StyleConstant.COUNT; i++) {
			if (props[i] != null) {
				if (!props[i].equals(tar.getProperty(i))) {
					return false;
				}
			} else {
				if (props[i] != tar.getProperty(i)) {
					return false;
				}
			}
		}

		return true;
	}

	public int hashCode() {
		return hashCode;
	}

	public static boolean isNull(Object value) {
		if (value == null)
			return true;
		if (value instanceof String)
			return StyleConstant.NULL.equalsIgnoreCase((String) value);
		return false;
	}

	public void setIsHyperlink(boolean isHyperlink) {
		this.isHyperlink = isHyperlink;
	}

	public boolean isHyperlink() {
		return isHyperlink;
	}
}
