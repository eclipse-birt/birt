/*******************************************************************************
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
 *  Actuate Corporation  - initial API and implementation
 *******************************************************************************/

package org.eclipse.birt.chart.device.swt;

import java.awt.Shape;
import java.awt.geom.Arc2D;
import java.awt.geom.FlatteningPathIterator;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import org.eclipse.birt.chart.computation.GObjectFactory;
import org.eclipse.birt.chart.computation.IGObjectFactory;
import org.eclipse.birt.chart.event.ArcRenderEvent;
import org.eclipse.birt.chart.event.StructureSource;
import org.eclipse.birt.chart.model.attribute.Bounds;
import org.eclipse.birt.chart.model.attribute.Cursor;
import org.eclipse.birt.chart.model.attribute.Location;
import org.eclipse.birt.chart.model.data.Action;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.graphics.Region;

/**
 * This class provides a region definition and an associated action that is
 * invoked when interaction occurs with a chart rendered on a SWT device.
 */
public final class RegionAction {

	private final StructureSource _oSource;

	private Cursor cursor = null;

	/**
	 * the RegionAction's working area
	 */
	private Region region;

	private final Action _ac;

	private static final IGObjectFactory goFactory = GObjectFactory.instance();

	private RegionAction(StructureSource source, Region region, Action ac) {
		this._oSource = source;
		this._ac = ac;
		this.region = region;
	}

	/**
	 * RegionAction constructor taking a polygon to define the region
	 * 
	 * @param oSource     StructureSource
	 * @param loa         Polygon points
	 * @param ac          Action
	 * @param dTranslateX X Translation to apply on polygon coordinates
	 * @param dTranslateY Y Translation to apply on polygon coordinates
	 * @param dScale      Scale to apply on polygon coordinates
	 * @param clipping    Clipping area, points outside it will be clipped
	 */
	RegionAction(StructureSource oSource, Location[] loa, Action ac, double dTranslateX, double dTranslateY,
			double dScale, Region clipping) {
		_oSource = oSource;
		final int[] i2a = SwtRendererImpl.getCoordinatesAsInts(loa, SwtRendererImpl.TRUNCATE, dTranslateX, dTranslateY,
				dScale);
		Region sh = new Region();
		sh.add(i2a);
		if (clipping != null) {
			sh.intersect(clipping);
		}
		_ac = ac;

		this.region = sh;
	}

	/**
	 * This constructor supports shape definition via a rectangle.
	 * 
	 * @param oSource     StructureSource
	 * @param bo          Rectangle
	 * @param ac          Action
	 * @param dTranslateX X translation to apply to rectangle
	 * @param dTranslateY Y translation to apply to rectangle
	 * @param dScale      scale to apply to rectangle
	 * @param clipping    Clipping area, points outside it will be clipped
	 */
	RegionAction(StructureSource oSource, Bounds bo, Action ac, double dTranslateX, double dTranslateY, double dScale,
			Region clipping) {
		_oSource = oSource;

		bo = goFactory.copyOf(bo);
		bo.translate(dTranslateX, dTranslateY);
		bo.scale(dScale);

		Rectangle rect = new Rectangle((int) bo.getLeft(), (int) bo.getTop(), (int) bo.getWidth(),
				(int) bo.getHeight());

		Region sh = new Region();
		sh.add(rect);
		if (clipping != null) {
			sh.intersect(clipping);
		}
		_ac = ac;

		this.region = sh;
	}

	private static final int toSwingArcType(int iArcStyle) {
		switch (iArcStyle) {
		case ArcRenderEvent.OPEN:
			return Arc2D.OPEN;
		case ArcRenderEvent.CLOSED:
			return Arc2D.CHORD;
		case ArcRenderEvent.SECTOR:
			return Arc2D.PIE;
		}
		return -1;
	}

