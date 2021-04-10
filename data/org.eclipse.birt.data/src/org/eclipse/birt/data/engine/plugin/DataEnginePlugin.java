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
