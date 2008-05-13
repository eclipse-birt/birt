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

package org.eclipse.birt.chart.plugin;

import org.eclipse.core.runtime.Plugin;

/**
 * Plugin class for Chart Engine. Holds the plugin ID
 */

public class ChartEnginePlugin extends Plugin
{

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.engine"; //$NON-NLS-1$ 

	private static ChartEnginePlugin plugin;

	public ChartEnginePlugin( )
	{
		super( );
		plugin = this;
	}

	public static ChartEnginePlugin getDefault( )
	{
		return plugin;
	}

}
