/*******************************************************************************
 * Copyright (c) 2005 Actuate Corporation.
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

import java.util.EventObject;

/**
 * Base Abstract class for various Chart Events: Primitive render events,
 * interaction events, structure change events
 * 
 * @see EventObjectCache
 */
public abstract class ChartEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	/**
	 * The constructor.
	 */
	public ChartEvent(Object source) {
		super(source);
	}

	/**
	 * Resets the inner state of current event. This must be implemented if the
	 * object is cached and reused.
	 */
	public abstract void reset();

	/**
	 * Sets the source object of current event.
	 * 
	 * @param oSource
	 */
	public void setSourceObject(Object oSource) {
		super.source = oSource;
	}

}
