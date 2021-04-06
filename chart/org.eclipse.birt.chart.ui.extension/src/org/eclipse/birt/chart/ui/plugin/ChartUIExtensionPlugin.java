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

package org.eclipse.birt.chart.ui.plugin;

import org.eclipse.ui.plugin.AbstractUIPlugin;

/**
 * Plugin class for Chart UI Extension
 */

public class ChartUIExtensionPlugin extends AbstractUIPlugin {

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.ui.extension"; //$NON-NLS-1$

	private static ChartUIExtensionPlugin plugin = null;

	/**
	 * Constructor.
	 */
	public ChartUIExtensionPlugin() {
		plugin = this;
	}

	/**
	 * Returns current plugin instance.
	 * 
	 * @return
	 * @since 2.5
	 */
	public static AbstractUIPlugin getDefault() {
		return plugin;
	}
}
