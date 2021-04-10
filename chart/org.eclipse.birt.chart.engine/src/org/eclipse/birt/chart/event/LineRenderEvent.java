/***********************************************************************
 * Copyright (c) 2004 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.attribute.Location;

/**
 * A rendering event type for rendering Line object.
 */
public class LineRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = 7216549683820618553L;

	private transient Location loStart;

	private transient Location loEnd;

	private int zOrder;

	protected transient LineAttributes lia;

	/**
	 * The constructor.
	 */
	public LineRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the start location of the line.
	 */
	public final void setStart(Location _loStart) {
		loStart = _loStart;
	}

	/**
	 * @return Returns the start location of the line.
	 */
	public final Location getStart() {
		return loStart;
	}

	/**
	 * Sets the end location of the line.
	 */
	public final void setEnd(Location _loEnd) {
		loEnd = _loEnd;
	}

	/**
	 * @return Returns the end location of the line.
	 */
	public final Location getEnd() {
		return loEnd;
	}

	/**
	 * Sets the line attributes of this event.
	 */
	public final void setLineAttributes(LineAttributes _lia) {
		lia = _lia;
	}

	/**
	 * @return Returns the line attributes.
	 */
	public final LineAttributes getLineAttributes() {
		return lia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#getBounds()
	 */
	public final Bounds getBounds() {
		final double dMinX = Math.min(loStart.getX(), loEnd.getX());
		final double dMaxX = Math.max(loStart.getX(), loEnd.getX());
		final double dMinY = Math.min(loStart.getY(), loEnd.getY());
		final double dMaxY = Math.max(loStart.getY(), loEnd.getY());
		return goFactory.createBounds(dMinX, dMinY, dMaxX - dMinX, dMaxY - dMinY);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		LineRenderEvent lre = new LineRenderEvent(source);
		lre.setLineAttributes(goFactory.copyOf(lia));

		if (loStart != null) {
			lre.setStart(loStart.copyInstance());
		}

		if (loEnd != null) {
			lre.setEnd(loEnd.copyInstance());
		}

		lre.setZOrder(zOrder);
		return lre;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public void fill(IDeviceRenderer idr) throws ChartException {
		draw(idr);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public final void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawLine(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	public void reset() {
		this.loEnd = null;
		this.loStart = null;
		this.lia = null;
		this.zOrder = 0;
	}

	/**
	 * Sets the zOrder of the line.
	 */
	public final void setZOrder(int _zOrder) {
		zOrder = _zOrder;
	}

	/**
	 * @return Returns the zOrder of the line.
	 */
	public final int getZOrder() {
		return zOrder;
	}
}
