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

package org.eclipse.birt.chart.reportitem.plugin;

import org.eclipse.birt.chart.computation.ChartComputationFactory;
import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IChartComputationFactory;
import org.eclipse.birt.chart.device.IImageWriterFactory;
import org.eclipse.birt.chart.device.IScriptMenuHelper;
import org.eclipse.birt.chart.device.ImageWriterFactory;
import org.eclipse.birt.chart.device.ScriptMenuHelper;
import org.eclipse.birt.chart.model.IChartModelHelper;
import org.eclipse.birt.chart.model.impl.ChartModelHelper;
import org.eclipse.birt.chart.reportitem.ChartReportItemUtil;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemConstants;
import org.eclipse.birt.chart.reportitem.api.ChartReportItemHelper;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * Plugin class for Chart Report Item Integration
 */

public class ChartReportItemPlugin extends Plugin {

	/** Plugin ID */
	public static final String ID = ChartReportItemConstants.ID;

	/**
	 * The shared instance.
	 */
	private static ChartReportItemPlugin plugin;

	public ChartReportItemPlugin() {
		super();
		plugin = this;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChartReportItemPlugin getDefault() {
		return plugin;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.Plugin#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		initChartComputation(this);
		initImageWriterFactory(this);
		initChartModelHelper(this);
		initChartScriptMenuHelper(this);
		initChartReportItemHelper(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	private static void initChartScriptMenuHelper(ChartReportItemPlugin plugin) {
		IScriptMenuHelper factory = ChartReportItemUtil.getAdapter(plugin, IScriptMenuHelper.class);
		if (factory != null) {
			ScriptMenuHelper.initInstance(factory);
		}
	}

	private static void initChartComputation(ChartReportItemPlugin plugin) {
		IChartComputationFactory factory = ChartReportItemUtil.getAdapter(plugin, IChartComputationFactory.class);
		if (factory != null) {
			ChartComputationFactory.initInstance(factory);
			GObjectFactory.initInstance(factory.createGObjectFactory());
		}
	}

	private static void initImageWriterFactory(ChartReportItemPlugin plugin) {
		IImageWriterFactory factory = ChartReportItemUtil.getAdapter(plugin, IImageWriterFactory.class);
		if (factory != null) {
			ImageWriterFactory.initInstance(factory);
		}
	}

	private static void initChartModelHelper(ChartReportItemPlugin plugin) {
		IChartModelHelper factory = ChartReportItemUtil.getAdapter(plugin, IChartModelHelper.class);
		if (factory != null) {
			ChartModelHelper.initInstance(factory);
		}
	}

	private static void initChartReportItemHelper(ChartReportItemPlugin plugin) {
		ChartReportItemHelper factory = ChartReportItemUtil.getAdapter(plugin, ChartReportItemHelper.class);
		if (factory != null) {
			ChartReportItemHelper.initInstance(factory);
		}
	}
}
