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

package org.eclipse.birt.core.preference;

import java.io.IOException;

public interface IPreferences {

	/**
	 * The default-default value for boolean preferences (<code>false</code>).
	 */
	public static final boolean BOOLEAN_DEFAULT_DEFAULT = false;

	/**
	 * The default-default value for double preferences (<code>0.0</code>).
	 */
	public static final double DOUBLE_DEFAULT_DEFAULT = 0.0;

	/**
	 * The default-default value for float preferences (<code>0.0f</code>).
	 */
	public static final float FLOAT_DEFAULT_DEFAULT = 0.0f;

	/**
	 * The default-default value for int preferences (<code>0</code>).
	 */
	public static final int INT_DEFAULT_DEFAULT = 0;

	/**
	 * The default-default value for long preferences (<code>0L</code>).
	 */
	public static final long LONG_DEFAULT_DEFAULT = 0L;

	/**
	 * The default-default value for String preferences (<code>""</code>).
	 */
	public static final String STRING_DEFAULT_DEFAULT = ""; //$NON-NLS-1$

	/**
	 * The string representation used for <code>true</code> (<code>"true"</code>).
	 */
	public static final String TRUE = "true"; //$NON-NLS-1$

	/**
	 * The string representation used for <code>false</code> (<code>"false"</code>).
	 */
	public static final String FALSE = "false"; //$NON-NLS-1$

	public void save() throws IOException;

	public void setValue(String name, boolean value);

	public void setValue(String name, String value);

	public void setValue(String name, long value);

	public void setValue(String name, int value);

	public void setValue(String name, float value);

	public void setValue(String name, double value);

	public void setToDefault(String name);

	public void setDefault(String name, boolean value);

	public void setDefault(String name, String value);

	public void setDefault(String name, long value);

	public void setDefault(String name, int value);

	public void setDefault(String name, float value);

	public void setDefault(String name, double value);

	public void putValue(String name, String value);

	public boolean isDefault(String name);

	public boolean contains(String name);

	public String getString(String name);

	public long getLong(String name);

	public int getInt(String name);

	public float getFloat(String name);

	public double getDouble(String name);

	public boolean getBoolean(String name);

	public String getDefaultString(String name);

	public long getDefaultLong(String name);

	public int getDefaultInt(String name);

	public float getDefaultFloat(String name);

	public double getDefaultDouble(String name);

	public boolean getDefaultBoolean(String name);

	public void addPreferenceChangeListener(IPreferenceChangeListener pcl);

	public void removePreferenceChangeListener(IPreferenceChangeListener pcl);

	public void firePreferenceChangeEvent(String name, Object oldValue, Object newValue);
}