	private int[] shape2polyCoords(Shape shape) {
		if (shape == null) {
			return null;
		}

		ArrayList<Integer> al = new ArrayList<Integer>();

		FlatteningPathIterator pitr = new FlatteningPathIterator(shape.getPathIterator(null), 1);
		double[] data = new double[6];

		while (!pitr.isDone()) {
			int type = pitr.currentSegment(data);

			switch (type) {
			case PathIterator.SEG_MOVETO:
				al.add((int) data[0]);
				al.add((int) data[1]);
				break;
			case PathIterator.SEG_LINETO:
				al.add((int) data[0]);
				al.add((int) data[1]);
				break;
			case PathIterator.SEG_QUADTO:
				al.add((int) data[0]);
				al.add((int) data[1]);
				al.add((int) data[2]);
				al.add((int) data[3]);
				break;
			case PathIterator.SEG_CUBICTO:
				al.add((int) data[0]);
				al.add((int) data[1]);
				al.add((int) data[2]);
				al.add((int) data[3]);
				al.add((int) data[4]);
				al.add((int) data[5]);
				break;
			case PathIterator.SEG_CLOSE:
				break;
			}

			pitr.next();
		}

		if (al.size() == 0) {
			return null;
		}

		int[] coords = new int[al.size()];

		for (int i = 0; i < al.size(); i++) {
			coords[i] = al.get(i);
		}
		return coords;
	}

	/**
	 * This constructor supports shape definition via an elliptical arc
	 * 
	 * @param oSource
	 * @param boEllipse
	 * @param dStart
	 * @param dExtent
	 * @param iArcType
	 * @param ac
	 */
	RegionAction(StructureSource oSource, Bounds boEllipse, double dStart, double dExtent, int iArcType, Action ac,
			double dTranslateX, double dTranslateY, double dScale, Region clipping) {
		_oSource = oSource;

		boEllipse = goFactory.copyOf(boEllipse);
		boEllipse.translate(dTranslateX, dTranslateY);
		boEllipse.scale(dScale);

		Shape shape = new Arc2D.Double(boEllipse.getLeft(), boEllipse.getTop(), boEllipse.getWidth(),
				boEllipse.getHeight(), dStart, dExtent, toSwingArcType(iArcType));

		int[] i2a = shape2polyCoords(shape);
		Region sh = new Region();
		sh.add(i2a);

		if (clipping != null) {
			sh.intersect(clipping);
		}

		_ac = ac;
		this.region = sh;

	}

	/**
	 * @return The action associated with current ShapedAction.
	 */
	public final Action getAction() {
		return _ac;
	}

	/**
	 * @return The source object associated with current ShapedAction
	 */
	public final StructureSource getSource() {
		return _oSource;
	}

	/**
	 * Note the Region object is value copied, others are just reference copy.
	 * <b>The invoker must call <code>dispose()</code> explicitly when this is not
	 * used anymore</b>.
	 * 
	 * @return A copy of current RegionAction
	 */
	public RegionAction copy() {
		return new RegionAction(_oSource, region, _ac);
	}

	/**
	 * Returns if the current region contains given point.
	 * 
	 * @param p
	 * @param gc
	 * @return if the current region contains given point
	 */
	public boolean contains(Point p, GC gc) {
		return contains(p.x, p.y, gc);
	}

	/**
	 * Returns if the current region contains given x,y.
	 * 
	 * @param x
	 * @param y
	 * @param gc
	 * @return if the current region contains given x,y
	 */
	public boolean contains(double x, double y, GC gc) {
		if (region != null) {
			return region.contains((int) x, (int) y);
		}

		return false;
	}

	/**
	 * Dispose the resources.
	 */
	public void dispose() {
		if (region != null) {
			region.dispose();
		}
	}

	/**
	 * Returns if current region is empty.
	 * 
	 * @return if current region is empty
	 */
	public boolean isEmpty() {
		if (region != null) {
			return region.isEmpty();
		}

		return true;
	}

	/**
	 * Returns mouse cursor of the region.
	 * 
	 * @return
	 */
	public Cursor getCursor() {
		return cursor;
	}

	/**
	 * Set mouse cursor of the region.
	 * 
	 * @param cursor
	 */
	public void setCursor(Cursor cursor) {
		this.cursor = cursor;
	}
}
