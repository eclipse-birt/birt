/***********************************************************************
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
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.device;

import org.eclipse.birt.chart.model.Chart;

/**
 * Defines methods that allow model updating via a device event handler used in
 * conjunction with a device renderer.
 *
 * @see IDeviceRenderer
 */
public interface IUpdateNotifier {

	/**
	 * Requests the container to regenerate the chart using the design-time model.
	 * It should call IGenerator.build() or refresh() and render()
	 */
	void regenerateChart();

	/**
	 * Requests the container to repaint the last generated chart This should call
	 * IGenerator.render(), but not build() nor refresh().
	 */
	void repaintChart();

	/**
	 * Returns an instance of the peer (component) used for device-specific actions
	 *
	 * @return An instance of the peer (component) used for device-specific actions
	 */
	Object peerInstance();

	/**
	 * Returns an instance of the chart design-time model
	 *
	 * @return An instance of the chart design-time model
	 */
	Chart getDesignTimeModel();

	/**
	 * Returns an instance of the chart run-time model for the last generated
	 * instance
	 *
	 * @return An instance of the chart run-time model for the last generated
	 *         instance
	 */
	Chart getRunTimeModel();

}
