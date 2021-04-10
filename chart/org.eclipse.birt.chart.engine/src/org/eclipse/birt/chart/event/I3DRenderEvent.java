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
