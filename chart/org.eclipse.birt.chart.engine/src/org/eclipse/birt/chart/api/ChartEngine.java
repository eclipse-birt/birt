/*******************************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.api;

import org.eclipse.birt.chart.datafeed.IDataSetProcessor;
import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.factory.Generator;
import org.eclipse.birt.chart.factory.IGenerator;
import org.eclipse.birt.chart.model.Serializer;
import org.eclipse.birt.chart.model.impl.SerializerImpl;
import org.eclipse.birt.chart.util.PluginSettings;
import org.eclipse.birt.core.framework.PlatformConfig;

/**
 * The Entry Point class to access the Eclipse Chart Engine (ECE) API
 */

public class ChartEngine {

	private PluginSettings ps = null;
	static private ChartEngine ce = null;

	/**
	 * A non-instantiable constructor
	 */
	private ChartEngine(PluginSettings ps) {
		this.ps = ps;
	}

	/**
	 * Retrieves a singleton instance of the Chart Engine
	 * 
	 * Initializes the OSGi Platform framework to load chart extension bundles
	 * unless the STANDALONE flag was set in PlatformConfig property.
	 * 
	 * If the config is null, it will use the BIRT_HOME JVM property to find the
	 * OSGi chart bundles. Subsequent calls to this method will ignore the config
	 * parameter .
	 * 
	 * @param config The OSGi platform configuration. Can be null.
	 */
	public static ChartEngine instance(PlatformConfig config) {

		if (ce == null) {
			synchronized (ChartEngine.class) {
				if (ce == null) {
					PluginSettings ps = PluginSettings.instance(config);
					ce = new ChartEngine(ps);
				}
			}
		}
		return ce;
	}

	/**
	 * Returns a singleton instance of the Chart Engine
	 * 
	 * @return A singleton instance of the Chart Engine
	 */
	public static ChartEngine instance() {
		return instance(null);
	}

	/**
	 * Retrieve the Serializer interface, used for loading/saving chart design
	 * from/to an XML stream
	 * 
	 * @return A Serializer instance
	 * @deprecated The Serializer instance is decoupled from the ChartEngine. To
	 *             obtain a instance of SerializerImpl use: SerializerImpl.instance(
	 *             ).
	 */
	public Serializer getSerializer() {
		return SerializerImpl.instance();
	}

	/**
	 * Returns the IChartGenerator interface used to run and render charts
	 * 
	 * @return IChartGenerator
	 */
	public IGenerator getGenerator() {
		return Generator.instance();
	}

	/**
	 * Loads a device renderer, required by IChartGenerator to render charts
	 * 
	 * @param deviceID The type of output. Examples are dv.SWT, dv.PNG, dv.JPG,
	 *                 dv.PDF, dv.SVG
	 * @return An IDeviceRenderer instance
	 * @throws ChartException If the device renderer does not exist or there is
	 *                        problem loading it.
	 */
	public IDeviceRenderer getRenderer(String deviceID) throws ChartException {
		return ps.getDevice(deviceID);
	}

	/**
	 * Retrieves the first instance of a data set processor registered as an
	 * extension for a given series type.
	 * 
	 * @param cSeries The Class instance associated with the given series type
	 * 
	 * @return A newly created instance of a registered data set processor extension
	 * 
	 * @throws ChartException
	 */
	public final IDataSetProcessor getDataSetProcessor(Class cSeries) throws ChartException {
		return ps.getDataSetProcessor(cSeries);
	}

}
