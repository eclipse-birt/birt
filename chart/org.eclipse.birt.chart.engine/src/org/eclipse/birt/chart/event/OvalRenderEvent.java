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
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 * A rendering event type for rendering Oval object.
 */
public class OvalRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = -6716453650694010927L;

	protected transient Bounds _bo = null;

	protected transient LineAttributes _lia;

	protected transient Fill _ifBackground;

	/**
	 * The constructor.
	 */
	public OvalRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the bounds of the oval.
	 */
	public final void setBounds(Bounds bo) {
		_bo = bo;
	}

	/**
	 * @return Returns the bounds of the oval.
	 */
	public final Bounds getBounds() {
		final Bounds bo = goFactory.createBounds(_bo.getLeft(), _bo.getTop(), _bo.getWidth(), _bo.getHeight());
		return bo;
	}

	/**
	 * @return Returns the background.
	 */
	public Fill getBackground() {
		return _ifBackground;
	}

	/**
	 * Sets the background attributes.
	 */
	public void setBackground(Fill ifBackground) {
		_ifBackground = ifBackground;
	}

	/**
	 * @return Returns the outline.
	 */
	public LineAttributes getOutline() {
		return _lia;
	}

	/**
	 * Sets the outline attributes.
	 * 
	 * @param ls The outline to set.
	 */
	public void setOutline(LineAttributes lia) {
		_lia = lia;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() {
		final OvalRenderEvent ore = new OvalRenderEvent(source);
		ore.setBounds(goFactory.copyOf(_bo));

		ore.setOutline(goFactory.copyOf(_lia));

		ore.setBackground(goFactory.copyOf(_ifBackground));
		return ore;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public final void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawOval(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public final void fill(IDeviceRenderer idr) throws ChartException {
		idr.fillOval(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	public void reset() {
		this._bo = null;
		this._ifBackground = null;
		this._lia = null;

	}

	public LineAttributes getLineAttributes() {
		return getOutline();
	}
}
