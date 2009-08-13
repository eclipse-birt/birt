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

import org.eclipse.birt.chart.computation.ChartComputationFactory;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputationFactory;
import org.eclipse.birt.chart.device.IImageWriterFactory;
import org.eclipse.birt.chart.device.ImageWriterFactory;
import org.eclipse.birt.chart.internal.log.JavaUtilLoggerImpl;
import org.eclipse.birt.chart.model.IChartModelHelper;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.util.ChartUtil;
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

	@Override
	public void start( BundleContext context ) throws Exception
	{
		super.start( context );
		JavaUtilLoggerImpl.setStateDir( getStateLocation( ).toOSString( ) );
		initChartComputation( this );
		initImageWriterFactory( this );
		initChartModelHelper( this );
	}

	private static void initChartComputation( ChartEnginePlugin plugin )
	{
		IChartComputationFactory factory = ChartUtil.getAdapter( plugin,
				IChartComputationFactory.class );
		if ( factory != null )
		{
			ChartComputationFactory.initInstance( factory );
			GObjectFactory.initInstance( factory.createGObjectFactory( ) );
		}
	}

	private static void initImageWriterFactory( ChartEnginePlugin plugin )
	{
		IImageWriterFactory factory = ChartUtil.getAdapter( plugin,
				IImageWriterFactory.class );
		if ( factory != null )
		{
			ImageWriterFactory.initInstance( factory );
		}
	}

	private static void initChartModelHelper( ChartEnginePlugin plugin )
	{
		IChartModelHelper factory = ChartUtil.getAdapter( plugin,
				IChartModelHelper.class );
		if ( factory != null )
		{
			ChartModelHelper.initInstance( factory );
		}
	}

}
