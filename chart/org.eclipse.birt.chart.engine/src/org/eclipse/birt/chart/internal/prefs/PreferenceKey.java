/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.internal.prefs;

/**
 * PreferenceKey
 */
final class PreferenceKey {

	public static final PreferenceKey N_LABEL = new PreferenceKey("label"); //$NON-NLS-1$

	public static final PreferenceKey N_DATAPOINT = new PreferenceKey("datapoint"); //$NON-NLS-1$

	public static final PreferenceKey N_AXIS = new PreferenceKey("axis"); //$NON-NLS-1$

	public static final PreferenceKey N_LEGEND = new PreferenceKey("legend"); //$NON-NLS-1$

	public static final PreferenceKey N_TITLE = new PreferenceKey("title"); //$NON-NLS-1$

	public static final PreferenceKey N_CHART = new PreferenceKey("chart"); //$NON-NLS-1$

	public static final PreferenceKey N_SERIES = new PreferenceKey("series"); //$NON-NLS-1$

	public static final String PK_FONT = "font"; //$NON-NLS-1$

	public static final String PK_TEXTCOLOR = "textcolor"; //$NON-NLS-1$

	public static final String PK_FILL = "fill"; //$NON-NLS-1$

	public static final String PK_OUTLINE = "outline"; //$NON-NLS-1$

	private final String sKey;

	private PreferenceKey(String _sKey) {
		sKey = _sKey;
	}

	final String getKey() {
		return sKey;
	}
}