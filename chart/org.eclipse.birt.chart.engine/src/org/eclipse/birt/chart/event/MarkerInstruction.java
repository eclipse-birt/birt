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

import java.util.Collections;
import java.util.List;

import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.render.DeferredCache;

/**
 * This class wraps a rendering event for Marker
 */

public class MarkerInstruction implements IRenderInstruction {

	private final DeferredCache dc;

	private final int iInstruction;

	private final double iMarkerSize;

	private final int zOrder;

	private final PrimitiveRenderEvent pre;

	public MarkerInstruction(DeferredCache dc, PrimitiveRenderEvent pre, int iInstruction, double iMarkerSize,
			int zOrder) {
		this.dc = dc;
		this.pre = pre;
		this.iInstruction = iInstruction;
		this.iMarkerSize = iMarkerSize;
		this.zOrder = zOrder;
	}

	public int compareTo(Object o) {
		if (o instanceof MarkerInstruction) {
			if (((MarkerInstruction) o).zOrder != zOrder) {
				return (zOrder - ((MarkerInstruction) o).zOrder);
			}
			// Descending order
			return (int) (((MarkerInstruction) o).getMarkerSize() - iMarkerSize);
		}
		Bounds bo = null;

		if (o instanceof PrimitiveRenderEvent) {
			try {
				bo = ((PrimitiveRenderEvent) o).getBounds();
			} catch (ChartException e) {
				assert false;
				return -1;
			}
		} else if (o instanceof WrappedInstruction) {
			bo = ((WrappedInstruction) o).getBounds();
		}

		return (dc != null && dc.isTransposed())
				? (bo == null ? 1 : PrimitiveRenderEvent.compareTransposed(getBounds(), bo))
				: (bo == null ? 1 : PrimitiveRenderEvent.compareRegular(getBounds(), bo));

	}

	/**
	 * Returns the associated event.
	 * 
	 * @return Render Event
	 */
	public final PrimitiveRenderEvent getEvent() {
		return pre;
	}

	/**
	 * @return Returns the associated instruction. The value could be one of these:
	 *         <ul>
	 *         <li>PrimitiveRenderEvent.DRAW
	 *         <li>PrimitiveRenderEvent.FILL
	 *         </ul>
	 */
	public final int getInstruction() {
		return iInstruction;
	}

	/**
	 * Returns the size of marker
	 * 
	 * @return marker size
	 */
	public final double getMarkerSize() {
		return iMarkerSize;
	}

	/**
	 * @return Returns the minimum bounds required to contain the rendering area of
	 *         associated rendering event.
	 */
	public final Bounds getBounds() {
		try {
			return pre.getBounds();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("rawtypes")
	public List getModel() {
		return Collections.EMPTY_LIST;
	}

	public boolean isModel() {
		// Always single event
		return false;
	}

	/**
	 * Returns the zOrder of marker
	 * 
	 * @return marker zOrder
	 */
	public final int getMarkerZOrder() {
		return zOrder;
	}

}
