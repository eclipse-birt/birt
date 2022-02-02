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

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.model.attribute.Location;

/**
 * This class encapsulates clipping information required for clip implementation
 * in a device. Support for clipped areas is limited to polygons. In the future,
 * it may be upgraded to support additional arbitrary shape definitions.
 */
public final class ClipRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = -1609479639743164885L;

	private transient Location[] _loa;

	/**
	 * The constructor.
	 */
	public ClipRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * @return Returns the vertices associated with a polygon.
	 */
	public final Location[] getVertices() {
		return _loa;
	}

	/**
	 * Sets the vertices of the clip.
	 * 
	 * @param loa The vertices associated with the polygon area to be clipped
	 */
	public final void setVertices(Location[] loa) {
		_loa = loa;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	public void reset() {
		this._loa = null;

	}
}
