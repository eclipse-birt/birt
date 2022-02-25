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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.computation.Object3D;

/**
 * An interface that all 3D rendering events must extend.
 */

public interface I3DRenderEvent {

	/**
	 * Returns the 3D object associated with this event.
	 *
	 * @return
	 */
	Object3D getObject3D();

	/**
	 * Prepares the coordinates to render on 2D plane.
	 *
	 * @param xOffset
	 * @param yOffset
	 */
	void prepare2D(double xOffset, double yOffset);
}
