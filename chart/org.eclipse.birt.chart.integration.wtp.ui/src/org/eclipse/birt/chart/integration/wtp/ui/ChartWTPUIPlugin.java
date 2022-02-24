/*************************************************************************************
 * Copyright (c) 2007 Actuate Corporation and others.
 * 
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * https://www.eclipse.org/legal/epl-2.0/.
 * 
 * SPDX-License-Identifier: EPL-2.0
 * 
 * Contributors:
 *     Actuate Corporation - Initial implementation.
 ************************************************************************************/

package org.eclipse.birt.chart.integration.wtp.ui;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ChartWTPUIPlugin implements BundleActivator {

	public static final String PLUGIN_ID = "org.eclipse.birt.chart.integration.wtp.ui"; //$NON-NLS-1$

	// project facet id of Runtime Component
	public static final String RUNTIME_FACET_ID = "birt.chart.runtime"; //$NON-NLS-1$

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	public void start(BundleContext context) throws Exception {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext context) throws Exception {
	}

}
