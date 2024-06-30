/*************************************************************************************
 * Copyright (c) 2011, 2012, 2013 James Talbut.
 *  jim-emitters@spudsoft.co.uk
 *
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     James Talbut - Initial implementation.
 ************************************************************************************/

package uk.co.spudsoft.birt.emitters.excel.framework;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * ExcelEmitterPlugin represents the SpudSoft Excel emitter. <br/>
 * This is the activator for the plugin and is necessary to capture the eclipse
 * log. <br/>
 * Note that the BIRT runtime is not OSGi and does not activate the bundle, so
 * getDefault always returns null when used in that environment.
 *
 * @author Jim Talbut
 *
 */
public class ExcelEmitterPlugin extends Plugin {

	private static ExcelEmitterPlugin plugin;

	/**
	 * Get the plugin, if it has been activated.
	 *
	 * @return The plugin, if it has been activated, or null otherwise.
	 */
	public static ExcelEmitterPlugin getDefault() {
		return plugin;
	}

	private Logger logger;

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		super.start(bundleContext);
		plugin = this;
		logger = new Logger(getLog(), bundleContext.getBundle().getSymbolicName());
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		super.stop(bundleContext);
	}

	/**
	 * Get the logger.
	 *
	 * @return The logger.
	 */
	public Logger getLogger() {
		return logger;
	}

}
