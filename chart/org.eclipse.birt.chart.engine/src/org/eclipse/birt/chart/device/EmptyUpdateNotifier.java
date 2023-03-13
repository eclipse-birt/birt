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

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.model.Chart;

/**
 * A convenient empty update notifier doing nothing.
 */
public class EmptyUpdateNotifier implements IUpdateNotifier {

	private Chart designModel = null;
	private Chart runtimeModel = null;

	/**
	 * The constructor.
	 *
	 * @param designModel
	 * @param runtimeModel
	 */
	public EmptyUpdateNotifier(Chart designModel, Chart runtimeModel) {
		this.designModel = designModel;
		this.runtimeModel = runtimeModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#regenerateChart()
	 */
	@Override
	public void regenerateChart() {
		// DOING NOTHING.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#repaintChart()
	 */
	@Override
	public void repaintChart() {
		// DOING NOTHING.
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#peerInstance()
	 */
	@Override
	public Object peerInstance() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#getDesignTimeModel()
	 */
	@Override
	public Chart getDesignTimeModel() {
		return designModel;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.device.IUpdateNotifier#getRunTimeModel()
	 */
	@Override
	public Chart getRunTimeModel() {
		return runtimeModel;
	}

}
