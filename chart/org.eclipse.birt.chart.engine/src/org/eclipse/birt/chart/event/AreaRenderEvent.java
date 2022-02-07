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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.birt.chart.device.IDeviceRenderer;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.log.ILogger;
import org.eclipse.birt.chart.log.Logger;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Fill;
import org.eclipse.birt.chart.model.attribute.LineAttributes;

/**
 * A rendering event type for rendering Area object.
 */
public class AreaRenderEvent extends PrimitiveRenderEvent {

	private static final long serialVersionUID = 4924819106091024348L;

	protected final List<PrimitiveRenderEvent> alLinesAndArcs = new ArrayList<PrimitiveRenderEvent>();

	protected transient Fill fill;

	protected transient LineAttributes lia;

	private static ILogger logger = Logger.getLogger("org.eclipse.birt.chart.engine/event"); //$NON-NLS-1$

	/**
	 * The constructor.
	 */
	public AreaRenderEvent(Object oSource) {
		super(oSource);
	}

	/**
	 * Removes all sub events associated with current area.
	 */
	public final void clear() {
		alLinesAndArcs.clear();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.ChartEvent#reset()
	 */
	public void reset() {
		alLinesAndArcs.clear();
		fill = null;
		lia = null;
	}

	/**
	 * Add a sub event to this area.
	 * 
	 * @param pre
	 */
	public final void add(PrimitiveRenderEvent pre) {
		alLinesAndArcs.add(pre);
	}

	/**
	 * @return Returns the sub events count of this area.
	 */
	public final int getElementCount() {
		return alLinesAndArcs.size();
	}

	/**
	 * Returns the iterator for the subordinate event list.
	 * 
	 * @return
	 */
	public final Iterator<PrimitiveRenderEvent> iterator() {
		return alLinesAndArcs.iterator();
	}

	/**
	 * Returns the specific sub event by given index.
	 * 
	 * @param i
	 * @return
	 */
	public final PrimitiveRenderEvent getElement(int i) {
		return alLinesAndArcs.get(i);
	}

	/**
	 * @return Returns the background.
	 */
	public final Fill getBackground() {
		return fill;
	}

	/**
	 * Sets the background of this area.
	 * 
	 * @param fill The fill to set.
	 */
	public final void setBackground(Fill fill) {
		this.fill = fill;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#getBounds()
	 */
	public final Bounds getBounds() {
		Bounds bo, boFull = null;
		PrimitiveRenderEvent pre;
		double dDelta;

		for (int i = 0; i < getElementCount(); i++) {
			pre = getElement(i);
			try {
				bo = pre.getBounds();
				if (i == 0) {
					boFull = goFactory.copyOf(bo);
				} else {
					if (bo.getLeft() < boFull.getLeft()) {
						dDelta = boFull.getLeft() - bo.getLeft();
						boFull.setLeft(boFull.getLeft() - dDelta);
						boFull.setWidth(boFull.getWidth() + dDelta);
					}
					if (bo.getTop() < boFull.getTop()) {
						dDelta = boFull.getTop() - bo.getTop();
						boFull.setTop(boFull.getTop() - dDelta);
						boFull.setHeight(boFull.getHeight() + dDelta);
					}
					if (bo.getLeft() + bo.getWidth() > boFull.getLeft() + boFull.getWidth()) {
						dDelta = bo.getLeft() + bo.getWidth() - (boFull.getLeft() + boFull.getWidth());
						boFull.setWidth(boFull.getWidth() + dDelta);
					}
					if (bo.getTop() + bo.getHeight() > boFull.getTop() + boFull.getHeight()) {
						dDelta = bo.getTop() + bo.getHeight() - (boFull.getTop() + boFull.getHeight());
						boFull.setHeight(boFull.getHeight() + dDelta);
					}
				}
			} catch (ChartException ufex) {
				logger.log(ufex);
			}
		}
		return boFull;
	}

	/**
	 * @return Returns the outline.
	 */
	public final LineAttributes getOutline() {
		return lia;
	}

	/**
	 * Sets the outline of this area.
	 * 
	 * @param outline The outline to set.
	 */
	public final void setOutline(LineAttributes outline) {
		this.lia = outline;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.birt.chart.event.PrimitiveRenderEvent#copy()
	 */
	public PrimitiveRenderEvent copy() throws ChartException {
		AreaRenderEvent are = new AreaRenderEvent(source);

		if (fill != null) {
			are.setBackground(goFactory.copyOf(fill));
		}

		if (lia != null) {
			are.setOutline(goFactory.copyOf(lia));
		}

		for (Iterator<PrimitiveRenderEvent> itr = alLinesAndArcs.iterator(); itr.hasNext();) {
			are.add(itr.next().copy());
		}

		return are;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#draw(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public void draw(IDeviceRenderer idr) throws ChartException {
		idr.drawArea(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.birt.chart.event.PrimitiveRenderEvent#fill(org.eclipse.birt.chart
	 * .device.IDeviceRenderer)
	 */
	public void fill(IDeviceRenderer idr) throws ChartException {
		idr.fillArea(this);
	}

	public LineAttributes getLineAttributes() {
		return getOutline();
	}
}
