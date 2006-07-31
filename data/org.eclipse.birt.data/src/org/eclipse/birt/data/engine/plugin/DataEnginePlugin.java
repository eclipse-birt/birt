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
import org.eclipse.birt.data.engine.aggregation.AggregationFactory;
import org.osgi.framework.BundleContext;


/**
 * Obtain support from BIRTPlugin
 */
public class DataEnginePlugin extends BIRTPlugin
{
	/**
	 * Destroy shared instance of AggregationFactory.
	 * 
	 */
	private void DestroyAggregationFactoryInstance( )
	{
		AggregationFactory.destroyInstance( );
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	public void stop( BundleContext context ) throws Exception
	{
		DestroyAggregationFactoryInstance( );
		super.stop( context );
	}
	
}
