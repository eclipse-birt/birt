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

import java.io.IOException;

import org.eclipse.birt.chart.internal.log.JavaUtilLoggerImpl;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Plugin class for Chart Engine. Holds the plugin ID
 */

public class ChartEnginePlugin extends Plugin
{

	/**
	 * Plugin ID.
	 */
	public static final String ID = "org.eclipse.birt.chart.engine"; //$NON-NLS-1$ 

	private static ILogger logger = Logger.getLogger( "org.eclipse.birt.chart.engine/ChartEnginePlugin" ); //$NON-NLS-1$

	@Override
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );

		// initialize the file logger
		try
		{
			JavaUtilLoggerImpl.initFileHandler( this.getStateLocation( )
					.toOSString( ) );
		}
		catch ( SecurityException e )
		{
			logger.log( e );
		}
		catch ( IOException e )
		{
			logger.log( e );
		}
	}

}
