/*******************************************************************************
 * Copyright (c) 2007 Actuate Corporation.
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

import java.util.List;

import org.eclipse.birt.chart.model.attribute.Bounds;

/**
 * A render instruction wraps a render event or several events. It could be used
 * to compare with other events or instructions.
 */

public interface IRenderInstruction extends Comparable {

	/**
	 * Returns the associated instruction.
	 *
	 * @return The value could be one of these:
	 *         <ul>
	 *         <li>PrimitiveRenderEvent.DRAW
	 *         <li>PrimitiveRenderEvent.FILL
	 *         </ul>
	 */
	int getInstruction();

	/**
	 * Returns the associated event.
	 *
	 * @return render event
	 */
	PrimitiveRenderEvent getEvent();

	/**
	 * @return Returns the minimum bounds required to contain the rendering area of
	 *         associated rendering event.
	 */
	Bounds getBounds();

	/**
	 * @return Returns if wraps multiple events currently.
	 */
	boolean isModel();

	/**
	 * @return Returns list of events currently wraps.
	 */
	List getModel();
}
