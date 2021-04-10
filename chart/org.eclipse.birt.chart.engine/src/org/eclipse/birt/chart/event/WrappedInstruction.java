/***********************************************************************
 * Copyright (c) 2004, 2007 Actuate Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Actuate Corporation - initial API and implementation
 ***********************************************************************/

package org.eclipse.birt.chart.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.engine.i18n.Messages;
import org.eclipse.birt.chart.exception.ChartException;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.render.DeferredCache;

import com.ibm.icu.util.ULocale;

/**
 * This class wraps different types of rendering events. It could stand for one
 * or a list of events.
 */
public final class WrappedInstruction implements IRenderInstruction {

	private final DeferredCache dc;

	private final int iInstruction;

	private ArrayList alEvents = null;

	private PrimitiveRenderEvent pre = null;

	private long zorder = 0;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	/**
	 * The cache stores render events which lie within the same plane with current
	 * render event.
	 */
	private DeferredCache subDeferredCache = null;

	private Bounds compareBounds = null;

	/**
	 * The constructor.
	 */
	public WrappedInstruction(DeferredCache dc, ArrayList alEvents, int iInstruction, long zorder) {
		this.dc = dc;
		this.alEvents = alEvents;
		this.iInstruction = iInstruction;
		this.zorder = zorder;
	}

	public WrappedInstruction(DeferredCache dc, ArrayList alEvents, int iInstruction) {
		this(dc, alEvents, iInstruction, 0);
	}

	/**
	 * The constructor.
	 */
	public WrappedInstruction(DeferredCache dc, PrimitiveRenderEvent pre, int iInstruction, long zorder) {
		this.dc = dc;
		this.pre = pre;
		this.iInstruction = iInstruction;
		this.zorder = zorder;
	}

	public WrappedInstruction(DeferredCache dc, PrimitiveRenderEvent pre, int iInstruction) {
		this(dc, pre, iInstruction, 0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	public int compareTo(Object o) {
		Bounds bo = null;

		if (o instanceof PrimitiveRenderEvent) {
			try {
				bo = ((PrimitiveRenderEvent) o).getBounds();
			} catch (ChartException e) {
				assert false;
				return -1;
			}
		} else if (o instanceof IRenderInstruction) {
			if (o instanceof WrappedInstruction) {
				bo = ((WrappedInstruction) o).getCompareBounds();
				long zorder_that = ((WrappedInstruction) o).zorder;
				if (this.zorder < zorder_that) {
					return -1;
				} else if (this.zorder > zorder_that) {
					return 1;
				}
			} else {
				bo = ((IRenderInstruction) o).getBounds();
			}
		}

		return (dc != null && dc.isTransposed()) ? PrimitiveRenderEvent.compareTransposed(getCompareBounds(), bo)
				: (bo == null ? 1 : PrimitiveRenderEvent.compareRegular(getCompareBounds(), bo));
	}

	/**
	 * Returns the associated event.
	 * 
	 * @return
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return Messages.getString("wrapped.instruction.to.string", //$NON-NLS-1$
				new Object[] { super.toString(), Boolean.valueOf(isModel()), getBounds() }, ULocale.getDefault());
	}

	/**
	 * This method set a bounds to be used for polygon comparison to reset polygon
	 * rendering order. Under some cases, like core, triangle charts, we don't use
	 * actual plan to do order comparison, it is difficult. We just sets a compare
	 * bounds instead of actual bound for comparison.
	 * 
	 * @param bounds
	 */
	public void setCompareBounds(Bounds bounds) {
		this.compareBounds = bounds;
	}

	/**
	 * Returns compare bounds.
	 * 
	 * @return
	 */
	public Bounds getCompareBounds() {
		if (compareBounds != null) {
			return compareBounds;
		}
		return getBounds();
	}

	/**
	 * @return Returns the mimimum bounds required to contain the rendering area of
	 *         associated rendering event.
	 */
	public final Bounds getBounds() {
		if (compareBounds != null) {
			return compareBounds;
		}

		if (!isModel()) {
			try {
				return pre.getBounds();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} else {
			Bounds bo = null;
			for (int i = 0; i < alEvents.size(); i++) {
				try {
					if (i == 0) {
						bo = goFactory.copyOf(((PrimitiveRenderEvent) alEvents.get(i)).getBounds());
					} else {
						bo.max(((PrimitiveRenderEvent) alEvents.get(i)).getBounds());
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			return bo;
		}
		return null;
	}

	/**
	 * @return Returns if wraps multiple events currently.
	 */
	public boolean isModel() {
		return pre == null;
	}

	/**
	 * @return Returns list of events currently wraps.
	 */
	public List getModel() {
		return alEvents;
	}

	public long getZOrder() {
		return zorder;
	}

	public void setZOrder(int zorder) {
		this.zorder = zorder;
	}

	public static Comparator<?> getDefaultComarator() {
		return new WIComparator();
	}

	private static class WIComparator implements Comparator<Object>, Serializable {

		private static final long serialVersionUID = 1L;

		private long getZOrder(Object o) {
			if (o instanceof WrappedInstruction) {
				return ((WrappedInstruction) o).getZOrder();
			} else {
				return 0;
			}
		}

		public int compare(Object o1, Object o2) {
			return Long.valueOf(getZOrder(o1)).compareTo(getZOrder(o2));
		}
	}

	/**
	 * Set sub-deferred cache instance.
	 * 
	 * @param dc
	 */
	public void setSubDeferredCache(DeferredCache dc) {
		this.subDeferredCache = dc;
	}

	/**
	 * Returns instance of sub-deferred cache.
	 * 
	 * @return
	 */
	public DeferredCache getSubDeferredCache() {
		return subDeferredCache;
	}
}
