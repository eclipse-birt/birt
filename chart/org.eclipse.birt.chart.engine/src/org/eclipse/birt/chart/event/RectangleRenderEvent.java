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

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;
import org.eclipse.birt.chart.model.layout.Block;

/**
 * A rendering event type for rendering Rectangle object.
 */
public final class RectangleRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = -2020892672024222905L;

	private transient Bounds _bo;

	private transient LineAttributes _lia;

	private transient Fill _ifBackground;

	/**
	 * The constructor.
	 */
	public RectangleRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * @return Returns the bounds associated with this rectangle.
	 */
	@Override
	public Bounds getBounds() {
		return _bo;
	}

	/**
	 * Sets the bounds associated with this rectangle.
	 */
	public void setBounds(Bounds bo) {
		_bo = bo;
	}

	/**
	 * @return Returns the background fill associated with the rectangle.
	 */
	@Override
	public Fill getBackground() {
		return _ifBackground;
	}

	/**
	 * Sets the background fill associated with the rectangle.
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
	 * Sets the outline attributes of current rectangle.
	 */
	public void setOutline(LineAttributes lia) {
		_lia = lia;
	}

	/**
	 * Updates current event by given Block object.
	 */
	public void updateFrom(Block bl, double dScale) {
		_lia = bl.getOutline();
		_ifBackground = bl.getBackground();
		_bo = goFactory.scaleBounds(bl.getBounds(), dScale);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	@Override
	public PrimitiveRenderEvent copy() {
		final RectangleRenderEvent rre = new RectangleRenderEvent(source);
		if (_bo != null) {
			rre.setBounds(goFactory.copyOf(_bo));
		}

		if (_lia != null) {
			rre.setOutline(goFactory.copyOf(_lia));
		}

		if (_ifBackground != null) {
			rre.setBackground(goFactory.copyOf(_ifBackground));
		}
		return rre;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	@Override
	public void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawRectangle(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	@Override
	public void fill(IDeviceRenderer idr) throws ChartException {
		idr.fillRectangle(this);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	@Override
	public void reset() {
		this._bo = null;
		this._ifBackground = null;
		this._lia = null;

	}

	@Override
	public LineAttributes getLineAttributes() {
		return getOutline();
	}
}
