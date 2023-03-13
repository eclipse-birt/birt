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

import java.io.IOException;

public interface IPreferences {

	/**
	 * The default-default value for boolean preferences (<code>false</code>).
	 */
	boolean BOOLEAN_DEFAULT_DEFAULT = false;

	/**
	 * The default-default value for double preferences (<code>0.0</code>).
	 */
	double DOUBLE_DEFAULT_DEFAULT = 0.0;

	/**
	 * The default-default value for float preferences (<code>0.0f</code>).
	 */
	float FLOAT_DEFAULT_DEFAULT = 0.0f;

	/**
	 * The default-default value for int preferences (<code>0</code>).
	 */
	int INT_DEFAULT_DEFAULT = 0;

	/**
	 * The default-default value for long preferences (<code>0L</code>).
	 */
	long LONG_DEFAULT_DEFAULT = 0L;

	/**
	 * The default-default value for String preferences (<code>""</code>).
	 */
	String STRING_DEFAULT_DEFAULT = ""; //$NON-NLS-1$

	/**
	 * The string representation used for <code>true</code> (<code>"true"</code>).
	 */
	String TRUE = "true"; //$NON-NLS-1$

	/**
	 * The string representation used for <code>false</code> (<code>"false"</code>).
	 */
	String FALSE = "false"; //$NON-NLS-1$

	void save() throws IOException;

	void setValue(String name, boolean value);

	void setValue(String name, String value);

	void setValue(String name, long value);

	void setValue(String name, int value);

	void setValue(String name, float value);

	void setValue(String name, double value);

	void setToDefault(String name);

	void setDefault(String name, boolean value);

	void setDefault(String name, String value);

	void setDefault(String name, long value);

	void setDefault(String name, int value);

	void setDefault(String name, float value);

	void setDefault(String name, double value);

	void putValue(String name, String value);

	boolean isDefault(String name);

	boolean contains(String name);

	String getString(String name);

	long getLong(String name);

	int getInt(String name);

	float getFloat(String name);

	double getDouble(String name);

	boolean getBoolean(String name);

	String getDefaultString(String name);

	long getDefaultLong(String name);

	int getDefaultInt(String name);

	float getDefaultFloat(String name);

	double getDefaultDouble(String name);

	boolean getDefaultBoolean(String name);

	void addPreferenceChangeListener(IPreferenceChangeListener pcl);

	void removePreferenceChangeListener(IPreferenceChangeListener pcl);

	void firePreferenceChangeEvent(String name, Object oldValue, Object newValue);
}
