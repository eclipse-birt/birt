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

package org.eclipse.birt.chart.reportitem.plugin;

import org.eclipse.birt.chart.reportitem.ui.ChartPreferencePage;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Plugin class for Chart Report Item Integration
 */

public class ChartReportItemPlugin extends Plugin
{

	/** Plugin ID */
	public static final String ID = "org.eclipse.birt.chart.reportitem"; //$NON-NLS-1$

	/** Preference ID */
	public static final String PREFERENCE_ENALBE_LIVE = "enable_live"; //$NON-NLS-1$
	public static final String PREFERENCE_MAX_ROW = "max_row"; //$NON-NLS-1$

	/**
	 * The shared instance.
	 */
	private static ChartReportItemPlugin plugin;

	public ChartReportItemPlugin( )
	{
		super( );
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChartReportItemPlugin getDefault( )
	{
		return plugin;
	}

	public void start( BundleContext context ) throws Exception
	{
		super.start( context );

		// Initializes all chart related preference values
		ChartPreferencePage.init( );
	}	

}
