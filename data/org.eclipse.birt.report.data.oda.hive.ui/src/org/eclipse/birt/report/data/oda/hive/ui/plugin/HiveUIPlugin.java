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
package org.eclipse.birt.report.data.oda.hive.ui.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 *
 */
public class HiveUIPlugin extends Plugin {

	// The shared instance.
	private static HiveUIPlugin plugin;

	public HiveUIPlugin() {
		super();
		plugin = this;
	}

	public static HiveUIPlugin getDefault() {
		return plugin;
	}
}
