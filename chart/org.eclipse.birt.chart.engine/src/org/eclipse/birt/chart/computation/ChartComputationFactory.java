/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.computation;

/**
 * 
 */

public class ChartComputationFactory implements IChartComputationFactory {

	private static IChartComputationFactory factory = new ChartComputationFactory();

	public static void initInstance(IChartComputationFactory tFactory) {
		factory = tFactory;
	}

	public static IChartComputationFactory instance() {
		return factory;
	}

	public IGObjectFactory createGObjectFactory() {
		return new GObjectFactory();
	}

	public IChartComputation createChartComputation() {
		return new BIRTChartComputation();
	}

}
