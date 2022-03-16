/*******************************************************************************
 * Copyright (c) 2008 Actuate Corporation.
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

	@Override
	public IGObjectFactory createGObjectFactory() {
		return new GObjectFactory();
	}

	@Override
	public IChartComputation createChartComputation() {
		return new BIRTChartComputation();
	}

}
