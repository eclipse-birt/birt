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
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.util.FillUtil;

/**
 * A rendering event type for rendering Polygon object.
 */
public class PolygonRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = 7825900630615976817L;

	private transient Location[] _loa;

	protected transient LineAttributes _lia;

	protected transient Fill _ifBackground;

	/**
	 * The constructor.
	 */
	public PolygonRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Sets the co-ordinates for each point that defines the polygon.
	 */
	public final void setPoints(Location[] la) {
		_loa = la;
	}

	/**
	 * @return Returns the co-ordinates for each point in the polygon
	 */
	public final Location[] getPoints() {
		return _loa;
	}

	/**
	 * @return Returns the background fill attributes for the polygon
	 */
	@Override
	public Fill getBackground() {
		return _ifBackground;
	}

	/**
	 * Sets the background fill attributes for the polygon
	 */
	public void setBackground(Fill ifBackground) {
		_ifBackground = ifBackground;
	}

	/**
	 * @return Returns the polygon outline attributes.
	 */
	public LineAttributes getOutline() {
		return _lia;
	}

	/**
	 * Sets the polygon outline attributes
	 */
	public void setOutline(LineAttributes lia) {
		_lia = lia;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#getBounds()
	 */
	@Override
	public Bounds getBounds() throws ChartException {
		final Bounds bo = goFactory.createBounds(0, 0, 0, 0);
		bo.updateFrom(_loa);
		return bo;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	@Override
	public PrimitiveRenderEvent copy() {
		final PolygonRenderEvent pre = new PolygonRenderEvent(source);
		if (_loa != null) {
			final Location[] loa = new Location[this._loa.length];
			for (int i = 0; i < loa.length; i++) {
				loa[i] = _loa[i].copyInstance();
			}
			pre.setPoints(loa);
		}

		if (_lia != null) {
			pre.setOutline(goFactory.copyOf(_lia));
		}

		if (_ifBackground != null) {
			pre.setBackground(FillUtil.copyOf(_ifBackground));
		}

		pre.setDepth(getDepth());
		return pre;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	@Override
	public final void draw(IDeviceRenderer idr) throws ChartException {
		if (bEnabled) {
			idr.drawPolygon(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	@Override
	public final void fill(IDeviceRenderer idr) throws ChartException {
		if (bEnabled) {
			idr.fillPolygon(this);
		}
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	@Override
	public void reset() {
		this._ifBackground = null;
		this._lia = null;
		this._loa = null;

	}

	@Override
	public LineAttributes getLineAttributes() {
		return getOutline();
	}
}
