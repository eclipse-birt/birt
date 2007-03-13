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

package org.eclipse.birt.report.item.crosstab.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 * 
 */

public class CrosstabPlugin extends Plugin
{
	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.core.ui"; //$NON-NLS-1$

	// The shared instance.
	private static CrosstabPlugin plugin;

	/**
	 * The constructor.
	 */
	public CrosstabPlugin( )
	{
		super( );
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static CrosstabPlugin getDefault( )
	{
		return plugin;
	}

}
