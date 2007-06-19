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

import org.eclipse.birt.report.item.crosstab.ui.preference.CrosstabPreferencePage;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * 
 */

public class CrosstabPlugin extends Plugin
{
	
	/** Plugin ID */
	public static final String ID = "org.eclipse.birt.report.item.crosstab.ui"; //$NON-NLS-1$
	
	/** Preference ID */
	public static final String PREFERENCE_FILTER_LIMIT = "Filter.Limit"; //$NON-NLS-1$
	
	public static final String PREFERENCE_AUTO_DEL_BINDINGS="Auto.Del.Bindings";

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
	
	/**
	 * This method is called upon plug-in activation
	 */
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );

		// Initializes all chart related preference values
		CrosstabPreferencePage.init( );
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop( BundleContext context ) throws Exception
	{
		super.stop( context );
		plugin = null;
	}



}
