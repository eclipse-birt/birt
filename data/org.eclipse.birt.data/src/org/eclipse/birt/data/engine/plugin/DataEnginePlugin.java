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
package org.eclipse.birt.data.engine.plugin;

import org.eclipse.birt.core.plugin.BIRTPlugin;
import org.eclipse.birt.data.engine.api.aggregation.AggregationManager;
import org.eclipse.birt.data.engine.odaconsumer.ConnectionManager;
import org.osgi.framework.BundleContext;

/**
 * Obtain support from BIRTPlugin
 */
public class DataEnginePlugin extends BIRTPlugin {
	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		destroyAggregationFactoryInstance();
		ConnectionManager.releaseInstance();
		super.stop(context);
	}

	/**
	 * Destroy shared instance of AggregationManager.
	 *
	 */
	private void destroyAggregationFactoryInstance() {
		AggregationManager.destroyInstance();
	}

}
