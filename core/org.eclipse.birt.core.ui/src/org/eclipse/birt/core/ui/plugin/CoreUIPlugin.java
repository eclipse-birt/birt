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

package org.eclipse.birt.core.ui.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 * Plugin class for Chart UI Extension
 */

public class CoreUIPlugin extends Plugin {

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.core.ui"; //$NON-NLS-1$

	// The shared instance.
	private static CoreUIPlugin plugin;

	/**
	 * The constructor.
	 */
	public CoreUIPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CoreUIPlugin getDefault() {
		return plugin;
	}

}
