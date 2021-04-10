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
